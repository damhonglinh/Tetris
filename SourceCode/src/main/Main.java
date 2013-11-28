package main;

import java.awt.Toolkit;
import java.awt.event.WindowEvent;
import model.Model;
import javax.swing.JApplet;
import javax.swing.JFrame;
import javax.swing.UIManager;
import viewcontroller.MainPanel;

/**
 *
 * @author Dam Linh
 */
public class Main extends JApplet {

    private static JFrame frame;
    public static final int WIDTH_MAIN = 805;
    public static final int HEIGHT_MAIN = 740;

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        frame = new JFrame();
        final Main applet = new Main();
        applet.init();
        frame.add(applet);

        frame.setResizable(false);
        frame.setUndecorated(true);
        frame.setVisible(true);
        frame.setTitle("Tetris");
        frame.setSize(WIDTH_MAIN, HEIGHT_MAIN);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setIconImage(Toolkit.getDefaultToolkit().getImage(ClassLoader.getSystemResource("Images/icon.png")));
    }

    @Override
    public void init() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        Model model = new Model();
        MainPanel mainPanel = new MainPanel(model);

        model.addObserver(mainPanel);
        add(mainPanel);
    }

    public static void closeWindow() {
        if (frame != null) {
            frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
        }
    }

    public static boolean isApplet() {
        return frame == null;
    }
}
