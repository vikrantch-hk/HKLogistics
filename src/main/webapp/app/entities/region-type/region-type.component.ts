import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { IRegionType } from 'app/shared/model/region-type.model';
import { Principal } from 'app/core';
import { RegionTypeService } from './region-type.service';
import * as XLSX from 'xlsx';

@Component({
    selector: 'jhi-region-type',
    templateUrl: './region-type.component.html'
})
export class RegionTypeComponent implements OnInit, OnDestroy {
    regionTypes: IRegionType[];
    currentAccount: any;
    eventSubscriber: Subscription;
    currentSearch: string;

    constructor(
        private regionTypeService: RegionTypeService,
        private jhiAlertService: JhiAlertService,
        private eventManager: JhiEventManager,
        private activatedRoute: ActivatedRoute,
        private principal: Principal
    ) {
        this.currentSearch =
            this.activatedRoute.snapshot && this.activatedRoute.snapshot.params['search']
                ? this.activatedRoute.snapshot.params['search']
                : '';
    }

    loadAll() {
        if (this.currentSearch) {
            this.regionTypeService
                .search({
                    query: this.currentSearch
                })
                .subscribe(
                    (res: HttpResponse<IRegionType[]>) => (this.regionTypes = res.body),
                    (res: HttpErrorResponse) => this.onError(res.message)
                );
            return;
        }
        this.regionTypeService.query().subscribe(
            (res: HttpResponse<IRegionType[]>) => {
                this.regionTypes = res.body;
                this.currentSearch = '';
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
    }

    search(query) {
        if (!query) {
            return this.clear();
        }
        this.currentSearch = query;
        this.loadAll();
    }

    clear() {
        this.currentSearch = '';
        this.loadAll();
    }

    upload(event) {
        const elem = event.target;
        if (elem.files.length > 0) {
          const fileSelected: File = elem.files[0];
          this.regionTypeService.uploadFile(fileSelected)
             .subscribe( response => {
          console.log('set any success actions...');
          this.jhiAlertService.success('uploaded file please refresh after sometime', null, null);
         // this.loadAll();
          return response;
    }.
     error => {
       console.log(error.message);
       this.jhiAlertService.error(error.statusText, null, null);
     });
        event.target.value = null;
        }
    }

    ExportToExcel() {
      const ws: XLSX.WorkSheet = XLSX.utils.json_to_sheet(this.regionTypes);
      const wb: XLSX.WorkBook = XLSX.utils.book_new();
      XLSX.utils.book_append_sheet(wb, ws, 'Sheet1');
      /* save to file */
      XLSX.writeFile(wb, 'ExportSheet.xlsx');
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
