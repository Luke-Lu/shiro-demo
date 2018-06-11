package com.shiro.dao;

import com.shiro.entity.User;
import org.springframework.stereotype.Component;

import java.util.Set;

/**
 * @author Luke
 * @date 2018/6/1.
 */
@Component
public interface UserDao {

    User getPasswordByUserName(String username);

    Set<String> getRolesByUserName(String username);
}
