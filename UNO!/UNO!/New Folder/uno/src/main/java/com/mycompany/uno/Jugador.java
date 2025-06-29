/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.uno;

import java.util.ArrayList;
import java.util.List;
//método jugador, que cuenta con una mano, un booleano para revisar si es el turno
public class Jugador {
    private final String nombre;
    private final List<Carta> mano;
    private boolean esTurno;
    //constructor que recibe el nombre del jugador
    public Jugador(String nombre) {
        this.nombre = nombre;
        this.mano = new ArrayList<>();
        this.esTurno = false;
    }
    
    //métodos set y get para usar los atributos de jugador en otras clases
    public String getNombre() {
        return nombre;
    }

    public List<Carta> getMano() {
        return mano;
    }
    //para añadirle una carta
    public void recibirCarta(Carta carta) {
        mano.add(carta);
    }
    //para llenar la mano
    public void recibirCartas(List<Carta> cartas) {
        mano.addAll(cartas);
    }
    //quitarle una carta
    public void quitarCarta(Carta carta) {
        mano.remove(carta);
    }
    //revisar el estado actual de su mano en cuestión de una carta en especifico
    public boolean tieneCarta(Carta carta) {
        return mano.contains(carta);
    }
    //revisar si contiene alguna carta que cumpla los requisitos
    public boolean puedeJugarCarta(Carta carta, Carta.Color colorActual, Carta.Valor valorActual) {
        return carta.getColor() == colorActual || 
               carta.getValor() == valorActual || 
               carta.getColor() == Carta.Color.negro;
    }
    //más métodos set y get
    public boolean isEsTurno() {
        return esTurno;
    }

    public void setEsTurno(boolean esTurno) {
        this.esTurno = esTurno;
    }
    
    public int getPuntos() {
        return mano.stream().mapToInt(Carta::getPuntos).sum();
    }

    //para obtener el ganador
    public boolean haGanado() {
        return mano.isEmpty();
    }
}
