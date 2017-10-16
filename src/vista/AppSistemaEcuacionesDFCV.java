/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vista;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import logica.MetodoDeGaussJordan;

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

        switch (this.n) {
            case 2:
                setPreferredSize(new Dimension(200, 165));
                break;
            case 3:
                setPreferredSize(new Dimension(200, 190));
                break;
            default:
                break;
        }

        setLayout(new BorderLayout());

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
        add(panelCoeficientes, BorderLayout.NORTH);

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

        add(panelBotones, BorderLayout.SOUTH);
        panelBotones.setLayout(new GridLayout(1, 2));

        btnRegresar.setPreferredSize(new Dimension(20, 40));
        btnRegresar.addActionListener((ActionEvent evt) -> {
            btnRegresarActionPerformed(evt);
        });

        btnCalcular.setPreferredSize(new Dimension(20, 40));
        btnCalcular.addActionListener((ActionEvent evt) -> {
            btnCalcularActionPerformed(evt);
        });
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

    private void btnCalcularActionPerformed(ActionEvent evt) {
        double[][] coeficientes;
        double[] resultados;
        String respuesta;
        
        coeficientes = new double[n][n];
        resultados = new double[n];
        respuesta = "<html>Ecuaciones: <br>";
        
        try {
            for (int i = 0; i < n; i++) {
                respuesta += "Ecuacion " + String.valueOf(i+1) + " → ";
                for (int j = 0; j < n; j++) {
                    coeficientes[i][j] = Double.parseDouble(txtCoeficientes[i][j].getText());
                    System.out.print(coeficientes[i][j] + " ");
                    respuesta += coeficientes[i][j] + "*x" + String.valueOf(i+1) + " ";
                }
                resultados[i] = Double.parseDouble(txtResultado[i].getText());
                System.out.println(resultados[i]);
                respuesta += " = " + String.valueOf(resultados[i]) + "<br>";
            }
            
            MetodoDeGaussJordan metodo = new MetodoDeGaussJordan();
            resultados = metodo.cargarMatriz(coeficientes, resultados);
            
            respuesta += "<br>Solución a las ecuaciones: <br>";
            for (int i = 0; i < n; i++) {
                respuesta += "x" + (i + 1) + " = " + resultados[i] + "<br>";
                System.out.println(resultados[i]);
            }
            
            JOptionPane.showMessageDialog(this, respuesta);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Se presentó un problema: " + e.getMessage());
        }
    }
}
