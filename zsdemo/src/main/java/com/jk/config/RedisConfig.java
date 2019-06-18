package com.jk.config;
/**
 *
 * @ClassName: RedisConfig
 * @Description: redis配置
 * @author cheng
 * @date 2018年3月7日 上午11:53:11
 */
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

@Configuration
@PropertySource(value = { "classpath:application.properties" })
public class RedisConfig {
    /**
     * 日志管理
     */
    private Logger log = LoggerFactory.getLogger(RedisConfig.class);

    @Value("${spring.redis.host}")
    private String host;

    @Value("${spring.redis.port}")
    private int port;

   /* @Value("${spring.redis.password}")
    private String password;
*/
    @Value("${spring.redis.timeout}")
    private int timeout;

    /**
     *
     * @Title: getJedisPool
     * @Description: 获取jedisPool
     * @return
     */
    @Bean
    public JedisPool getJedisPool(){
        log.info("==>初始化jedis连接池");
        JedisPoolConfig config = new JedisPoolConfig();
        JedisPool pool = new JedisPool(config, host, port,timeout);
        return pool;
    }
}
