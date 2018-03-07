import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';

import { Observable } from 'rxjs/Observable';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { Booking } from './booking.model';
import { BookingPopupService } from './booking-popup.service';
import { BookingService } from './booking.service';
import { Car, CarService } from '../car';
import { Customer, CustomerService } from '../customer';

@Component({
    selector: 'jhi-booking-dialog',
    templateUrl: './booking-dialog.component.html'
})
export class BookingDialogComponent implements OnInit {

    booking: Booking;
    isSaving: boolean;

    concerns: Car[];

    customers: Customer[];

    constructor(
        public activeModal: NgbActiveModal,
        private jhiAlertService: JhiAlertService,
        private bookingService: BookingService,
        private carService: CarService,
        private customerService: CustomerService,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.carService
            .query({filter: 'concerned-is-null'})
            .subscribe((res: HttpResponse<Car[]>) => {
                if (!this.booking.concernsId) {
                    this.concerns = res.body;
                } else {
                    this.carService
                        .find(this.booking.concernsId)
                        .subscribe((subRes: HttpResponse<Car>) => {
                            this.concerns = [subRes.body].concat(res.body);
                        }, (subRes: HttpErrorResponse) => this.onError(subRes.message));
                }
            }, (res: HttpErrorResponse) => this.onError(res.message));
        this.customerService.query()
            .subscribe((res: HttpResponse<Customer[]>) => { this.customers = res.body; }, (res: HttpErrorResponse) => this.onError(res.message));
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.booking.id !== undefined) {
            this.subscribeToSaveResponse(
                this.bookingService.update(this.booking));
        } else {
            this.subscribeToSaveResponse(
                this.bookingService.create(this.booking));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<Booking>>) {
        result.subscribe((res: HttpResponse<Booking>) =>
            this.onSaveSuccess(res.body), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess(result: Booking) {
        this.eventManager.broadcast({ name: 'bookingListModification', content: 'OK'});
        this.isSaving = false;
        this.activeModal.dismiss(result);
    }

    private onSaveError() {
        this.isSaving = false;
    }

    private onError(error: any) {
        this.jhiAlertService.error(error.message, null, null);
    }

    trackCarById(index: number, item: Car) {
        return item.id;
    }

    trackCustomerById(index: number, item: Customer) {
        return item.id;
    }
}

@Component({
    selector: 'jhi-booking-popup',
    template: ''
})
export class BookingPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private bookingPopupService: BookingPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.bookingPopupService
                    .open(BookingDialogComponent as Component, params['id']);
            } else {
                this.bookingPopupService
                    .open(BookingDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
