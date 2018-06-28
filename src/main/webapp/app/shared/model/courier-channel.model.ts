import { IVendorWHCourierMapping } from 'app/shared/model//vendor-wh-courier-mapping.model';
import { ICourier } from 'app/shared/model//courier.model';

export interface ICourierChannel {
    id?: number;
    minWeight?: number;
    maxWeight?: number;
    natureOfProduct?: string;
    vendorWHCourierMappings?: IVendorWHCourierMapping[];
    channelName?: string;
    channelId?: number;
    couriers?: ICourier[];
}

export class CourierChannel implements ICourierChannel {
    constructor(
        public id?: number,
        public minWeight?: number,
        public maxWeight?: number,
        public natureOfProduct?: string,
        public vendorWHCourierMappings?: IVendorWHCourierMapping[],
        public channelName?: string,
        public channelId?: number,
        public couriers?: ICourier[]
    ) {}
}
