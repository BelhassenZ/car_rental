/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';

import { CarRentalTestModule } from '../../../test.module';
import { BookingDetailComponent } from '../../../../../../main/webapp/app/entities/booking/booking-detail.component';
import { BookingService } from '../../../../../../main/webapp/app/entities/booking/booking.service';
import { Booking } from '../../../../../../main/webapp/app/entities/booking/booking.model';

describe('Component Tests', () => {

    describe('Booking Management Detail Component', () => {
        let comp: BookingDetailComponent;
        let fixture: ComponentFixture<BookingDetailComponent>;
        let service: BookingService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [CarRentalTestModule],
                declarations: [BookingDetailComponent],
                providers: [
                    BookingService
                ]
            })
            .overrideTemplate(BookingDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(BookingDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(BookingService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                spyOn(service, 'find').and.returnValue(Observable.of(new HttpResponse({
                    body: new Booking(123)
                })));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.find).toHaveBeenCalledWith(123);
                expect(comp.booking).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
