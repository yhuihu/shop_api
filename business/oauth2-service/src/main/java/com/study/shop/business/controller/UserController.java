package com.study.shop.business.controller;

import com.google.common.collect.Maps;
import com.study.shop.business.BusinessException;
import com.study.shop.business.ExceptionStatus;
import com.study.shop.business.dto.LoginInfo;
import com.study.shop.business.dto.LoginParam;
import com.study.shop.business.feign.ProfileFeign;
import com.study.shop.commons.dto.ResponseResult;
import com.study.shop.provider.domain.TbUser;
import com.study.shop.utils.MapperUtils;
import com.study.shop.utils.OkHttpClientUtil;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.Objects;

/**
 * @author Tiger
 * @date 2019-09-11
 * @see com.study.shop.business.controller
 **/
@Slf4j
//@CrossOrigin(origins = "*", maxAge = 3600)gateway统一配置，重复会冲突
@RestController
@RequestMapping("user")
public class UserController {

    private static final String URL_OAUTH_TOKEN = "http://localhost:9001/oauth/token";

    @Value("${business.oauth2.grant_type}")
    public String oauth2GrantType;

    @Value("${business.oauth2.client_id}")
    public String oauth2ClientId;

    @Value("${business.oauth2.client_secret}")
    public String oauth2ClientSecret;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Resource(name = "userDetailsServiceBean")
    private UserDetailsService userDetailsService;

    @Resource
    public TokenStore tokenStore;

    @Resource
    private ProfileFeign profileFeign;

    @Resource
    private RedisTemplate<String, String> redisTemplate;

    @PostMapping(value = "/login")
    @ApiOperation(value = "1、用户登录", notes = "使用账号密码登录", httpMethod = "POST")
    public ResponseResult<Map<String, Object>> login(@RequestBody LoginParam loginParam) {
        Map<String, Object> result = Maps.newHashMap();
        UserDetails userDetails = userDetailsService.loadUserByUsername(loginParam.getUsername());
        //验证密码是否正确
        if (userDetails == null || !passwordEncoder.matches(loginParam.getPassword(), userDetails.getPassword())) {
            throw new BusinessException(ExceptionStatus.PASSWORD_ERROR);
        }
        // 通过 HTTP 客户端请求登录接口
        Map<String, String> params = Maps.newHashMap();
        params.put("username", loginParam.getUsername());
        params.put("password", loginParam.getPassword());
        params.put("grant_type", oauth2GrantType);
        params.put("client_id", oauth2ClientId);
        params.put("client_secret", oauth2ClientSecret);

        try {
            String key = loginParam.getUsername() + ":Token";
            String userToken = redisTemplate.opsForValue().get(key);
            if (userToken != null) {
                OAuth2AccessToken oAuth2AccessToken = tokenStore.readAccessToken(userToken);
                if (oAuth2AccessToken!=null) {
                    tokenStore.removeAccessToken(oAuth2AccessToken);
                }
                redisTemplate.delete(key);
            }
            // 解析响应结果封装并返回
            Response response = OkHttpClientUtil.getInstance().postData(URL_OAUTH_TOKEN, params);
            String jsonString = Objects.requireNonNull(response.body()).string();
            Map<String, Object> jsonMap = MapperUtils.json2map(jsonString);
            String token = String.valueOf(jsonMap.get("access_token"));
            result.put("token", token);
            redisTemplate.opsForValue().set(key, token);
        } catch (Exception e) {
            log.info("出现异常{}", e.getMessage());
            return new ResponseResult<>(ResponseResult.CodeStatus.OK, "登录超时", null);
        }

        return new ResponseResult<>(ResponseResult.CodeStatus.OK, "登录成功", result);
    }

    /**
     * 获取用户信息
     *
     * @return {@link ResponseResult}
     */
    @PreAuthorize("hasAuthority('USER')")
    @GetMapping(value = "/info")
    public ResponseResult info() throws Exception {
        // 获取个人信息
        String jsonString = profileFeign.info();
        TbUser tbUser = MapperUtils.json2pojoByTree(jsonString, "data", TbUser.class);

        // 如果触发熔断则返回熔断结果
        if (tbUser == null) {
            return MapperUtils.json2pojo(jsonString, ResponseResult.class);
        }

        // 封装并返回结果
        LoginInfo loginInfo = new LoginInfo();
        loginInfo.setName(tbUser.getUsername());
        loginInfo.setAvatar(tbUser.getIcon());
        loginInfo.setNickName(tbUser.getNickName());
        return new ResponseResult<>(ResponseResult.CodeStatus.OK, "获取用户信息", loginInfo);
    }

    /**
     * 注销
     *
     * @return {@link ResponseResult}
     */
    @PreAuthorize("hasAuthority('USER')")
    @PostMapping(value = "/logout")
    public ResponseResult<Void> logout(HttpServletRequest request) {
        // 删除 token 以注销
        OAuth2AccessToken oAuth2AccessToken = tokenStore.readAccessToken(
                request.getHeader("authorization").split(" ")[1]
        );
        tokenStore.removeAccessToken(oAuth2AccessToken);
        return new ResponseResult<Void>(ResponseResult.CodeStatus.OK, "用户已注销");
    }

}
