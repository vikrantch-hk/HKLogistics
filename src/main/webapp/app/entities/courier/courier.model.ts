import { BaseEntity } from './../../shared';

export class Courier implements BaseEntity {
    constructor(
        public id?: number,
        public name?: string,
        public shortCode?: string,
        public active?: boolean,
        public trackingParameter?: string,
        public trackingUrl?: string,
        public parentCourierId?: number,
        public hkShipping?: boolean,
        public vendorShipping?: boolean,
        public reversePickup?: boolean,
        public courierGroupName?: string,
        public courierGroupId?: number,
        public courierChannels?: BaseEntity[],
    ) {
        this.active = false;
        this.hkShipping = false;
        this.vendorShipping = false;
        this.reversePickup = false;
    }
}
