package com.frame.hariko.springboot.redis.idgenerator;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.util.StringUtils;

public class RedisIdGenerator implements IdGenerator<Long> {

    Logger logger = LoggerFactory.getLogger(RedisIdGenerator.class);

    private RedisTemplate<Object, Object> redisTemplate;
    private IdWorker<Long> worker;
    private String serviceIp;
    private Integer servicePort;
    private Short businessId;
    private static final String MACHINE_ID_HASH_KEY = "IDGENERATOR:REDIS:MACHINE:ID:HASHKEY";
    private static final String SCRIPT = "local val = redis.call('HGET',KEYS[1],ARGV[1]); if(val == nil or (type(val) == 'boolean' and val)) then return val; else local machineId = redis.call('HLEN', KEYS[1]) + 1; redis.call('HSET',KEYS[1],ARGV[1], machineId); return machineId; end";

    private DefaultRedisScript<Long> redisScript;

    /**
     * @param serviceIp     服务Ip
     * @param servicePort   服务端口号
     * @param businessId    业务类型Id
     * @param redisTemplate redisTemplate
     */
    public RedisIdGenerator(String serviceIp, Integer servicePort, Short businessId, RedisTemplate<Object, Object> redisTemplate) {
        if (StringUtils.isEmpty(serviceIp)) {
            String ipAddress = NetworkInterfaceManager.INSTANCE.getLocalHostAddress();
            if (null == ipAddress) {
                throw new IdGeneratorException("获取ip地址失败！");
            }
            this.serviceIp = ipAddress;
        }
        logger.info("RedisIdGenerator Get Local Ip Address: {},", this.serviceIp);
        if (servicePort == null || servicePort == 0) {
            throw new IdGeneratorException("获取端口号失败！");
        }

        if (redisTemplate == null) {
            throw new IdGeneratorException("获取redisTemplate失败！");
        }

        this.redisTemplate = redisTemplate;
        this.servicePort = servicePort;
        this.businessId = businessId == null ? 0 : businessId;
        this.redisScript = new DefaultRedisScript<>();
        this.redisScript.setScriptText(SCRIPT);
        this.redisScript.setResultType(Long.class);
    }


    public Long generateId() {
        return doNextId();
    }

    public Long doNextId() {
        if (worker == null) {
            Long machineId = getMachineId(String.format("%s:%s", serviceIp.trim(), servicePort));
            worker = new RedisIdWorker(machineId, businessId);
        }

        return worker.nextId();
    }

    /**
     * 获取机器ID
     *
     * @param address IP+PORT
     * @return 机器ID
     */
    private Long getMachineId(String address) {
        List<Object> keys = new ArrayList<>();
        keys.add(MACHINE_ID_HASH_KEY);
        List<Object> argv = new ArrayList<>();
        argv.add(address);
        Long machineId = redisTemplate.execute(redisScript, keys, address);
        logger.info("IdGenerator get machineId:[{}]", machineId);
        if (machineId == null || machineId == 0L) {
            throw new IdGeneratorException("获取机器Id异常");
        }
        return machineId;
    }
}
