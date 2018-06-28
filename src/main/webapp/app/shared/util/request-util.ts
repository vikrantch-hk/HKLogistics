import { HttpParams } from '@angular/common/http';

export const createRequestOption = (req?: any): HttpParams => {
    let options: HttpParams = new HttpParams();
    console.log('createRequestOption');
    if (req) {
            Object.keys(req).forEach(key => {
                if (key !== 'sort' && key !== 'courierCriteria') {
                    options = options.set(key, req[key]);
                }
            });
    if (req.courierCriteria) {
        req.courierCriteria.forEach(criterion => {
            if (criterion.startsWith('active') ) {
                console.log('courierCriteria ' + criterion.split(':')[0] + criterion.split(':')[1]);
                options = options.set(criterion.split(':')[0] + '.equals', criterion.split(':')[1]);
            }
            if (criterion.startsWith('operations') ) {
                console.log('courierCriteria ' + criterion.split(':')[1] + criterion.split(':')[2]);
                options = options.set(criterion.split(':')[1] + '.equals', criterion.split(':')[2]);
            }
            if (criterion.startsWith('courierGroupId') ) {
                console.log('courierCriteria ' + criterion.split(':')[0] + criterion.split(':')[1]);
                options = options.set(criterion.split(':')[0] + '.equals', criterion.split(':')[1]);
            }
        });
        if (req.sort) {
            req.sort.forEach(val => {
                options = options.append('sort', val);
            });
        }
    }

}
    return options;
};
