package com.shiro;

import com.shiro.realm.CustomRealm;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.subject.Subject;
import org.junit.Test;

/**
 * @author Luke
 * @date 2018/6/1.
 */
public class CustomRealmTest {

    CustomRealm customRealm = new CustomRealm();

    @Test
    public void testAuthentication(){
        //1.构建SecurityManager环境
        DefaultSecurityManager defaultSecurityManager = new DefaultSecurityManager();
        defaultSecurityManager.setRealm(customRealm);

        //声明CustomRealm使用md5加密
        HashedCredentialsMatcher matcher = new HashedCredentialsMatcher();
        matcher.setHashAlgorithmName("md5");
        matcher.setHashIterations(1);
        customRealm.setCredentialsMatcher(matcher);

        SecurityUtils.setSecurityManager(defaultSecurityManager);
        //获取主体信息
        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken("luke", "123456");
        //2.主体提交认证请求
        subject.login(token);

        System.out.println("是否登录:" + subject.isAuthenticated());

        subject.checkRole("admin");

        subject.checkPermission("user:add");
        subject.checkPermissions("user:add","user:delete");


    }
}
