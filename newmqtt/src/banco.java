import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.awt.*;
import java.sql.SQLException;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;

import javax.swing.*;

public class banco {

    private static final  String URL = "jdbc:mysql://localhost:3306/dados";
    private static final  String USUARIO = "mendi";
    private static final  String SENHA = "47117";
    

    public void addTemp(String temp, String umidade) {
        Connection conn = null;
        PreparedStatement pstmt = null;
       
        try {
            conn = DriverManager.getConnection(URL, USUARIO, SENHA);
    
            String sql = "INSERT INTO medicoes (temp,umidade) VALUES (?, ?)";
            pstmt = conn.prepareStatement(sql);
    
            pstmt.setString(1, temp);
            pstmt.setString(2, umidade);
    
            pstmt.executeUpdate();
            System.out.println("dados adicionados com sucesso!");
    
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (pstmt != null) pstmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void addSinalIrrigador(int sinal){
        Connection conn = null;
        PreparedStatement pstmt = null;
       
        try {
            conn = DriverManager.getConnection(URL, USUARIO, SENHA);
    
            String sql = "INSERT INTO irriga (onoff) VALUES (?)";
            pstmt = conn.prepareStatement(sql);
    
            pstmt.setInt(1, sinal);
            pstmt.executeUpdate();
            System.out.println("dados adicionados com sucesso!");
    
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (pstmt != null) pstmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    public void addSinalVentoinha(int sinal){
        Connection conn = null;
        PreparedStatement pstmt = null;
       
        try {
            conn = DriverManager.getConnection(URL, USUARIO, SENHA);
    
            String sql = "INSERT INTO ventoinha (idEsp32) VALUES (?)";
            pstmt = conn.prepareStatement(sql);
    
            pstmt.setInt(1, sinal);
            pstmt.executeUpdate();
            System.out.println("dados adicionados com sucesso!");
    
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (pstmt != null) pstmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public JPanel getGraficoUmidade(String startDate, String endDate) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
    
        try {
            conn = DriverManager.getConnection(URL, USUARIO, SENHA);
            System.out.println("Conectado ao banco de dados!");
    
            // Consulta SQL com intervalo de datas
            String sql = "SELECT * FROM medicoes WHERE dia BETWEEN ? AND ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, startDate);
            pstmt.setString(2, endDate);
            rs = pstmt.executeQuery();
    
            while (rs.next()) {
                String umidade = rs.getString("umidade");
                String dia = rs.getString("dia");
    
                System.out.println("umidade: " + umidade + " dia: " + dia);
                dataset.addValue(Double.parseDouble(umidade), "Umidade (%)", dia);
            }
    
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    
        // Criação do gráfico
        JFreeChart lineChart = ChartFactory.createLineChart(
                "Medições de Umidade",   // Título do gráfico
                "Data",                 // Rótulo do eixo X
                "Umidade (%)",          // Rótulo do eixo Y
                dataset,                // Dados do gráfico
                PlotOrientation.VERTICAL, // Orientação
                true,                   // Legenda
                true,                   // Tooltips
                false                   // URLs
        );
    
        // Empacota o gráfico em um painel
        ChartPanel chartPanel = new ChartPanel(lineChart);
        chartPanel.setPreferredSize(new Dimension(800, 600));
        return chartPanel;
    }
        public JPanel getGraficoTemp(String startDate, String endDate) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        try {
            conn = DriverManager.getConnection(URL, USUARIO, SENHA);
            System.out.println("Conectado ao banco de dados!");

            // Consulta SQL com intervalo de datas
            String sql = "SELECT * FROM medicoes WHERE dia BETWEEN ? AND ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, startDate);
            pstmt.setString(2, endDate);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                String temp = rs.getString("temp");
                String dia = rs.getString("dia");

                System.out.println("temp: " + temp + " dia: " + dia);
                dataset.addValue(Double.parseDouble(temp), "Temperatura (C°)", dia);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        // Criação do gráfico
        JFreeChart lineChart = ChartFactory.createLineChart(
                "Medições do Ambiente", // Título do gráfico
                "Data",                // Rótulo do eixo X
                "Temperatura (C°)",    // Rótulo do eixo Y
                dataset,               // Dados do gráfico
                PlotOrientation.VERTICAL, // Orientação
                true,                  // Legenda
                true,                  // Tooltips
                false                  // URLs
        );

        // Empacota o gráfico em um painel
        ChartPanel chartPanel = new ChartPanel(lineChart);
        chartPanel.setPreferredSize(new Dimension(800, 600));
        return chartPanel;
    }
    
    public JPanel getGraficoVentoinha() {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        try {
            conn = DriverManager.getConnection(URL, USUARIO, SENHA);
            System.out.println("Conectado ao banco de dados!");

            String sql = "SELECT * FROM ventoinha";
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                String ligado = rs.getString("idEsp32");
                String dia = rs.getString("dia");
                int ligouDesligou=Integer.parseInt(ligado);
                dataset.addValue(ligouDesligou, " ", dia);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        // Create the chart
        JFreeChart lineChart = ChartFactory.createLineChart(
                "Atiavação da ventoinha", // Chart title
                "Data e Hora",          // X-Axis label
                "ativado",               // Y-Axis label
                dataset,                // Dataset
                PlotOrientation.VERTICAL, // Orientation
                true,                   // Include legend
                true,                   // Tooltips
                false                   // URLs
        );

        // Wrap chart in a panel and return
        ChartPanel chartPanel = new ChartPanel(lineChart);
        chartPanel.setPreferredSize(new java.awt.Dimension(800, 600));
        return chartPanel;
    }
    
    

    public JPanel getGraficoIrriga() {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        try {
            conn = DriverManager.getConnection(URL, USUARIO, SENHA);
            System.out.println("Conectado ao banco de dados!");

            String sql = "SELECT * FROM irriga";
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                String ligado = rs.getString("onoff");
                String dia = rs.getString("dia");
                int ligouDesligou=Integer.parseInt(ligado);
                dataset.addValue(ligouDesligou, "Dia:", dia);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        // Create the chart
        JFreeChart lineChart = ChartFactory.createLineChart(
                "Atiavação Do Irrigador", // Chart title
                "Data e Hora",          // X-Axis label
                "ativado",               // Y-Axis label
                dataset,                // Dataset
                PlotOrientation.VERTICAL, // Orientation
                true,                   // Include legend
                true,                   // Tooltips
                false                   // URLs
        );

        // Wrap chart in a panel and return
        ChartPanel chartPanel = new ChartPanel(lineChart);
        chartPanel.setPreferredSize(new java.awt.Dimension(800, 600));
        return chartPanel;
    }
    
    public JList<String> getMedicoes(){
        //esse get retorna os usuarios JA cadastrados
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        
        DefaultListModel<String> listModel = new DefaultListModel<>();
        JList<String> listBox = new JList<>(listModel);

        listBox.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        listBox.setVisibleRowCount(-1); 
    
        try {
            
            conn = DriverManager.getConnection(URL, USUARIO, SENHA);
            System.out.println("Conectado ao banco de dados!");
    
            String sql = "SELECT * FROM medicoes";
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();
    
            while (rs.next()) {
                String temp= rs.getString("temp");
                String umidade= rs.getString("umidade");
                String dia = rs.getString("dia");
                String elemento = "Temperatura:"+temp+"C° Umidade:"+umidade+"%"+" Dia e Horario:"+dia;
                listModel.addElement(elemento);
            }
    
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    
        return listBox; 
    }
    public JList<String> getListaIrriga(){
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        
        DefaultListModel<String> listModel = new DefaultListModel<>();
        JList<String> listBox = new JList<>(listModel);

        listBox.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        listBox.setVisibleRowCount(-1); 
    
        try {
            
            conn = DriverManager.getConnection(URL, USUARIO, SENHA);
            System.out.println("Conectado ao banco de dados!");
    
            String sql = "SELECT * FROM irriga";
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();
    
            while (rs.next()) {
                String ativou= rs.getString("onoff");
                String dia = rs.getString("dia");
                String elemento = "Sinal:"+ativou+"Dia e Horario:"+dia;
                listModel.addElement(elemento);
            }
    
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    
        return listBox; 
    }   

    public JList<String> getListaVentoinha(){
        //esse get retorna os usuarios JA cadastrados
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        
        DefaultListModel<String> listModel = new DefaultListModel<>();
        JList<String> listBox = new JList<>(listModel);

        listBox.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        listBox.setVisibleRowCount(-1); 
    
        try {
            
            conn = DriverManager.getConnection(URL, USUARIO, SENHA);
            System.out.println("Conectado ao banco de dados!");
    
            String sql = "SELECT * FROM ventoinha";
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();
    
            while (rs.next()) {
                String ativou= rs.getString("idEsp32");
                String dia = rs.getString("dia");
                String elemento = "Sinal:"+ativou+"Dia e Horario:"+dia;
                listModel.addElement(elemento);
            }
    
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    
        return listBox; 
    }   
}
