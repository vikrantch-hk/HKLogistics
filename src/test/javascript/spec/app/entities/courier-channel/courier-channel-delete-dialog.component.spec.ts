/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable } from 'rxjs/Observable';
import { JhiEventManager } from 'ng-jhipster';

import { HkLogisticsTestModule } from '../../../test.module';
import { CourierChannelDeleteDialogComponent } from '../../../../../../main/webapp/app/entities/courier-channel/courier-channel-delete-dialog.component';
import { CourierChannelService } from '../../../../../../main/webapp/app/entities/courier-channel/courier-channel.service';

describe('Component Tests', () => {

    describe('CourierChannel Management Delete Component', () => {
        let comp: CourierChannelDeleteDialogComponent;
        let fixture: ComponentFixture<CourierChannelDeleteDialogComponent>;
        let service: CourierChannelService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [HkLogisticsTestModule],
                declarations: [CourierChannelDeleteDialogComponent],
                providers: [
                    CourierChannelService
                ]
            })
            .overrideTemplate(CourierChannelDeleteDialogComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(CourierChannelDeleteDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(CourierChannelService);
            mockEventManager = fixture.debugElement.injector.get(JhiEventManager);
            mockActiveModal = fixture.debugElement.injector.get(NgbActiveModal);
        });

        describe('confirmDelete', () => {
            it('Should call delete service on confirmDelete',
                inject([],
                    fakeAsync(() => {
                        // GIVEN
                        spyOn(service, 'delete').and.returnValue(Observable.of({}));

                        // WHEN
                        comp.confirmDelete(123);
                        tick();

                        // THEN
                        expect(service.delete).toHaveBeenCalledWith(123);
                        expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
                        expect(mockEventManager.broadcastSpy).toHaveBeenCalled();
                    })
                )
            );
        });
    });

});
