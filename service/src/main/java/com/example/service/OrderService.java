package com.example.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.dto.OrderVo;
import com.example.entity.Order;
import com.example.mapper.OrderMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Service
public class OrderService extends ServiceImpl<OrderMapper, Order> {

    @Resource
    private OrderMapper orderMapper;

    public IPage<Order> findPage(Page<Object> page, OrderVo orderVo) {
        return orderMapper.findPage(page, orderVo);
    }

    public List<Map<String, Object>> getHome() {


       return orderMapper.getHome();
    }
}
