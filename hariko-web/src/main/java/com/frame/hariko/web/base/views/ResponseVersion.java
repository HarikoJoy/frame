package com.frame.hariko.web.base.views;

public enum ResponseVersion {

    /**
     * 此版本数据主要是用来前端兼容,便于前端区分是老格式的json还是新框架的json格式;
     * 此数据固定为4,为了和app4.0版本的概念统一.
     */
    VERSION_4(4);

    private int version;

    ResponseVersion(int version){
        this.version = version;
    }

    public int getVersion(){
        return this.version;
    }
}
