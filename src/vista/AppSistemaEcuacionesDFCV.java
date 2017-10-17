/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vista;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.SwingConstants;
import logica.MetodoDeGaussJordan;

/**
 *
 * @author Fernando
 */
public class AppSistemaEcuacionesDFCV extends JFrame {

    private final int n; // Número de ecuaciones e incognitas
    private final JFrame padre; // Frame de la primera ventana
    private final JPanel panelCoeficientes, panelBotones; // Paneles para ingreso de coeficientes y para los botones
    private final JTextField txtCoeficientes[][], txtResultado[]; // Campos de textos para la matriz de coeficientes
    private final JLabel lEcuaciones[], lVariables[], lIgual[]; // Labels para indicar las variables y numero de ecuaciones
    private final JButton btnRegresar, btnCalcular; // Botones para volver a la primera ventana y para resolver el sistema de ecuaciones
    private final JScrollPane scroll; // Scroll para cuando la cantidad de incongnitas es muy grande
    private JTextPane area;
    private JScrollPane scroll2;

    public AppSistemaEcuacionesDFCV(JFrame padre, int n) {
        this.n = n;
        this.padre = padre;

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // En caso de presionar el boton salir, se cierra toda la aplicación
        setTitle("Sistema de ecuaciones " + this.n + "x" + this.n); // Titulo de la ventana
        setResizable(true); // Se permite redimensionar la ventana

        // Se valida cantidad de variables para determinar el tamaño de la ventana
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

        setLayout(new BorderLayout()); // Se establece layout principal

        panelCoeficientes = new JPanel();
        panelCoeficientes.setLayout(new GridLayout(this.n + 1, this.n + 2)); // Se establece layout para ingreso de coeficientes, n+1 filas, n+2 columnas

        txtCoeficientes = new JTextField[this.n][this.n];
        lEcuaciones = new JLabel[this.n];
        lVariables = new JLabel[this.n];
        lIgual = new JLabel[this.n + 1];
        txtResultado = new JTextField[this.n];

        panelBotones = new JPanel();
        btnRegresar = new JButton("Regresar");
        btnCalcular = new JButton("Calcular");
        scroll = new JScrollPane(panelCoeficientes); // Se crea scrollbar con panel de coeficientes
    }

    public void agregarComponentes() {
        panelCoeficientes.add(new JLabel("")); // Se agrega label vacio para primer posicion de la matriz del panel
        getContentPane().add(scroll); // Se agrega panel scrollable

        // Se recorre matriz para indicar la variable de la columna
        for (int i = 0; i < this.n; i++) { 
            lEcuaciones[i] = new JLabel("x" + (i + 1) + " ↓");
            lEcuaciones[i].setHorizontalAlignment(SwingConstants.CENTER);
            panelCoeficientes.add(lEcuaciones[i]);
        }

        panelCoeficientes.add(new JLabel("=", SwingConstants.CENTER));
        panelCoeficientes.add(new JLabel("b ↓", SwingConstants.CENTER));

        // Se recorre matriz para ingreso de coeficientes
        for (int i = 0; i < this.n; i++) {
            lVariables[i] = new JLabel("E" + (i + 1) + "→", SwingConstants.CENTER);
            panelCoeficientes.add(lVariables[i]);
            for (int j = 0; j < this.n; j++) {
                txtCoeficientes[i][j] = new JTextField("0"); // Se inicializa el texto con valor de 0
                txtCoeficientes[i][j].addFocusListener(new FocusListener() {
                    @Override
                    public void focusGained(FocusEvent e) {
                        validarIngreso(e); // Se valida evento de ingreso al campo de texto
                    }

                    @Override
                    public void focusLost(FocusEvent e) {
                        validarSalida(e); // Se valida evento de ingreso al campo de texto
                    }
                });
                panelCoeficientes.add(txtCoeficientes[i][j]);
            }
            if (n > 0) {
                lIgual[i] = new JLabel("=", SwingConstants.CENTER);
                panelCoeficientes.add(lIgual[i]);
            }

            txtResultado[i] = new JTextField("0"); // Se inicializa el texto con valor de 0 para el resultado de cada ecuacion
            txtResultado[i].addFocusListener(new FocusListener() {
                @Override
                public void focusGained(FocusEvent e) {
                    validarIngreso(e); // Se valida evento de ingreso al campo de texto
                }

                @Override
                public void focusLost(FocusEvent e) {
                    validarSalida(e); // Se valida evento de ingreso al campo de texto
                }
            });
            panelCoeficientes.add(txtResultado[i]);
        }

        panelBotones.add(btnRegresar);
        panelBotones.add(btnCalcular);

        add(panelBotones, BorderLayout.SOUTH); // Se agrega panel de botones en la parte inferior de la ventsna
        panelBotones.setLayout(new GridLayout(1, 2)); // Se crea layout para los botones, 1 fila, 2 columnas

        btnRegresar.addActionListener((ActionEvent evt) -> {
            btnRegresarActionPerformed(evt);
        });

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
        double[][] coeficientes; // Matriz con los valores ingresados
        double[] resultados; // Matriz con el resultado de cada ecuacion
        String respuesta, aux, signo;
        
        coeficientes = new double[this.n][this.n];
        resultados = new double[this.n];
        respuesta = "<html>Ecuaciones: <br>";
        
        try {
            long inicio, fin;
            inicio = System.currentTimeMillis();
            
            for (int i = 0; i < this.n; i++) {
                respuesta += "Ecuacion " + String.valueOf(i+1) + " → ";
                for (int j = 0; j < this.n; j++) {
                    coeficientes[i][j] = Double.parseDouble(txtCoeficientes[i][j].getText());
                    signo = (coeficientes[i][j] < 0) ? " " : "+ ";
                    aux = (j < 1) ? coeficientes[i][j] + "*<strong>x" + String.valueOf(j+1) + "</strong> " : signo + coeficientes[i][j] + "*<strong>x" + String.valueOf(j+1) + "</strong> ";
                    respuesta += aux;
                }
                resultados[i] = Double.parseDouble(txtResultado[i].getText());
                respuesta += " = " + String.valueOf(resultados[i]) + "<br>";
            }
            
            MetodoDeGaussJordan metodo = new MetodoDeGaussJordan();
            resultados = metodo.cargarMatriz(coeficientes, resultados);
            
            respuesta += "<br>Solución a las ecuaciones: <br>";
            for (int i = 0; i < this.n; i++) {
                respuesta += "<strong>x" + (i + 1) + "</strong> = " + resultados[i] + "<br>";
            }
            
            fin = System.currentTimeMillis();
            System.out.println("Con el sistema de ecuaciones " + this.n + "x" + this.n + " \nla tarea tomó "+ String.valueOf((double) fin - inicio ) +" milisegundos");
            
            area = new JTextPane();
            area.setContentType("text/html");
            area.setText(respuesta);
            area.setBackground(new Color(0,0,0,0));
            scroll2 = new JScrollPane(area);
            
            JOptionPane.showConfirmDialog(null, scroll2,
                "Sistema de ecuaciones " + this.n + "x" + this.n, 
                JOptionPane.CLOSED_OPTION, 
                JOptionPane.PLAIN_MESSAGE);
        } catch (HeadlessException | NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Se presentó un problema: " + e.getMessage()); // Se captura error
        }
    }
}
