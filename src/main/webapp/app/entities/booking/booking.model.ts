import { BaseEntity } from './../../shared';

export class Booking implements BaseEntity {
    constructor(
        public id?: number,
        public noofrentdays?: number,
        public startDay?: any,
        public endDay?: any,
        public rentPerDay?: number,
        public totalAmountPayable?: number,
        public concernsId?: number,
        public doneId?: number,
    ) {
    }
}
