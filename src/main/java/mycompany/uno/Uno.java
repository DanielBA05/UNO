/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package mycompany.uno;
import javax.swing.SwingUtilities;

/**
 *
 * @author jos23
 */
public class Uno {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new RegistroJugadores();
        });
    }
}


