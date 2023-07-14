package com.stephen.demo.controller;

import com.stephen.demo.entity.Product;
import com.stephen.demo.service.IProductService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author <a href="shishouchao@foresee.com.cn">shouchao.shi</a>
 * @version 1.0.0
 * @date 2023/06/08 10:38
 * @Description
 */
@RestController
@RequestMapping("product")
public class ProductController {

    @Resource
    private IProductService productService;

    @RequestMapping("list")
    public Object list() {
        return productService.list();
    }

    @RequestMapping("insert")
    public Object insert(Product product) {
        return productService.insert(product);
    }


}
