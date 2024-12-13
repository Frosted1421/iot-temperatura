import org.eclipse.paho.client.mqttv3.*;

public class mqttEnvia {
    private String broker;
    private String clientId;
    private String username;
    private String password;
    private MqttClient client;

    public mqttEnvia(String broker, String clientId, String username, String password) throws MqttException {
        this.broker = broker;
        this.clientId = clientId;
        this.username = username;
        this.password = password;
        this.client = new MqttClient(broker, clientId);
        connectClient();
    }

    // Connect to the broker
    private void connectClient() throws MqttException {
        MqttConnectOptions options = new MqttConnectOptions();
        options.setUserName(username);
        options.setPassword(password.toCharArray());
        client.connect(options);
        System.out.println("Publisher connected to broker: " + broker);
    }

    // Publish a message to a topic
    public void publish(String topic, String message) throws MqttException {
        MqttMessage mqttMessage = new MqttMessage(message.getBytes());
        client.publish(topic, mqttMessage);
        System.out.println("Published to '" + topic + "': " + message);
    }

    // Close the connection
    public void close() throws MqttException {
        if (client.isConnected()) {
            client.disconnect();
            System.out.println("Publisher disconnected from broker.");
        }
        client.close();
    }
}
