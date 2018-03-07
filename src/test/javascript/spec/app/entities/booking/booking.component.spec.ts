/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { Observable } from 'rxjs/Observable';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { CarRentalTestModule } from '../../../test.module';
import { BookingComponent } from '../../../../../../main/webapp/app/entities/booking/booking.component';
import { BookingService } from '../../../../../../main/webapp/app/entities/booking/booking.service';
import { Booking } from '../../../../../../main/webapp/app/entities/booking/booking.model';

describe('Component Tests', () => {

    describe('Booking Management Component', () => {
        let comp: BookingComponent;
        let fixture: ComponentFixture<BookingComponent>;
        let service: BookingService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [CarRentalTestModule],
                declarations: [BookingComponent],
                providers: [
                    BookingService
                ]
            })
            .overrideTemplate(BookingComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(BookingComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(BookingService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN
                const headers = new HttpHeaders().append('link', 'link;link');
                spyOn(service, 'query').and.returnValue(Observable.of(new HttpResponse({
                    body: [new Booking(123)],
                    headers
                })));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.query).toHaveBeenCalled();
                expect(comp.bookings[0]).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
