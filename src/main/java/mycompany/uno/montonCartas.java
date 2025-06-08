/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mycompany.uno;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
/**
 *
 * @author jos23
 */
public class montonCartas {
    private List<Carta> cartas;

    public montonCartas() {
        cartas = new ArrayList<>();
        inicializarCartas();
        System.out.println("MontonCartas: Total de cartas iniciales: " + cartas.size());
    }

    private void inicializarCartas() {
        int[] colores = {Carta.ROJO, Carta.AZUL, Carta.VERDE, Carta.AMARILLO};

        for (int color : colores) {
            cartas.add(new Carta(0, color));

            for (int num = 1; num <= 9; num++) {
                cartas.add(new Carta(num, color));
                cartas.add(new Carta(num, color));
            }

            cartas.add(new Carta(color, Carta.ROBA_DOS));
            cartas.add(new Carta(color, Carta.ROBA_DOS));

            cartas.add(new Carta(color, Carta.REVERSA));
            cartas.add(new Carta(color, Carta.REVERSA));

            cartas.add(new Carta(color, Carta.SALTA));
            cartas.add(new Carta(color, Carta.SALTA));
        }

        for (int i = 0; i < 4; i++) {
            cartas.add(new Carta(Carta.COMODIN, Carta.CAMBIA_COLOR));
            cartas.add(new Carta(Carta.COMODIN, Carta.COME_CUATRO));
        }

        Collections.shuffle(cartas);
    }

    public Carta robarCarta() {
        if (cartas.isEmpty()) {
            System.out.println("¡El mazo está vacío!");
            return null;
        }
        return cartas.remove(0);
    }

    public int getCantidadCartas() {
        return cartas.size();
    }
    public void devolverCarta(Carta cartaDevuelta) {
    cartas.add(cartaDevuelta);
    Collections.shuffle(cartas);
    System.out.println("Una carta fue devuelta al mazo. Total actual: " + cartas.size());
}
    public void devolverCartas(List<Carta> cartasDevueltas) {
        cartas.addAll(cartasDevueltas);
        Collections.shuffle(cartas);
        System.out.println("Cartas devueltas al mazo. Total actual: " + cartas.size());
    }

    public void reiniciarMonton() {
        cartas.clear();
        inicializarCartas();
    }

    public void mostrarPrimerasCartas() {
        System.out.println("\n--- Primeras 5 cartas del mazo ---");
        for (int i = 0; i < Math.min(5, cartas.size()); i++) {
            System.out.println(cartas.get(i).toString());
        }
    }
}

