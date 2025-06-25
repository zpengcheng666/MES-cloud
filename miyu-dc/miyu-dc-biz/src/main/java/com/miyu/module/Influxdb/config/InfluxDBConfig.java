package com.miyu.module.Influxdb.config;

import org.influxdb.InfluxDB;
import org.influxdb.InfluxDBFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Component
@Configuration
public class InfluxDBConfig {

    @Value("${spring.influx.user}")
    private String userName;

    @Value("${spring.influx.password}")
    private String password;

    @Value("${spring.influx.url}")
    private String url;

    //数据库
    @Value("${spring.influx.database}")
    private String database;

    //保留策略
    private String retentionPolicy;

    public InfluxDBConfig() {
    }

    public InfluxDBConfig(String userName, String password, String url, String database) {
        this.userName = userName;
        this.password = password;
        this.url = url;
        this.database = database;
        // autogen默认的数据保存策略
        this.retentionPolicy = retentionPolicy == null || "".equals(retentionPolicy) ? "autogen" : retentionPolicy;
        influxDbBuild();
    }


    /**
     * 连接时序数据库；获得InfluxDB
     **/
    @Bean
    public InfluxDB influxDbBuild() {
        InfluxDB influxDB = InfluxDBFactory.connect(url, userName, password);
        influxDB.setDatabase(database);
        return influxDB;
    }

}