/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mycompany.uno;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import java.util.ArrayList;
import java.awt.image.BufferedImage;

/**
 *
 * @author jos23
 */

public class Interfaz extends JFrame {
    private static final int CARD_WIDTH = 120;
    private static final int CARD_HEIGHT = 200;
    
    private final List<CartaSwing> cartasJugador = new ArrayList<>();
    private Partida juego;
    private JPanel panelMesa;
    private JLabel lblPilaDescarte;
    private JLabel lblMonton;
    private JPanel atrilPanel;
    private JLabel lblTurno;
    private JLabel lblDireccion;
    private JButton btnTerminar;
    private ColorSelector colorSelector;

    public Interfaz(List<String> nombresJugadores) {
        List<Jugador> jugadores = new ArrayList<>();
        for (String nombre : nombresJugadores) {
            jugadores.add(new Jugador(nombre));
        }

        juego = new Partida(jugadores);
        colorSelector = new ColorSelector(this);
        
        setTitle("UNO!");
        setSize(900, 700);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));
        getContentPane().setBackground(new Color(0, 70, 0));

        initMesa();
        initAtril();
        initControles();
        initInfoPanel();

        setVisible(true);
    }

    private void initMesa() {
        panelMesa = new JPanel(new FlowLayout(FlowLayout.CENTER, 40, 40));
        panelMesa.setBackground(new Color(0, 100, 0));
        panelMesa.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        lblPilaDescarte = crearCartaVisual(juego.getCartaSuperiorPila());
        panelMesa.add(lblPilaDescarte);

        lblMonton = new JLabel(new ImageIcon(crearImagenCartaBocaAbajo()));
        lblMonton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        lblMonton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                juego.getJugadorActual().robarCarta(juego.getMonton(), 1);
                refresh();
                verificarFinTurnoAutomatico();
            }
        });
        panelMesa.add(lblMonton);

        add(panelMesa, BorderLayout.CENTER);
    }

    private void initAtril() {
        atrilPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 5));
        atrilPanel.setBackground(new Color(70, 70, 70));

        for (Carta carta : juego.getJugadorActual().getMano()) {
            CartaSwing cartaSwing = new CartaSwing(carta);
            cartasJugador.add(cartaSwing);
            atrilPanel.add(cartaSwing.getPanel());
        }

        JScrollPane scroll = new JScrollPane(atrilPanel);
        scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
        scroll.setPreferredSize(new Dimension(800, 180));
        add(scroll, BorderLayout.SOUTH);
    }

    private void initControles() {
        JPanel controlesPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        controlesPanel.setBackground(new Color(0, 70, 0));

        btnTerminar = new JButton("Terminar Turno");
        btnTerminar.setFont(new Font("Arial", Font.BOLD, 14));
        btnTerminar.addActionListener(e -> {
            juego.siguienteTurno();
            refresh();
        });
        controlesPanel.add(btnTerminar);

        add(controlesPanel, BorderLayout.NORTH);
    }

    private void initInfoPanel() {
        JPanel infoPanel = new JPanel(new GridLayout(2, 1));
        infoPanel.setBackground(new Color(0, 70, 0));
        infoPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        lblTurno = new JLabel("Turno de: " + juego.getJugadorActual().getNombre());
        lblTurno.setFont(new Font("Arial", Font.BOLD, 16));
        lblTurno.setForeground(Color.WHITE);
        infoPanel.add(lblTurno);

        lblDireccion = new JLabel("Dirección: " + (juego.isDireccionNormal() ? "Normal" : "Reversa"));
        lblDireccion.setFont(new Font("Arial", Font.PLAIN, 14));
        lblDireccion.setForeground(Color.WHITE);
        infoPanel.add(lblDireccion);

        add(infoPanel, BorderLayout.EAST);
    }

    private JLabel crearCartaVisual(Carta carta) {
        JLabel lblCarta = new JLabel();
        lblCarta.setPreferredSize(new Dimension(CARD_WIDTH, CARD_HEIGHT));
        lblCarta.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        lblCarta.setOpaque(true);
        lblCarta.setBackground(Color.WHITE);
        lblCarta.setHorizontalAlignment(SwingConstants.CENTER);

        if (carta != null) {
            lblCarta.setLayout(new BorderLayout());

            JLabel lblNumero = new JLabel(getTextoCartas(carta), SwingConstants.CENTER);
            lblNumero.setFont(new Font("Arial", Font.BOLD, 24));
            lblNumero.setForeground(getColor(carta.getColor()));

            JPanel colorIndicator = new JPanel();
            colorIndicator.setBackground(getColor(carta.getColor()));
            colorIndicator.setPreferredSize(new Dimension(CARD_WIDTH, 10));

            lblCarta.add(colorIndicator, BorderLayout.NORTH);
            lblCarta.add(lblNumero, BorderLayout.CENTER);
        }

        return lblCarta;
    }

    private Image crearImagenCartaBocaAbajo() {
        BufferedImage img = new BufferedImage(CARD_WIDTH, CARD_HEIGHT, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = img.createGraphics();
        
        g2d.setColor(new Color(0, 0, 139));
        g2d.fillRect(0, 0, CARD_WIDTH, CARD_HEIGHT);
        
        g2d.setColor(Color.WHITE);
        g2d.setStroke(new BasicStroke(3));
        g2d.drawRect(2, 2, CARD_WIDTH-5, CARD_HEIGHT-5);
        
        g2d.setFont(new Font("Arial", Font.BOLD, 20));
        FontMetrics fm = g2d.getFontMetrics();
        String text = "UNO";
        int x = (CARD_WIDTH - fm.stringWidth(text)) / 2;
        int y = (CARD_HEIGHT - fm.getHeight()) / 2 + fm.getAscent();
        g2d.drawString(text, x, y);
        
        g2d.dispose();
        return img;
    }

    private void refresh() {
        panelMesa.remove(lblPilaDescarte);
        lblPilaDescarte = crearCartaVisual(juego.getCartaSuperiorPila());
        panelMesa.add(lblPilaDescarte, 0);
        
        atrilPanel.removeAll();
        cartasJugador.clear();
        for (Carta carta : juego.getJugadorActual().getMano()) {
            CartaSwing cartaSwing = new CartaSwing(carta);
            cartasJugador.add(cartaSwing);
            atrilPanel.add(cartaSwing.getPanel());
        }

        lblTurno.setText("Turno de: " + juego.getJugadorActual().getNombre());
        lblDireccion.setText("Dirección: " + (juego.isDireccionNormal() ? "Normal" : "Reversa"));
        

        if (juego.getJugadorActual().getMano().isEmpty()) {
            String winnerName = juego.getJugadorActual().getNombre();
            JOptionPane.showMessageDialog(this, 
                "¡El ganador es " + winnerName + "!", 
                "¡Juego terminado!", 
                JOptionPane.INFORMATION_MESSAGE);
            System.exit(0);
        }

        panelMesa.revalidate();
        panelMesa.repaint();
        atrilPanel.revalidate();
        atrilPanel.repaint();
    }

    private void verificarFinTurnoAutomatico() {
        boolean puedeJugar = false;
        for (Carta carta : juego.getJugadorActual().getMano()) {
            if (juego.puedeJugarCarta(juego.getJugadorActual(), carta)) {
                puedeJugar = true;
                break;
            }
        }
        
        if (!puedeJugar) {
            JOptionPane.showMessageDialog(this, "No puedes jugar ninguna carta. Turno terminado automáticamente.");
            juego.siguienteTurno();
            refresh();
        }
    }

    public static String getTextoCartas(Carta carta) {
        if (carta == null) return "";
        
        if (carta.esComodin()) {
            return carta.getEfecto().equals(Carta.CAMBIA_COLOR) ? "CC" : "+4";
        }
        if (carta.esEfecto()) {
            switch (carta.getEfecto()) {
                case Carta.SALTA: return "S";
                case Carta.REVERSA: return "R";
                case Carta.ROBA_DOS: return "+2";
                default: return "";
            }
        }
        return String.valueOf(carta.getNumero());
    }

    public static Color getColor(int colorCode) {
        switch (colorCode) {
            case Carta.ROJO: return new Color(220, 50, 50);
            case Carta.AZUL: return new Color(50, 50, 220);
            case Carta.VERDE: return new Color(50, 180, 50);
            case Carta.AMARILLO: return new Color(220, 220, 50);
            case Carta.COMODIN: return Color.BLACK;
            default: return Color.BLACK;
        }
    }

    public void mostrarSelectorColor() {
        colorSelector.mostrar();
    }

    class CartaSwing {
        private final Carta carta;
        private final JPanel panel;

        public CartaSwing(Carta carta) {
            this.carta = carta;
            this.panel = new JPanel();
            panel.setPreferredSize(new Dimension(CARD_WIDTH, CARD_HEIGHT));
            panel.setBackground(Color.WHITE);
            panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.BLACK, 2),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)
            ));
            panel.setLayout(new BorderLayout());

            JPanel colorIndicator = new JPanel();
            colorIndicator.setBackground(getColor(carta.getColor()));
            colorIndicator.setPreferredSize(new Dimension(CARD_WIDTH, 10));
            panel.add(colorIndicator, BorderLayout.NORTH);

            JLabel label = new JLabel(getTextoCartas(carta), SwingConstants.CENTER);
            label.setFont(new Font("Arial", Font.BOLD, 24));
            label.setForeground(getColor(carta.getColor()));
            panel.add(label, BorderLayout.CENTER);

            panel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            panel.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                       if (juego.puedeJugarCarta(juego.getJugadorActual(), carta)) {
        if (carta.esComodin()) {
      
            int colorElegido = colorSelector.mostrar(); 
       
            carta.setColor(colorElegido);
       
            refresh();
        }
        
        
        juego.getJugadorActual().jugarCarta(carta, juego);
        juego.agregarCartaDescarte(carta);
        
        if (carta.esEfecto()) {
            juego.aplicarEfectoCarta(carta);
        } else {
           
            juego.siguienteTurno();
        }
        
        refresh();
        
    }  else {
                        JOptionPane.showMessageDialog(panel, 
                            "No puedes jugar esta carta ahora.\n" +
                            "Debe coincidir en color o número con la carta superior\n" +
                            "o ser un comodín.",
                            "Movimiento inválido",
                            JOptionPane.WARNING_MESSAGE);
                    }
                }
                
                @Override
                public void mouseEntered(MouseEvent e) {
                    panel.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(Color.YELLOW, 3),
                        BorderFactory.createEmptyBorder(5, 5, 5, 5)
                    ));
                }
                
                @Override
                public void mouseExited(MouseEvent e) {
                    panel.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(Color.BLACK, 2),
                        BorderFactory.createEmptyBorder(5, 5, 5, 5)
                    ));
                }
            });
        }

        public Carta getCarta() {
            return carta;
        }

        public JPanel getPanel() {
            return panel;
        }
    }

    class ColorSelector {
        private final JDialog dialog;
        private int selectedColor;
        private final Interfaz interfaz;

        public ColorSelector(Interfaz interfaz) {
            this.interfaz = interfaz;
            dialog = new JDialog(interfaz, "Selecciona un color", true);
            dialog.setSize(300, 200);
            dialog.setLayout(new GridLayout(2, 2, 10, 10));
            dialog.setLocationRelativeTo(interfaz);

            JButton btnRojo = new JButton("Rojo");
            btnRojo.setBackground(new Color(220, 50, 50));
            btnRojo.addActionListener(e -> {
                selectedColor = Carta.ROJO;
                dialog.dispose();
            });

            JButton btnAzul = new JButton("Azul");
            btnAzul.setBackground(new Color(50, 50, 220));
            btnAzul.addActionListener(e -> {
                selectedColor = Carta.AZUL;
                dialog.dispose();
            });

            JButton btnVerde = new JButton("Verde");
            btnVerde.setBackground(new Color(50, 180, 50));
            btnVerde.addActionListener(e -> {
                selectedColor = Carta.VERDE;
                dialog.dispose();
            });

            JButton btnAmarillo = new JButton("Amarillo");
            btnAmarillo.setBackground(new Color(220, 220, 50));
            btnAmarillo.addActionListener(e -> {
                selectedColor = Carta.AMARILLO;
                dialog.dispose();
            });

            dialog.add(btnRojo);
            dialog.add(btnAzul);
            dialog.add(btnVerde);
            dialog.add(btnAmarillo);
        }

       public int mostrar() {
    dialog.setVisible(true); 
    return selectedColor; 
}
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            List<String> jugadores = new ArrayList<>();
            jugadores.add("Jugador 1");
            jugadores.add("Jugador 2");
            new Interfaz(jugadores);
        });
    }
}
