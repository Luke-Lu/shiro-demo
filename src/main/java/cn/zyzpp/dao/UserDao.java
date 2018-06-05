package cn.zyzpp.dao;

import java.util.List;

import org.springframework.stereotype.Component;

import cn.zyzpp.vo.User;
@Component
public interface UserDao {

	User getUserByUsername(String username);

	List<String> getRolesByUserName(String username);

}
