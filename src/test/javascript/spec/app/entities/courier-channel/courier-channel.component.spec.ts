/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { Observable } from 'rxjs/Observable';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { HkLogisticsTestModule } from '../../../test.module';
import { CourierChannelComponent } from '../../../../../../main/webapp/app/entities/courier-channel/courier-channel.component';
import { CourierChannelService } from '../../../../../../main/webapp/app/entities/courier-channel/courier-channel.service';
import { CourierChannel } from '../../../../../../main/webapp/app/entities/courier-channel/courier-channel.model';

describe('Component Tests', () => {

    describe('CourierChannel Management Component', () => {
        let comp: CourierChannelComponent;
        let fixture: ComponentFixture<CourierChannelComponent>;
        let service: CourierChannelService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [HkLogisticsTestModule],
                declarations: [CourierChannelComponent],
                providers: [
                    CourierChannelService
                ]
            })
            .overrideTemplate(CourierChannelComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(CourierChannelComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(CourierChannelService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN
                const headers = new HttpHeaders().append('link', 'link;link');
                spyOn(service, 'query').and.returnValue(Observable.of(new HttpResponse({
                    body: [new CourierChannel(123)],
                    headers
                })));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.query).toHaveBeenCalled();
                expect(comp.courierChannels[0]).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
