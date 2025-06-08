/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mycompany.uno;
import java.util.ArrayList;
import java.util.List;
/**
 *
 * @author jos23
 */
public class Jugador {
    private String nombre;
    private List<Carta> mano;

    public Jugador(String nombre) {
        this.nombre = nombre;
        this.mano = new ArrayList<>();
    }

    public void agregarCarta(Carta carta) {
        mano.add(carta);
    }
    public void robarCarta(montonCartas monton, int cantidad) {
    for (int i = 0; i < cantidad; i++) {
        Carta robada = monton.robarCarta();
        if (robada != null) {
            agregarCarta(robada);
        }
    }
}
    public boolean jugarCarta(Carta carta, Partida partida) {

        if (!mano.contains(carta)) {
            return false;
        }

        if (partida.puedeJugarCarta(this, carta)) {
            mano.remove(carta);
            
            partida.agregarCartaDescarte(carta);
            
            if (carta.esEfecto()) {
                partida.aplicarEfectoCarta(carta);
            }
            
            return true;
        }
        
        return false;
    }
    public void removerCarta(Carta carta) {
        mano.remove(carta);
    }

    public List<Carta> getMano() {
        return mano;
    }

    public int getCantidadCartas() {
        return mano.size();
    }

    public void resetMano() {
        mano.clear();
    }

    public String getNombre() {
        return nombre;
    }
}
