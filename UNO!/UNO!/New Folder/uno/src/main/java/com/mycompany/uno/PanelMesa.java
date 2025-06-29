/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.uno;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

// Clase para representar la mesa en la interfaz gráfica del juego UNO
public class PanelMesa extends JPanel {
    // Referencias al modelo del juego y a la interfaz principal
    private final JuegoUno juego;
    private final Interfaz interfaz;
    
    // Componentes de la interfaz
    private final JPanel panelCartaSuperior;  // Panel para mostrar la carta superior del descarte
    private final JButton btnMazo;           // Botón que representa el mazo de cartas
    private final JLabel lblColorActual;     // Etiqueta para mostrar el color actual del juego
    private final JLabel lblCartasMazoConteo; // Etiqueta para mostrar la cantidad de cartas restantes

    // Constructor que inicializa la mesa con referencias al juego y la interfaz
    public PanelMesa(JuegoUno juego, Interfaz interfaz) {
        this.juego = juego;
        this.interfaz = interfaz;
        
        // Configuración del layout y apariencia del panel principal
        setLayout(new BorderLayout(20, 20));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        setBackground(new Color(0, 70, 0));  // Fondo verde oscuro para la mesa
        
        // Panel central que contendrá el mazo y la carta superior
        JPanel panelCentral = new JPanel(new GridBagLayout());
        panelCentral.setOpaque(false);  // Hacemos transparente este panel
        
        // Panel personalizado para mostrar la carta superior del descarte
        panelCartaSuperior = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                // Si no hay cartas, dibujamos un espacio vacío
                if (getComponentCount() == 0) {
                    Graphics2D g2d = (Graphics2D) g;
                    g2d.setColor(new Color(0, 80, 0));  // Color de fondo para el espacio vacío
                    g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);  // Rectángulo redondeado
                }
            }
        };
        panelCartaSuperior.setPreferredSize(new Dimension(120, 180));  // Tamaño fijo para las cartas
        panelCartaSuperior.setBorder(
            BorderFactory.createLineBorder(new Color(255, 255, 255, 100), 3)  // Borde blanco semi-transparente
        );
        panelCartaSuperior.setOpaque(false);  // Hacemos transparente el panel
        
        // Botón personalizado que representa el mazo de cartas
        btnMazo = new JButton() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
               
                g.setColor(new Color(200, 0, 0));  // Color rojo para el reverso de las cartas
                g.fillRoundRect(5, 5, getWidth()-10, getHeight()-10, 20, 20);  // Rectángulo redondeado
                
                // Texto "UNO" en amarillo
                g.setColor(Color.YELLOW);
                Font font = new Font("SansSerif", Font.BOLD, 36);
                g.setFont(font);
                
                // lo centramos (el UNO en amarillo)
                FontMetrics fm = g.getFontMetrics();
                String text = "UNO";
                int x = (getWidth() - fm.stringWidth(text)) / 2;
                int y = ((getHeight() - fm.getHeight()) / 2) + fm.getAscent();
                g.drawString(text, x, y);
            }
        };
        
        // Configuración del botón del mazo
        btnMazo.setPreferredSize(new Dimension(120, 180));
        btnMazo.setBorder(BorderFactory.createLineBorder(Color.WHITE, 3));
        btnMazo.setContentAreaFilled(false);  // Sin relleno para que se vea nuestro dibujo personalizado
        btnMazo.addActionListener(this::accionRobarCarta);  // Manejador para robar cartas
        btnMazo.setCursor(new Cursor(Cursor.HAND_CURSOR));  // Cambia el cursor al pasar sobre el botón
        
        // Etiqueta para mostrar el conteo de cartas en el mazo
        lblCartasMazoConteo = new JLabel("Cartas: " + juego.getCartasRestantesEnMazo(), SwingConstants.CENTER);
        lblCartasMazoConteo.setFont(new Font("SansSerif", Font.BOLD, 14));
        lblCartasMazoConteo.setForeground(Color.WHITE);

        // Configuración del layout para posicionar los componentes
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;  
        gbc.gridy = 0;  
        gbc.insets = new Insets(0, 0, 5, 30);  // Margen inferior y derecho
        panelCentral.add(btnMazo, gbc);

        gbc = new GridBagConstraints(); 
        gbc.gridx = 0;  
        gbc.gridy = 1;  
        gbc.insets = new Insets(0, 0, 0, 30);  // Margen derecho
        panelCentral.add(lblCartasMazoConteo, gbc);

        gbc = new GridBagConstraints(); 
        gbc.gridx = 1;  
        gbc.gridy = 0;  
        gbc.gridheight = 2;  
        gbc.insets = new Insets(0, 30, 0, 0);  // Margen izquierdo
        panelCentral.add(panelCartaSuperior, gbc);        
        
        // Añadimos el panel central al centro del BorderLayout
        add(panelCentral, BorderLayout.CENTER);
        
        // Panel inferior para mostrar información adicional
        JPanel panelInfo = new JPanel();
        panelInfo.setOpaque(false);  
        
        // Etiqueta para mostrar el color actual del juego
        lblColorActual = new JLabel("", SwingConstants.CENTER);
        lblColorActual.setFont(new Font("SansSerif", Font.BOLD, 16));
        lblColorActual.setForeground(Color.WHITE);
        panelInfo.add(lblColorActual);
        
        // Añadimos el panel de información en la parte inferior
        add(panelInfo, BorderLayout.SOUTH);
        
        // Actualizamos la interfaz 
        actualizar();
    }

    // Método para actualizar la visualización de la mesa
    public void actualizar() {
        // Limpiamos y actualizamos la carta superior del descarte
        panelCartaSuperior.removeAll();
        Carta carta = juego.getCartaSuperiorDescarte();
        if (carta != null) {            
            CartaUI cartaUI = new CartaUI(carta);
            cartaUI.setOpaque(false); 
            panelCartaSuperior.add(cartaUI);
        }
        
        // Actualizamos el color mostrado según el color actual del juego
        Color colorJuego = switch(juego.getColorActual()) {
            case rojo -> Color.RED;
            case amarillo -> new Color(240, 210, 0);  
            case verde -> Color.GREEN;
            case azul -> Color.BLUE;
            case negro -> Color.BLACK;  
            default -> Color.WHITE;  
        };
        
        lblColorActual.setForeground(colorJuego);
        panelCartaSuperior.revalidate();
        panelCartaSuperior.repaint();

        // Actualizamos el contador de cartas en el mazo
        lblCartasMazoConteo.setText("Cartas: " + juego.getCartasRestantesEnMazo());
    }

    // Manejador de evento para robar una carta del mazo
    private void accionRobarCarta(ActionEvent e) {
        // Verificamos que sea el turno del jugador actual
        if (!juego.getJugadorActual().isEsTurno()) {
            JOptionPane.showMessageDialog(this,
                "¡No es tu turno para robar!",
                "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // El jugador roba una carta y actualizamos la interfaz
        juego.robarCarta(juego.getJugadorActual());
        interfaz.actualizarInterfaz();
    }
}