/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mycompany.uno;
import javax.swing.JOptionPane;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
/**
 *
 * @author jos23
 */
public class Partida {
    private final List<Jugador> jugadores;
    private int jugadorActualIndex;
    private final montonCartas monton;
    private final Stack<Carta> pilaDescarte;
    private final int cantJugadores;
    private boolean direccionNormal;


    public Partida(List<Jugador> jugadoresOrdenados) {
        this.jugadores = new ArrayList<>(jugadoresOrdenados);
        this.monton = new montonCartas(); 
        this.pilaDescarte = new Stack<>();
        this.jugadorActualIndex = 0;
        this.cantJugadores = jugadores.size();
        this.direccionNormal = true;

        repartirCartasIniciales();
        iniciarPilaDescarte(); 
    }


    public List<Jugador> getJugadores() {
        return new ArrayList<>(jugadores); 
    }

    public int getJugadorActualIndex() {
        return jugadorActualIndex;
    }
    public montonCartas getMonton() {
        return monton;
    }

    public Stack<Carta> getPilaDescarte() {
        return (Stack<Carta>) pilaDescarte.clone(); 
    }

    public int getCantJugadores() {
        return cantJugadores;
    }

    public boolean isDireccionNormal() {
        return direccionNormal;
    }

    public Jugador getJugadorActual() {
        return jugadores.get(jugadorActualIndex);
    }

    public Carta getCartaSuperiorPila() {
        return pilaDescarte.isEmpty() ? null : pilaDescarte.peek();
    }

 
    public void setJugadorActualIndex(int jugadorActualIndex) {
        if (jugadorActualIndex >= 0 && jugadorActualIndex < cantJugadores) {
            this.jugadorActualIndex = jugadorActualIndex;
        }
    }
    public boolean puedeJugarCarta(Jugador jugador, Carta carta) {
    Carta superior = getCartaSuperiorPila();
    if (superior == null) return true;
    
    return carta.getColor() == superior.getColor() ||
           carta.getNumero() == superior.getNumero() ||
           carta.esComodin() ||
           (carta.getEfecto() != null && carta.getEfecto().equals(superior.getEfecto()));
}
    public void setDireccionNormal(boolean direccionNormal) {
        this.direccionNormal = direccionNormal;
    }

    
    public void agregarCartaDescarte(Carta carta) {
        if (carta != null) {
            pilaDescarte.push(carta);
        }
    }

    public Carta tomarCartaDescarte() {
        return pilaDescarte.isEmpty() ? null : pilaDescarte.pop();
    }


    private void repartirCartasIniciales() {
        for (Jugador jugador : jugadores) {
            for (int i = 0; i < 7; i++) {
                Carta carta = monton.robarCarta();
                if (carta != null) {
                    jugador.agregarCarta(carta);
                }
            }
        }
    }

    private void iniciarPilaDescarte() {
        Carta primeraCarta = monton.robarCarta();
        while (primeraCarta != null && primeraCarta.esComodin()) {
            monton.devolverCarta(primeraCarta);
            primeraCarta = monton.robarCarta();
        }
        if (primeraCarta != null) {
            pilaDescarte.push(primeraCarta);
        }
    }
    
    public void siguienteTurno() {
        if (direccionNormal) {
            jugadorActualIndex = (jugadorActualIndex + 1) % cantJugadores;
        } else {
            jugadorActualIndex = (jugadorActualIndex - 1 + cantJugadores) % cantJugadores;
        }
    }

    public void aplicarEfectoCarta(Carta carta) {
        if (!carta.esEfecto()) return;

        switch (carta.getEfecto()) {
            case Carta.SALTA:
                siguienteTurno();
                break;

            case Carta.REVERSA:
                direccionNormal = !direccionNormal;
                break;

            case Carta.ROBA_DOS:
                siguienteTurno();
                Jugador siguiente = getJugadorActual();
                siguiente.robarCarta(monton, 2);
                break;

            case Carta.COME_CUATRO:
                siguienteTurno();
                Jugador siguiente2 = getJugadorActual();
                siguiente2.robarCarta(monton, 4);
                break;
        }
    }
}