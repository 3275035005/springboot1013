package com.example.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.entity.User;

import java.util.List;

public interface UserMapper extends BaseMapper<User> {

    List<User> getMaintainerFindAll();

    List<User> getMaintainerFindAll2();
}
