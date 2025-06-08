/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mycompany.uno;

/**
 *
 * @author jos23
 */
public class Carta {
    private int numero;
    private int color;
    private String efecto;

    public static final int ROJO = 1;
    public static final int AZUL = 2;
    public static final int VERDE = 3;
    public static final int AMARILLO = 4;
    public static final int COMODIN = 5;

    public static final String CAMBIA_COLOR = "CAMBIA_COLOR";
    public static final String COME_CUATRO = "COME_CUATRO";
    public static final String ROBA_DOS = "ROBA_DOS";
    public static final String REVERSA = "REVERSA";
    public static final String SALTA = "SALTA";

    public Carta(int numero, int color) {
        this.numero = numero;
        this.color = color;
        this.efecto = null;
    }

    public Carta(int color, String efecto) {
        this.numero = -1;
        this.color = color;
        this.efecto = efecto;
    }

    public int getNumero() {
        return numero;
    }
    public void setColor(int color) {
    this.color = color;
}
    public int getColor() {
        return color;
    }

    public String getEfecto() {
        return efecto;
    }

    public boolean esComodin() {
        return color == COMODIN;
    }

    public boolean esEfecto() {
        return efecto != null;
    }

}

