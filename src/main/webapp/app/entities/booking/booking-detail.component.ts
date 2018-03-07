import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs/Subscription';
import { JhiEventManager } from 'ng-jhipster';

import { Booking } from './booking.model';
import { BookingService } from './booking.service';

@Component({
    selector: 'jhi-booking-detail',
    templateUrl: './booking-detail.component.html'
})
export class BookingDetailComponent implements OnInit, OnDestroy {

    booking: Booking;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private bookingService: BookingService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInBookings();
    }

    load(id) {
        this.bookingService.find(id)
            .subscribe((bookingResponse: HttpResponse<Booking>) => {
                this.booking = bookingResponse.body;
            });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInBookings() {
        this.eventSubscriber = this.eventManager.subscribe(
            'bookingListModification',
            (response) => this.load(this.booking.id)
        );
    }
}
