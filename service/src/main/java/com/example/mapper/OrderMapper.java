package com.example.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.dto.OrderVo;
import com.example.entity.Order;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface OrderMapper extends BaseMapper<Order> {

    IPage<Order> findPage(@Param("page") Page<Object> page,@Param("orderVo") OrderVo orderVo);

    List<Map<String, Object>> getHome();


}
