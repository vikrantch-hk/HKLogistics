package com.hk.logistics.service.dto;

import java.io.Serializable;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;






/**
 * Criteria class for the Channel entity. This class is used in ChannelResource to
 * receive all the possible filtering options from the Http GET request parameters.
 * For example the following could be a valid requests:
 * <code> /channels?id.greaterThan=5&amp;attr1.contains=something&amp;attr2.specified=false</code>
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class ChannelCriteria implements Serializable {
    private static final long serialVersionUID = 1L;


    private LongFilter id;

    private StringFilter name;

    private StringFilter store;

    private LongFilter courierChannelId;

    public ChannelCriteria() {
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getName() {
        return name;
    }

    public void setName(StringFilter name) {
        this.name = name;
    }

    public StringFilter getStore() {
        return store;
    }

    public void setStore(StringFilter store) {
        this.store = store;
    }

    public LongFilter getCourierChannelId() {
        return courierChannelId;
    }

    public void setCourierChannelId(LongFilter courierChannelId) {
        this.courierChannelId = courierChannelId;
    }

    @Override
    public String toString() {
        return "ChannelCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (name != null ? "name=" + name + ", " : "") +
                (store != null ? "store=" + store + ", " : "") +
                (courierChannelId != null ? "courierChannelId=" + courierChannelId + ", " : "") +
            "}";
    }

}
