package com.miyu.cloud.macs.restServer.webSocket;

import cn.iocoder.yudao.framework.common.util.json.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.stream.Collectors;

@Component
@Slf4j
@ServerEndpoint("/macs/websocket/{userId}")
public class WebSocket {

    private Session session;

    private static CopyOnWriteArraySet<WebSocket> webSockets =new CopyOnWriteArraySet<>();
    private static Map<String,Session> sessionPool = new HashMap<String,Session>();
    private static Map<String,String> collectorRead = new HashMap<String,String>();

    @OnOpen
    public void onOpen(Session session, @PathParam(value="userId")String userId) {
        try {
            this.session = session;
            webSockets.add(this);
            sessionPool.put(userId, session);
            log.info("【websocket消息】有新的连接，总数为:"+webSockets.size());
        } catch (Exception e) {
        }
    }

    @OnClose
    public void onClose() {
        try {
            webSockets.remove(this);
            log.info("【websocket消息】连接断开，总数为:"+webSockets.size());
        } catch (Exception e) {
        }
    }

    @OnMessage
    public void onMessage(String message) {
        if (message.startsWith("collectorRead:")) {
            Map<String, String> jsonObject = JsonUtils.parseObject(message.substring(14), Map.class);
            assert jsonObject != null;
            collectorRead.put(jsonObject.get("collectorCode"), jsonObject.get("userId"));
        }
        if (message.startsWith("collectorReadClean:")) {
            String userId = message.substring(19);
            List<String> deleteList = collectorRead.entrySet().stream().filter(entry -> userId.equals(entry.getValue())).map(Map.Entry::getKey).collect(Collectors.toList());
            for (String key : deleteList) {
                collectorRead.remove(key);
            }
        }
    }

    // 此为广播消息
    public void sendAllMessage(String message) {
//        log.info("【websocket消息】广播消息:"+message);
        for(WebSocket webSocket : webSockets) {
            try {
                if(webSocket.session.isOpen()) {
                    webSocket.session.getAsyncRemote().sendText(message);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    // 此为单点消息
    public void sendOneMessage(String userId, String message) {
        Session session = sessionPool.get(userId);
        if (session != null&&session.isOpen()) {
            try {
//                log.info("【websocket消息】 单点消息:"+message);
                session.getAsyncRemote().sendText(message);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    // 此为单点消息(多人)
    public void sendMoreMessage(String[] userIds, String message) {
        for(String userId:userIds) {
            Session session = sessionPool.get(userId);
            if (session != null&&session.isOpen()) {
                try {
                    log.info("【websocket消息】 单点消息:"+message);
                    session.getAsyncRemote().sendText(message);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

    }

    public String getUserIdByCollect(String collectorCode) {
        String userId = collectorRead.remove(collectorCode);
        if (userId != null && !sessionPool.containsKey(userId)) return null;
        return userId;
    }

}
