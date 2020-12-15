package com.beiwangshan.blog.pojo;

import java.io.Serializable;
import java.util.List;

/**
 * @className: com.beiwangshan.blog.pojo-> PageList
 * @description: 分页处理
 * @author: 曾豪
 * @createDate: 2020-12-14 23:25
 * @version: 1.0
 * @todo:
 */
public class PageList<T> implements Serializable {
    //做分页要多少数据
    /**
     * 当前页码
     */
    private long currentPages;
    /**
     * 总数量
     */
    private long totalCount;
    /**
     * 每一页有多少数量
     */
    private long pageSize;
    /**
     * 总页数 = 总的数量 / 每页数量
     */
    private long totalPage;
    /**
     * 是否是第一页
     */
    private boolean isFirst;
    /**
     * 是否是最后一页
     */
    private boolean isLast;

    private List<T> contents;

    public long getCurrentPages() {
        return currentPages;
    }

    public void setCurrentPages(long currentPages) {
        this.currentPages = currentPages;
    }

    public long getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(long totalCount) {
        this.totalCount = totalCount;
    }

    public long getPageSize() {
        return pageSize;
    }

    public void setPageSize(long pageSize) {
        this.pageSize = pageSize;
    }

    public long getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(long totalPage) {
        this.totalPage = totalPage;
    }

    public boolean isFirst() {
        return isFirst;
    }

    public void setFirst(boolean first) {
        isFirst = first;
    }

    public boolean isLast() {
        return isLast;
    }

    public void setLast(boolean last) {
        isLast = last;
    }

    public List<T> getContents() {
        return contents;
    }

    public void setContents(List<T> contents) {
        this.contents = contents;
    }
}
