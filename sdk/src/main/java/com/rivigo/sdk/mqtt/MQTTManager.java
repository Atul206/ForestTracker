package com.rivigo.sdk.mqtt;

import android.content.Context;
import android.os.Handler;
import android.util.Log;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.DisconnectedBufferOptions;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttMessageListener;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * Created by Atul on 11-04-2017.
 */

public class MQTTManager implements MqttConnectionCallBack, MqttCallback {

    public final static String TAG = MQTTManager.class.getName();
    public static boolean isConnected;
    private final String CLIENT = "RIVIGO_PILOT:";
    private Context context;
    private int id;
    private Thread publishThreadMQTT;
    private BlockingDeque<String> queue = new LinkedBlockingDeque<String>();
    private MqttAndroidClient mqttAndroidClient;
    //private String brokerUrl = "tcp://52.76.183.107:1883"; // prod
    private String brokerUrl = "tcp://52.221.141.251:1883"; // stg
    private String userName;
    private String password;
    private int QOS;
    private String topic;
    private OnMQTTConnection onConnection;


    public MQTTManager(Context context, int id, String brokerUrl, String userName, String passWord, int Qos, String topic, OnMQTTConnection onMQTTConnection) {
        this.context = context;
        this.id = id;
        this.brokerUrl = brokerUrl;
        this.userName = userName;
        this.password = passWord;
        this.QOS = Qos;
        this.topic = topic;
        this.onConnection = onMQTTConnection;
    }

    /*
    * Initiate connection with MQTT
    * */
    public void init() {
        setupConnectionFactory();
    }

    /**
     * We can subscribe topic from here
     */

    public void subscribeToTopic() {
        try {
            /**
             * @param topic_name
             * @param QOS_level (0,1,2)
             * @param userContext
             * @param Listener
             */
            mqttAndroidClient.subscribe(topic, QOS, null, new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    addToHistory("Subscribed!");
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    addToHistory("Failed to subscribe");
                }
            });

            /**
             * @param topic_name
             * @param QOS_level (0,1,2)
             * @param Listener
             */
            mqttAndroidClient.subscribe(topic, QOS, new IMqttMessageListener() {
                @Override
                public void messageArrived(String topic, MqttMessage message) throws Exception {
                    /*
                    message Arrived!
                    * */
                    System.out.println("Message: " + topic + " : " + new String(message.getPayload()));
                }
            });

        } catch (MqttException ex) {
            System.err.println("Exception whilst subscribing");
            ex.printStackTrace();
        }
    }

    /**
     * @param mainText subscribe message arrived you can add into history or database
     */
    private void addToHistory(String mainText) {
        //System.out.println("LOG: " + mainText);
        /*
        * TODO : implement database history
        * */
    }

    /*
    * Publisher method: publishing data over MQTT broker (RABBIT_MQ)
    * */

    public void publishMessage() {
        publishThreadMQTT = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    if (mqttAndroidClient != null) {
                        MqttMessage message = new MqttMessage(queue.takeFirst().getBytes());
                        if (message != null) {
                            mqttAndroidClient.publish(topic, message);
                            onConnection.onPublish();

                        }
                        /*if (!mqttAndroidClient.isConnected()) {
                            //addToHistory(mqttAndroidClient.getBufferedMessageCount() + " messages in buffer.");
                        }*/
                    }
                } catch (MqttException e) {
                    onConnection.onPublishFail();
                    System.err.println("Error Publishing: " + e.getMessage());
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    onConnection.onPublishFail();
                    e.printStackTrace();
                }
            }
        });
        publishThreadMQTT.start();

    }

    /**
     * SetConnection establish or not
     */
    private void setConnectionCallback() {
        mqttAndroidClient.setCallback(new MqttCallbackExtended() {
            @Override
            public void connectComplete(boolean reconnect, String serverURI) {

                if (reconnect) {
                    addToHistory("Reconnected to : " + serverURI);
                    // Because Clean Session is true, we need to re-subscribe
                    //subscribeToTopic();
                } else {
                    addToHistory("Connected to: " + serverURI);
                }
            }

            @Override
            public void connectionLost(Throwable cause) {
                addToHistory("The Connection was lost.");
            }

            @Override
            public void messageArrived(String topic, MqttMessage message) throws Exception {
                addToHistory("Incoming message: " + new String(message.getPayload()));
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken token) {

            }
        });
    }

    /**
     * setup MQTT connection option setup
     *
     * @param mqttConnectOptions
     */
    private void setupConnectionOptions(MqttConnectOptions mqttConnectOptions) {

        mqttConnectOptions.setUserName(userName);
        mqttConnectOptions.setPassword(password.toCharArray());
        mqttConnectOptions.setKeepAliveInterval(86400);
        mqttConnectOptions.setAutomaticReconnect(true);
        mqttConnectOptions.setCleanSession(false);
    }

    /**
     * Client Connection setup
     *
     * @param mqttConnectOptions
     */
    private void mqttConnectClientConnection(final MqttConnectOptions mqttConnectOptions) {
        if(mqttAndroidClient.isConnected()){
            return;
        }
        try {
            mqttAndroidClient.connect(mqttConnectOptions, null, new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    try {
                        DisconnectedBufferOptions disconnectedBufferOptions = new DisconnectedBufferOptions();
                        disconnectedBufferOptions.setBufferEnabled(true);
                        disconnectedBufferOptions.setBufferSize(5000);
                        disconnectedBufferOptions.setPersistBuffer(false);
                        disconnectedBufferOptions.setDeleteOldestMessages(false);
                        if (mqttAndroidClient != null) {
                            mqttAndroidClient.setBufferOpts(disconnectedBufferOptions);
                        }
                        onConnection.onSuccess();
                        Log.d(TAG, "CONNECTION SUCCESS");
                    }catch (Exception e){
                        e.printStackTrace();
                    }

                    //subscribeToTopic();
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    Log.d(TAG, "CONNECTION SUCCESS");
                    onConnection.onFail();
                    exception.printStackTrace();
                    //addToHistory("Failed to connect to: " + brokerUrl);
                }
            });


        } catch (MqttException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void setupConnectionFactory() {
        if (mqttAndroidClient == null || !mqttAndroidClient.isConnected()) {
            mqttAndroidClient = new MqttAndroidClient(context, brokerUrl, (CLIENT + id));
            MqttConnectOptions mqttConnectOptions = new MqttConnectOptions();
            setupConnectionOptions(mqttConnectOptions);
            mqttConnectClientConnection(mqttConnectOptions);
        }
    }

    @Override
    public void publishToAMQP() {

    }

    @Override
    public void setupPubButton() {

    }

    @Override
    public void subscribe(Handler incomingMessageHandler) {

    }

    @Override
    public boolean publishMessage(String message) {
        if (mqttAndroidClient.isConnected()) {
            try {
                isConnected = true;
                Log.d(TAG, message);
                queue.putLast(message);
                publishMessage();
                return true;
            } catch (InterruptedException e) {
                e.printStackTrace();
                return false;
            }
        }
        return false;
    }

    @Override
    public void stopMqtt() {

    }

    @Override
    public void startMqtt() {

    }

    @Override
    public void connectionLost(Throwable cause) {

    }

    @Override
    public void messageArrived(String topic, MqttMessage message) throws Exception {

    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {
        Log.d(TAG, String.valueOf(token.getResponse()));
    }

    public void destroyClient() {
        if(mqttAndroidClient == null) return;
        try {
            mqttAndroidClient.disconnect();
        } catch (MqttException e) {
            e.printStackTrace();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public boolean isConnected() {
        Log.d(TAG, "CONNECTION CHECK");
        if(mqttAndroidClient == null) {
            Log.d(TAG, "MQTT NOT CONNECTED");
            return false;
        }
        if (mqttAndroidClient.isConnected()) {
            Log.d(TAG, "MQTT CONNECTED");
            return true;
        } else {
            Log.d(TAG, "MQTT CONNECTION FAIL");
            return false;
        }


    }

}
