/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async, inject, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable } from 'rxjs/Observable';
import { JhiEventManager } from 'ng-jhipster';

import { CarRentalTestModule } from '../../../test.module';
import { BookingDialogComponent } from '../../../../../../main/webapp/app/entities/booking/booking-dialog.component';
import { BookingService } from '../../../../../../main/webapp/app/entities/booking/booking.service';
import { Booking } from '../../../../../../main/webapp/app/entities/booking/booking.model';
import { CarService } from '../../../../../../main/webapp/app/entities/car';
import { CustomerService } from '../../../../../../main/webapp/app/entities/customer';

describe('Component Tests', () => {

    describe('Booking Management Dialog Component', () => {
        let comp: BookingDialogComponent;
        let fixture: ComponentFixture<BookingDialogComponent>;
        let service: BookingService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [CarRentalTestModule],
                declarations: [BookingDialogComponent],
                providers: [
                    CarService,
                    CustomerService,
                    BookingService
                ]
            })
            .overrideTemplate(BookingDialogComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(BookingDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(BookingService);
            mockEventManager = fixture.debugElement.injector.get(JhiEventManager);
            mockActiveModal = fixture.debugElement.injector.get(NgbActiveModal);
        });

        describe('save', () => {
            it('Should call update service on save for existing entity',
                inject([],
                    fakeAsync(() => {
                        // GIVEN
                        const entity = new Booking(123);
                        spyOn(service, 'update').and.returnValue(Observable.of(new HttpResponse({body: entity})));
                        comp.booking = entity;
                        // WHEN
                        comp.save();
                        tick(); // simulate async

                        // THEN
                        expect(service.update).toHaveBeenCalledWith(entity);
                        expect(comp.isSaving).toEqual(false);
                        expect(mockEventManager.broadcastSpy).toHaveBeenCalledWith({ name: 'bookingListModification', content: 'OK'});
                        expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
                    })
                )
            );

            it('Should call create service on save for new entity',
                inject([],
                    fakeAsync(() => {
                        // GIVEN
                        const entity = new Booking();
                        spyOn(service, 'create').and.returnValue(Observable.of(new HttpResponse({body: entity})));
                        comp.booking = entity;
                        // WHEN
                        comp.save();
                        tick(); // simulate async

                        // THEN
                        expect(service.create).toHaveBeenCalledWith(entity);
                        expect(comp.isSaving).toEqual(false);
                        expect(mockEventManager.broadcastSpy).toHaveBeenCalledWith({ name: 'bookingListModification', content: 'OK'});
                        expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
                    })
                )
            );
        });
    });

});
