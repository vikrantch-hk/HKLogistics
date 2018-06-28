import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpErrorResponse, HttpHeaders, HttpResponse } from '@angular/common/http';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs';
import { JhiEventManager, JhiParseLinks, JhiAlertService } from 'ng-jhipster';

import { ICourier } from 'app/shared/model/courier.model';
import { Principal } from 'app/core';

import { ITEMS_PER_PAGE } from 'app/shared';
import { CourierService } from './courier.service';
import { ICourierGroup } from 'app/shared/model/courier-group.model';
import { CourierGroupService } from 'app/entities/courier-group';
import { ICourierChannel } from 'app/shared/model/courier-channel.model';
import { CourierChannelService } from 'app/entities/courier-channel';

@Component({
    selector: 'jhi-courier',
    templateUrl: './courier.component.html'
})
export class CourierComponent implements OnInit, OnDestroy {
    currentAccount: any;
    couriers: ICourier[];
    error: any;
    success: any;
    eventSubscriber: Subscription;
    currentSearch: string;
    routeData: any;
    links: any;
    totalItems: any;
    queryCount: any;
    itemsPerPage: any;
    page: any;
    predicate: any;
    previousPage: any;
    reverse: any;
    filterOptions: String[];
    couriergroups: ICourierGroup[];
    courierchannels: ICourierChannel[];

    constructor(
        private courierService: CourierService,
        private courierGroupService: CourierGroupService,
        private parseLinks: JhiParseLinks,
        private jhiAlertService: JhiAlertService,
        private principal: Principal,
        private activatedRoute: ActivatedRoute,
        private router: Router,
        private eventManager: JhiEventManager
    ) {
        this.itemsPerPage = ITEMS_PER_PAGE;
        this.routeData = this.activatedRoute.data.subscribe(data => {
            this.page = data.pagingParams.page;
            this.previousPage = data.pagingParams.page;
            this.reverse = data.pagingParams.ascending;
            this.predicate = data.pagingParams.predicate;
        });
        this.currentSearch =
            this.activatedRoute.snapshot && this.activatedRoute.snapshot.params['search']
                ? this.activatedRoute.snapshot.params['search']
                : '';
        this.filterOptions =
            this.activatedRoute.snapshot && this.activatedRoute.snapshot.params['courierCriteria']
                ? this.activatedRoute.snapshot.params['courierCriteria'].split(',')
                : [];
    }

    loadAll() {
        if (this.currentSearch) {
            this.courierService
                .search({
                    page: this.page - 1,
                    query: this.currentSearch,
                    size: this.itemsPerPage,
                    sort: this.sort()
                })
                .subscribe(
                    (res: HttpResponse<ICourier[]>) => this.paginateCouriers(res.body, res.headers),
                    (res: HttpErrorResponse) => this.onError(res.message)
                );
            return;
        }
        this.courierService
            .query({
                page: this.page - 1,
                size: this.itemsPerPage,
                sort: this.sort()
            })
            .subscribe(
                (res: HttpResponse<ICourier[]>) => this.paginateCouriers(res.body, res.headers),
                (res: HttpErrorResponse) => this.onError(res.message)
            );
        if (this.filterOptions) {
            this.courierService
                .filter({
                    page: this.page - 1,
                    size: this.itemsPerPage,
                    sort: this.sort(),
                    courierCriteria : this.filterOptions
                })
                .subscribe(
                    (res: HttpResponse<ICourier[]>) => this.paginateCouriers(res.body, res.headers),
                    (res: HttpErrorResponse) => this.onError(res.message)
                );
            console.log('hii');
            return;
        }
    }

    loadPage(page: number) {
        if (page !== this.previousPage) {
            this.previousPage = page;
            this.transition();
        }
    }

    transition() {
        this.router.navigate(['/courier'], {
            queryParams: {
                page: this.page,
                size: this.itemsPerPage,
                search: this.currentSearch,
                sort: this.predicate + ',' + (this.reverse ? 'asc' : 'desc')
            }
        });
        this.router.navigate(['/courier'], {
            queryParams: {
                page: this.page,
                size: this.itemsPerPage,
                courierCriteria: this.filterOptions,
                sort: this.predicate + ',' + (this.reverse ? 'asc' : 'desc')
            }
        });
        this.loadAll();
    }

    clear() {
        this.page = 0;
        this.currentSearch = '';
        this.router.navigate([
            '/courier',
            {
                page: this.page,
                sort: this.predicate + ',' + (this.reverse ? 'asc' : 'desc')
            }
        ]);
        this.loadAll();
    }

    clearFilter() {
        this.page = 0;
        this.filterOptions = [];
        this.filterOptions[0] = '';
        this.filterOptions[1] = '';
        this.filterOptions[2] = '';
        this.router.navigate([
            '/courier',
            {
                page: this.page,
                sort: this.predicate + ',' + (this.reverse ? 'asc' : 'desc')
            }
        ]);
        this.loadAll();
    }

    search(query) {
        if (!query) {
            return this.clear();
        }
        this.page = 0;
        this.currentSearch = query;
        this.router.navigate([
            '/courier',
            {
                search: this.currentSearch,
                page: this.page,
                sort: this.predicate + ',' + (this.reverse ? 'asc' : 'desc')
            }
        ]);
        this.loadAll();
    }

    ngOnInit() {
        this.loadAll();
        this.principal.identity().then(account => {
            this.currentAccount = account;
        });
        this.registerChangeInCouriers();
        this.courierGroupService.query().subscribe(
            (res: HttpResponse<ICourierGroup[]>) => {
                this.couriergroups = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: ICourier) {
        return item.id;
    }

    registerChangeInCouriers() {
        this.eventSubscriber = this.eventManager.subscribe('courierListModification', response => this.loadAll());
    }

    sort() {
        const result = [this.predicate + ',' + (this.reverse ? 'asc' : 'desc')];
        if (this.predicate !== 'id') {
            result.push('id');
        }
        return result;
    }

    private paginateCouriers(data: ICourier[], headers: HttpHeaders) {
        this.links = this.parseLinks.parse(headers.get('link'));
        this.totalItems = parseInt(headers.get('X-Total-Count'), 10);
        this.queryCount = this.totalItems;
        this.couriers = data;
    }

    private onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }

    filter(filterOptions: String[]) {
        console.log('controller filter');
        this.page = 0;
        if (filterOptions[2] !== '' && !filterOptions[2].toString().startsWith('courierGroupId') ) {
            filterOptions[2] = 'courierGroupId:' + filterOptions[2];
        }
        this.router.navigate([
            '/courier',
            {
                    page: this.page,
                    size: this.itemsPerPage,
                    sort: this.sort(),
                    courierCriteria: this.filterOptions
                    }]);
        console.log('couriers');
        this.loadAll();
    }

    trackCourierGroupById(index: number, item: ICourierGroup) {
        return item.id;
    }

}
