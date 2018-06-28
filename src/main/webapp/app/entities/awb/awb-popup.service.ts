import { Injectable, Component } from '@angular/core';
import { Router } from '@angular/router';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { HttpResponse } from '@angular/common/http';
import { Awb } from './awb.model';
import { AwbService } from './awb.service';

@Injectable()
export class AwbPopupService {
    private ngbModalRef: NgbModalRef;

    constructor(
        private modalService: NgbModal,
        private router: Router,
        private awbService: AwbService

    ) {
        this.ngbModalRef = null;
    }

    open(component: Component, id?: number | any): Promise<NgbModalRef> {
        return new Promise<NgbModalRef>((resolve, reject) => {
            const isOpen = this.ngbModalRef !== null;
            if (isOpen) {
                resolve(this.ngbModalRef);
            }

            if (id) {
                this.awbService.find(id)
                    .subscribe((awbResponse: HttpResponse<Awb>) => {
                        const awb: Awb = awbResponse.body;
                        if (awb.createDate) {
                            awb.createDate = {
                                year: awb.createDate.getFullYear(),
                                month: awb.createDate.getMonth() + 1,
                                day: awb.createDate.getDate()
                            };
                        }
                        this.ngbModalRef = this.awbModalRef(component, awb);
                        resolve(this.ngbModalRef);
                    });
            } else {
                // setTimeout used as a workaround for getting ExpressionChangedAfterItHasBeenCheckedError
                setTimeout(() => {
                    this.ngbModalRef = this.awbModalRef(component, new Awb());
                    resolve(this.ngbModalRef);
                }, 0);
            }
        });
    }

    awbModalRef(component: Component, awb: Awb): NgbModalRef {
        const modalRef = this.modalService.open(component, { size: 'lg', backdrop: 'static'});
        modalRef.componentInstance.awb = awb;
        modalRef.result.then((result) => {
            this.router.navigate([{ outlets: { popup: null }}], { replaceUrl: true, queryParamsHandling: 'merge' });
            this.ngbModalRef = null;
        }, (reason) => {
            this.router.navigate([{ outlets: { popup: null }}], { replaceUrl: true, queryParamsHandling: 'merge' });
            this.ngbModalRef = null;
        });
        return modalRef;
    }
}
