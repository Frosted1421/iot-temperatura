import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
public class menuPrincipal {
    public static void main(String[] args) {
        JFrame tela = new JFrame("Menu principal");
        JPanel painel = new JPanel();
        GridBagConstraints gbc = new GridBagConstraints();
        
        painel.setLayout(new GridBagLayout());
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

    JLabel lbl1 = new JLabel("Bem vindo, utilize os botões abaixo para\nrealizar as ações relacionadas ao esp32.");
    gbc.gridx=0;
    gbc.gridy=0;

    painel.add(lbl1,gbc);

    JButton botaoTemp = new JButton("Graficos da temperatura");
    botaoTemp.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e){
        graficoMedicoesTemperatura.main(new String[0]);
      }  
    });

    gbc.gridx=0;
    gbc.gridy=1;

    painel.add(botaoTemp,gbc);

    JButton botaoHTemp = new JButton("historico da temperatura e umidade");
    botaoHTemp.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e){
        listaMedicoes.main(new String[0]);
      }  
    });
    gbc.gridx=0;
    gbc.gridy=3;
    painel.add(botaoHTemp,gbc);
    
    
    JButton botaoUmidade = new JButton("Graficos da Umidade");
    botaoUmidade.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e){
        graficoMedicoesUmidade.main(new String[0]);
      }  
    });
    gbc.gridx=0;
    gbc.gridy=2;

    painel.add(botaoUmidade,gbc);

    JButton botaoGIrriga = new JButton("Graficos do Irrigador");
    botaoGIrriga.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e){
        graficoIrriga.main(new String[0]);
      }  
    });
    gbc.gridx=0;
    gbc.gridy=4;

    painel.add(botaoGIrriga,gbc);

    JButton botaoHIrrigador = new JButton("Historico do Irrigador");
    botaoHIrrigador.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e){
        historicoIrriga.main(new String[0]);
      }  
    });
    gbc.gridx=0;
    gbc.gridy=5;

    painel.add(botaoHIrrigador,gbc);


    JButton botaoGVentoinha = new JButton("Grafico da ventoinha");
    botaoGVentoinha.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e){
        graficoVentoinha.main(new String[0]);
      }  
    });
    gbc.gridx=0;
    gbc.gridy=6;

    painel.add(botaoGVentoinha,gbc);

    JButton btnNovosDados = new JButton("Novos Dados para o esp32");
    btnNovosDados.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e){
            setTemperatura.main(new String[0]);
        }  
      });      
    gbc.gridx=0;
    gbc.gridy=8;
    painel.add(btnNovosDados,gbc);


    JButton btnHVentoinha = new JButton("Historico das ventoinhas");
    btnHVentoinha.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e){
          historicoVentoinha.main(new String[0]);
        }  
      });      
    gbc.gridx=0;
    gbc.gridy=7;
    painel.add(btnHVentoinha,gbc);

    tela.add(painel);
    tela.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    tela.setSize(600, 400);
    tela.setVisible(true);
      
    }
    
}
