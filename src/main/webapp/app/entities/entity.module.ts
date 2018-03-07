import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';

import { CarRentalBookingModule } from './booking/booking.module';
import { CarRentalCarModule } from './car/car.module';
import { CarRentalCustomerModule } from './customer/customer.module';
/* jhipster-needle-add-entity-module-import - JHipster will add entity modules imports here */

@NgModule({
    imports: [
        CarRentalBookingModule,
        CarRentalCarModule,
        CarRentalCustomerModule,
        /* jhipster-needle-add-entity-module - JHipster will add entity modules here */
    ],
    declarations: [],
    entryComponents: [],
    providers: [],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class CarRentalEntityModule {}
