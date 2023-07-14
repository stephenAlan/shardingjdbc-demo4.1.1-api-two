package com.stephen.demo.mapper;

import com.stephen.demo.entity.Stu;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @author <a href="shishouchao@foresee.com.cn">shouchao.shi</a>
 * @version 1.0.0
 * @date 2023/06/08 10:39
 * @Description
 */
@Mapper
public interface StuMapper {

    List<Stu> list();

    List<Stu> selectList(Map<String,Object> param);

    int insert(Stu stu);

    int selectTable(String tableName);

    int createTable(@Param("logicTableName") String logicTableName, @Param("tableName") String tableName);

}
