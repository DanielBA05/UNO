package com.mycompany.uno;

import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;

// Esta clase gestiona toda la lógica del juego 
public class JuegoUno {
    private Mazo juegoMazo; // Mazo principal del juego

    private List<Jugador> jugadores; // Lista de jugadores
    private int jugadorActualIndex;  // Índice del jugador que tiene el turno
    private Carta cartaSuperiorDescarte; // Última carta jugada
    private int direccion; // Dirección del juego: 1 = normal, -1 = reversa
    private Carta.Color colorActual;  // Color actual en juego
    private Carta.Valor valorActual;  // Valor actual en juego

    // Estados del turno actual
    private boolean haJugadoCartaEnTurno;
    private boolean haRobadoCartaEnTurno;
    private boolean puedeTerminarTurno;
    private boolean requiereSegundaJugadaPorComodin; 
    private boolean ultimoTurnoFueToma4; 
    private Carta.Valor efectoActivoPendiente; 
    private boolean haRobado; 
    private Carta ultimaCartaRobada; 

    // Constructor: inicializa el juego con los jugadores dados
    public JuegoUno(List<String> nombresJugadores) {
        this.juegoMazo = new Mazo();

        this.jugadores = new ArrayList<>();
        for (String nombre : nombresJugadores) {
            jugadores.add(new Jugador(nombre));
        }

        this.jugadorActualIndex = 0;
        this.direccion = 1;

        repartirCartasIniciales();         // Reparte las 7 cartas a cada jugador
        iniciarPrimeraCartaDescarte();     // Saca la primera carta válida para iniciar el juego

        getJugadorActual().setEsTurno(true); // Marca el turno del primer jugador
        resetearEstadoTurno();              // Inicializa los estados del turno
    }

    // Reinicia las variables del estado del turno actual
    public void resetearEstadoTurno() {
        haJugadoCartaEnTurno = false;
        haRobadoCartaEnTurno = false;
        puedeTerminarTurno = false;
        requiereSegundaJugadaPorComodin = false; 
        ultimoTurnoFueToma4 = false; 
        this.efectoActivoPendiente = null;
        this.haRobado = false; 
        this.ultimaCartaRobada = null; 
    }

    public boolean haJugadoCartaEnTurno() {
        return haJugadoCartaEnTurno;
    }

    public boolean haRobadoCartaEnTurno() {
        return haRobadoCartaEnTurno;
    }

    // Verifica si el jugador puede terminar su turno
    public boolean puedeTerminarTurno() {
        return (haJugadoCartaEnTurno || haRobadoCartaEnTurno) && !requiereSegundaJugadaPorComodin;
    }

    // Lógica para que un jugador robe una carta
    public void robarCarta(Jugador jugador) {
        if (!jugador.equals(getJugadorActual())) {
            JOptionPane.showMessageDialog(null, "¡No es tu turno para robar!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (requiereSegundaJugadaPorComodin) {
            JOptionPane.showMessageDialog(null, "Acabas de jugar un comodín. Debes jugar otra carta para determinar el nuevo color, no puedes robar.", "Acción Inválida", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (haRobadoCartaEnTurno) {
            JOptionPane.showMessageDialog(null, "Ya robaste una carta este turno. Puedes jugar la carta robada o terminar tu turno.", "Acción Ya Realizada", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (haJugadoCartaEnTurno) {
            JOptionPane.showMessageDialog(null, "Ya has jugado una carta este turno. No puedes robar.", "Acción Inválida", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Carta cartaRobada = juegoMazo.tomarCarta();
        jugador.recibirCarta(cartaRobada);

        this.haRobadoCartaEnTurno = true;
        this.puedeTerminarTurno = true;
        this.haRobado = true;
        this.ultimaCartaRobada = cartaRobada;
    }

    // Lógica para jugar una carta
    public void jugarCarta(Jugador jugador, Carta carta) {
        if (!jugador.equals(getJugadorActual())) {
            JOptionPane.showMessageDialog(null, "¡No es tu turno para jugar!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!jugador.tieneCarta(carta)) {
            JOptionPane.showMessageDialog(null, "¡No tienes esa carta en tu mano!", "Error de Jugada", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Si robó una carta, solo puede jugar esa misma
        if (haRobado && carta != ultimaCartaRobada) {
            JOptionPane.showMessageDialog(null, "Has robado una carta. Solo puedes jugar la carta que acabas de robar o terminar tu turno.", "Acción Inválida", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (haRobado) {
            haRobado = false;
            ultimaCartaRobada = null;
        }

        // Si no hay comodín pendiente, limpia el efecto anterior
        if (!requiereSegundaJugadaPorComodin && carta.getColor() != Carta.Color.negro) {
            efectoActivoPendiente = null;
        }

        // Primera jugada de un comodín
        if (!haJugadoCartaEnTurno && !requiereSegundaJugadaPorComodin &&
            (carta.getValor() == Carta.Valor.comodin || carta.getValor() == Carta.Valor.tome4)) {

            jugador.quitarCarta(carta);
            juegoMazo.descartar(carta);
            cartaSuperiorDescarte = juegoMazo.getCartaSuperiorDescarte();

            if (jugador.haGanado()) {
                haJugadoCartaEnTurno = true;
                puedeTerminarTurno = true;
                return;
            }

            if (carta.getValor() == Carta.Valor.tome4) {
                ultimoTurnoFueToma4 = true;
            }

            requiereSegundaJugadaPorComodin = true;
            JOptionPane.showMessageDialog(null, "Has jugado un " + (carta.getValor() == Carta.Valor.tome4 ? "TOMA 4" : "comodín") + ". Ahora debes jugar OTRA CARTA DE COLOR para determinar el nuevo color.", "Jugar Segunda Carta", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        // Segunda jugada tras un comodín
        else if (requiereSegundaJugadaPorComodin) {
            if (carta.getColor() == Carta.Color.negro) {
                JOptionPane.showMessageDialog(null, "No puedes jugar un comodín como segunda carta después de otro comodín. Debes usar una carta de color para determinar el nuevo color.", "Error de Jugada", JOptionPane.ERROR_MESSAGE);
                return;
            }

            jugador.quitarCarta(carta);
            juegoMazo.descartar(carta);

            cartaSuperiorDescarte = juegoMazo.getCartaSuperiorDescarte();
            colorActual = carta.getColor();
            valorActual = carta.getValor();

            requiereSegundaJugadaPorComodin = false;
            haJugadoCartaEnTurno = true;
            puedeTerminarTurno = true;

            // Aplica efectos si corresponde
            if (carta.getValor() == Carta.Valor.reversa) {
                direccion *= -1;
                if (jugadores.size() == 2) {
                    efectoActivoPendiente = Carta.Valor.salto;
                }
            } else if (carta.getValor() == Carta.Valor.tome2) {
                efectoActivoPendiente = Carta.Valor.tome2;
            } else if (carta.getValor() == Carta.Valor.salto) {
                efectoActivoPendiente = Carta.Valor.salto;
            }

            if (jugador.haGanado()) {
                return;
            }

            return;
        }

        // Jugada normal
        else {
            if (haJugadoCartaEnTurno) {
                JOptionPane.showMessageDialog(null, "Ya has jugado una carta este turno. Por favor, termina tu turno.", "Acción Ya Realizada", JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (!jugador.puedeJugarCarta(carta, colorActual, valorActual)) {
                JOptionPane.showMessageDialog(null, "¡No puedes jugar esa carta! No coincide en color ni en valor, ni es un comodín.", "Error de Jugada", JOptionPane.ERROR_MESSAGE);
                return;
            }

            jugador.quitarCarta(carta);
            juegoMazo.descartar(carta);

            cartaSuperiorDescarte = juegoMazo.getCartaSuperiorDescarte();
            if (carta.getColor() != Carta.Color.negro) {
                colorActual = carta.getColor();
            }
            valorActual = carta.getValor();

            haJugadoCartaEnTurno = true;
            puedeTerminarTurno = true;

            if (carta.getValor() == Carta.Valor.reversa) {
                direccion *= -1;
                if (jugadores.size() == 2) {
                    efectoActivoPendiente = Carta.Valor.salto;
                }
            } else if (carta.getValor() == Carta.Valor.tome2) {
                efectoActivoPendiente = Carta.Valor.tome2;
            } else if (carta.getValor() == Carta.Valor.salto) {
                efectoActivoPendiente = Carta.Valor.salto;
            }

            if (jugador.haGanado()) {
                return;
            }
        }
    }

    // Retorna el siguiente jugador que debe recibir efecto (robo o salto)
    private Jugador getSiguienteJugadorParaEfecto() {
        int nextIndex = (jugadorActualIndex + direccion + jugadores.size()) % jugadores.size();
        return jugadores.get(nextIndex);
    }

    // Cambia el turno al siguiente jugador
    public void siguienteJugador() {
        if (isJuegoTerminado()) {
            return;
        }

        if (requiereSegundaJugadaPorComodin) {
            JOptionPane.showMessageDialog(null, "Debes jugar la segunda carta para determinar el color del comodín antes de terminar tu turno.", "Acción Pendiente", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (!haJugadoCartaEnTurno && !haRobadoCartaEnTurno) {
            JOptionPane.showMessageDialog(null, "Debes jugar una carta o robar una antes de terminar tu turno.", "Acción Pendiente", JOptionPane.WARNING_MESSAGE);
            return;
        }

        getJugadorActual().setEsTurno(false);

        int turnosAvanzar = 1;

        // Aplica efecto +4
        if (ultimoTurnoFueToma4) {
            Jugador siguienteRobarCuatro = getSiguienteJugadorParaEfecto();
            for (int i = 0; i < 4; i++) {
                siguienteRobarCuatro.recibirCarta(juegoMazo.tomarCarta());
            }
            JOptionPane.showMessageDialog(null, siguienteRobarCuatro.getNombre() + " roba 4 cartas y PIERDE SU TURNO.", "Efecto +4", JOptionPane.INFORMATION_MESSAGE);
            turnosAvanzar = 2;
        }
        // Aplica efectos especiales a las cartas
        else if (efectoActivoPendiente != null) {
            switch (efectoActivoPendiente) {
                case tome2:
                    Jugador siguienteRobarDos = getSiguienteJugadorParaEfecto();
                    for (int i = 0; i < 2; i++) {
                        siguienteRobarDos.recibirCarta(juegoMazo.tomarCarta());
                    }
                    JOptionPane.showMessageDialog(null, siguienteRobarDos.getNombre() + " roba 2 cartas y PIERDE SU TURNO.", "Efecto +2", JOptionPane.INFORMATION_MESSAGE);
                    turnosAvanzar = 2;
                    break;
                case salto:
                    JOptionPane.showMessageDialog(null, getSiguienteJugadorParaEfecto().getNombre() + " pierde su turno.", "Efecto Salto", JOptionPane.INFORMATION_MESSAGE);
                    turnosAvanzar = 2;
                    break;
                default:
                    break;
            }
            efectoActivoPendiente = null;
        }

        for (int i = 0; i < turnosAvanzar; i++) {
            jugadorActualIndex = (jugadorActualIndex + direccion + jugadores.size()) % jugadores.size();
        }

        getJugadorActual().setEsTurno(true);
        resetearEstadoTurno();
    }

    // Getters
    public Jugador getJugadorActual() {
        return jugadores.get(jugadorActualIndex);
    }

    public List<Jugador> getJugadores() {
        return jugadores;
    }

    public int getCartasRestantesEnMazo() {
        return juegoMazo.getCartasEnMazo();
    }

    public Carta getCartaSuperiorDescarte() {
        return juegoMazo.getCartaSuperiorDescarte();
    }

    public Carta.Color getColorActual() {
        return colorActual;
    }

    public Carta.Valor getValorActual() {
        return valorActual;
    }

    public int getJugadorActualIndex() {
        return jugadorActualIndex;
    }

    public boolean requiereSegundaJugadaPorComodin() {
        return requiereSegundaJugadaPorComodin;
    }

    public int getDireccion() {
        return direccion;
    }

    // Verifica si algún jugador ha ganado 
    public boolean isJuegoTerminado() {
        for (Jugador j : jugadores) {
            if (j.getMano().isEmpty()) {
                return true;
            }
        }
        return false;
    }

    // Reparte 7 cartas iniciales a cada jugador
    private void repartirCartasIniciales() {
        for (Jugador jugador : jugadores) {
            for (int i = 0; i < 7; i++) {
                jugador.recibirCarta(juegoMazo.tomarCarta());
            }
        }
    }

    // Inicia el juego sacando la primera carta válida (que no sea comodín)
    private void iniciarPrimeraCartaDescarte() {
        Carta primeraCarta;
        do {
            primeraCarta = juegoMazo.tomarCarta();
        } while (primeraCarta.getColor() == Carta.Color.negro);

        juegoMazo.descartar(primeraCarta);
        this.cartaSuperiorDescarte = juegoMazo.getCartaSuperiorDescarte();
        this.colorActual = primeraCarta.getColor();
        this.valorActual = primeraCarta.getValor();
    }
}
