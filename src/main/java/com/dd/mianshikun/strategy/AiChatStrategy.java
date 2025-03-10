package com.dd.mianshikun.strategy;


import io.reactivex.Flowable;
import reactor.core.publisher.Flux;

public interface AiChatStrategy {

    String doSyncStableRequest(String systemMessage, String userMessage);

    String doSyncUnstableRequest(String systemMessage, String userMessage);

    Flowable<Character> doStreamStableRequest(String systemMessage, String userMessage);

    Flowable<Character> doStreamUnstableRequest(String systemMessage, String userMessage);
}
