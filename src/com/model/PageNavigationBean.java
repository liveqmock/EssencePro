package com.model;

import java.util.ArrayList;
import java.util.List;

public class PageNavigationBean<T> {
    private Integer currentPage = 1;
    private Integer pageSize = 30;

    private Integer totalCount = 0;

    private List<T> resultList = new ArrayList<T>();

    public Integer getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        if (currentPage > 1) {
            this.currentPage = currentPage;
        }
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        if (pageSize > 0) {
            this.pageSize = pageSize;
        }
    }

    public Integer getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        if (totalCount > 0) {
            this.totalCount = totalCount;
            if (currentPage > getTotalPage()) {
                currentPage = getTotalPage();
            }
        }
    }

    public Integer getTotalPage() {
        return (totalCount - 1) / pageSize + 1;
    }

    public Integer getCurrentPoint() {
        int currentPoint = (currentPage - 1) * pageSize + 1;
        if (currentPoint < 1) {
            return 1;
        }
        return currentPoint;
    }

    public List<T> getResultList() {
        return resultList;
    }

    public void setResultList(List<T> resultList) {
        if (resultList != null) {
            this.resultList = resultList;
        }
    }
}
