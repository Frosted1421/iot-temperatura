import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import java.sql.*;
import org.jfree.chart.*;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.ChartPanel;
import org.jfree.data.category.DefaultCategoryDataset;

public class graficoMedicoesTemperatura {
    public static void main(String[] args) {
        banco bd = new banco(); // Sua classe de conexão ao banco
        JFrame frame = new JFrame("Medições:");
        frame.setSize(1000, 900);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        // Painel para seleção de datas
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

        // Painel inicial para o gráfico
        JPanel chartPanel = new JPanel(new BorderLayout());
        frame.add(chartPanel, BorderLayout.CENTER);

        // Ação do botão Filtrar
        filterButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String startDate = startDateField.getText();
                String endDate = endDateField.getText();

                if (!startDate.isEmpty() && !endDate.isEmpty()) {
                    // Gera o novo painel de gráfico com os dados filtrados
                    JPanel newChartPanel = bd.getGraficoTemp(startDate, endDate);

                    // Remove o painel antigo
                    chartPanel.removeAll();
                    chartPanel.add(newChartPanel, BorderLayout.CENTER);

                    // Atualiza a interface
                    chartPanel.revalidate();
                    chartPanel.repaint();
                } else {
                    JOptionPane.showMessageDialog(frame, "Por favor, insira ambas as datas.");
                }
            }
        });

        frame.setVisible(true);
    }
}