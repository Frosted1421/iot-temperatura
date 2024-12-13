import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.internal.wire.MqttSubscribe;


public class setTemperatura {

    public static void main(String[] args) {
        JFrame tela = new JFrame("Nova temperatura, Umidade, Intervalo ou Ativar Sprinkles");
        JPanel painel = new JPanel();
        GridBagConstraints gbc = new GridBagConstraints();
        
        painel.setLayout(new GridBagLayout());
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel idDoNovoUsuario = new JLabel("Digite o que deseja alterar e confime");
        gbc.gridx = 0;
        gbc.gridy = 0;
        painel.add(idDoNovoUsuario, gbc);

        JLabel lbNome = new JLabel("Temperatura:");
        gbc.gridx = 0;
        gbc.gridy = 1;
        painel.add(lbNome, gbc);
        
        JTextField txtTemp = new JTextField(20);
        gbc.gridx = 0;
        gbc.gridy = 2;
        painel.add(txtTemp, gbc);
        JButton btnNovaTemp = new JButton("Nova temperatura");
        btnNovaTemp.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                String temp = txtTemp.getText();
                int tempInt;
                try {
                    tempInt=Integer.parseInt(temp);
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(tela, "Temperatura inválida.", "Erro", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if(tempInt>60||tempInt<0){
                    System.out.println("temperatura invalida");
                    return;
                }
                try {
                    MQTTSubscriber.enviarTemp(temp);
                    JOptionPane.showMessageDialog(tela, "Dados enviados com sucesso!");
                
                } catch (MqttException ex) {
                
                    JOptionPane.showMessageDialog(tela, "Erro ao enviar dados: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
                    ex.printStackTrace();
                
                }

        }});
    
        gbc.gridx = 0;
        gbc.gridy = 3;
        painel.add(btnNovaTemp, gbc);
    
        JLabel lbSenha = new JLabel("Umidade");
        gbc.gridx = 0;
        gbc.gridy = 4;
        painel.add(lbSenha, gbc);

        JTextField txtUmidade = new JTextField(20);
        gbc.gridx = 0;
        gbc.gridy = 5;
        painel.add(txtUmidade, gbc);
        JButton btnNovaUmidade = new JButton("Nova Umidade");
        btnNovaUmidade.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                String umidade= txtUmidade.getText();
                int umidadeInt;
                try {
                    umidadeInt=Integer.parseInt(umidade);
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(tela, "Umidade inválida.", "Erro", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if(umidadeInt>100||umidadeInt<0){
                    JOptionPane.showMessageDialog(tela, "Umidade inválida.", "Erro", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                try {
                    MQTTSubscriber.enviarUmidade(umidade);
                    JOptionPane.showMessageDialog(tela, "Dados enviados com sucesso!");
                
                } catch (MqttException ex) {
                
                    JOptionPane.showMessageDialog(tela, "Erro ao enviar dados: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
                    ex.printStackTrace();
                
                }

        }});

        gbc.gridx = 0;
        gbc.gridy = 6;
        painel.add(btnNovaUmidade, gbc);
    
        JLabel lbIntervalo = new JLabel("Intervalo em minutos");
        gbc.gridx = 0;
        gbc.gridy = 7;
        painel.add(lbIntervalo, gbc);

        JTextField txtIntervalo = new JTextField(20);
        gbc.gridx = 0;
        gbc.gridy = 8;
        painel.add(txtIntervalo, gbc);

        JButton btnConfirma = new JButton("Confirma Novo Intervalo");
        btnConfirma.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                String intervalo = txtIntervalo.getText();
                float intervaloFt;

                //impede que o usuario foda com o programa
                try {
                    intervaloFt=Float.parseFloat(intervalo);    
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(tela, "Intervalo inválido.", "Erro", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                try {
                    MQTTSubscriber.enviarIntervalo(intervalo);
                    JOptionPane.showMessageDialog(tela, "Dados enviados com sucesso!");
                
                } catch (MqttException ex) {
                
                    JOptionPane.showMessageDialog(tela, "Erro ao enviar dados: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
                    ex.printStackTrace();
                
                }
                
                tela.dispose();
          
            }
        });

        gbc.gridx = 0;
        gbc.gridy = 9;
        painel.add(btnConfirma, gbc);

        JButton btnAtivarSp = new JButton("Ativar Sprinkler");
        btnAtivarSp.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                try {
                    MQTTSubscriber.ativarSprinkler();
                    JOptionPane.showMessageDialog(tela, "Dados enviados com sucesso!");
                
                } catch (MqttException ex) {
                
                    JOptionPane.showMessageDialog(tela, "Erro ao enviar dados: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
                    ex.printStackTrace();
                
                }                

            }
        });

        gbc.gridx = 0;
        gbc.gridy = 10;
        painel.add(btnAtivarSp, gbc);
        
        
        JButton btnSair = new JButton("Sair");
        btnSair.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) { 
                tela.dispose();
          
            }
        });

        gbc.gridx = 0;
        gbc.gridy = 12;
        painel.add(btnSair, gbc);
        
        tela.add(painel);
        tela.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        tela.setSize(400, 600);
        tela.setVisible(true);
           
    }
}
