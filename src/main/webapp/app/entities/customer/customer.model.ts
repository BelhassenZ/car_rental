import { BaseEntity } from './../../shared';

export class Customer implements BaseEntity {
    constructor(
        public id?: number,
        public firstName?: string,
        public lastName?: string,
        public phoneNumber?: string,
        public streetAddress?: string,
        public age?: number,
        public eligible?: boolean,
        public books?: BaseEntity[],
    ) {
        this.eligible = false;
    }
}
