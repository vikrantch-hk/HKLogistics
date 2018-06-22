import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { IRegionType } from 'app/shared/model/region-type.model';
import { Principal } from 'app/core';
import { RegionTypeService } from './region-type.service';

@Component({
    selector: 'jhi-region-type',
    templateUrl: './region-type.component.html'
})
export class RegionTypeComponent implements OnInit, OnDestroy {
    regionTypes: IRegionType[];
    currentAccount: any;
    eventSubscriber: Subscription;
    currentSearchPriority: string;
    currentSearchName: string;

    constructor(
        private regionTypeService: RegionTypeService,
        private jhiAlertService: JhiAlertService,
        private eventManager: JhiEventManager,
        private activatedRoute: ActivatedRoute,
        private principal: Principal
    ) {
        this.currentSearchPriority =
            this.activatedRoute.snapshot && this.activatedRoute.snapshot.params['searchPriority']
                ? this.activatedRoute.snapshot.params['searchPriority']
                : '';
        this.currentSearchName =
            this.activatedRoute.snapshot && this.activatedRoute.snapshot.params['searchName']
                ? this.activatedRoute.snapshot.params['searchName']
                : '';
    }

    loadAll() {
        if (this.currentSearchPriority) {
            this.regionTypeService
                .searchPriority({
                    query: this.currentSearchPriority
                })
                .subscribe(
                    (res: HttpResponse<IRegionType[]>) => (this.regionTypes = res.body),
                    (res: HttpErrorResponse) => this.onError(res.message)
                );
                this.currentSearchName = '';
            return;
        }
        if (this.currentSearchName) {
            this.regionTypeService
                .searchName({
                    query: this.currentSearchName
                })
                .subscribe(
                    (res: HttpResponse<IRegionType[]>) => (this.regionTypes = res.body),
                    (res: HttpErrorResponse) => this.onError(res.message)
                );
                this.currentSearchPriority = '';
            return;
        }
        this.regionTypeService.query().subscribe(
            (res: HttpResponse<IRegionType[]>) => {
                this.regionTypes = res.body;
                this.currentSearchPriority = '';
                this.currentSearchName = '';
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
    }

    searchPriority(query) {
        if (!query) {
            return this.clearPriority();
        }
        console.log('hii');
        this.currentSearchPriority = query;
        this.loadAll();
    }

    clearPriority() {
        this.currentSearchPriority = '';
        this.loadAll();
    }

    searchName(query) {
        if (!query) {
            return this.clearName();
        }
        console.log('hii');
        this.currentSearchName = query;
        this.loadAll();
    }

    clearName() {
        this.currentSearchName = '';
        this.loadAll();
    }

    ngOnInit() {
        this.loadAll();
        this.principal.identity().then(account => {
            this.currentAccount = account;
        });
        this.registerChangeInRegionTypes();
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: IRegionType) {
        return item.id;
    }

    registerChangeInRegionTypes() {
        this.eventSubscriber = this.eventManager.subscribe('regionTypeListModification', response => this.loadAll());
    }

    private onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }
}
