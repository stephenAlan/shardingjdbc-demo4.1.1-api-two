package org.apache.ibatis.type;

import cn.hutool.core.convert.Convert;
import org.springframework.stereotype.Component;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

/**
 * @description: LocalDateTime是为了解决shardingResultSet转换LocalDateTime的问题
 * 查询时就会有问题,无法解析,老的项目有问题,springboot项目正常,如果有问题,可以加下这个类
 * Error attempting to get column 'xxx' from result set.  Cause: java.sql.SQLFeatureNotSupportedException: getObject with type
 * @date: 2023/7/10 15:41
 * mybatis相同路径下也有一个同名文件,需要在项目中写相同的路径,优先会读取项目中的
 * ps:
 * 有尝试引入过下边这个依赖,里边对LocalDateTimeTypeHandler进行了覆写,但是经过排查,会进这个代码,但是问题依然存在
 * <dependency>
 * 		<groupId>org.mybatis</groupId>
 * 		<artifactId>mybatis-typehandlers-jsr310</artifactId>
 * 		<version>1.0.2</version>
 * </dependency>
 *
 */
@Component
@MappedTypes(LocalDateTime.class)
@MappedJdbcTypes(value = JdbcType.DATE, includeNullJdbcType = true)
public class LocalDateTimeTypeHandler extends BaseTypeHandler<LocalDateTime> {

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, LocalDateTime parameter, JdbcType jdbcType)
            throws SQLException {
        ps.setObject(i, parameter);
    }

    @Override
    public LocalDateTime getNullableResult(ResultSet rs, String columnName) throws SQLException {
        return Convert.toLocalDateTime(rs.getObject(columnName));
    }

    @Override
    public LocalDateTime getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        return Convert.toLocalDateTime(rs.getObject(columnIndex));
    }

    @Override
    public LocalDateTime getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        return Convert.toLocalDateTime(cs.getObject(columnIndex));
    }
}

