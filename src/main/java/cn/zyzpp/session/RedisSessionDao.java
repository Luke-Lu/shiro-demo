package cn.zyzpp.session;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.UnknownSessionException;
import org.apache.shiro.session.mgt.eis.AbstractSessionDAO;
import org.springframework.util.SerializationUtils;

import cn.zyzpp.util.JedisUtil;

/**
 * Create by yster@foxmail.com 2018-05-14 09:47:36
**/
public class RedisSessionDao extends AbstractSessionDAO {
	
	@Resource
	private JedisUtil jedisUtil;
	
	private final String SHIRO_SESSION_PREFIX = "test-session";
	
	private byte[] getKey(String key) {
		return (SHIRO_SESSION_PREFIX + key).getBytes();
	}
	
	/**
	 * 保存Session
	 * @param session
	 */
	private void saveSession(Session session) {
		if(session != null && session.getId() != null) {
			byte[] key = getKey(session.getId().toString());
			byte[] value = SerializationUtils.serialize(session);
			jedisUtil.set(key, value);
			jedisUtil.expire(key, 600);
		}
	}
	
	/**
	 * 更新Session
	 */
	@Override
	public void update(Session session) throws UnknownSessionException {
		saveSession(session);
	}

	/**
	 * 删除Session
	 */
	@Override
	public void delete(Session session) {
		if(session == null || session.getId() == null) {
			return;
		}
		byte[] key = getKey(session.getId().toString());
		jedisUtil.del(key);
	}

	/**
	 * 获取存活的session
	 */
	@Override
	public Collection<Session> getActiveSessions() {
		Set<byte[]> keys = jedisUtil.keys(SHIRO_SESSION_PREFIX);
		Set<Session> sessions = new HashSet<>();
		if(CollectionUtils.isEmpty(keys)) {
			return sessions;
		}
		for(byte[] key : keys) {
			Session session = (Session) SerializationUtils.deserialize(jedisUtil.get(key));
			sessions.add(session);
		}
		return sessions;
	}

	/**
	 * 创建seesion
	 */
	@Override
	protected Serializable doCreate(Session session) {
		Serializable sessionId = generateSessionId(session);
		assignSessionId(session, sessionId);
		saveSession(session);
		return sessionId;
	}

	/**
	 * 获取seesion
	 */
	@Override
	protected Session doReadSession(Serializable sessionId) {
		System.out.println("read session");
		if(sessionId == null) {
			return null;
		}
		byte[] key = getKey(sessionId.toString());
		byte[] value = jedisUtil.get(key);
		return (Session) SerializationUtils.deserialize(value);
	}

}
