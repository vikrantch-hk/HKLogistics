/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async, inject, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable } from 'rxjs/Observable';
import { JhiEventManager } from 'ng-jhipster';

import { HkLogisticsTestModule } from '../../../test.module';
import { AwbDialogComponent } from '../../../../../../main/webapp/app/entities/awb/awb-dialog.component';
import { AwbService } from '../../../../../../main/webapp/app/entities/awb/awb.service';
import { Awb } from '../../../../../../main/webapp/app/entities/awb/awb.model';
import { ChannelService } from '../../../../../../main/webapp/app/entities/channel';
import { VendorWHCourierMappingService } from '../../../../../../main/webapp/app/entities/vendor-wh-courier-mapping';
import { AwbStatusService } from '../../../../../../main/webapp/app/entities/awb-status';

describe('Component Tests', () => {

    describe('Awb Management Dialog Component', () => {
        let comp: AwbDialogComponent;
        let fixture: ComponentFixture<AwbDialogComponent>;
        let service: AwbService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [HkLogisticsTestModule],
                declarations: [AwbDialogComponent],
                providers: [
                    ChannelService,
                    VendorWHCourierMappingService,
                    AwbStatusService,
                    AwbService
                ]
            })
            .overrideTemplate(AwbDialogComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(AwbDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(AwbService);
            mockEventManager = fixture.debugElement.injector.get(JhiEventManager);
            mockActiveModal = fixture.debugElement.injector.get(NgbActiveModal);
        });

        describe('save', () => {
            it('Should call update service on save for existing entity',
                inject([],
                    fakeAsync(() => {
                        // GIVEN
                        const entity = new Awb(123);
                        spyOn(service, 'update').and.returnValue(Observable.of(new HttpResponse({body: entity})));
                        comp.awb = entity;
                        // WHEN
                        comp.save();
                        tick(); // simulate async

                        // THEN
                        expect(service.update).toHaveBeenCalledWith(entity);
                        expect(comp.isSaving).toEqual(false);
                        expect(mockEventManager.broadcastSpy).toHaveBeenCalledWith({ name: 'awbListModification', content: 'OK'});
                        expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
                    })
                )
            );

            it('Should call create service on save for new entity',
                inject([],
                    fakeAsync(() => {
                        // GIVEN
                        const entity = new Awb();
                        spyOn(service, 'create').and.returnValue(Observable.of(new HttpResponse({body: entity})));
                        comp.awb = entity;
                        // WHEN
                        comp.save();
                        tick(); // simulate async

                        // THEN
                        expect(service.create).toHaveBeenCalledWith(entity);
                        expect(comp.isSaving).toEqual(false);
                        expect(mockEventManager.broadcastSpy).toHaveBeenCalledWith({ name: 'awbListModification', content: 'OK'});
                        expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
                    })
                )
            );
        });
    });

});
