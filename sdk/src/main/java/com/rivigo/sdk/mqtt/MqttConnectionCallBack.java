package com.rivigo.sdk.mqtt;

import android.os.Handler;

/**
 * Created by Atul on 11-04-2017.
 */

public interface MqttConnectionCallBack {
    void  setupConnectionFactory();
    void publishToAMQP();
    void setupPubButton();
    void subscribe(Handler incomingMessageHandler);
    boolean publishMessage(String msg);
    void stopMqtt();
    void startMqtt();
}
