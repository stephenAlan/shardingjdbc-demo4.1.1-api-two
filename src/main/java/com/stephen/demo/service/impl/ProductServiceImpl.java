package com.stephen.demo.service.impl;

import com.stephen.demo.entity.Product;
import com.stephen.demo.mapper.ProductMapper;
import com.stephen.demo.service.IProductService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author <a href="shishouchao@foresee.com.cn">shouchao.shi</a>
 * @version 1.0.0
 * @date 2023/06/07 18:07
 * @Description
 */
@Service
public class ProductServiceImpl implements IProductService {

    @Resource
    ProductMapper productMapper;


    @Override
    public List<Product> list() {
        Map<String,Object> queryMap = new HashMap<>();
        // queryMap.put("nd", "2023");
        queryMap.put("comment", "iphone13");
        List<Product> list = productMapper.selectList(queryMap);
       return list;
    }

    @Override
    public Map<String,Object> listMap() {
        Map<String,Object> queryMap = new HashMap<>();
        queryMap.put("nd", "2023");
        queryMap.put("comment", "iphone13");
        Map<String, Object> resultMap = productMapper.selectListMap(queryMap);
        System.out.println("resultMap = " + resultMap);
        Object create_date = resultMap.get("create_date");
        System.out.println("create_date = " + create_date);
        return resultMap;
    }

    @Override
    public int insert(Product product) {
        return productMapper.insert(product);
    }


}
