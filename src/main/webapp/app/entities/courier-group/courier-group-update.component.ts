import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { JhiAlertService } from 'ng-jhipster';

import { ICourierGroup } from 'app/shared/model/courier-group.model';
import { CourierGroupService } from './courier-group.service';
import { ICourier } from 'app/shared/model/courier.model';
import { CourierService } from 'app/entities/courier';

@Component({
    selector: 'jhi-courier-group-update',
    templateUrl: './courier-group-update.component.html'
})
export class CourierGroupUpdateComponent implements OnInit {
    private _courierGroup: ICourierGroup;
    isSaving: boolean;

    couriers: ICourier[];

    constructor(
        private jhiAlertService: JhiAlertService,
        private courierGroupService: CourierGroupService,
        private courierService: CourierService,
        private activatedRoute: ActivatedRoute
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ courierGroup }) => {
            this.courierGroup = courierGroup;
        });
        this.courierService.query().subscribe(
            (res: HttpResponse<ICourier[]>) => {
                this.couriers = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        if (this.courierGroup.id !== undefined) {
            this.subscribeToSaveResponse(this.courierGroupService.update(this.courierGroup));
        } else {
            this.subscribeToSaveResponse(this.courierGroupService.create(this.courierGroup));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<ICourierGroup>>) {
        result.subscribe((res: HttpResponse<ICourierGroup>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess() {
        this.isSaving = false;
        this.previousState();
    }

    private onSaveError() {
        this.isSaving = false;
    }

    private onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }

    trackCourierById(index: number, item: ICourier) {
        return item.id;
    }

    getSelected(selectedVals: Array<any>, option: any) {
        if (selectedVals) {
            for (let i = 0; i < selectedVals.length; i++) {
                if (option.id === selectedVals[i].id) {
                    return selectedVals[i];
                }
            }
        }
        return option;
    }
    get courierGroup() {
        return this._courierGroup;
    }

    set courierGroup(courierGroup: ICourierGroup) {
        this._courierGroup = courierGroup;
    }
}
