import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import java.sql.*;
import org.jfree.chart.*;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.ChartPanel;
import org.jfree.data.category.DefaultCategoryDataset;

public class graficoMedicoesUmidade {

    public static void main(String[] args) {
        banco bd = new banco();
        JFrame frame = new JFrame("Medições:");
        frame.setSize(1000, 900);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        JPanel datePanel = new JPanel();
        datePanel.setLayout(new FlowLayout());

        JLabel startDateLabel = new JLabel("Data Inicial (YYYY-MM-DD):");
        JTextField startDateField = new JTextField(10);

        JLabel endDateLabel = new JLabel("Data Final (YYYY-MM-DD):");
        JTextField endDateField = new JTextField(10);

        JButton filterButton = new JButton("Filtrar");

        datePanel.add(startDateLabel);
        datePanel.add(startDateField);
        datePanel.add(endDateLabel);
        datePanel.add(endDateField);
        datePanel.add(filterButton);

        frame.add(datePanel, BorderLayout.NORTH);

        JPanel umidadePanel = bd.getGraficoUmidade();
        JPanel umidadeChartPanel = new JPanel(new BorderLayout());
        umidadeChartPanel.add(umidadePanel, BorderLayout.CENTER);
        frame.add(umidadeChartPanel, BorderLayout.CENTER);

        filterButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String startDate = startDateField.getText();
                String endDate = endDateField.getText();

                if (!startDate.isEmpty() && !endDate.isEmpty()) {
                    JPanel newTempChartPanel = bd.getGraficoTemp(startDate, endDate);

                    umidadeChartPanel.removeAll();

                    umidadeChartPanel.add(newTempChartPanel, BorderLayout.CENTER);

                    umidadeChartPanel.revalidate();
                    umidadeChartPanel.repaint();
                } else {
                    JOptionPane.showMessageDialog(frame, "Por favor, insira ambas as datas.");
                }
            }
        });

        frame.setVisible(true);
    }
}
