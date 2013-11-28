package tetris;

import javax.swing.JApplet;
import javax.swing.JFrame;

/**
 *
 * @author Dam Linh
 */
public class Main extends JApplet {

    public static void main(String[] args) {
        JFrame frame = new JFrame();
        final Main applet = new Main();
        applet.init();
        frame.add(applet);

        frame.setVisible(true);
        frame.setTitle("Tetris");
        frame.setSize(805, 740);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
    }

    @Override
    public void init() {
        Model model = new Model();
        MainPanel mainPanel = new MainPanel(model);

        model.addObserver(mainPanel);
        add(mainPanel);
    }
}
