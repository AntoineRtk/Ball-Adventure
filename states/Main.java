package states;

import java.awt.Dimension;
import javax.swing.JFrame;

public class Main {
    
    public static JFrame frame;
    public static Screen screen;
    
    public Main() {
        GameSave.init();
        frame = new JFrame("Ball Adventure");
        frame.setSize(700, 700);
        frame.setLocationRelativeTo(null);
        screen = new Screen();
        frame.add(screen);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setMinimumSize(new Dimension(700, 700));
    }
    
    public static void main(String[] args) {
        new Main();
    }
}