package com.example.controller;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.auth0.jwt.JWT;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.common.Result;
import com.example.dto.OrderVo;
import com.example.entity.*;
import com.example.exception.CustomException;
import com.example.service.*;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.*;

@RestController
@RequestMapping("/api/order")
public class OrderController {
    @Resource
    private OrderService orderService;
    @Resource
    private HttpServletRequest request;
    @Resource
    private UserService userService;
    @Resource
    private GoodsService goodsService;

    public User getUser() {
        String token = request.getHeader("token");
        String username = JWT.decode(token).getAudience().get(0);
        return userService.getOne(Wrappers.<User>lambdaQuery().eq(User::getUsername, username));
    }

    /**
     * 提交订单
     * @param order
     * @return
     */
    @Transactional
    @PostMapping
    public Result<?> save(@RequestBody Order order) {
        order.setUserId(getUser().getId());
        order.setOrderNo(DateUtil.format(new Date(), "yyyyMMddHHmmss") + RandomUtil.randomNumbers(6));
        order.setCreateTime(DateUtil.now());
        orderService.save(order);

        // 加销量
        Goods goods = goodsService.getById(order.getGoodsId());
        goods.setSales(goods.getSales() + 1);
        goodsService.updateById(goods);

        return Result.success(order);
    }

    /**
     * 修改订单
     * @param order
     * @return
     */
    @PutMapping
    public Result<?> update(@RequestBody Order order) {
        orderService.updateById(order);
        return Result.success();
    }

    /**
     * 付款
     * @param id
     * @return
     */
    @Transactional
    @PutMapping("/pay/{id}")
    public Result<?> pay(@PathVariable Long id) {
        Order order = orderService.getById(id);
        BigDecimal totalPrice = order.getTotalPrice();

        Long userId = getUser().getId();
        User user = userService.findById(userId);

        if (user.getAccount().compareTo(totalPrice) <= 0) {
            throw new CustomException("-1", "余额不足");
        }

        user.setAccount(user.getAccount().subtract(totalPrice)); // 设置用户余额
        userService.updateById(user);
        order.setState("1");
        orderService.updateById(order);
        return Result.success();
    }

    /**
     * 删除订单
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    public Result<?> delete(@PathVariable Long id) {
        orderService.removeById(id);
        return Result.success();
    }

    /**
     * 获取订单的确认信息
     * @return
     */
    @PostMapping("/pre/{id}")
    public Result<?> pre(@PathVariable String id){
        Map<String, Object> all = new HashMap<>();
        Goods goods = goodsService.getById(id);
        List<Object> list = new ArrayList<>();
        list.add(goods);
        all.put("list",list);
        all.put("totalPrice",goods.getPrice());

        return Result.success(all);
    }

    /**
     * 通过订单id查询数据
     * @return
     */
    @GetMapping("/{id}")
    public Result<?> findById(@PathVariable Long id) {
        return Result.success(orderService.getById(id));
    }

    /**
     * 查询所有订单
     * @return
     */
    @GetMapping
    public Result<?> findAll() {
        List<Order> list = orderService.list();
        return Result.success(list);
    }

    /**
     * 分页条件查询
     * @param
     * @param pageNum
     * @param pageSize
     * @return
     */
    @PostMapping("/page/{pageNum}/{pageSize}")
    public Result<?> findPage(
            @PathVariable Integer pageNum,
            @PathVariable Integer pageSize,
            @RequestBody OrderVo orderVo) {

        IPage<Order> page = orderService.findPage(new Page<>(pageNum, pageSize), orderVo);
        return Result.success(page);
    }



    /**
     * 前台查询订单列表
     * @param state
     * @param pageNum
     * @param pageSize
     * @return
     */
    @GetMapping("/page/front")
    public Result<?> findPageFront(@RequestParam(required = false, defaultValue = "") String state,
                                   @RequestParam(required = false, defaultValue = "1") Integer pageNum,
                                   @RequestParam(required = false, defaultValue = "10") Integer pageSize) {
        LambdaQueryWrapper<Order> query = Wrappers.<Order>lambdaQuery().orderByDesc(Order::getId);
        query.eq(Order::getUserId, getUser().getId());
        // 根据状态查询
        if (StrUtil.isNotBlank(state)) {
            query.eq(Order::getState, state);
        }
        IPage<Order> page = orderService.page(new Page<>(pageNum, pageSize), query);

        for (Order order : page.getRecords()) {
            order.setGoods(goodsService.getById(order.getGoodsId()));
        }
        return Result.success(page);
    }

    /**
     * 首页饼状图统计
     * @return
     */
    @GetMapping("getHome")
    public Result<List<Map<String, Object>>> getHome(){
        List<Map<String, Object>> list = orderService.getHome();
        return Result.success(list);
    }


}
