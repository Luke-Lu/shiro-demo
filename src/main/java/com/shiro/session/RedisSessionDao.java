package com.shiro.session;

import com.shiro.util.JedisUtil;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.UnknownSessionException;
import org.apache.shiro.session.mgt.eis.AbstractSessionDAO;
import org.apache.shiro.util.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.SerializationUtils;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * redis存储session
 *
 * @author Luke
 * @date 2018/6/11.
 */
public class RedisSessionDao extends AbstractSessionDAO {

    @Autowired
    private JedisUtil jedisUtil;

    public static final String SESSION_PREFIX = "test_session:";

    private byte[] getKey(String key) {
        return (SESSION_PREFIX + key).getBytes();
    }

    @Override
    protected Serializable doCreate(Session session) {
        //1.获取sessionid
        Serializable sessionId = generateSessionId(session);
        //2.关联sessionId和session
        assignSessionId(session, sessionId);
        //3.存储到redis中
        saveSession(session);
        return sessionId;
    }

    private void saveSession(Session session) {
        if (session != null && session.getId() != null) {
            byte[] key = getKey(session.getId().toString());
            byte[] value = SerializationUtils.serialize(session);
            jedisUtil.set(key, value);
            jedisUtil.expire(key, 600);
        }
    }

    @Override
    protected Session doReadSession(Serializable sessionId) {
        //默认会读取多次的session
        System.out.println("read redis session");
        if (sessionId != null) {
            byte[] value = jedisUtil.get(getKey(sessionId.toString()));
            return (Session) SerializationUtils.deserialize(value);
        }
        return null;
    }

    @Override
    public void update(Session session) throws UnknownSessionException {
        saveSession(session);
    }

    @Override
    public void delete(Session session) {
        if (session != null && session.getId() != null) {
            jedisUtil.del(getKey(session.getId().toString()));
        }
    }

    @Override
    public Collection<Session> getActiveSessions() {
        Set<byte[]> keys =  jedisUtil.keys(SESSION_PREFIX);
        Set<Session> sessions = new HashSet<>();
        //若查询到的redis中的keys为空，则返回空列表
        if (CollectionUtils.isEmpty(keys)) {
            return sessions;
        }
        for (byte[] key : keys) {
            Session session = (Session) SerializationUtils.deserialize(jedisUtil.get(key));
            sessions.add(session);
        }
        return sessions;
    }
}
