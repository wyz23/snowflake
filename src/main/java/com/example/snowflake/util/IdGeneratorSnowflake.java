package com.example.snowflake.util;

import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.net.NetUtil;
import cn.hutool.core.util.IdUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * 使用 ############雪花算法############### 生成唯一ID之实践， 一秒钟可以生成大概26万个ID
 *
 * 优点：
 *  ① 毫秒数在高位，自增序列在低位，整个ID都是趋势递增的。
 *  ② 不依靠数据库等第三方系统，以服务的方式部署，稳定性更高，生成ID的性能也是非常高的。
 *  ③ 可以根据自身业务特性分配bit位，非常灵活
 *
 * 缺点：
 *  ①依赖机器时钟，如果机器时钟回拨，会导致重复ID生成
 *  ②在单机上是递增的，但是由于设计到分布式环境，每台机器上的时钟不可能完全同步，有时候会出现不是全局递增的情况
 *  （此缺点可以认为无所谓，一般分布式ID只要求趋势递增，并不会严格要求递增，90%的需求都只要求趋势递增）
 *
 * 时钟回拨解决方案，可以参考以下：
 *  ① 百度开源的分布式唯一ID生成器UidGenerator
 *  ② Leaf（叶子）--美团点评分布式ID生产系统
 */

@Component
@Slf4j
public class IdGeneratorSnowflake {
    // xx号机房，范围0~31
    private long workerId = 0;
    // xx号机器，范围0~31
    private long datacenterId = 0;

    private Snowflake snowflake = IdUtil.createSnowflake(workerId, datacenterId);

    @PostConstruct // 作用，加载一些初始化的工作
    public void init() {
        try {
        // NetUtil.ipv4ToLong: 获取网络机房的IP
        workerId = NetUtil.ipv4ToLong(NetUtil.getLocalhostStr()); // 获取本机workerId
        log.info("当前机器的wokerId：{}", workerId);
        } catch (Exception e) {
            e.printStackTrace();
            log.warn("当前机器的wokerId获取失败", e);
            workerId = NetUtil.getLocalhostStr().hashCode();
        }
    }

    public synchronized long snowflakeId() {
        return snowflake.nextId();
    }

    public synchronized long snowflakeId(long workerId, long datacenterId) {
        Snowflake snowflake = IdUtil.createSnowflake(workerId, datacenterId);
        return snowflake.nextId();
    }

    public static void main(String[] args) {
        System.out.println(new IdGeneratorSnowflake().snowflakeId());
    }
}
