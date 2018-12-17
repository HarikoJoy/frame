package com.frame.hariko.springboot.actuator;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class Druid {

    private List<Object> poolingConnections;
    private List<List<String>> activeConnStackTraces;
    private List<Map<String, Object>> dataSourceStatDatas;
    private Map<String, Object> basicStat;
    private List<Object> wallStats;

    public void setWallStats(List<Object> wallStats) {
        this.wallStats = wallStats;
    }

    public void setPoolingConnections(List<Object> poolingConnections) {
        this.poolingConnections = poolingConnections;
    }

    public void setActiveConnStackTraces(List<List<String>> activeConnStackTraces) {
        this.activeConnStackTraces = activeConnStackTraces;
    }

    public void setDataSourceStatDatas(List<Map<String, Object>> dataSourceStatDatas) {
        this.dataSourceStatDatas = dataSourceStatDatas;
    }

    public void setBasicStat(Map<String, Object> basicStat) {
        this.basicStat = basicStat;
    }

    public List<Object> getPoolingConnections() {
        return poolingConnections;
    }

    public List<List<String>> getActiveConnStackTraces() {
        return activeConnStackTraces;
    }

    public List<Map<String, Object>> getDataSourceStatDatas() {
        return dataSourceStatDatas;
    }

    public Map<String, Object> getBasicStat() {
        return basicStat;
    }

    public List<Object> getWallStats() {
        return wallStats;
    }
}
