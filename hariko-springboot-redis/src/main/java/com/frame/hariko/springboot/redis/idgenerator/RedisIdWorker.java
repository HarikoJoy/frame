package com.frame.hariko.springboot.redis.idgenerator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Snowflake  64位ID (1(符号) +41(毫秒)+5(机器ID)+5(业务Id)+12(重复累加))
 */
public class RedisIdWorker implements IdWorker<Long> {

    private static final Logger logger = LoggerFactory.getLogger(RedisIdWorker.class);
    //公司成立timestamp
    private static final long PP_TIMESTAMP = 0x13b_8ab1_b400L;
    // 机器Id位数
    private static final long MACHINE_ID_BITS = 5L;
    // 业务id位数
    private static final long BUSINESS_ID_BITS = 5L;
    // 毫秒内增量位数
    private static final long COUNT_BITS = 12L;
    // 机器Id最大值(31)
    private static final long MAX_MACHINE_ID = ~(-1L << MACHINE_ID_BITS);
    // 业务ID最大值(31)
    private static final long MAX_BUSINESS_ID = ~(-1L << BUSINESS_ID_BITS);
    // 毫秒内增量最大值(4095)
    private static final long MAX_COUNT = ~(-1L << COUNT_BITS);
    // 业务Id偏左移12位
    private static final long BUSINESS_ID_SHIFT = COUNT_BITS;
    // 机器Id左移17位
    private static final long MACHINE_ID_SHIFT = COUNT_BITS + BUSINESS_ID_BITS;
    // 时间毫秒左移22位
    private static final long TIMESTAMP_LEFT_SHIFT = COUNT_BITS + BUSINESS_ID_BITS + MACHINE_ID_BITS;

    private final long machineId;
    private final long businessId;

    private static long lastTimestamp = -1L;
    private long count = 0L;

    /**
     * @param machineId  机器Id
     * @param businessId 业务类型Id
     */
    public RedisIdWorker(long machineId, long businessId) {
        if (machineId > MAX_MACHINE_ID || machineId < 0) {
            logger.error("machine Id can't be greater than" + MAX_MACHINE_ID + "or less than 0");

            throw new IllegalArgumentException("machine Id can't be greater than %d or less than 0");
        }
        if (businessId > MAX_BUSINESS_ID || businessId < 0) {
            logger.error("business Id can't be greater than" + MAX_BUSINESS_ID + "or less than 0");

            throw new IllegalArgumentException("business Id can't be greater than %d or less than 0");
        }
        this.machineId = machineId;
        this.businessId = businessId;
    }

    /**
     * @return Id
     */
    @Override
    public synchronized Long nextId() {
        long timestamp = getCurrentMillis();
        if (timestamp < lastTimestamp) {
            try {
                throw new IllegalArgumentException("Clock moved backwards.  Refusing to generate id for " + (lastTimestamp - timestamp) +
                        " milliseconds");
            } catch (Exception e) {
                logger.error("Clock moved backwards.", e);
            }
        }

        if (lastTimestamp == timestamp) {
            // 当前毫秒内，则+1
            count = (count + 1) & MAX_COUNT;
            if (count == 0) {
                // 当前毫秒内计数满了，则等待下一毫秒
                timestamp = tilNextMillis(lastTimestamp);
            }
        } else {
            count = 0;
        }
        lastTimestamp = timestamp;
        // ID偏移组合生成最终的ID，并返回ID
        return ((timestamp - PP_TIMESTAMP) << TIMESTAMP_LEFT_SHIFT)
                | (businessId << BUSINESS_ID_SHIFT)
                | (machineId << MACHINE_ID_SHIFT) | count;
    }

    private long tilNextMillis(final long lastTimestamp) {
        long timestamp = this.getCurrentMillis();
        while (timestamp <= lastTimestamp) {
            timestamp = this.getCurrentMillis();
        }
        return timestamp;
    }

    private long getCurrentMillis() {
        return System.currentTimeMillis();
    }
}
