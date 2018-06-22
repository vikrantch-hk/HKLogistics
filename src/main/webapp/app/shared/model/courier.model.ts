export interface ICourier {
    id?: number;
    name?: string;
    active?: boolean;
    trackingParameter?: string;
    trackingUrl?: string;
    parentCourierId?: number;
    hkShipping?: boolean;
    vendorShipping?: boolean;
    reversePickup?: boolean;
    courierChannelId?: number;
    courierGroupName?: string;
    courierGroupId?: number;
}

export class Courier implements ICourier {
    constructor(
        public id?: number,
        public name?: string,
        public active?: boolean,
        public trackingParameter?: string,
        public trackingUrl?: string,
        public parentCourierId?: number,
        public hkShipping?: boolean,
        public vendorShipping?: boolean,
        public reversePickup?: boolean,
        public courierChannelId?: number,
        public courierGroupName?: string,
        public courierGroupId?: number
    ) {
        this.active = false;
        this.hkShipping = false;
        this.vendorShipping = false;
        this.reversePickup = false;
    }
}
