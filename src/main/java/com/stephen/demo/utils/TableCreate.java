// package com.stephen.demo.utils;
//
// import lombok.Getter;
// import lombok.Setter;
// import lombok.extern.slf4j.Slf4j;
// import org.apache.commons.lang3.StringUtils;
// import org.apache.shardingsphere.shardingjdbc.jdbc.core.datasource.ShardingDataSource;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.boot.context.properties.ConfigurationProperties;
// import org.springframework.boot.env.OriginTrackedMapPropertySource;
// import org.springframework.core.env.MutablePropertySources;
// import org.springframework.core.env.PropertySource;
// import org.springframework.scheduling.annotation.EnableScheduling;
// import org.springframework.stereotype.Component;
// import org.springframework.util.Assert;
// import org.springframework.web.context.support.StandardServletEnvironment;
//
// import javax.annotation.PostConstruct;
// import javax.annotation.Resource;
// import javax.sql.DataSource;
// import java.sql.Connection;
// import java.sql.ResultSet;
// import java.sql.SQLException;
// import java.sql.Statement;
// import java.time.LocalDateTime;
// import java.util.HashMap;
// import java.util.Map;
//
// /**
//  * @author zzs
//  */
// @Slf4j
// @Setter
// @Getter
// @Component
// @ConfigurationProperties(prefix = "sharding.create")
// @EnableScheduling
// public class TableCreate {
//
//     @Resource
//     private ShardingDataSource dataSource;
//     @Autowired
//     StandardServletEnvironment env;
//     Map<String, Object> allTable = new HashMap<>();
//     Map<String, Object> dbAndTable = new HashMap<>();
//     Map<String, Object> createdTables = new HashMap<>();
//     private Map<String, String> tables;
//     private Integer maxMonth;
//     private Integer beforeDefaultMonth;
//     private Integer afterDefaultMonth;
//     private Integer beforeDefaultDate;
//
//     @PostConstruct
//     public void init() {
//         String v1 = "actual-data-nodes";
//         String v2 = "spring.shardingsphere.sharding.tables";
//         MutablePropertySources propertySources = env.getPropertySources();
//
//         for (PropertySource<?> propertySource : propertySources) {
//             if (propertySource instanceof OriginTrackedMapPropertySource) {
//                 OriginTrackedMapPropertySource originTrackedMapPropertySource = (OriginTrackedMapPropertySource) propertySource;
//                 String[] propertyNames = originTrackedMapPropertySource.getPropertyNames();
//
//                 for (String propertyName : propertyNames) {
//                     if (propertyName.startsWith(v2)) {
//                        // System.out.println("propertyName.."+propertyName);
//                         Object property = originTrackedMapPropertySource.getProperty(propertyName);
//                         allTable.put(propertyName, property);
//                        // System.out.println(" allTable.."+ allTable);
//                     }
//                 }
//             }
//         }
//         String finalSp = "$";
//         allTable.forEach((k, v) -> {
//             if (k.contains(v1) && String.valueOf(v).contains(finalSp)) {
//                 String table = StringUtils.substringBetween(k, v2 + ".", "." + v1);
//                 System.out.println("String.valueOf(v).."+String.valueOf(v)+"...table.."+table);
//                 String dbname = StringUtils.substringBefore(String.valueOf(v), "." + table);
//                 System.out.println("dbname.."+dbname);
//                 table = table.trim();
//                 dbname = dbname.trim();
//                 dbAndTable.put(table, dbname);
//             }
//         });
//         //dbAndTable...{goods=database0}
//         System.out.println("dbAndTable..."+dbAndTable);
//         dbAndTable.forEach((k, v) -> {
//             System.out.println("creat....");
//             createTable(k, String.valueOf(v));
//         });
//       //  createTablePreMonths();
//     }
//
//
//     private void createTablePreMonths() {
//         //tables...{202004=goods, 202011=goods}
//         System.out.println("tables..."+tables);
//         tables.forEach((date, d) -> {
//             System.out.println("createpre...");
//             createPreTables(date, d);
//         });
//     }
//
//     private void createPreTables(String date, String tables) {
//         String[] tabs = tables.split(",");
//         for (String table : tabs) {
//             table = table.trim();
//             if (dbAndTable.containsKey(table)) {
//                 String db = (String) dbAndTable.get(table);
//                 for (int j = 1; j <= maxMonth; j++) {
//                     String localDateString = getLocalDateString(LocalDateTime.now().plusMonths(-j), table);
//                     if (localDateString.contains(date)) {
//                         createNeedTime(table, db, localDateString);
//                         break;
//                     }
//                     createNeedTime(table, db, localDateString);
//                 }
//             }
//         }
//     }
//
//     private void createTable(String table, String db) {
//         if (afterDefaultMonth != null && afterDefaultMonth > 0) {
//             createNeedTime(table, db, getLocalDateString(LocalDateTime.now(), table));
//             for (int i = 1; i <= afterDefaultMonth; i++) {
//                 createNeedTime(table, db, getLocalDateString(LocalDateTime.now().plusMonths(i), table));
//             }
//         }
//         if (beforeDefaultMonth != null) {
//             for (int i = 0; i <= beforeDefaultDate; i++) {
//                 createNeedTime(table, db, getLocalDateString(LocalDateTime.now().plusMonths(-i), table));
//             }
//         }
//     }
//
//     private void createNeedTime(String table, String db, String create) {
//         DataSource dataSource = this.dataSource.getDataSourceMap().get(db);
//         String sql = "SHOW CREATE TABLE " + table;
//         String existSql = "select * from information_schema.tables where table_name ='" + table + "'; ";
//         doCreate(dataSource, sql, existSql, create, db, table);
//     }
//
//     private void doCreate(DataSource dataSource, String sql, String existSql, String create, String db, String table) {
//         String msg = " create table: " + create + "  origin table: " + table + "  db: " + db;
//         Connection conn = null;
//         Statement stmt = null;
//         try {
//             conn = dataSource.getConnection();
//             stmt = conn.createStatement();
//             ResultSet resultSet = stmt.executeQuery(existSql);
//             Assert.isTrue(resultSet.next(), msg + "初始化表不存在");
//
//             ResultSet resTable = stmt.executeQuery(sql);
//             Assert.isTrue(resTable.next(), msg + "初始化表不存在");
//             String existTableName = resTable.getString(1);
//             String createSqlOrigin = resTable.getString(2);
//             // log.info(existTableName, createSqlOrigin);
//
//             String existSqlNew = StringUtils.replaceOnce(existSql, existTableName, create);
//             ResultSet executeQuery = stmt.executeQuery(existSqlNew);
//             if (executeQuery.next()) {
//                 log.info("table exist :" + msg);
//             } else {
//                 String creatsql = StringUtils.replaceOnce(createSqlOrigin, existTableName, create);
//                 System.out.println("createSqlOrigin.."+createSqlOrigin+"....existTableName..."+existTableName+"....create.."+create);
//                 if (0 == stmt.executeUpdate(creatsql)) {
//                     log.info(msg + "success ！");
//
//                     createdTables.put(create, db);
//                 } else {
//                     log.error(msg + "fail ！");
//                 }
//             }
//         } catch (Exception e) {
//             log.error("create  table fail  error : {} ", e.getMessage());
//         } finally {
//             if (stmt != null) {
//                 try {
//                     stmt.close();
//                 } catch (SQLException e) {
//                     log.error("SQLException", e);
//                 }
//             }
//             if (conn != null) {
//                 try {
//                     conn.close();
//                 } catch (SQLException e) {
//                     log.error("SQLException", e);
//                 }
//             }
//         }
//     }
//
//     private String getLocalDateString(LocalDateTime now, String table) {
//         int startYear = now.getYear();
//         int startMonth = now.getMonthValue();
//         int startDate=now.getDayOfMonth();
//
//
//         int month = 10;
//         if (startMonth < month) {
//             //return table + "_" + startYear + "0" + startMonth;
//             return table + "_" + startYear +"_"+ startMonth+"_"+startDate;
//         }
//         return table + "_" + startYear + "_"+startMonth+"_"+startDate;
//     }
//
//     /**
//      * 定时建表
//      *
//      * @return
//      * @throws SQLException
//      */
//    // @Scheduled(cron = "0 */1 * * * ?")//每个一分钟"0*/1***?"
//     public Object cfWdtRdCalculateTask() throws SQLException {
//         init();
//         return "success";
//     }
//
//
// }
