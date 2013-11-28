package model;

import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.RenderingHints;
import javax.swing.JLabel;
import javax.swing.border.LineBorder;

/**
 *
 * @author Dam Linh
 */
public class Token extends JLabel {

    private int type;
//    private Color inner1;
//    private Color inner2;
    private Color backgroundColor;
    private Color color2;
//    private Color squareColor;
//    private Color left;
//    private Color right;
    private Color borderColor;
    private boolean frozen;

    public Token() {
        setType(0);
    }

    public int getType() {
        return type;
    }

    //<editor-fold defaultstate="collapsed" desc="setType">
    protected void setType(int type) {
        this.type = type;
        //set color
        switch (Math.abs(type)) {
            case 1:
//                inner1 = new Color(215, 15, 55);
//                inner2 = new Color(254, 78, 113);
                backgroundColor = new Color(255, 87, 107);
                color2 = new Color(255, 176, 185);
//                left = new Color(255, 181, 181);
//                right = new Color(199, 14, 51);
                borderColor = new Color(158, 12, 41);
                break;
            case 2:
//                inner1 = new Color(15, 155, 215);
//                inner2 = new Color(33, 190, 255);
                backgroundColor = new Color(89, 249, 255);
                color2 = new Color(168, 252, 255);
//                left = new Color(95, 206, 254);
//                right = new Color(9, 140, 196);
                borderColor = new Color(2, 138, 143);
                break;
            case 3:
//                inner1 = new Color(33, 65, 198);
//                inner2 = new Color(47, 114, 220);
                backgroundColor = new Color(58, 115, 250);
                color2 = new Color(130, 168, 255);
//                left = new Color(71, 134, 226);
//                right = new Color(27, 70, 169);
                borderColor = new Color(1, 36, 118);
                break;
            case 4:
//                inner1 = new Color(255, 194, 37);
//                inner2 = new Color(255, 234, 76);
                backgroundColor = new Color(252, 255, 69);
                color2 = new Color(253, 255, 135);
//                left = new Color(255, 249, 155);
//                right = new Color(255, 177, 37);
                borderColor = new Color(153, 102, 0);
                break;
            case 5:
//                inner1 = new Color(89, 177, 1);
//                inner2 = new Color(128, 211, 22);
                backgroundColor = new Color(130, 255, 20);
                color2 = new Color(182, 252, 121);
//                left = new Color(182, 236, 108);
//                right = new Color(84, 162, 13);
                borderColor = new Color(2, 92, 1);
                break;
            case 6:
//                inner1 = new Color(210, 76, 173);
//                inner2 = new Color(246, 93, 220);
                backgroundColor = new Color(255, 97, 242);
                color2 = new Color(255, 168, 248);
//                left = new Color(254, 131, 242);
//                right = new Color(189, 68, 166);
                borderColor = new Color(102, 0, 102);
                break;
            case 7:
//                inner1 = new Color(253, 102, 2);
//                inner2 = new Color(252, 148, 46);
                backgroundColor = new Color(255, 162, 0);
                color2 = new Color(255, 199, 102);
//                left = new Color(254, 168, 85);
//                right = new Color(219, 88, 2);
                borderColor = new Color(153, 51, 0);
                break;
            default:
                backgroundColor = new Color(1, 1, 1);
                borderColor = new Color(25, 25, 25);
        }
        if (type < 0) {
//            inner1 = inner1.darker().darker().darker();
//            inner2 = inner2.darker().darker().darker();
            color2 = backgroundColor.darker().darker();
            backgroundColor = backgroundColor.darker().darker().darker();
//            squareColor = new Color(120, 120, 120);
//            left = left.darker().darker().darker();
//            right = right.darker().darker().darker();
            borderColor = borderColor.darker().darker().darker();
//        } else {
//            squareColor = Color.WHITE;
        }
        setBorder(new LineBorder(borderColor));
    }
    //</editor-fold>

    public void flash() {
//        inner1 = new Color(211, 211, 211);
//        backgroundColor = new Color(192, 192, 192);
//        right = new Color(169, 169, 169);
        borderColor = Color.WHITE;
        setBorder(new LineBorder(borderColor, 2));
    }

    public void unFlash() {
        setType(type);
    }

    public void setBackgroundColor(Color backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public void setBorderColor(Color borderColor) {
        this.borderColor = borderColor;
    }

    public boolean isFrozen() {
        return frozen;
    }

    public void setFrozen(boolean frozen) {
        this.frozen = frozen;
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        int width = getWidth();
        int height = getHeight();

        drawInner3(g2d, width, height);
        if (type != 0) {
            drawBackground(g2d, width, height);
        }

//        if (type != 0) {
//            drawInner2(g2d, width, height);
//            drawInner1(g2d, width, height);
//            drawLeft(g2d, width, height);
//            drawWhiteSquare(g2d, width, height);
//            drawRight(g2d, width, height);
//        }
    }

    //drawing inner-3 (background)
    private void drawInner3(Graphics2D g2d, int width, int height) {
        g2d.setColor(backgroundColor);
        g2d.fillRect(0, 0, width, height);
    }

    private void drawBackground(Graphics2D g2d, int width, int height) {
        Paint p = new GradientPaint(
                0, 0, color2,
                0, 2 * height / 3, backgroundColor);
        g2d.setPaint(p);
        g2d.fillRect(0, 0, width, height);
    }

//    private void drawInner1(Graphics2D g2d, int width, int height) {
//        int x = (width / 3);
//        int y = (height / 3);
//
//        g2d.setColor(inner1);
//        g2d.fillRect(x, y, width - 2 * x, height - 2 * y);
//    }
//
//    private void drawInner2(Graphics2D g2d, int width, int height) {
//        int x = (width / 4);
//        int y = (height / 4);
//
//        g2d.setColor(inner2);
//        g2d.fillRect(x, y, width - 2 * x, height - 2 * y);
//    }
//
//    private void drawLeft(Graphics2D g2d, int width, int height) {
//        //upper
//        g2d.setColor(left);
//
//        int x = (width / 8) + 1;// +1 to make it shorter
//        int y = 0;
//        int widthRec = width - 2 * x;
//        int heightRec = ((height * 3) / 32);// 3/32 = 1/16 + 1/32
//        g2d.fillRect(x, y, widthRec, heightRec);
//
//        //left
//        x = 0;
//        y = (height / 8) + 1;
//        widthRec = ((width * 3) / 32);// 3/32 = 1/16 + 1/32
//        heightRec = height - 2 * y;
//        g2d.fillRect(x, y, widthRec, heightRec);
//    }
//
//    private void drawWhiteSquare(Graphics2D g2d, int width, int height) {
//        int widthRec = (width / 8);
//        int heightRec = (height / 8);
//
//        g2d.setColor(squareColor);
//        g2d.fillRect(0, 0, widthRec, heightRec);
//    }
//
//    private void drawRight(Graphics2D g2d, int width, int height) {
//        //lower
//        g2d.setColor(right);
//
//        int heightRec = ((height * 3) / 32);// 3/32 = 1/16 + 1/32
//        int x = (width / 8) + 1;
//        int widthRec = width - 2 * x;
//        int y = height - heightRec;
//        g2d.fillRect(x, y, widthRec, heightRec);
//
//        //right
//        widthRec = ((width * 3) / 32);// 3/32 = 1/16 + 1/32
//        x = width - widthRec;
//        y = (height / 8) + 1;
//        heightRec = height - 2 * y;
//        g2d.fillRect(x, y, widthRec, heightRec);
//    }
    @Override
    public String toString() {
        return frozen + "." + type;
    }
}
