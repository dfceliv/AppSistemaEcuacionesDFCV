/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vista;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

/**
 *
 * @author Fernando
 */
public class AppSistemaEcuacionesDFCV extends JFrame {

    private final int n;
    private final JFrame padre;
    
    private final JPanel panelCoeficientes, panelBotones;
    
    private final JTextField txtCoeficientes[][], txtResultado[];

    private final JLabel lEcuaciones[], lVariables[], lIgual[];
    
    private final JButton btnRegresar, btnCalcular;
   

    public AppSistemaEcuacionesDFCV(JFrame padre, int n) {
        this.n = n;
        this.padre = padre;
        
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setTitle("Sistema de ecuaciones " + n + "x" + n);
        setResizable(true);
        setPreferredSize(new Dimension(n * 50, n * 50));

        setLayout(new GridLayout(2, 1));
        
        panelCoeficientes = new JPanel();
        panelCoeficientes.setLayout(new GridLayout(n + 1, n + 2));
        
        txtCoeficientes = new JTextField[n][n];
        lEcuaciones = new JLabel[n];
        lVariables = new JLabel[n];
        lIgual = new JLabel[n + 1];
        txtResultado = new JTextField[n];

        panelBotones = new JPanel();
        btnRegresar = new JButton("Regresar");
        btnCalcular = new JButton("Calcular");
    }
    
    public void agregarComponentes() {
        panelCoeficientes.add(new JLabel(""));
        add(panelCoeficientes);
        
        for (int i = 0; i < n; i++) {
            lEcuaciones[i] = new JLabel("x" + (i + 1) + " ↓");
            lEcuaciones[i].setHorizontalAlignment(SwingConstants.CENTER);
            panelCoeficientes.add(lEcuaciones[i]);
        }
        
        panelCoeficientes.add(new JLabel("=", SwingConstants.CENTER));
        panelCoeficientes.add(new JLabel("b ↓", SwingConstants.CENTER));
        
        for (int i = 0; i < n; i++) {
            lVariables[i] = new JLabel("E" + (i + 1) + "→", SwingConstants.CENTER);
            panelCoeficientes.add(lVariables[i]);
            for (int j = 0; j < n; j++) {
                txtCoeficientes[i][j] = new JTextField("0");
                txtCoeficientes[i][j].addFocusListener(new FocusListener() {
                    @Override
                    public void focusGained(FocusEvent e) {
                        validarIngreso(e);
                    }

                    @Override
                    public void focusLost(FocusEvent e) {
                        validarSalida(e);
                    }
                });
                panelCoeficientes.add(txtCoeficientes[i][j]);
            }
            if (n > 0) {
                lIgual[i] = new JLabel("=", SwingConstants.CENTER);
                panelCoeficientes.add(lIgual[i]);
            }
            
            txtResultado[i] = new JTextField("0");
            txtResultado[i].addFocusListener(new FocusListener() {
                @Override
                public void focusGained(FocusEvent e) {
                    validarIngreso(e);
                }

                @Override
                public void focusLost(FocusEvent e) {
                    validarSalida(e);
                }
            });
            panelCoeficientes.add(txtResultado[i]);
        }
        
        panelBotones.add(btnRegresar);
        panelBotones.add(btnCalcular);
        
        add(panelBotones);
        panelBotones.setLayout(new GridLayout(1, 2));
        
        btnRegresar.setPreferredSize(new Dimension(20, 40));
        btnRegresar.addActionListener((ActionEvent evt) -> {
            btnRegresarActionPerformed(evt);
        });

        
        btnCalcular.setPreferredSize(new Dimension(20, 40));
    }
    
    private void btnRegresarActionPerformed(ActionEvent evt) {
        padre.setVisible(true);
        this.dispose();
    }

    private void validarIngreso(FocusEvent e) {
        if (((JTextField) e.getSource()).getText().equals("0")) {
            ((JTextField) e.getSource()).setText("");
        }
    }
    
    private void validarSalida(FocusEvent e) {
        if (((JTextField) e.getSource()).getText().equals("")) {
            ((JTextField) e.getSource()).setText("0");
        }
    }
}
