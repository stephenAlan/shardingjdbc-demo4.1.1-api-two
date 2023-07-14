package com.stephen.demo;

import com.stephen.demo.entity.Stu;
import com.stephen.demo.mapper.StuMapper;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SpringBootTest
class StuTests {

    @Resource
    private StuMapper stuMapper;

    /**
     * 会依次查出所有表的数量,加起来
     */
    @Test
    void testCount() {
        // QueryWrapper<Product> queryWrapper = new QueryWrapper<>();
        // queryWrapper.like("comment", "李四");
        // int count = productMapper.selectCount(queryWrapper);
        // System.out.println("products count = " + count);
    }

    @Test
    // @Transactional
    void testInsert() {
        Stu stu = Stu.builder()
                // .id(2L)
                .nd("2022")
                .comment("iphone12")
                .build();
        stuMapper.insert(stu);
        // int i = 1 / 0;
    }

    @Test
    void testQuery() {
        Map<String,Object> queryMap = new HashMap<>();
        queryMap.put("nd", "2023");
        queryMap.put("comment", "iphone13");
        List<Stu> list = stuMapper.selectList(queryMap);
        System.out.println("Stu = " + list);
    }

    @Test
    void testSharding() {
        String schemaNameForTOrder = "ds.table_name_$->{2023...2024}";
        // this.reloadShardRuleActualDataNodes(shardingSphereDataSource, schemaNameForTOrder);
    }

    @Test
    void testYear() {
        LocalDateTime now = LocalDateTime.now();
        int year = now.getYear();
        System.out.println("year = " + year);
    }

    public static void main(String[] args) {
        LocalDateTime now = LocalDateTime.now();
        int year = now.getYear();
        int nextYear = year + 1;



        System.out.println("year = " + year);
    }

}
