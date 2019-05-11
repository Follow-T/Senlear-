package com.spring.boot.community.qqfilter;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.spring.boot.community.domin.Authority;
import com.spring.boot.community.domin.User;
import com.spring.boot.community.service.AuthorityService;
import com.spring.boot.community.service.UserService;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
public class QQAuthenticationManager implements AuthenticationManager {


    @Autowired
    private UserService userService;

    @Autowired
    private AuthorityService authorityService;

    private  static QQAuthenticationManager qqAuthenticationManager;

    @PostConstruct
    public void init(){
        qqAuthenticationManager=this;
        qqAuthenticationManager.userService=this.userService;
        qqAuthenticationManager.authorityService=this.authorityService;
    }

    private static final List<GrantedAuthority> AUTHORITIES = new ArrayList<>();

    /**
     * client_id 由腾讯提供
     */
    static final String clientId = "101568799";

    /**
     * 获取 QQ 登录信息的 API 地址
     */
    private final static String userInfoUri = "https://graph.qq.com/user/get_user_info";

    /**
     * 获取 QQ 用户信息的地址拼接
     */
    private final static String USER_INFO_API = "%s?access_token=%s&oauth_consumer_key=%s&openid=%s";

    static {
        AUTHORITIES.add(new SimpleGrantedAuthority("ROLE_TG"));
    }

    @Override
    public Authentication authenticate(Authentication auth) throws AuthenticationException {
        if (auth.getName() != null && auth.getCredentials() != null) {
            User user = getUserInfo(auth.getName(), (String) (auth.getCredentials()));
            return new UsernamePasswordAuthenticationToken(user,
                    null, AUTHORITIES);

        }
        throw new BadCredentialsException("Bad Credentials");
    }

    private User getUserInfo(String accessToken, String openId) {
        String url = String.format(USER_INFO_API, userInfoUri, accessToken, clientId, openId);
        Document document;
        try {
            document = Jsoup.connect(url).get();
        } catch (IOException e) {
            throw new BadCredentialsException("Bad Credentials!");
        }
        String resultText = document.text();

        //System.out.println(resultText);
        JSONObject json = JSON.parseObject(resultText);


        //以openId作为username的唯一标识
        User user = qqAuthenticationManager.userService.findByUsername(openId);
        if(user==null) {
            user=new User(openId, json.getString("nickname"),"123456",1);
            user.setSex(json.getString("gender"));
            user.setAvatar(json.getString("figureurl_qq_2"));
            user.setAddress(json.getString("province"));
            List<Authority> authorities=new ArrayList<>();
            authorities.add(qqAuthenticationManager.authorityService.getAuthorityById(1L));
            user.setAuthorities(authorities);
            qqAuthenticationManager.userService.saveUser(user);
        }
        return user;
    }
}
