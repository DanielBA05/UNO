package com.mycompany.uno;
//clase carta con atributos de color y de su valor, le ponemos cantidades fuera del rango a los comodines para diferenciarlas
public class Carta {
    public enum Color {
        rojo, amarillo, verde, azul, negro
    }

    public enum Valor {
        cero(0), uno(1), dos(2), tres(3), cuatro(4), cinco(5), seis(6), siete(7), 
        ocho(8), nueve(9), salto(20), reversa(20), tome2(20), comodin(50), tome4(50);

        private final int puntos;

        Valor(int puntos) {
            this.puntos = puntos;
        }

        public int getPuntos() {
            return puntos;
        }
    }

    private final Color color;
    private final Valor valor;
    //constructor
    public Carta(Color color, Valor valor) {
        this.color = color;
        this.valor = valor;
    }
    // métodos get y set para trabajar con los atributos del objeto desde otras clases
    public Color getColor() {
        return color;
    }

    public Valor getValor() {
        return valor;
    }

    public int getPuntos() {
        return valor.getPuntos();
    }

    public boolean esEspecial() {
        return valor == Valor.comodin || valor == Valor.tome4 || 
               valor == Valor.salto || valor == Valor.reversa || 
               valor == Valor.tome2;
    }

    @Override
    public String toString() {
        return color + " " + valor;
    }
}