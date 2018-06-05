package cn.zyzpp.vo;
/**
 * Create by yster@foxmail.com 2018-05-13 11:01:56
**/
public class User {
	private String username;
	private String password;
	private boolean rememberMe;
	
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
}
	public boolean isRememberMe() {
		return rememberMe;
	}
	public void setRememberMe(boolean rememberMe) {
		this.rememberMe = rememberMe;
	}

}
