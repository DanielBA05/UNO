/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.uno;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;

public class PanelJugador extends JPanel {
    private final JuegoUno juego;
    private final Interfaz interfaz;
    private final JPanel panelCartas;
    private final JButton btnTerminarTurno;

    public PanelJugador(Jugador jugador, JuegoUno juego, Interfaz interfaz) {
        this.juego = juego;
        this.interfaz = interfaz;

        setLayout(new BorderLayout());
        setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(0, 100, 0), 2),
            "",
            javax.swing.border.TitledBorder.CENTER,
            javax.swing.border.TitledBorder.DEFAULT_POSITION,
            new Font("SansSerif", Font.BOLD, 14),
            new Color(0, 80, 0)
        ));
        setBackground(new Color(240, 240, 240));

        panelCartas = new JPanel();
        panelCartas.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 15));
        panelCartas.setBackground(new Color(240, 240, 240));

        JScrollPane scrollPane = new JScrollPane(panelCartas);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
        scrollPane.getHorizontalScrollBar().setUnitIncrement(20);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());

        add(scrollPane, BorderLayout.CENTER);

        JPanel panelInferior = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        panelInferior.setBackground(new Color(230, 230, 230));
        panelInferior.setBorder(BorderFactory.createEmptyBorder(5, 0, 10, 0));
        btnTerminarTurno = new JButton("Terminar Turno");
        btnTerminarTurno.setFont(new Font("SansSerif", Font.BOLD, 14));
        btnTerminarTurno.setBackground(new Color(0, 120, 0));
        btnTerminarTurno.setForeground(Color.WHITE);
        btnTerminarTurno.setFocusPainted(false);
        btnTerminarTurno.addActionListener(e -> interfaz.intentarTerminarTurno());
        panelInferior.add(btnTerminarTurno);
        add(panelInferior, BorderLayout.SOUTH);
    }

    public void actualizar(Jugador jugadorActualEnJuego) { 
        Jugador jugador = juego.getJugadorActual(); 
        panelCartas.removeAll(); 
        List<Carta> mano = jugador.getMano();
        for (Carta carta : mano) {
            CartaUI btnCarta = new CartaUI(carta); 
            
            boolean habilitarCarta = false;
            if (jugador.isEsTurno()) {
                if (juego.requiereSegundaJugadaPorComodin()) {
                    if (carta.getColor() != Carta.Color.negro) {
                        habilitarCarta = true;
                    } else {
                        habilitarCarta = false;
                    }
                }
                else {
                   
                    if (juego.haJugadoCartaEnTurno()) {
                         habilitarCarta = false; 
                    } else {
                        habilitarCarta = jugador.puedeJugarCarta(carta, juego.getColorActual(), juego.getValorActual());
                    }
                }
            } else {
                habilitarCarta = false;
            }
            btnCarta.addActionListener(e -> {
                juego.jugarCarta(jugador, carta);
                interfaz.actualizarInterfaz(); 
            });
            panelCartas.add(btnCarta);
        }
        btnTerminarTurno.setEnabled(jugador.isEsTurno() && juego.puedeTerminarTurno());

        panelCartas.revalidate(); 
        panelCartas.repaint(); 
    }
}
