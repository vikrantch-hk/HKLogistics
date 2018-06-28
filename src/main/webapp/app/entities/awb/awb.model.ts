import { BaseEntity } from './../../shared';

export class Awb implements BaseEntity {
    constructor(
        public id?: number,
        public awbNumber?: string,
        public awbBarCode?: string,
        public cod?: boolean,
        public createDate?: any,
        public returnAwbNumber?: string,
        public returnAwbBarCode?: string,
        public isBrightAwb?: boolean,
        public trackingLink?: string,
        public channelName?: string,
        public channelId?: number,
        public vendorWHCourierMappingId?: number,
        public awbStatusStatus?: string,
        public awbStatusId?: number,
    ) {
        this.cod = false;
        this.isBrightAwb = false;
    }
}
