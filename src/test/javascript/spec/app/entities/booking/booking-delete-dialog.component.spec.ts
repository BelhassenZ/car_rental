/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable } from 'rxjs/Observable';
import { JhiEventManager } from 'ng-jhipster';

import { CarRentalTestModule } from '../../../test.module';
import { BookingDeleteDialogComponent } from '../../../../../../main/webapp/app/entities/booking/booking-delete-dialog.component';
import { BookingService } from '../../../../../../main/webapp/app/entities/booking/booking.service';

describe('Component Tests', () => {

    describe('Booking Management Delete Component', () => {
        let comp: BookingDeleteDialogComponent;
        let fixture: ComponentFixture<BookingDeleteDialogComponent>;
        let service: BookingService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [CarRentalTestModule],
                declarations: [BookingDeleteDialogComponent],
                providers: [
                    BookingService
                ]
            })
            .overrideTemplate(BookingDeleteDialogComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(BookingDeleteDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(BookingService);
            mockEventManager = fixture.debugElement.injector.get(JhiEventManager);
            mockActiveModal = fixture.debugElement.injector.get(NgbActiveModal);
        });

        describe('confirmDelete', () => {
            it('Should call delete service on confirmDelete',
                inject([],
                    fakeAsync(() => {
                        // GIVEN
                        spyOn(service, 'delete').and.returnValue(Observable.of({}));

                        // WHEN
                        comp.confirmDelete(123);
                        tick();

                        // THEN
                        expect(service.delete).toHaveBeenCalledWith(123);
                        expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
                        expect(mockEventManager.broadcastSpy).toHaveBeenCalled();
                    })
                )
            );
        });
    });

});
