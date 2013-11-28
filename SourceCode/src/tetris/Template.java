package tetris;

import java.awt.Color;
import java.awt.Font;

/**
 *
 * @author Dam Linh
 */
public class Template {

    private static Color background = Color.getHSBColor(0f, 0.1f, 1f);
    private static Color foreground = Color.getHSBColor(230, 0.4f, 0.27f);
    private static Color contrast = new Color(100, 55, 155);
    private static Color button1 = Color.getHSBColor(0, 1, 0.4f);
    private static Color button2 = new Color(184, 80, 0);
    private static Font font = new Font("Arial", 0, 28);

    public static Color getBackground() {
        return background;
    }

    public static void setBackground(Color background) {
        Template.background = background;
    }

    public static Color getForeground() {
        return foreground;
    }

    public static void setForeground(Color foreground) {
        Template.foreground = foreground;
    }

    public static Color getContrast() {
        return contrast;
    }

    public static void setContrast(Color contrast) {
        Template.contrast = contrast;
    }

    public static Font getFont() {
        return font;
    }

    public static void setFont(Font font) {
        Template.font = font;
    }

    public static Color getButton1() {
        return button1;
    }

    public static void setButton1(Color button1) {
        Template.button1 = button1;
    }

    public static Color getButton2() {
        return button2;
    }

    public static void setButton2(Color button2) {
        Template.button2 = button2;
    }
}
