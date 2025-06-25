package com.miyu.module.Influxdb.service;

import com.miyu.module.Influxdb.dal.CommonCollection;
import com.miyu.module.Influxdb.dal.CommonCollection;
import org.influxdb.InfluxDB;
import org.influxdb.dto.BatchPoints;
import org.influxdb.dto.Point;
import org.influxdb.dto.Query;
import org.influxdb.dto.QueryResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Component
public class DataCollectionService {

    @Resource
    private InfluxDB influxDB;
    //数据库
    @Value("${spring.influx.database}")
    private String database;


    /**
     * 设置数据保存策略 defalut 策略名 /database 数据库名/ 30d 数据保存时限30天/ 1 副本个数为1/ 结尾DEFAULT
     * 表示 设为默认的策略
     */
    public void createRetentionPolicy() {
        String command = String.format("CREATE RETENTION POLICY \"%s\" ON \"%s\" DURATION %s REPLICATION %s DEFAULT",
                "defalut", database, "30d", 1);
        this.query(command);
    }

    /**
     * 插入
     *
     * @param measurement 表
     * @param tags        标签
     * @param fields      字段
     */
    public void insert(String measurement, Map<String, String> tags, Map<String, Object> fields) {
        Point.Builder builder = Point.measurement(measurement);
        builder.time(System.currentTimeMillis(), TimeUnit.MILLISECONDS);
        builder.tag(tags);
        builder.fields(fields);
        influxDB.write(database, "", builder.build());
    }

    /**
     * 无时间插入
     *
     */
    public void insert(CommonCollection commonConllection) {
        this.insert(commonConllection.getMeasurement(),commonConllection.getTags(),commonConllection.getFields());
    }

    /**
     * @param measurement
     * @param time
     * @param tags
     * @param fields
     * @return void
     * @desc 插入, 带时间time
     * @date 2021/3/27
     */
    public void insert(String measurement, long time, Map<String, String> tags, Map<String, Object> fields) {
        Point.Builder builder = Point.measurement(measurement);
        builder.time(time, TimeUnit.MILLISECONDS);
        builder.tag(tags);
        builder.fields(fields);
        influxDB.write(database, "", builder.build());
    }

    /**
     * @param measurement
     * @param time
     * @param tags
     * @param fields
     * @return void
     * @desc influxDB开启UDP功能, 默认端口:8089,默认数据库:udp,没提供代码传数据库功能接口
     * @date 2021/3/13
     */
    public void insertUDP(String measurement, long time, Map<String, String> tags, Map<String, Object> fields) {
        Point.Builder builder = Point.measurement(measurement);
        builder.time(time, TimeUnit.MILLISECONDS);
        builder.tag(tags);
        builder.fields(fields);
        int udpPort = 8089;
        influxDB.write(udpPort, builder.build());
    }

    /**
     * 查询
     *
     * @param command 查询语句
     * @return
     */
    public QueryResult query(String command) {
        return influxDB.query(new Query(command, database));
    }

    /**
     * @param queryResult
     * @desc 查询结果处理
     * @date 2021/5/12
     */
    public List<Map<String, Object>> queryResultProcess(QueryResult queryResult) {
        List<Map<String, Object>> mapList = new ArrayList<>();
        List<QueryResult.Result> resultList = queryResult.getResults();
        //把查询出的结果集转换成对应的实体对象，聚合成list
        for (QueryResult.Result query : resultList) {
            List<QueryResult.Series> seriesList = query.getSeries();
            if (seriesList != null && seriesList.size() != 0) {
                for (QueryResult.Series series : seriesList) {
                    List<String> columns = series.getColumns();
                    String[] keys = columns.toArray(new String[columns.size()]);
                    List<List<Object>> values = series.getValues();
                    if (values != null && values.size() != 0) {
                        for (List<Object> value : values) {
                            Map<String, Object> map = new HashMap(keys.length);
                            for (int i = 0; i < keys.length; i++) {
                                map.put(keys[i], value.get(i));
                            }
                            mapList.add(map);
                        }
                    }
                }
            }
        }
        return mapList;
    }

    /**
     * @desc InfluxDB 查询 count总条数
     * @date 2021/4/8
     */
    public long countResultProcess(QueryResult queryResult) {
        long count = 0;
        List<Map<String, Object>> list = queryResultProcess(queryResult);
        if (list != null && list.size() != 0) {
            Map<String, Object> map = list.get(0);
            double num = (Double) map.get("count");
            count = new Double(num).longValue();
        }
        return count;
    }

    /**
     * 创建
     *
     * @param dbName 创建数据库
     * @return
     */
    public void createDB(String dbName) {
        influxDB.createDatabase(dbName);
    }


    /**
     * 批量写入测点
     *
     * @param batchPoints
     */
    public void batchInsert(BatchPoints batchPoints) {
        influxDB.write(batchPoints);
    }

    /**
     * 批量写入数据
     *
     * @param database        数据库
     * @param retentionPolicy 保存策略
     * @param consistency     一致性
     * @param records         要保存的数据（调用BatchPoints.lineProtocol()可得到一条record）
     */
    public void batchInsert(final String database, final String retentionPolicy,
                            final InfluxDB.ConsistencyLevel consistency, final List<String> records) {
        influxDB.write(database, retentionPolicy, consistency, records);
    }

    /**
     * @param consistency
     * @param records
     * @desc 批量写入数据
     * @date 2021/3/19
     */
    public void batchInsert(final InfluxDB.ConsistencyLevel consistency, final List<String> records) {
        influxDB.write(database, "", consistency, records);
    }

}
