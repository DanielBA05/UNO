package com.mycompany.uno;

import javax.swing.*;
import java.awt.*;

public class CartaUI extends JButton {
    private final Carta carta;
    private static final int ancho = 100;
    private static final int alto = 160;

    private static final Font fuenteCentral = new Font("SansSerif", Font.BOLD, 36);
    private static final Font fuenteEsquinas = new Font("SansSerif", Font.BOLD, 14);

    public CartaUI(Carta carta) {
        this.carta = carta;
        setPreferredSize(new Dimension(ancho, alto));
        setContentAreaFilled(false);
        setBorderPainted(false);
        setFocusPainted(false);
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create(); //creamos de copia para trabajar desde aqui
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON); //para bordes suaves
        // pedimos el color de la carta y el simbolo que es determinado desde el método getsimbolo de esta misma clase
        Color colorCarta = getColorParaCarta();
        String simbolo = getSimboloCarta();

        //borde original de la carta
        g2.setColor(colorCarta);
        g2.fillRoundRect(0, 0, ancho, alto, 20, 20);
        // hacemos un borde blanco para la carta, necesario para separar la carta del borde en caso de que sea verde por ejemplo, que es el color que escogimos para el fondo
        g2.setColor(Color.WHITE );
        g2.setStroke(new BasicStroke(2));
        g2.drawRoundRect(0, 0, ancho - 1, alto - 1, 20, 20);

        //aqui definimos el color del texto, que queremos que sea blanco (en negro se ve raro y desentona con la estetica de uno)
        g2.setColor(carta.getColor() == Carta.Color.negro ? Color.WHITE : Color.WHITE);
        //aqui dibujamos el simbolo de la carta
        g2.setFont(fuenteCentral);
        FontMetrics fm = g2.getFontMetrics();
        int x = (ancho - fm.stringWidth(simbolo)) / 2;
        int y = (alto - fm.getHeight()) / 2 + fm.getAscent();
        g2.drawString(simbolo, x, y);

        //aqui dibujamos el simbolo en la esquina superior izquierda e inferior derecha
        g2.setFont(fuenteEsquinas);
        g2.drawString(simbolo, 8, 16);

        g2.rotate(Math.PI, ancho / 2.0, alto / 2.0);
        g2.drawString(simbolo, 8, 16);

        g2.dispose();

        if (!isEnabled()) {
            g.setColor(new Color(0, 0, 0, 100));
            g.fillRoundRect(0, 0, ancho, alto, 20, 20);
        }
    }
    //metodo get color donde agarramos el color del metodo get del objeto carta para definir el color de la carta, tomandonos libertades usando rgb para crear colores más vistosos
    private Color getColorParaCarta() {
        switch (carta.getColor()) {
            case rojo: return new Color(200, 0, 0);
            case amarillo: return new Color(255, 215, 0);
            case verde: return new Color(0, 128, 0);
            case azul: return new Color(0, 0, 200);
            case negro: return Color.BLACK;
            default: return Color.GRAY;
        }
    }
   // metodo para representar el simbolo de la carta dependiendo de su getValor
    private String getSimboloCarta() {
        switch (carta.getValor()) {
            case cero: return "0";
            case uno: return "1";
            case dos: return "2";
            case tres: return "3";
            case cuatro: return "4";
            case cinco: return "5";
            case seis: return "6";
            case siete: return "7";
            case ocho: return "8";
            case nueve: return "9";
            case salto: return "Ø";
            case reversa: return "⮂";
            case tome2: return "+2";
            case comodin: return "★";
            case tome4: return "+4";
            default: return "?";
        }
    }

    public Carta getCarta() {
        return carta;
    }
}

