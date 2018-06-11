package com.shiro.filter;

import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authz.AuthorizationFilter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

/**
 * 自定义的权限过滤器
 * @author Luke
 * @date 2018/6/11.
 */
public class RolesOrFilter extends AuthorizationFilter {
    @Override
    protected boolean isAccessAllowed(ServletRequest request,
                                      ServletResponse response, Object o) throws Exception {
        //1.获取主体对象
        Subject subject = getSubject(request, response);
        //2.获取shiro配置中的角色限制列表   /testRoles1 = rolesOr["admin","admin1"]
        String[] roles = (String[]) o;
        //3.若角色列表为空，则说明无限制
        if (roles == null || roles.length == 0) {
            return true;
        }

        //否则有限制，则判断当前角色是否有对应权限
        for (String role : roles) {
            if (subject.hasRole(role)) {
                return true;
            }
        }
        
        return false;
    }
}
