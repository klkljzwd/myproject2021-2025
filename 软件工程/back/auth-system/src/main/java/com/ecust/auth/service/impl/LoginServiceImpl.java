package com.ecust.auth.service.impl;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.json.JSONUtil;
import com.ecust.auth.advicer.NormalException;
import com.ecust.auth.mapper.AuthorityMapper;
import com.ecust.auth.mapper.RoleAuthorityMapper;
import com.ecust.auth.mapper.UserMapper;
import com.ecust.auth.mapper.UserRoleMapper;
import com.ecust.auth.service.LoginService;
import com.ecust.base.ResponseResult;
import com.ecust.base.constant.Constant;
import com.ecust.base.dto.AdminLoginDTO;
import com.ecust.base.dto.LoginDTO;
import com.ecust.base.dto.LoginPhoneDTO;
import com.ecust.base.entity.Authority;
import com.ecust.base.entity.RoleUser;
import com.ecust.base.entity.User;
import com.ecust.base.vo.LoginVo;
import com.ecust.utils.JwtUtils;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class LoginServiceImpl implements LoginService {
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private UserRoleMapper userRoleMapper;
    @Autowired
    private RoleAuthorityMapper roleAuthorityMapper;
    @Autowired
    private AuthorityMapper authorityMapper;

    @Override
    public ResponseResult<LoginVo> login(LoginDTO loginDTO) {
        String username = loginDTO.getUsername();
        String password = loginDTO.getPassword();

        User userByName = userMapper.getUserByName(username);
        //检查合法性
        if(userByName==null){
            return null;
        }
        String pwd = DigestUtils.md5DigestAsHex(password.getBytes());
        if(!pwd.equals(userByName.getPassword())){
            return null;
        }
        Long id = userByName.getId();
        //查询用户所属角色
        List<Long> roleUser = userRoleMapper.getRoleUser(id);
        //查询角色的权限
        List<Long> roleAuthority = roleAuthorityMapper.getRoleAuthority(roleUser);
        //根据id集合查询权限
        List<Authority> authority = authorityMapper.getAuthority(roleAuthority);
        String s = JSONUtil.toJsonStr(authority);
        stringRedisTemplate.opsForValue().set(Constant.userPrix+id,s);
        //生成jwt返回
        String jwt = JwtUtils.createJWT(id.toString());
        ResponseResult<LoginVo> responseResult = new ResponseResult<>();
        LoginVo loginVo = new LoginVo();
        loginVo.setToken(jwt);
        responseResult.setData(loginVo);
        responseResult.setCode(200);
        Claims x = JwtUtils.parseJWT(jwt);
        System.out.println(x.get("id"));
        return responseResult;
    }

    @Override
    public ResponseResult userRegister(LoginDTO loginDTO) {
        String username = loginDTO.getUsername();
        String password = loginDTO.getPassword();
        User userByName = userMapper.getUserByName(username);
        if(userByName!=null){
            return null;
        }
        User user = new User();
        String pwd = DigestUtils.md5DigestAsHex(password.getBytes());
        user.setPassword(pwd);
        user.setStatus(1);
        user.setUsername(username);
        userMapper.addUser(user);
        //绑定游客角色
        Long id = user.getId();
        RoleUser roleUser = new RoleUser();
        roleUser.setUserId(id);
        roleUser.setRoleId(Constant.VISITOR_ROLE_ID);
        userRoleMapper.addUserRole(roleUser);
        ResponseResult responseResult = new ResponseResult();
        responseResult.setCode(200);
        return responseResult;
    }

    @Override
    public ResponseResult adminLogin(AdminLoginDTO adminLoginDTO) {
        User userByName = userMapper.getUserByName(adminLoginDTO.getUsername());
        if(userByName==null){
            throw new NormalException("未找到该用户");
        }
        String pwd = DigestUtils.md5DigestAsHex(adminLoginDTO.getPassword().getBytes());
        if(!pwd.equals(userByName.getPassword())){
            throw new NormalException("密码错误");
        }
        User user = userMapper.getUserByName(adminLoginDTO.getUsername());
        //查询用户所属角色
        List<Long> roleUser = userRoleMapper.getRoleUser(user.getId());
        //查询角色的权限
        List<Long> roleAuthority = roleAuthorityMapper.getRoleAuthority(roleUser);
        //根据id集合查询权限
        List<Authority> adminAuth = authorityMapper.getAuthority(roleAuthority);
        String s = JSONUtil.toJsonStr(adminAuth);
        stringRedisTemplate.opsForValue().set(Constant.userPrix+userByName.getId(),s);
        //生成jwt返回
        String jwt = JwtUtils.createJWT(userByName.getId()+"");
        ResponseResult<LoginVo> responseResult = new ResponseResult<>();
        LoginVo loginVo = new LoginVo();
        loginVo.setToken(jwt);
        responseResult.setData(loginVo);
        responseResult.setCode(200);
        Claims x = JwtUtils.parseJWT(jwt);
        System.out.println(x.get("id"));
        return responseResult;
    }
}
