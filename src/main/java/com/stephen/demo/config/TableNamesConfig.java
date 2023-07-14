package com.stephen.demo.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@Data
@Component
@ConfigurationProperties(prefix = "shardingjdbc")
public class TableNamesConfig {

    /**
     * 分表的表名,可多个
     */
    List<String> tableNames;

    /**
     * 起始年
     */
    int startYear;

    /**
     * 数据源别名
     */
    String datasourceName;

    /**
     * 分表字段
     */
    String keyColumn;

}