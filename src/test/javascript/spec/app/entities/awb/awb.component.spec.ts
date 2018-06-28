/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { Observable } from 'rxjs/Observable';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { HkLogisticsTestModule } from '../../../test.module';
import { AwbComponent } from '../../../../../../main/webapp/app/entities/awb/awb.component';
import { AwbService } from '../../../../../../main/webapp/app/entities/awb/awb.service';
import { Awb } from '../../../../../../main/webapp/app/entities/awb/awb.model';

describe('Component Tests', () => {

    describe('Awb Management Component', () => {
        let comp: AwbComponent;
        let fixture: ComponentFixture<AwbComponent>;
        let service: AwbService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [HkLogisticsTestModule],
                declarations: [AwbComponent],
                providers: [
                    AwbService
                ]
            })
            .overrideTemplate(AwbComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(AwbComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(AwbService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN
                const headers = new HttpHeaders().append('link', 'link;link');
                spyOn(service, 'query').and.returnValue(Observable.of(new HttpResponse({
                    body: [new Awb(123)],
                    headers
                })));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.query).toHaveBeenCalled();
                expect(comp.awbs[0]).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
