package com.rivigo.sdk.manager;

import android.content.Context;
import android.util.Log;

import com.rivigo.sdk.Constant;
import com.rivigo.sdk.mqtt.MQTTManager;
import com.rivigo.sdk.mqtt.OnMQTTConnection;

/**
 * Created by gauravk on 29/6/16.
 */
public class SDKSensorManager implements Constant {

    private static final String TAG = SDKSensorManager.class.getSimpleName();
    private MQTTManager mqttManager;
    public static SDKSensorManager mInstance;
    private Context context;

    public SDKSensorManager(Context ctx) {
        this.context = ctx;
    }

    public static SDKSensorManager getInstance(Context context) {
            if(mInstance == null) {
                mInstance = new SDKSensorManager(context);
            }

            return mInstance;
    }

    public SDKSensorManager setup(int uniqueId, String host, String userName, String password, int qos, String topic, OnMQTTConnection mqttConnection) {
        if(mqttManager == null) {
            mqttManager = new MQTTManager(context, uniqueId, host, userName, password, qos, topic, mqttConnection);
            Log.d(MQTTManager.TAG, "MQTT DEFINED");
        }else{
            Log.d(MQTTManager.TAG, "MQTT ALREADY DEFINED");
        }
        return this;
    }

    public MQTTManager getMqttManager() {
        return mqttManager;
    }

    public void closeMqttConnection() {
        if (mqttManager != null) {
            mqttManager.destroyClient();
            Log.d(MQTTManager.TAG, "MQTT DESTROY");
        }else{
            Log.d(MQTTManager.TAG, "ALREADY DESTORY");
        }
    }

    public boolean initMQTTConnection(){
        if(mqttManager != null) {
            if (!mqttManager.isConnected()) {
                Log.d(MQTTManager.TAG, "Connection making");
                mqttManager.init();
                return false;
            }
            Log.d(MQTTManager.TAG, "Already had connection");
            return true;
        }
        Log.d(MQTTManager.TAG, "MQTT not defined");
        return false;
    }
}
