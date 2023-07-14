package com.stephen.demo;

import com.stephen.demo.entity.Product;
import com.stephen.demo.mapper.ProductMapper;
import com.stephen.demo.service.IProductService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SpringBootTest
class ProductTests {

    @Resource
    private ProductMapper productMapper;
    @Resource
    private IProductService productService;
    @Autowired
    private YsdjNsrssxxDao ysdjNsrssxxDao;

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
    void testInsert() {
        Product product = Product.builder()
                // .id(2L)
                .nd("2021")
                .comment("iphone21")
                .build();
        productMapper.insert(product);
    }

    @Test
    void testDual() {
        productMapper.selectDual();
    }


    @Test
    void testQuery() {
        Map<String,Object> queryMap = new HashMap<>();
        queryMap.put("nd", "2023");
        queryMap.put("comment", "iphone13");

        List<String> list = new ArrayList<>();
        list.add("iphone13");
        queryMap.put("list", list);
        // List<Product> products = productMapper.selectList(queryMap);
        // productMapper.update();
        // System.out.println("products = " + products);
        // Map<String,Object> productMap = productMapper.selectListMap(queryMap);
        // System.out.println("productMap = " + productMap);
        // Object create_date = productMap.get("create_date");

        Map<String,Object> nsrMap = new HashMap<>();
        nsrMap.put("djxh", "10114412000132181884");
        Map<String, Object> querytNsrssxx = ysdjNsrssxxDao.querytNsrssxx(nsrMap);


        System.out.println("querytNsrssxx = " + querytNsrssxx);
        Object zhsxsj = querytNsrssxx.get("zhsxsj");
        System.out.println("zhsxsj = " + zhsxsj);
        // Map<String, Object> productMap = productService.listMap();



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
