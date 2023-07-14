package com.stephen.demo.config;

import com.stephen.demo.mapper.ProductMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import org.yaml.snakeyaml.Yaml;

import javax.annotation.Resource;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 动态加载表名
 */
@Slf4j
@Component
public class TestConfig {

    @Resource
    private TableNamesConfig tableNamesConfig;
    @Resource
    private ProductMapper productMapper;
    // @Resource
    // private SpringBootShardingRuleConfigurationProperties shardingRuleConfigurationProperties;

    @Resource
    private Environment environment;

    private static String driverClass;
    private static String url;
    private static String user;   //user是数据库的用户名
    private static String password;  //用户登录密码
    private static Connection connection;


    // 定时任务执行
    // @PostConstruct
    // @Scheduled(cron = "0 0 0 1 * ?")
    // public void actualTablesRefresh(){
    //
    //     int startYear = tableNamesConfig.getStartYear();
    //     String[] names = tableNamesConfig.getNames();
    //     int endYear = getThisYear();
    //
    //     Map<String, YamlTableRuleConfiguration> tables =
    //             shardingRuleConfigurationProperties.getTables();
    //     YamlTableRuleConfiguration order = tables.get("product");
    //     String actualDataNodesTo = order.getActualDataNodes();
    //     //TODO: 可以查询数据库，根据数据关联表 db1.product_$->{2020..2023}
    //     StringBuilder stringBuilder = new StringBuilder("db1.product_$->{");
    //     stringBuilder.append(startYear);
    //     stringBuilder.append("..");
    //     stringBuilder.append(endYear);
    //     stringBuilder.append("}");
    //     String actualDataNodes = stringBuilder.toString();
    //     log.debug("shardingjdbc=>actualDataNodes:{}", actualDataNodes);
    //     order.setActualDataNodes(actualDataNodes);
    // }

    private int getThisYear() {
        LocalDateTime now = LocalDateTime.now();
        return now.getYear();
    }

    /**
     * 若表不存在,则创建
     * 每月最后一天执行
     * @return
     */
    // @Scheduled(cron = "0 59 23 L * ? *")
    // @Scheduled(cron = "0/10 * * * * ?")
    private void createTableIfNotExist() {
        LocalDateTime now = LocalDateTime.now();
        int year = now.getYear();
        // int nextYear = year + 1;
        int nextYear = year;
        List<String> tableNames = tableNamesConfig.getTableNames();

        System.out.println("tableNames = " + tableNames);

        String logicTableName = "product";
        String tableName = logicTableName + "_" + nextYear;
        boolean flag = false;
        // 判断表是否存在 不存在则添加表
        try {
            productMapper.selectTable(tableName);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            flag = true;
            log.info("{}表不存在,即将准备创建", tableName);
        }
        try {
            if (flag) {
                createTable(logicTableName, tableName);
            }
        } catch (Exception e) {
            log.error("创建表{}失败", tableName, e);
        }
    }

    // shardingjdbc不支持DDL语句,需要使用原生的jdbc
    public void createTable(String tableName, String newTableName) throws Exception {
        try {
            driverClass = "com.mysql.cj.jdbc.Driver";
            url = "jdbc:mysql://192.168.189.128/test?allowMultiQueries=true&rewriteBatchedStatements=true&useUnicode=true&characterEncoding=utf8&serverTimezone=GMT%2B8&useSSL=false";
            user = "root";
            password = "123456";
            Class.forName(driverClass);
            connection = DriverManager.getConnection(url, user, password);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        PreparedStatement preparedStatement = null;
        String create = "CREATE TABLE IF NOT EXISTS " + newTableName + " LIKE "+ tableName +";";
        log.info("newCreateTableSQL:{}", create);
        try {
            preparedStatement = connection.prepareStatement(create);
            preparedStatement.execute();
            log.info("表{}创建成功~~", newTableName);
        } catch (SQLException e) {
            throw new Exception(tableName + "创建失败..");
        }
    }

    public static String getYmlConfig(String key) {
        String[] split = key.split("\\.");
        Yaml yaml = new Yaml();
        InputStream inputStream;
        org.springframework.core.io.Resource resource;
        try {
            resource = new ClassPathResource("application.properties-1");
            inputStream = resource.getInputStream();
            Map<String, Object> load = yaml.loadAs(inputStream, Map.class);
            for (int i = 0; i < split.length; i++) {
                if (i == split.length-1) {
                    return (String) load.get(split[i]);
                } else {
                    load = (Map<String, Object>) load.get(split[i]);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}


