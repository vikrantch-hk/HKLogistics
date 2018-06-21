import { ICourierChannel } from 'app/shared/model//courier-channel.model';

export interface IChannel {
    id?: number;
    name?: string;
    courierChannels?: ICourierChannel[];
}

export class Channel implements IChannel {
    constructor(public id?: number, public name?: string, public courierChannels?: ICourierChannel[]) {}
}
