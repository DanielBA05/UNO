/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.uno;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;
import static javax.swing.WindowConstants.EXIT_ON_CLOSE;

public class RegistroJugadores extends JFrame {
    private final List<JTextField> camposJugadores = new ArrayList<>();
    private final JPanel panelCampos = new JPanel();
    private final JSpinner spinnerCantidad;

    public RegistroJugadores() {
        setTitle("Registro de Jugadores");
        setSize(600, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));
        setResizable(false);

        JPanel panelSuperior = new JPanel();
        panelSuperior.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));
        panelSuperior.setBorder(BorderFactory.createTitledBorder("Configuración"));

        JLabel etiqueta = new JLabel("Cantidad de jugadores (2-4):");
        etiqueta.setFont(new Font("SansSerif", Font.PLAIN, 14));
        panelSuperior.add(etiqueta);

        spinnerCantidad = new JSpinner(new SpinnerNumberModel(2, 2, 4, 1));
        JComponent editor = spinnerCantidad.getEditor();
        if (editor instanceof JSpinner.DefaultEditor) {
            JFormattedTextField textField = ((JSpinner.DefaultEditor) editor).getTextField();
            textField.setEditable(false);
            textField.setFocusable(false);
            textField.setHorizontalAlignment(JTextField.CENTER);
            textField.setFont(new Font("SansSerif", Font.BOLD, 14));
        }

        spinnerCantidad.setPreferredSize(new Dimension(50, 25));
        spinnerCantidad.addChangeListener(e -> actualizarCampos());
        panelSuperior.add(spinnerCantidad);

        add(panelSuperior, BorderLayout.NORTH);

     
        panelCampos.setLayout(new BoxLayout(panelCampos, BoxLayout.Y_AXIS));
        panelCampos.setBorder(BorderFactory.createEmptyBorder(10, 30, 10, 30));
        add(panelCampos, BorderLayout.CENTER);

    
        JButton btnIniciar = new JButton("Iniciar Juego");
        btnIniciar.setFont(new Font("SansSerif", Font.BOLD, 14));
        btnIniciar.setBackground(new Color(0, 153, 76));
        btnIniciar.setForeground(Color.WHITE);
        btnIniciar.setFocusPainted(false);
        btnIniciar.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        btnIniciar.addActionListener(this::iniciarJuego);

        JPanel panelInferior = new JPanel();
        panelInferior.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panelInferior.add(btnIniciar);

        add(panelInferior, BorderLayout.SOUTH);

        actualizarCampos();
        setVisible(true);
    }

    private void actualizarCampos() {
        panelCampos.removeAll();
        camposJugadores.clear();
        int cantidad = (int) spinnerCantidad.getValue();
        for (int i = 0; i < cantidad; i++) {
            JTextField campo = new JTextField();
            campo.setFont(new Font("SansSerif", Font.PLAIN, 14));
            campo.setBorder(BorderFactory.createTitledBorder("Jugador " + (i + 1)));
            campo.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));
            camposJugadores.add(campo);
            panelCampos.add(Box.createVerticalStrut(10));
            panelCampos.add(campo);
        }
        panelCampos.revalidate();
        panelCampos.repaint();
    }

    private void iniciarJuego(ActionEvent e) {
        List<String> nombres = new ArrayList<>();
        for (JTextField campo : camposJugadores) {
            String nombre = campo.getText().trim();
            if (!nombre.isEmpty()) {
                nombres.add(nombre);
            }
        }

        if (nombres.size() < 2) {
            JOptionPane.showMessageDialog(this,
                    "Debe ingresar al menos 2 jugadores.",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        dispose();
        new Interfaz(nombres); 
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(RegistroJugadores::new);
    }
}

