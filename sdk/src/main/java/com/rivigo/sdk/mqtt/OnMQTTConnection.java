package com.rivigo.sdk.mqtt;

/**
 * Created by atulsakhala on 08/11/17.
 */

public interface OnMQTTConnection {
    void onSuccess();
    void onFail();
    void onPublish();
    void onPublishFail();
}
