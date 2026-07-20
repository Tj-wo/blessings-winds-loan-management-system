package com.alganiug.systems.loanManagement.views;

import com.alganiug.systems.loanManagement.core.services.GenericService;
import com.alganiug.systems.loanManagement.models.base.BaseEntity;

import javax.annotation.PostConstruct;
import java.io.Serializable;
import java.util.Collections;
import java.util.List;

public abstract class EntityView<T extends BaseEntity> implements Serializable {

    private static final long serialVersionUID = 1L;

    private List<T> records = Collections.emptyList();
    private int offset;
    private int pageSize = 25;
    private long totalRecords;

    protected abstract GenericService<T> getService();

    @PostConstruct
    public void init() {
        reload();
    }

    public void reload() {
        records = getService().getInstances(offset, pageSize);
        totalRecords = getService().countInstances();
    }

    public void delete(T record) {
        getService().deleteInstance(record);
        reload();
    }

    protected void setRecords(List<T> records) {
        this.records = records == null ? Collections.emptyList() : records;
        this.totalRecords = this.records.size();
    }

    public List<T> getRecords() {
        return records;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public long getTotalRecords() {
        return totalRecords;
    }
}
