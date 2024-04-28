package com.ecust.auth.service.impl;

import com.ecust.auth.advicer.NormalException;
import com.ecust.auth.mapper.RoleMapper;
import com.ecust.auth.mapper.UserMapper;
import com.ecust.auth.mapper.UserRoleMapper;
import com.ecust.auth.service.UserService;
import com.ecust.base.PageResult;
import com.ecust.base.ResponseResult;
import com.ecust.base.ThreadLocalAuth;
import com.ecust.base.annotation.AuthPermission;
import com.ecust.base.constant.Constant;
import com.ecust.base.dto.UserRegister;
import com.ecust.base.entity.RoleAuthority;
import com.ecust.base.entity.RoleUser;
import com.ecust.base.entity.User;
import com.ecust.utils.FlushUser;
import com.ecust.utils.RedisIdUtils;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;



@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private UserRoleMapper userRoleMapper;
    @Autowired
    private RoleMapper roleMapper;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;


    @Override
    @AuthPermission(level = 0)
    public ResponseResult updateUser(User user) {
        userMapper.updateUser(user);
        ResponseResult responseResult = new ResponseResult();
        responseResult.setCode(200);
        return responseResult;
    }

    @Override
    @AuthPermission(level = 0)
    public ResponseResult addUser(User user) {
        if(user.getUsername()==null || user.getPhone()==null || user.getStatus()==null){
            throw new NormalException("参数错误");
        }
        long l = RedisIdUtils.nextId(stringRedisTemplate, "");
        user.setId(l);
        user.setPassword("");
        userMapper.addUser(user);
        ResponseResult responseResult = new ResponseResult();
        responseResult.setCode(200);
        return responseResult;
    }

    @Override
    @AuthPermission(level = 0)
    public ResponseResult deleteUser(Long id) {
        userMapper.deleteUser(id);
        ResponseResult responseResult = new ResponseResult();
        responseResult.setCode(200);
        return responseResult;
    }

    @Override
    @AuthPermission(level = 0)
    public PageResult queryUser(Long currentPage, Long pageSize) {
        PageHelper.startPage(currentPage.intValue(),pageSize.intValue());
        Page<User> userPage = userMapper.getAll();
        PageResult pageResult = new PageResult();
        pageResult.setPage(currentPage);
        pageResult.setTotal(userPage.getTotal());
        ResponseResult responseResult = new ResponseResult();
        responseResult.setData(userPage.getResult());
        responseResult.setCode(200);
        pageResult.setResponseResult(responseResult);
        return pageResult;
    }

    @Override
    @Transactional
    public ResponseResult registerUser(UserRegister userRegister) {
        String phone = userRegister.getPhone();
        String username = userRegister.getUsername();
        Boolean isVip = userRegister.getIsVip();

        User userByPhone = userMapper.getUserByName(username);
        User user = new User();
        user.setPhone(phone);
        user.setId(userByPhone.getId());
        userMapper.updateUser(user);
        Long userId = userByPhone.getId();
        if(isVip){
            userRoleMapper.registerReader(Constant.VIP_ROLE_ID,userId);
        }else {
            userRoleMapper.registerReader(Constant.READER_ROLE_ID,userId);
        }

        ResponseResult responseResult = new ResponseResult();
        responseResult.setCode(200);
        FlushUser.flush(stringRedisTemplate,Constant.userPrix+ ThreadLocalAuth.get());
        responseResult.setMsg("注册成功，请重新登录");
        return responseResult;
    }
}
