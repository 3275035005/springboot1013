package com.example.controller;

import cn.hutool.core.date.DateUtil;
import com.auth0.jwt.JWT;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.common.Result;
import com.example.entity.Message;
import com.example.entity.Order;
import com.example.entity.User;
import com.example.service.MessageService;
import com.example.service.OrderService;
import com.example.service.UserService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@RestController
@RequestMapping("/api/message")
public class MessageController {
    @Resource
    private MessageService messageService;
    @Resource
    private UserService userService;
    @Resource
    private OrderService orderService;

    @Resource
    HttpServletRequest request;

    public User getUser() {
        String token = request.getHeader("token");
        String username = JWT.decode(token).getAudience().get(0);
        return userService.getOne(Wrappers.<User>lambdaQuery().eq(User::getUsername, username));
    }

    @PostMapping
    public Result<?> save(@RequestBody Message Message) {
        Order order = orderService.getById(Message.getOrderId());
        order.setState("5");
        orderService.updateById(order);
        Message.setUserId(getUser().getId());
        Message.setTime(DateUtil.formatDateTime(new Date()));
        return Result.success(messageService.save(Message));
    }

    @PutMapping
    public Result<?> update(@RequestBody Message Message) {
        return Result.success(messageService.updateById(Message));
    }

    @DeleteMapping("/{id}")
    public Result<?> delete(@PathVariable Long id) {
        messageService.removeById(id);
        return Result.success();
    }

    @GetMapping("/{id}")
    public Result<?> findById(@PathVariable Long id) {
        return Result.success(messageService.getById(id));
    }

    @GetMapping
    public Result<?> findAll() {
        return Result.success(messageService.list());
    }

    @GetMapping("/foreign/{orderId}")
    public Result<?> findByForeign(@PathVariable Long orderId) {
        return Result.success(messageService.findByForeign(orderId));
    }

    @GetMapping("/page")
    public Result<?> findPage(@RequestParam(required = false, defaultValue = "") String name,
                                                @RequestParam(required = false, defaultValue = "1") Integer pageNum,
                                                @RequestParam(required = false, defaultValue = "10") Integer pageSize) {
        LambdaQueryWrapper<Message> query = Wrappers.<Message>lambdaQuery().like(Message::getContent, name).orderByDesc(Message::getId);
        return Result.success(messageService.page(new Page<>(pageNum, pageSize), query));
    }

}
