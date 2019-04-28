package br.com.zupfilms.model;

public class FilterIDAndPageSize {

    private final int pageSize;
    private final String filterID;

    public Integer getPageSize() {
        return pageSize;
    }

    public String getFilterID() {
        return filterID;
    }

    public FilterIDAndPageSize(int pageSize, String filterID) {
        this.pageSize = pageSize;
        this.filterID = filterID;
    }
}
