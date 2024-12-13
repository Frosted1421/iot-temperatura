import java.awt.*;
import javax.swing.*;

public class graficoVentoinha{
    public static void main(String[] args) {
        banco bd = new banco();
        JFrame frame = new JFrame("Ativações Ventoinha:");
        frame.setSize(1000, 900);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        JPanel painelGrafico = bd.getGraficoVentoinha();
        frame.add(painelGrafico, BorderLayout.CENTER);
        frame.setVisible(true);
    }
}
