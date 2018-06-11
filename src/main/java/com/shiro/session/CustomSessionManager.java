package com.shiro.session;

import org.apache.shiro.session.Session;
import org.apache.shiro.session.UnknownSessionException;
import org.apache.shiro.session.mgt.SessionKey;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.apache.shiro.web.session.mgt.WebSessionKey;

import javax.servlet.ServletRequest;
import java.io.Serializable;

/**
 * @author Luke
 * @date 2018/6/11.
 */
public class CustomSessionManager extends DefaultWebSessionManager {

    @Override
    protected Session retrieveSession(SessionKey sessionKey) throws UnknownSessionException {
        Serializable sessionId = getSessionId(sessionKey);
        //获取request
        ServletRequest request = null;
        if (sessionKey instanceof WebSessionKey) {
            request = ((WebSessionKey) sessionKey).getServletRequest();
        }
        //若request不为空，并且sessionID不为空，则从request中获取session
        if (request != null && sessionId != null) {
            Session session = (Session) request.getAttribute(sessionId.toString());
            //获取到的session不为空，则返回
            if (session != null) {
                return session;
            }
        }

        //否则从redisSessionDao中获取session
        Session session = super.retrieveSession(sessionKey);
        //若获取的session不为空，则设值到request的作用域中
        if (request != null && sessionId != null) {
            request.setAttribute(sessionId.toString(), session);
        }

        return session;
    }
}
