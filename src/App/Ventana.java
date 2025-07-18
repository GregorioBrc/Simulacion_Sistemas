package App;

import java.awt.Color;
import java.awt.Container;

import javax.swing.JFrame;

public class Ventana extends JFrame{

    private Container Cont = new Container();

    public Ventana(){
        setTitle("Juego de la Vida");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(800, 800);
        setLocationRelativeTo(null);
        setResizable(false);
        setLayout(null);
        setContentPane(Cont);
        setVisible(true);

        show();
    }

}
