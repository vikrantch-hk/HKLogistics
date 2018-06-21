import { ICourierChannel } from 'app/shared/model//courier-channel.model';
import { ICourierGroup } from 'app/shared/model//courier-group.model';

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
    courierChannels?: ICourierChannel[];
    courierGroups?: ICourierGroup[];
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
        public courierChannels?: ICourierChannel[],
        public courierGroups?: ICourierGroup[]
    ) {
        this.active = false;
        this.hkShipping = false;
        this.vendorShipping = false;
        this.reversePickup = false;
    }
}
