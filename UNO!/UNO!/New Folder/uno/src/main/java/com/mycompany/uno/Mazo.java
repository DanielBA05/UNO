/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.uno;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Stack;
//clase para el mazo donde estarán todas las cartas
public class Mazo {
    private final Stack<Carta> cartas = new Stack<>();
    private final Stack<Carta> descarte = new Stack<>();


    // constructor
    public Mazo() {
        inicializarMazo();
        barajar();
    }
    //crea el mazo con las cartas que lo consisten
    private void inicializarMazo() {
        for (Carta.Color color : Carta.Color.values()) {
            if (color != Carta.Color.negro) {
                cartas.add(new Carta(color, Carta.Valor.cero));
                
                for (int i = 1; i <= 9; i++) {
                    cartas.add(new Carta(color, Carta.Valor.values()[i]));
                    cartas.add(new Carta(color, Carta.Valor.values()[i]));
                }
                
                cartas.add(new Carta(color, Carta.Valor.salto));
                cartas.add(new Carta(color, Carta.Valor.salto));
                cartas.add(new Carta(color, Carta.Valor.reversa));
                cartas.add(new Carta(color, Carta.Valor.reversa));
                cartas.add(new Carta(color, Carta.Valor.tome2));
                cartas.add(new Carta(color, Carta.Valor.tome2));
            }
        }
        
        for (int i = 0; i < 4; i++) {
            cartas.add(new Carta(Carta.Color.negro, Carta.Valor.comodin));
            cartas.add(new Carta(Carta.Color.negro, Carta.Valor.tome4));
        }
    }
    //le hace shuffle
    public void barajar() {
        Collections.shuffle(cartas);
    }
    //método para pasar la carta 
    public Carta tomarCarta() {
        if (cartas.isEmpty()) {
            reciclarDescarte();
        }
        return cartas.pop();
    }
    //para tomar la carta inicial de la pila de descarte
    public List<Carta> tomarCartas(int cantidad) {
        List<Carta> cartasTomadas = new ArrayList<>();
        for (int i = 0; i < cantidad; i++) {
            cartasTomadas.add(tomarCarta());
        }
        return cartasTomadas;
    }
    // para descartar una carta
    public void descartar(Carta carta) {
        descarte.push(carta);
    }
    //para ver la carta superior
    public Carta getCartaSuperiorDescarte() {
        return descarte.peek();
    }
    //método necesario para reiniciar el mazo
    private void reciclarDescarte() {
        if (descarte.size() <= 1) {
            throw new IllegalStateException("No hay suficientes cartas para reciclar");
        }
        
        Carta cartaSuperior = descarte.pop(); 
        cartas.addAll(descarte); 
        descarte.clear(); 
        descarte.push(cartaSuperior); 
        barajar(); 
    }
    //método get para obtener el tamaño actual del mazo
     public int getCartasEnMazo() {
        return cartas.size(); 
    }
}
