package viewcontroller;

import java.awt.Color;
import java.awt.Font;

/**
 *
 * @author Dam Linh
 */
public class Template {

    private static Color background = new Color(187, 200, 255);
    private static Color foreground = new Color(255, 255, 255);
    private static Color contrast = new Color(100, 55, 155);
    private static Color buttonDefaultColor = new Color(255, 255, 255);
    private static Color buttonMouseOveredColor = new Color(255, 230, 125);
    private static Color buttonBGMouseOver = new Color(125, 155, 255, 60);
    private static Color buttonBGMouseClicked = new Color(125, 155, 255, 120);
    private static Color lineBorderColor = new Color(255, 255, 250);
    private static Font font = new Font("Arial", 0, 24);
    private static Font fontAlter = new Font("Tempus Sans ITC", 1, 24);
    private static Color foregroundContrast = new Color(120, 60, 200);

    public static Color getBackground() {
        return background;
    }

    public static Color getLineBorderColor() {
        return lineBorderColor;
    }

    public static void setLineBorderColor(Color lineBorderColor) {
        Template.lineBorderColor = lineBorderColor;
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

    public static Color getButtonDefaultColor() {
        return buttonDefaultColor;
    }

    public static void setButtonDefaultColor(Color buttonDefaultColor) {
        Template.buttonDefaultColor = buttonDefaultColor;
    }

    public static Color getButtonMouseOveredColor() {
        return buttonMouseOveredColor;
    }

    public static void setButtonMouseOveredColor(Color buttonMouseOveredColor) {
        Template.buttonMouseOveredColor = buttonMouseOveredColor;
    }

    public static Color getButtonBGMouseOver() {
        return buttonBGMouseOver;
    }

    public static void setButtonBGMouseOver(Color buttonBGMouseOver) {
        Template.buttonBGMouseOver = buttonBGMouseOver;
    }

    public static Color getButtonBGMouseClicked() {
        return buttonBGMouseClicked;
    }

    public static void setButtonBGMouseClicked(Color buttonBGMouseClicked) {
        Template.buttonBGMouseClicked = buttonBGMouseClicked;
    }

    public static Color getForegroundContrast() {
        return foregroundContrast;
    }

    public static void setForegroundContrast(Color foregroundContrast) {
        Template.foregroundContrast = foregroundContrast;
    }

    public static Font getFontAlter() {
        return fontAlter;
    }

    public static void setFontAlter(Font fontAlter) {
        Template.fontAlter = fontAlter;
    }
}
