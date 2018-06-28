/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async, inject, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable } from 'rxjs/Observable';
import { JhiEventManager } from 'ng-jhipster';

import { HkLogisticsTestModule } from '../../../test.module';
import { CourierChannelDialogComponent } from '../../../../../../main/webapp/app/entities/courier-channel/courier-channel-dialog.component';
import { CourierChannelService } from '../../../../../../main/webapp/app/entities/courier-channel/courier-channel.service';
import { CourierChannel } from '../../../../../../main/webapp/app/entities/courier-channel/courier-channel.model';
import { ChannelService } from '../../../../../../main/webapp/app/entities/channel';
import { CourierService } from '../../../../../../main/webapp/app/entities/courier';

describe('Component Tests', () => {

    describe('CourierChannel Management Dialog Component', () => {
        let comp: CourierChannelDialogComponent;
        let fixture: ComponentFixture<CourierChannelDialogComponent>;
        let service: CourierChannelService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [HkLogisticsTestModule],
                declarations: [CourierChannelDialogComponent],
                providers: [
                    ChannelService,
                    CourierService,
                    CourierChannelService
                ]
            })
            .overrideTemplate(CourierChannelDialogComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(CourierChannelDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(CourierChannelService);
            mockEventManager = fixture.debugElement.injector.get(JhiEventManager);
            mockActiveModal = fixture.debugElement.injector.get(NgbActiveModal);
        });

        describe('save', () => {
            it('Should call update service on save for existing entity',
                inject([],
                    fakeAsync(() => {
                        // GIVEN
                        const entity = new CourierChannel(123);
                        spyOn(service, 'update').and.returnValue(Observable.of(new HttpResponse({body: entity})));
                        comp.courierChannel = entity;
                        // WHEN
                        comp.save();
                        tick(); // simulate async

                        // THEN
                        expect(service.update).toHaveBeenCalledWith(entity);
                        expect(comp.isSaving).toEqual(false);
                        expect(mockEventManager.broadcastSpy).toHaveBeenCalledWith({ name: 'courierChannelListModification', content: 'OK'});
                        expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
                    })
                )
            );

            it('Should call create service on save for new entity',
                inject([],
                    fakeAsync(() => {
                        // GIVEN
                        const entity = new CourierChannel();
                        spyOn(service, 'create').and.returnValue(Observable.of(new HttpResponse({body: entity})));
                        comp.courierChannel = entity;
                        // WHEN
                        comp.save();
                        tick(); // simulate async

                        // THEN
                        expect(service.create).toHaveBeenCalledWith(entity);
                        expect(comp.isSaving).toEqual(false);
                        expect(mockEventManager.broadcastSpy).toHaveBeenCalledWith({ name: 'courierChannelListModification', content: 'OK'});
                        expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
                    })
                )
            );
        });
    });

});
