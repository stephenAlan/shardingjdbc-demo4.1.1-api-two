package com.stephen.demo.service;

import com.stephen.demo.entity.Product;

import java.util.List;
import java.util.Map;

/**
 * @author <a href="shishouchao@foresee.com.cn">shouchao.shi</a>
 * @version 1.0.0
 * @date 2023/06/07 18:07
 * @Description
 */
public interface IProductService {

    List<Product> list();

    Map<String,Object> listMap();

    int insert(Product product);

}
