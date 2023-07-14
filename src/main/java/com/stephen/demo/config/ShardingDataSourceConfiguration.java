package com.stephen.demo.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.apache.shardingsphere.api.config.sharding.ShardingRuleConfiguration;
import org.apache.shardingsphere.api.config.sharding.TableRuleConfiguration;
import org.apache.shardingsphere.api.config.sharding.strategy.StandardShardingStrategyConfiguration;
import org.apache.shardingsphere.shardingjdbc.api.ShardingDataSourceFactory;
import org.apache.shardingsphere.shardingjdbc.spring.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.*;


/**
 * @className: ShardingDataSourceConfiguration
 * @description:
 * @author: sh.Liu
 * @date: 2020-11-03 19:23
 */
// @Order(value = 1)
@Configuration
// @MapperScan(basePackages = "com.stephen.demo.mapper")
@EnableAutoConfiguration(exclude = {SpringBootConfiguration.class})
// @EnableAutoConfiguration(exclude = {SpringBootConfiguration.class, DataSourceAutoConfiguration.class})
public class ShardingDataSourceConfiguration {

    // @Value("${shardingsphere.datasourceName}")
    // private String ShardingDataSourceName;

    @Resource
    private TableNamesConfig tableNamesConfig;
 
    @Bean("shardingDataSource")
    // @Primary
    public DataSource getShardingDataSource() throws SQLException, ReflectiveOperationException {

        ShardingRuleConfiguration shardingRuleConfig = new ShardingRuleConfiguration();
        // 广播表
        // List<String> broadcastTables = new LinkedList<>();
        // broadcastTables.add("t_user");
        // broadcastTables.add("t_address");
        // shardingRuleConfig.setBroadcastTables(broadcastTables);
 
        // 默认策略
        // shardingRuleConfig.setDefaultDatabaseShardingStrategyConfig(new InlineShardingStrategyConfiguration("user_id", "ds0"));
        // shardingRuleConfig.setDefaultTableShardingStrategyConfig(new InlineShardingStrategyConfiguration("user_id", "t_order_${user_id % 4 + 1}"));
        // 获取表的分片规则配置
        // TableRuleConfiguration productTableRuleConfiguration = getProductTableRuleConfiguration();
        // TableRuleConfiguration stuTableRuleConfiguration = getStuTableRuleConfiguration();
        //

        shardingRuleConfig.setDefaultDataSourceName("ds1");
            shardingRuleConfig.getTableRuleConfigs().add(getProductTableRuleConfiguration());
            shardingRuleConfig.getTableRuleConfigs().add(getStuTableRuleConfiguration());
            shardingRuleConfig.getBindingTableGroups().add("product, stu");
            shardingRuleConfig.setDefaultTableShardingStrategyConfig(new StandardShardingStrategyConfiguration("nd", new NdPreciseShardingAlgorithm()));


        // String datasourceName = tableNamesConfig.getDatasourceName();
        // List<String> tableNames = tableNamesConfig.getTableNames();
        // String keyColumn = tableNamesConfig.getKeyColumn();
        // datasourceName = StringUtils.isBlank(datasourceName) ? "ds1" : datasourceName;
        // keyColumn = StringUtils.isBlank(keyColumn) ? "nd" : keyColumn;
        // tableNames = CollectionUtils.isEmpty(tableNames) ? Collections.singletonList("product") : tableNames;
        // for (String tableName : tableNames) {
        //     shardingRuleConfig.getTableRuleConfigs().add(getTableRuleConfiguration(datasourceName, tableName, keyColumn));
        // }
        // shardingRuleConfig.getBindingTableGroups().add("product, stu");
        // shardingRuleConfig.setDefaultTableShardingStrategyConfig(new StandardShardingStrategyConfiguration("nd", new NdPreciseShardingAlgorithm()));

        // Collection<TableRuleConfiguration> tableRuleConfigs = shardingRuleConfig.getTableRuleConfigs();
        // List<TableRuleConfiguration> tableRuleConfig = getTableRuleConfig();
        // tableRuleConfigs.addAll(tableRuleConfig);
        Properties props = new Properties();
        props.put("sql.show", "true");

        return ShardingDataSourceFactory.createDataSource(createDataSourceMap(), shardingRuleConfig, props);
    }
 
    /**
     * 配置真实数据源
     * @return 数据源map
     */
    private Map<String, DataSource> createDataSourceMap() throws ReflectiveOperationException {
        Map<String, DataSource> dataSourceMap = new HashMap<>();

        // 这里用多种数据源都可以
        // HikariDataSource
        DruidDataSource dataSource1 = new DruidDataSource();
        dataSource1.setDriverClassName("com.mysql.cj.jdbc.Driver");
        dataSource1.setUrl("jdbc:mysql://192.168.189.128/test?allowMultiQueries=true&rewriteBatchedStatements=true&useUnicode=true&characterEncoding=utf8&serverTimezone=GMT%2B8&useSSL=false");
        dataSource1.setUsername("root");
        dataSource1.setPassword("123456");
        dataSource1.setMaxActive(50);
        dataSource1.setInitialSize(1);
        dataSource1.setMaxWait(60000);
        dataSource1.setMinIdle(1);
        dataSource1.setKeepAlive(true);
        dataSource1.setMinEvictableIdleTimeMillis(300000);
        dataSource1.setTimeBetweenEvictionRunsMillis(60000);

        // // HikariDataSource
        // HikariDataSource dataSource1 = new HikariDataSource();
        // dataSource1.setDriverClassName("com.mysql.cj.jdbc.Driver");
        // dataSource1.setJdbcUrl("jdbc:mysql://192.168.189.128/test?allowMultiQueries=true&rewriteBatchedStatements=true&useUnicode=true&characterEncoding=utf8&serverTimezone=GMT%2B8&useSSL=false");
        // dataSource1.setUsername("root");
        // dataSource1.setPassword("123456");

        // Map<String, Object> dataSourceProperties = new HashMap<>();
        // dataSourceProperties.put("DriverClassName", "com.mysql.jdbc.Driver");
        // dataSourceProperties.put("jdbcUrl", "jdbc:mysql://192.168.189.128/test?serverTimezone=UTC&useSSL=false&useUnicode=true&characterEncoding=UTF-8");
        // dataSourceProperties.put("username", "root");
        // dataSourceProperties.put("password", "123456");

        // DataSource dataSource1 = DataSourceUtil.getDataSource("com.zaxxer.hikari.HikariDataSource", dataSourceProperties);
        // DataSource dataSource1 = DataSourceUtil.getDataSource("com.alibaba.druid.pool.DruidDataSource", dataSourceProperties);

        // Map<String, Object> dataSourceProperties2 = new HashMap<>();
        // dataSourceProperties2.put("DriverClassName", "com.mysql.jdbc.Driver");
        // dataSourceProperties2.put("jdbcUrl", "jdbc:mysql://localhost:3306/ds1?serverTimezone=UTC&useSSL=false&useUnicode=true&characterEncoding=UTF-8");
        // dataSourceProperties2.put("username", "root");
        // dataSourceProperties2.put("password", "root");
        //
        // DataSource dataSource2 = DataSourceUtil.getDataSource("com.zaxxer.hikari.HikariDataSource", dataSourceProperties2);

        String datasourceName = tableNamesConfig.getDatasourceName();
        datasourceName = StringUtils.isBlank(datasourceName) ? "ds1" : datasourceName;
        dataSourceMap.put(datasourceName, dataSource1);
        // dataSourceMap.put("ds1",dataSource2);
        return dataSourceMap;
    }

    private List<TableRuleConfiguration> getTableRuleConfig() {
        List<TableRuleConfiguration> resultList = Lists.newArrayList();
        String datasourceName = tableNamesConfig.getDatasourceName();
        List<String> tableNames = tableNamesConfig.getTableNames();
        String keyColumn = tableNamesConfig.getKeyColumn();
        datasourceName = StringUtils.isBlank(datasourceName) ? "ds1" : datasourceName;
        keyColumn = StringUtils.isBlank(keyColumn) ? "nd" : keyColumn;
        tableNames = CollectionUtils.isEmpty(tableNames) ? Collections.singletonList("product") : tableNames;
        for (String tableName : tableNames) {
            // 为user表配置数据节点
            String actualDataNodes = datasourceName + "." + tableName;
            TableRuleConfiguration ruleConfiguration = new TableRuleConfiguration(tableName, actualDataNodes);
            // 为user表配置分表分片策略及分片算法
            ruleConfiguration.setTableShardingStrategyConfig(new StandardShardingStrategyConfiguration(keyColumn, new NdPreciseShardingAlgorithm()));
            resultList.add(ruleConfiguration);
        }
        return resultList;
    }

    private TableRuleConfiguration getTableRuleConfiguration(String datasourceName, String tableName, String shardingKeyColumn) {
        // 为user表配置数据节点
        TableRuleConfiguration ruleConfiguration = new TableRuleConfiguration(tableName, datasourceName + "." + tableName);
        // 为user表配置分表分片策略及分片算法
        ruleConfiguration.setTableShardingStrategyConfig(new StandardShardingStrategyConfiguration(shardingKeyColumn, new NdPreciseShardingAlgorithm()));
        return ruleConfiguration;
    }
 
    /**
     * 配置user表的分片规则
     *
     * @return ser表的分片规则配置对象
     */
    private TableRuleConfiguration getProductTableRuleConfiguration() {
        // 为user表配置数据节点
        TableRuleConfiguration ruleConfiguration = new TableRuleConfiguration("product", "ds1.product");
        // 设置分片键
        String shardingKey = "nd";
        // 为user表配置分库分片策略及分片算法
        // ruleConfiguration.setDatabaseShardingStrategyConfig(new StandardShardingStrategyConfiguration(shardingKey, new NdPreciseShardingAlgorithm()));
        // 为user表配置分表分片策略及分片算法
        ruleConfiguration.setTableShardingStrategyConfig(new StandardShardingStrategyConfiguration(shardingKey, new NdPreciseShardingAlgorithm()));
        // ruleConfiguration.setKeyGeneratorConfig(new KeyGeneratorConfiguration("SNOWFLAKE", "order_id"));
        return ruleConfiguration;
    }

    /**
     * 配置stu表的分片规则
     *
     * @return 表的分片规则配置对象
     */
    private TableRuleConfiguration getStuTableRuleConfiguration() {
        // 为stu表配置数据节点
        TableRuleConfiguration ruleConfiguration = new TableRuleConfiguration("stu", "ds1.stu");
        // 设置分片键
        String shardingKey = "nd";
        // 为user表配置分库分片策略及分片算法
        // ruleConfiguration.setDatabaseShardingStrategyConfig(new StandardShardingStrategyConfiguration(shardingKey, new NdPreciseShardingAlgorithm()));
        // 为user表配置分表分片策略及分片算法
        ruleConfiguration.setTableShardingStrategyConfig(new StandardShardingStrategyConfiguration(shardingKey, new NdPreciseShardingAlgorithm()));
        // ruleConfiguration.setKeyGeneratorConfig(new KeyGeneratorConfiguration("SNOWFLAKE", "order_id"));
        return ruleConfiguration;
    }

}