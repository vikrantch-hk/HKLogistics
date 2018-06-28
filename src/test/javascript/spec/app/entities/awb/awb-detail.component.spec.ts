/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';

import { HkLogisticsTestModule } from '../../../test.module';
import { AwbDetailComponent } from '../../../../../../main/webapp/app/entities/awb/awb-detail.component';
import { AwbService } from '../../../../../../main/webapp/app/entities/awb/awb.service';
import { Awb } from '../../../../../../main/webapp/app/entities/awb/awb.model';

describe('Component Tests', () => {

    describe('Awb Management Detail Component', () => {
        let comp: AwbDetailComponent;
        let fixture: ComponentFixture<AwbDetailComponent>;
        let service: AwbService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [HkLogisticsTestModule],
                declarations: [AwbDetailComponent],
                providers: [
                    AwbService
                ]
            })
            .overrideTemplate(AwbDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(AwbDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(AwbService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                spyOn(service, 'find').and.returnValue(Observable.of(new HttpResponse({
                    body: new Awb(123)
                })));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.find).toHaveBeenCalledWith(123);
                expect(comp.awb).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
