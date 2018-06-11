package com.shiro.realm;

import com.shiro.dao.UserDao;
import com.shiro.entity.User;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * 自定义Realm继承AuthorizingRealm 重写AuthorizationInfo(授权)和AuthenticationInfo(认证)
 *
 * @author Luke
 * @date 2018/5/31.
 */
public class CustomRealm extends AuthorizingRealm {

    @Autowired
    private UserDao userDao;

    Map<String, String> userMap = new HashMap<>();

    {
//        userMap.put("luke", "123456");
        //加盐
        Md5Hash md5 = new Md5Hash("123456", "luke");
        userMap.put("luke", md5.toString());
    }

    /**
     * 授权
     *
     * @param principalCollection
     * @return
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        //获取用户名称
        String username = (String) principalCollection.getPrimaryPrincipal();
        //获取角色
        Set<String> roles = getRolesByUserName(username);
        //设置权限列表
        Set<String> permissions = getPermissionsByUserName();
        //权限信息对象
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        //添加权限列表
        info.setStringPermissions(permissions);
        //添加权限角色
        info.setRoles(roles);
        return info;
    }

    /**
     * 认证
     *
     * @param authenticationToken
     * @return
     * @throws AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        String username = (String) authenticationToken.getPrincipal();
        //若获取到用户名称为空，则未通过认证
        if (StringUtils.isEmpty(username)) {
            return null;
        }

        String password = getPasswordByUserName(username);
        if (StringUtils.isEmpty(password)) {
            return null;
        }

        SimpleAuthenticationInfo info = new SimpleAuthenticationInfo(username, password, this.getName());
        //加盐，加密
        info.setCredentialsSalt(ByteSource.Util.bytes(username));
        return info;
    }

    private Set<String> getPermissionsByUserName() {
        Set<String> sets = new HashSet<>();
        sets.add("user:delete");
        sets.add("user:add");
        return sets;
    }

    private Set<String> getRolesByUserName(String username) {
        System.out.println("从数据库中获取角色");
        Set<String> sets = userDao.getRolesByUserName(username);
//        sets.add("admin");
//        sets.add("user");
        return sets;
    }

    private String getPasswordByUserName(String username) {
        System.out.println("从数据库中获取密码");
        User user = userDao.getPasswordByUserName(username);
        if (user != null) {
            return user.getPassword();
        }
        return  null;
//        return userMap.get(username);
    }

    public static void main(String[] args) {
        Md5Hash md5Hash = new Md5Hash("123456", "luke");
        System.out.println(md5Hash);
    }
}
