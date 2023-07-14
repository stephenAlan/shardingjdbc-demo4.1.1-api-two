package com.stephen.demo;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.stephen.demo.config.TableNamesConfig;
import lombok.extern.slf4j.Slf4j;
import org.apache.shardingsphere.core.rule.ShardingRule;
import org.apache.shardingsphere.core.rule.TableRule;
import org.apache.shardingsphere.shardingjdbc.jdbc.core.datasource.ShardingDataSource;
import org.apache.shardingsphere.underlying.common.rule.DataNode;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.sql.DataSource;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@EnableScheduling
// @SpringBootApplication(exclude = SpringBootConfiguration.class)
@SpringBootApplication
// @MapperScan(basePackages = "com.stephen.demo.mapper")
public class DemoApplication {

    @Resource
    private TableNamesConfig tableNamesConfig;
    @Resource
    private DataSource dataSource;
    @Resource
    private DefaultListableBeanFactory beanFactory;

    @Resource
    private ShardingDataSource shardingDataSource;

    // @Resource
    // private SpringBootShardingRuleConfigurationProperties shardingRuleConfigurationProperties;

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }




    // 定时任务执行
    @PostConstruct
    // @Scheduled(cron = "0/10 * * * * ?")
    public void actualTablesRefresh() throws NoSuchFieldException, IllegalAccessException {

        log.info("===============dataSourceName before===============:{}", dataSource.getClass().getName());

        // beanFactory.destroySingleton("dataSource");
        // 覆盖掉dataSource
        // beanFactory.registerSingleton("dataSource", getAnotherDataSource());

        // Object dataSourceBean = SpringContextUtil.getBean("dataSource");
        // log.info("===============dataSource bean after===============:{}", dataSourceBean.getClass().getName());
        log.info("===============dataSourceName after===============:{}", this.dataSource.getClass().getName());

        // int startYear = tableNamesConfig.getStartYear();
        int endYear = getThisYear();

        // ShardingDataSource shardingDataSource = (ShardingDataSource) this.dataSource;
        ShardingRule shardingRule = shardingDataSource.getRuntimeContext().getRule();
        Collection<TableRule> tableRules = shardingRule.getTableRules();

        for (TableRule tableRule : tableRules) {
            int startYear = tableNamesConfig.getStartYear();
            List<DataNode> dataNodes = tableRule.getActualDataNodes();
            Field actualDataNodesField = TableRule.class.getDeclaredField("actualDataNodes");
            Field modifiersField = Field.class.getDeclaredField("modifiers");
            modifiersField.setAccessible(true);
            modifiersField.setInt(actualDataNodesField, actualDataNodesField.getModifiers() & ~Modifier.FINAL);

            String dataSourceName = dataNodes.get(0).getDataSourceName();
            String logicTableName = tableRule.getLogicTable();
            StringBuilder stringBuilder = new StringBuilder()
                    .append(dataSourceName).append(".").append(logicTableName);
            final int length = stringBuilder.length();
            List<DataNode> newDataNodes = new ArrayList<>();
            while (true) {
                stringBuilder.setLength(length);
                stringBuilder.append("_");
                stringBuilder.append(startYear);
                DataNode dataNode = new DataNode(stringBuilder.toString());
                newDataNodes.add(dataNode);
                int nextYear = startYear++;

                if (nextYear >= endYear) {
                    break;
                }
            }
            actualDataNodesField.setAccessible(true);
            actualDataNodesField.set(tableRule, newDataNodes);

            Set<String> actualTables = Sets.newHashSet();
            Map<DataNode, Integer> dataNodeIntegerMap = Maps.newHashMap();

            AtomicInteger a = new AtomicInteger(0);
            newDataNodes.forEach((dataNode -> {
                actualTables.add(dataNode.getTableName());
                if (a.intValue() == 0){
                    a.incrementAndGet();
                    dataNodeIntegerMap.put(dataNode, 0);
                }else {
                    dataNodeIntegerMap.put(dataNode, a.intValue());
                    a.incrementAndGet();
                }
            }));

            //动态刷新：actualTables
            Field actualTablesField = TableRule.class.getDeclaredField("actualTables");
            actualTablesField.setAccessible(true);
            log.info("=====================actualTables:{}", actualTables);
            actualTablesField.set(tableRule, actualTables);

            //动态刷新：dataNodeIndexMap
            Field dataNodeIndexMapField = TableRule.class.getDeclaredField("dataNodeIndexMap");
            dataNodeIndexMapField.setAccessible(true);
            dataNodeIndexMapField.set(tableRule, dataNodeIntegerMap);

            //动态刷新：datasourceToTablesMap
            Map<String, Collection<String>> datasourceToTablesMap = Maps.newHashMap();
            datasourceToTablesMap.put(dataSourceName, actualTables);
            Field datasourceToTablesMapField = TableRule.class.getDeclaredField("datasourceToTablesMap");
            datasourceToTablesMapField.setAccessible(true);
            datasourceToTablesMapField.set(tableRule, datasourceToTablesMap);
            log.info("-----------------end----------------");

        }

        // YamlTableRuleConfiguration order = tables.get("product");
        // String actualDataNodesTo = order.getActualDataNodes();
        // //TODO: 可以查询数据库，根据数据关联表 db1.product_$->{2020..2023}
        // StringBuilder stringBuilder = new StringBuilder("db1.product_$->{");
        // stringBuilder.append(startYear);
        // stringBuilder.append("..");
        // stringBuilder.append(endYear);
        // stringBuilder.append("}");
        // String actualDataNodes = stringBuilder.toString();
        // log.debug("shardingjdbc=>actualDataNodes:{}", actualDataNodes);
        // order.setActualDataNodes(actualDataNodes);
    }

    private int getThisYear() {
        LocalDateTime now = LocalDateTime.now();
        return now.getYear();
    }

}
