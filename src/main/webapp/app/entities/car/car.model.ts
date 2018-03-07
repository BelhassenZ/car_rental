import { BaseEntity } from './../../shared';

export class Car implements BaseEntity {
    constructor(
        public id?: number,
        public model?: string,
        public type?: string,
        public regNumber?: string,
        public available?: boolean,
        public concernedId?: number,
    ) {
        this.available = false;
    }
}
