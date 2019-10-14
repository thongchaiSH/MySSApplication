package com.soft.app.spring.security;

import com.soft.app.constant.ApplicationConstant;
import com.soft.app.entity.app.AppUser;
import com.soft.app.entity.app.AppUserOuAuth;
import com.soft.app.repository.AppUserOuAuthRepository;
import com.soft.app.repository.AppUserRepository;
import com.soft.app.repository.custom.AppUserRepositoryCustom;
import com.soft.app.util.BeanUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.cas.authentication.CasAssertionAuthenticationToken;
import org.springframework.security.core.userdetails.AuthenticationUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class CustomUserDetailsService implements UserDetailsService, AuthenticationUserDetailsService<CasAssertionAuthenticationToken> {

    private static final Logger LOGGER = LogManager.getLogger(CustomUserDetailsService.class);


    @Autowired
    AppUserRepository appUserRepository;

    @Autowired
    AppUserOuAuthRepository appUserOuAuthRepository;

    @Autowired
    AppUserRepositoryCustom appUserRepositoryCustom;

    @Autowired
    AuthorizeUtil authorizeUtil;

    //Default Attribute
    private String userName;
    private String password;


    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        try {
            AppUser appUser = appUserRepository.findByUsername(userName);
            UserInfo userInfo = new UserInfo();
            userInfo.setUsername(userName);
            this.userName = userName;
            if (BeanUtils.isNotNull(appUser)) {
                userInfo.setPassword(appUser.getPassword());
                LOGGER.info("---------------------------------");
                LOGGER.info("----   loadUserByUsername   -----");
                LOGGER.info("UserName : ===================>{}", userName);
                /* Support Detect for Tomcat Attribute */
                ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();

                List<AppUserOuAuth> appUserOuAuthList = appUserOuAuthRepository.findByAppUser(appUser);

                authorizeUtil.setUserName(userInfo.getUsername());
                if (BeanUtils.isNotEmpty(appUserOuAuthList)) {
                    authorizeUtil.setOuCode(appUserOuAuthList.get(0).getOuCode());
                    authorizeUtil.setRoleCode("user");
                } else {
                    authorizeUtil.setRoleCode("admin");
                }

                /* Add to Bean SESSION SCOPE */

                //initial Data

                attr.getRequest().getSession(true).setAttribute("userName", userName);

                LOGGER.info("session : " + attr.getRequest().getSession().getId());
                return userInfo;
            } else {
                LOGGER.error("No user with username " + userName + "' found!");
                throw new UsernameNotFoundException("No user with username '" + userName + "' found!");
            }

        } catch (Exception e) {
            LOGGER.error("error : {}", e.getMessage(), e);
            throw new RuntimeException(e.getMessage());
        }
    }


    @Override
    public UserDetails loadUserDetails(CasAssertionAuthenticationToken token) throws UsernameNotFoundException {
//        System.out.println("当前的用户名是：" + token.getName());
        LOGGER.info("LOGIN : {}",token.getName());
        /*这里我为了方便，就直接返回一个用户信息，实际当中这里修改为查询数据库或者调用服务什么的来获取用户信息*/
        UserInfo userInfo = new UserInfo();
        userInfo.setUsername(token.getName());
        userInfo.setName(token.getName());
        Set<AuthorityInfo> authorities = new HashSet<AuthorityInfo>();
        AuthorityInfo authorityInfo = new AuthorityInfo("TEST");
        authorities.add(authorityInfo);
        userInfo.setAuthorities(authorities);
        authorizeUtil.setUserName(userInfo.getUsername());

        //set session data
        ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        attr.getRequest().getSession(true).setAttribute("appVersion",ApplicationConstant.APPLICATION_VERSION);

        return userInfo;
    }
}
