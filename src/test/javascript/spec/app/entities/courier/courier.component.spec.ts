/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { Observable } from 'rxjs/Observable';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { HkLogisticsTestModule } from '../../../test.module';
import { CourierComponent } from '../../../../../../main/webapp/app/entities/courier/courier.component';
import { CourierService } from '../../../../../../main/webapp/app/entities/courier/courier.service';
import { Courier } from '../../../../../../main/webapp/app/entities/courier/courier.model';

describe('Component Tests', () => {

    describe('Courier Management Component', () => {
        let comp: CourierComponent;
        let fixture: ComponentFixture<CourierComponent>;
        let service: CourierService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [HkLogisticsTestModule],
                declarations: [CourierComponent],
                providers: [
                    CourierService
                ]
            })
            .overrideTemplate(CourierComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(CourierComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(CourierService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN
                const headers = new HttpHeaders().append('link', 'link;link');
                spyOn(service, 'query').and.returnValue(Observable.of(new HttpResponse({
                    body: [new Courier(123)],
                    headers
                })));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.query).toHaveBeenCalled();
                expect(comp.couriers[0]).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
