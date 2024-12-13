import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
public class historicoVentoinha {
    public static void main(String[] args) {
        banco bd = new banco();

        JFrame frame = new JFrame("Ativações da ventoinha:");
        frame.setSize(1000, 900);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JList<String> listaVentoinha = bd.getListaVentoinha(); 
        listaVentoinha.setFixedCellHeight(30);
        listaVentoinha.setFixedCellWidth(200);
        listaVentoinha.setBorder(BorderFactory.createTitledBorder("Ativações da ventoinha:"));

        JScrollPane scrollPane = new JScrollPane(listaVentoinha);
        scrollPane.setPreferredSize(new Dimension(600, 400));

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        panel.add(scrollPane, gbc);

        frame.add(panel, BorderLayout.CENTER);
        frame.setVisible(true);
    }
}
