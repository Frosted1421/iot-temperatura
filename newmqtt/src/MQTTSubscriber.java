/*
sudo systemctl start mysqld
sudo systemctl enable mysqld

sudo systemctl start httpd
sudo systemctl enable httpd

javac -cp "../lib/mysql-connector-java.jar:../libs/org.eclipse.paho.client.mqttv3-1.2.5.jar:../lib/jfreechart-1.0.19.jar:../lib/jcommon-1.0.23.jar:." banco.java MQTTSubscriber.java listaMedicoes.java graficoMedicoesTemperatura.java graficoMedicoesUmidade.java menuPrincipal.java setTemperatura.java historicoVentoinha.java graficoVentoinha.java

java -cp "../lib/mysql-connector-java.jar:../libs/org.eclipse.paho.client.mqttv3-1.2.5.jar:../lib/jfreechart-1.0.19.jar:../lib/jcommon-1.0.23.jar:." MQTTSubscriber.java

enviar id para teste
mosquitto_pub -h localhost -t test -m "< UID >" -u frosted27 -P Slaman2208
*/

import java.math.BigDecimal;
import java.text.ParseException;
import org.eclipse.paho.client.mqttv3.*;

public class MQTTSubscriber {
    public static void addTempUmi(String temp, String umidade) {
        banco bd = new banco();
        bd.addTemp(temp, umidade);
    }

    public static void enviarUmidade(String umidade) throws MqttException {
        String broker = "tcp://192.168.0.101:1884";
        String clientId = "JavaPublisher";
        String topicEsp = "esp32";
        mqttEnvia publisher = new mqttEnvia(broker, clientId, "frosted27", "Slaman2208");

        String mensagem = umidade + "UM";
        publisher.publish(topicEsp, mensagem);
        publisher.close();
    }
    public static void enviarTemp(String temp) throws MqttException{
        String broker ="tcp://192.168.0.28:1884";
        String clientId="JavaPublisher";
        String topicEsp="esp32";
        mqttEnvia publisher = new mqttEnvia(broker, clientId, "frosted27", "Slaman2208");

        String mensagem = temp+"TE";
        publisher.publish(topicEsp, mensagem);
        publisher.close();
    }
    public static void enviarIntervalo(String intervalo) throws MqttException{
        String broker ="tcp://192.168.0.28:1884";
        String clientId="JavaPublisher";
        String topicEsp="esp32";
        mqttEnvia publisher = new mqttEnvia(broker, clientId, "frosted27", "Slaman2208");

        String mensagem = intervalo+"TI";
        publisher.publish(topicEsp, mensagem);
        publisher.close();
    }
    public static void ativarSprinkler() throws MqttException{
        String broker ="tcp://192.168.0.28:1884";
        String clientId="JavaPublisher";
        String topicEsp="esp32";
        mqttEnvia publisher = new mqttEnvia(broker, clientId, "frosted27", "Slaman2208");

        String mensagem = "ATVA";
        publisher.publish(topicEsp, mensagem);
        publisher.close();
    }

    public static void main(String[] args) {
        String broker = "tcp://192.168.0.28:1884";
        String topic = "test";
        String clientId = "JavaSubscriber";
        String username = "frosted27";
        String password = "Slaman2208";

        try {
            MqttClient client = new MqttClient(broker, clientId);

            MqttConnectOptions options = new MqttConnectOptions();
            options.setUserName(username);
            options.setPassword(password.toCharArray());

            client.setCallback(new MqttCallback() {
                @Override
                public void connectionLost(Throwable cause) {
                    System.out.println("Connection lost: " + cause.getMessage());
                }

                @Override
                public void messageArrived(String topic, MqttMessage message) throws Exception {
                    String novaMedicao = new String(message.getPayload()).trim();
                    int sinal;
                    System.out.println(novaMedicao);
                    banco bd = new banco();

                    
                    if (novaMedicao.length() == 8 && novaMedicao!= null) {
                        String novaTemp = novaMedicao.substring(0, 2);
                        String flagTemp = novaMedicao.substring(2, 4);
                        String novaUmidade = novaMedicao.substring(4, 6);
                        String flagUmidade = novaMedicao.substring(6, 8);

                        if (flagTemp.equals("TE") && flagUmidade.equals("UM")) {
                            addTempUmi(novaTemp, novaUmidade);
                        } else {
                            System.out.println("Flags inválidas");
                        }
                    } else if(novaMedicao.length() == 4 ){
                        String novaAtivacao=novaMedicao.substring(1,2);
                        String flagAtivacao=novaMedicao.substring(2,4);

                        try {
                            System.out.println("tentando converter"+novaAtivacao);
                            sinal = Integer.parseInt(novaAtivacao);  
                            System.out.println("convertido:"+sinal);      
                        } catch (Exception e) {
                            throw new IllegalArgumentException(e);
                            
                        }
                        if(flagAtivacao.equals("AV")){
                            System.out.println("sinal da ventoinha:"+flagAtivacao);
                            bd.addSinalVentoinha(sinal);   
                        }else if(flagAtivacao.equals("AI")){
                            System.out.println("Sinal da irrigação"+flagAtivacao);
                            bd.addSinalIrrigador(sinal);
                        }
                    } 
                    
                }
                @Override
                public void deliveryComplete(IMqttDeliveryToken token) {
                    System.out.println("Delivery complete: " + token.getMessageId());
                }
            });

            client.connect(options);
            System.out.println("Connected to broker: " + broker);
            client.subscribe(topic);
            System.out.println("Subscribed to topic: " + topic);

        } catch (MqttException e) {
            e.printStackTrace();
        }
    }
}
