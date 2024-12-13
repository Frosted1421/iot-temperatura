import java.awt.*;
import javax.swing.*;
//javac -cp "../lib/mysql-connector-java.jar:../libs/org.eclipse.paho.client.mqttv3-1.2.5.jar:../lib/jfreechart-1.0.19.jar:../lib/jcommon-1.0.23.jar:." banco.java MQTTSubscriber.java listaMedicoes.java graficoMedicoesTemperatura.java graficoMedicoesUmidade.java setTemperatura.java


public class graficoIrriga {
    public static void main(String[] args) {
        banco bd = new banco();
        JFrame frame = new JFrame("Ativações do Irrigador");
        frame.setSize(1000, 900);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        // Painel do gráfico
        JPanel painelGrafico = bd.getGraficoIrriga();
        frame.add(painelGrafico, BorderLayout.CENTER);

        frame.setVisible(true);
    }
}
