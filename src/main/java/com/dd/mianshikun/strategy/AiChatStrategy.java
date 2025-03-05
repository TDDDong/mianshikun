package com.dd.mianshikun.strategy;


public interface AiChatStrategy {

    String doSyncStableRequest(String systemMessage, String userMessage);

    String doSyncUnstableRequest(String systemMessage, String userMessage);
}
