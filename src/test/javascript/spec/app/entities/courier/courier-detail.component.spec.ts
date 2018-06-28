/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';

import { HkLogisticsTestModule } from '../../../test.module';
import { CourierDetailComponent } from '../../../../../../main/webapp/app/entities/courier/courier-detail.component';
import { CourierService } from '../../../../../../main/webapp/app/entities/courier/courier.service';
import { Courier } from '../../../../../../main/webapp/app/entities/courier/courier.model';

describe('Component Tests', () => {

    describe('Courier Management Detail Component', () => {
        let comp: CourierDetailComponent;
        let fixture: ComponentFixture<CourierDetailComponent>;
        let service: CourierService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [HkLogisticsTestModule],
                declarations: [CourierDetailComponent],
                providers: [
                    CourierService
                ]
            })
            .overrideTemplate(CourierDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(CourierDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(CourierService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                spyOn(service, 'find').and.returnValue(Observable.of(new HttpResponse({
                    body: new Courier(123)
                })));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.find).toHaveBeenCalledWith(123);
                expect(comp.courier).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
