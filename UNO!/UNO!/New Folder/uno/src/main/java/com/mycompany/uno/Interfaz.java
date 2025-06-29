package com.mycompany.uno;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.ArrayList; 
import static javax.swing.WindowConstants.EXIT_ON_CLOSE;

// Esta es la clase principal de la interfaz del juego
public class Interfaz extends JFrame {
    private final JuegoUno juego;

    // Panel donde se muestran las cartas jugadas
    private final PanelMesa panelMesa;

    // Panel con las cartas del jugador actual
    private final PanelJugador panelJugadorActual;

    // Panel lateral con los demás jugadores
    private final JPanel panelJugadores;

    // Etiqueta que muestra el estado del juego 
    private final JLabel lblEstado;

    // Se inicializa la interfaz con los nombres de los jugadores
    public Interfaz(List<String> nombresJugadores) {
        this.juego = new JuegoUno(nombresJugadores);

        // Configura la ventana principal
        setTitle("UNO - " + juego.getJugadorActual().getNombre() + " es tu turno");
        setSize(1000, 800);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Etiqueta de estado en la parte superior
        lblEstado = new JLabel("", SwingConstants.CENTER);
        lblEstado.setFont(new Font("SansSerif", Font.BOLD, 16));
        lblEstado.setForeground(Color.WHITE);
        lblEstado.setOpaque(true);
        lblEstado.setBackground(new Color(0, 100, 0));
        add(lblEstado, BorderLayout.NORTH);

        // Panel central con la pila de cartas y carta actual
        panelMesa = new PanelMesa(juego, this);
        add(panelMesa, BorderLayout.CENTER);

        // Panel inferior con las cartas del jugador actual
        panelJugadorActual = new PanelJugador(juego.getJugadorActual(), juego, this);
        add(panelJugadorActual, BorderLayout.SOUTH);

        // Panel lateral con los demás jugadores
        panelJugadores = new JPanel();
        panelJugadores.setLayout(new BoxLayout(panelJugadores, BoxLayout.Y_AXIS));
        panelJugadores.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panelJugadores.setBackground(new Color(230, 230, 230));
        actualizarPanelJugadores();

        // Scroll en caso de muchos jugadores
        JScrollPane scrollJugadores = new JScrollPane(panelJugadores);
        scrollJugadores.setPreferredSize(new Dimension(200, 0));

        add(scrollJugadores, BorderLayout.EAST);

        // Actualiza la interfaz con la información inicial
        actualizarInterfaz();
        setVisible(true);
    }

    // Método que actualiza la interfaz visual según el estado actual del juego
    public void actualizarInterfaz() {
        Jugador jugadorActual = juego.getJugadorActual();
        setTitle("UNO - Turno de: " + jugadorActual.getNombre());

        // Determina la dirección del juego
        String direccionTexto = (juego.getDireccion() == 1) ? "Normal" : "Invertida";
        String estadoTurno = "";

        // Acá mostramos un mensaje de acuerdo al estado del turno
        if (juego.requiereSegundaJugadaPorComodin()) {
            estadoTurno = " | JUEGA OTRA CARTA (Comodín)";
        } else if (juego.haRobadoCartaEnTurno() && !juego.haJugadoCartaEnTurno()) {
            estadoTurno = " | Robaste. Juega o termina tu turno.";
        } else if (juego.haJugadoCartaEnTurno()) {
            estadoTurno = " | Carta jugada. Termina tu turno.";
        }

        // Actualiza la etiqueta de estado
        lblEstado.setText("Turno de: " + jugadorActual.getNombre() +
                                 " | Dirección: " + direccionTexto +
                                 estadoTurno); 

        panelMesa.actualizar();
        panelJugadorActual.actualizar(jugadorActual); 
        actualizarPanelJugadores();

        // Verifica si el juego ha terminado
        if (juego.isJuegoTerminado()) {
            JOptionPane.showMessageDialog(this,
                "¡" + juego.getJugadorActual().getNombre() + " ha ganado!",
                "Juego Terminado", JOptionPane.INFORMATION_MESSAGE);
            dispose(); 
        }
    }

    // Actualiza el panel que muestra a los otros jugadores y sus cartas
    private void actualizarPanelJugadores() {
        panelJugadores.removeAll();

        int current = juego.getJugadorActualIndex();
        int count = juego.getJugadores().size();
        int dir = juego.getDireccion();

        // Muestra a los demás jugadores en orden
        for (int i = 0; i < count; i++) {
            int index = (current + i * dir + count) % count;
            Jugador jugador = juego.getJugadores().get(index);

            if (!jugador.equals(juego.getJugadorActual())) {
                JPanel panel = new JPanel(new BorderLayout());

                // Borde rojo si es el turno del jugador, gris en caso contrario
                panel.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(
                        juego.getJugadorActual().equals(jugador) ? Color.RED : Color.GRAY, 
                        2
                    ),
                    BorderFactory.createEmptyBorder(5, 5, 5, 5)
                ));

                // Fondo rosado si el jugador tiene una sola carta
                panel.setBackground(
                    jugador.getMano().size() == 1 ? new Color(255, 200, 200) : Color.WHITE
                );

                // Muestra nombre y cantidad de cartas del jugador
                JLabel lblInfo = new JLabel(
                    "<html><center><b>" + jugador.getNombre() + "</b><br>" +
                    "Cartas: " + jugador.getMano().size() +
                    "</center></html>",
                    SwingConstants.CENTER
                );

                panel.add(lblInfo, BorderLayout.CENTER);
                panelJugadores.add(panel);
                panelJugadores.add(Box.createVerticalStrut(10));
            }
        }

        panelJugadores.revalidate();
        panelJugadores.repaint();
    }

    // Acá intentamos terminar el turno actual, verificando si es válido
    public void intentarTerminarTurno() {
        if (!juego.getJugadorActual().isEsTurno()) {
            JOptionPane.showMessageDialog(this,
                "¡No es tu turno!",
                "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        juego.siguienteJugador(); 
        actualizarInterfaz();
    }

    // Avanza al siguiente turno
    public void siguienteTurno() {
        juego.siguienteJugador();
        actualizarInterfaz();
    }
}
