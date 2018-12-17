package com.frame.hariko.springboot.actuator;

import com.alibaba.druid.stat.DruidDataSourceStatManager;
import com.alibaba.druid.stat.DruidStatManagerFacade;
import org.springframework.boot.actuate.endpoint.annotation.Endpoint;
import org.springframework.boot.actuate.endpoint.annotation.ReadOperation;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.*;


@ConfigurationProperties(prefix = "endpoints.druid")
@Endpoint(id = "druid")
public class DruidEndpoint {
    private static final DruidStatManagerFacade statManagerFacade = DruidStatManagerFacade.getInstance();

    @SuppressWarnings("unused")
	@ReadOperation
    public Druid invoke() {
        Druid druid = new Druid();
        Map<String ,Object> stat = new HashMap<>();

        druid.setBasicStat(statManagerFacade.returnJSONBasicStat());
        druid.setDataSourceStatDatas(statManagerFacade.getDataSourceStatDataList());
        druid.setActiveConnStackTraces(statManagerFacade.getActiveConnStackTraceList());

        List<Object> poolingConnections = new ArrayList<>();
        List<Object> wallStats = new ArrayList<>();
        Set<Object> datasources = DruidDataSourceStatManager.getInstances().keySet();
        for(Object datasource : datasources ){
            int id = System.identityHashCode(datasource);
            HashMap<Integer, Object> poolingConnection = new HashMap<>();
            poolingConnection.put(id,statManagerFacade.getPoolingConnectionInfoByDataSourceId(id));
            poolingConnections.add(poolingConnection);
            Map<Integer,Object> wallStat = new HashMap<>();
            wallStat.put(id,statManagerFacade.getWallStatMap(id));
            wallStats.add(wallStat);
        }

        druid.setPoolingConnections(poolingConnections);
        druid.setWallStats(wallStats);

        return druid;
    }
}
