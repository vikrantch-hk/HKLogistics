/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';

import { HkLogisticsTestModule } from '../../../test.module';
import { CourierChannelDetailComponent } from '../../../../../../main/webapp/app/entities/courier-channel/courier-channel-detail.component';
import { CourierChannelService } from '../../../../../../main/webapp/app/entities/courier-channel/courier-channel.service';
import { CourierChannel } from '../../../../../../main/webapp/app/entities/courier-channel/courier-channel.model';

describe('Component Tests', () => {

    describe('CourierChannel Management Detail Component', () => {
        let comp: CourierChannelDetailComponent;
        let fixture: ComponentFixture<CourierChannelDetailComponent>;
        let service: CourierChannelService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [HkLogisticsTestModule],
                declarations: [CourierChannelDetailComponent],
                providers: [
                    CourierChannelService
                ]
            })
            .overrideTemplate(CourierChannelDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(CourierChannelDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(CourierChannelService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                spyOn(service, 'find').and.returnValue(Observable.of(new HttpResponse({
                    body: new CourierChannel(123)
                })));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.find).toHaveBeenCalledWith(123);
                expect(comp.courierChannel).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
