package com.sjj.taskmanagement.service;

import org.springframework.amqp.core.Message;

public interface DelayedQueueService {
    void sendMsg(String message,Integer delayTime);
    void recieveDelayQueue(Message message);
}
