import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil } from 'ng-jhipster';

import { UserRouteAccessService } from '../../shared';
import { BookingComponent } from './booking.component';
import { BookingDetailComponent } from './booking-detail.component';
import { BookingPopupComponent } from './booking-dialog.component';
import { BookingDeletePopupComponent } from './booking-delete-dialog.component';

@Injectable()
export class BookingResolvePagingParams implements Resolve<any> {

    constructor(private paginationUtil: JhiPaginationUtil) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const page = route.queryParams['page'] ? route.queryParams['page'] : '1';
        const sort = route.queryParams['sort'] ? route.queryParams['sort'] : 'id,asc';
        return {
            page: this.paginationUtil.parsePage(page),
            predicate: this.paginationUtil.parsePredicate(sort),
            ascending: this.paginationUtil.parseAscending(sort)
      };
    }
}

export const bookingRoute: Routes = [
    {
        path: 'booking',
        component: BookingComponent,
        resolve: {
            'pagingParams': BookingResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'carRentalApp.booking.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'booking/:id',
        component: BookingDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'carRentalApp.booking.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const bookingPopupRoute: Routes = [
    {
        path: 'booking-new',
        component: BookingPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'carRentalApp.booking.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'booking/:id/edit',
        component: BookingPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'carRentalApp.booking.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'booking/:id/delete',
        component: BookingDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'carRentalApp.booking.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
