package com.dd.mianshikun.strategy;


import reactor.core.publisher.Flux;

public interface AiChatStrategy {

    String doSyncStableRequest(String systemMessage, String userMessage);

    String doSyncUnstableRequest(String systemMessage, String userMessage);

    Flux<?> doStreamStableRequest(String systemMessage, String userMessage);

    Flux<?> doStreamUnstableRequest(String systemMessage, String userMessage);
}
