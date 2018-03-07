import { Injectable, Component } from '@angular/core';
import { Router } from '@angular/router';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { HttpResponse } from '@angular/common/http';
import { DatePipe } from '@angular/common';
import { Booking } from './booking.model';
import { BookingService } from './booking.service';

@Injectable()
export class BookingPopupService {
    private ngbModalRef: NgbModalRef;

    constructor(
        private datePipe: DatePipe,
        private modalService: NgbModal,
        private router: Router,
        private bookingService: BookingService

    ) {
        this.ngbModalRef = null;
    }

    open(component: Component, id?: number | any): Promise<NgbModalRef> {
        return new Promise<NgbModalRef>((resolve, reject) => {
            const isOpen = this.ngbModalRef !== null;
            if (isOpen) {
                resolve(this.ngbModalRef);
            }

            if (id) {
                this.bookingService.find(id)
                    .subscribe((bookingResponse: HttpResponse<Booking>) => {
                        const booking: Booking = bookingResponse.body;
                        booking.startDay = this.datePipe
                            .transform(booking.startDay, 'yyyy-MM-ddTHH:mm:ss');
                        booking.endDay = this.datePipe
                            .transform(booking.endDay, 'yyyy-MM-ddTHH:mm:ss');
                        this.ngbModalRef = this.bookingModalRef(component, booking);
                        resolve(this.ngbModalRef);
                    });
            } else {
                // setTimeout used as a workaround for getting ExpressionChangedAfterItHasBeenCheckedError
                setTimeout(() => {
                    this.ngbModalRef = this.bookingModalRef(component, new Booking());
                    resolve(this.ngbModalRef);
                }, 0);
            }
        });
    }

    bookingModalRef(component: Component, booking: Booking): NgbModalRef {
        const modalRef = this.modalService.open(component, { size: 'lg', backdrop: 'static'});
        modalRef.componentInstance.booking = booking;
        modalRef.result.then((result) => {
            this.router.navigate([{ outlets: { popup: null }}], { replaceUrl: true, queryParamsHandling: 'merge' });
            this.ngbModalRef = null;
        }, (reason) => {
            this.router.navigate([{ outlets: { popup: null }}], { replaceUrl: true, queryParamsHandling: 'merge' });
            this.ngbModalRef = null;
        });
        return modalRef;
    }
}
