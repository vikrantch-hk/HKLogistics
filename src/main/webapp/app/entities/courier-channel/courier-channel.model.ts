import { BaseEntity } from './../../shared';

export class CourierChannel implements BaseEntity {
    constructor(
        public id?: number,
        public minWeight?: number,
        public maxWeight?: number,
        public natureOfProduct?: string,
        public vendorWHCourierMappings?: BaseEntity[],
        public channelName?: string,
        public channelId?: number,
        public courierId?: number,
    ) {
    }
}
