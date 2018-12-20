package com.frame.hariko.mybatis.page;

import com.github.pagehelper.IPage;

public class Pagination implements IPage {

    /**
     * 最大每页条数，防止前端传递参数值过大把整个数据库拖垮
     */
    private static final int MAX_PAGE_SIZE = 1000;

    private Integer pageNum;
    private Integer pageSize;
    private String orderBy;

    public Pagination(int pageNum, int pageSize){
        this.pageNum = pageNum;
        this.pageSize = pageSize;
    }

    public Pagination(Integer pageNum, Integer pageSize, String orderBy) {
        this.pageNum = pageNum;
        this.pageSize = pageSize;
        this.orderBy = orderBy;
    }

    public Pagination() {
    }

    public void setPageNum(Integer pageNum) {
        this.pageNum = pageNum;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
    }

    @Override
    public Integer getPageNum() {
        return pageNum;
    }

    @Override
    public Integer getPageSize() {
        return pageSize == null || pageSize > MAX_PAGE_SIZE ? MAX_PAGE_SIZE : pageSize;
    }

    @Override
    public String getOrderBy() {
        return orderBy;
    }
}
