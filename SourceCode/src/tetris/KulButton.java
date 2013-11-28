package tetris;

/**
 *
 * @author Dam Linh
 */
import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;

public class KulButton extends JLabel {

    private Color defaultColor;
    private Color borderHoverColor;
    private Border defaultBorder;
    private Border hoverBorder;
    private String textDisplay;
    private int moving;

    public KulButton(String text, Color defaultColor, Color hoverColor) {
        this.textDisplay = text;
        this.defaultColor = defaultColor;
        this.borderHoverColor = hoverColor;

        hoverBorder = new LineBorder(borderHoverColor, 2);
        defaultBorder = new LineBorder(defaultColor);
        setHorizontalAlignment(SwingConstants.CENTER);
        setVerticalAlignment(SwingConstants.CENTER);
        setBorder(defaultBorder);
        setFont(Template.getFont());
        setForeground(Template.getForeground());

        addMouseListener(new Listener());
    }

    public KulButton(String text) {
        this(text, Template.getButton1(), Template.getButton2());
    }

    public void setTextDisplay(String text) {
        this.textDisplay = text;
    }

    public void setDefaultBorder(Border defaultBorder) {
        this.defaultBorder = defaultBorder;
    }

    public void setHoverBorder(Border hoverBorder) {
        this.hoverBorder = hoverBorder;
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(getForeground());
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        FontMetrics fm = g2d.getFontMetrics();

        int height = fm.getAscent() - fm.getDescent();
        int width = fm.stringWidth(textDisplay);

        // horizontal position of text
        int x;
        if (getHorizontalAlignment() == SwingConstants.CENTER) {
            x = getWidth() / 2 - width / 2 - moving;
        } else if (getHorizontalAlignment() == SwingConstants.RIGHT) {
            x = getWidth() - width - moving;
        } else {
            x = 1 - moving;
        }
        // vertical position of text
        int y;
        if (getVerticalAlignment() == SwingConstants.CENTER) {
            y = getHeight() / 2 + height / 2 - moving;
        } else if (getVerticalAlignment() == SwingConstants.TOP) {
            y = getHeight() - height - moving;
        } else {
            y = 1 - moving;
        }

        g2d.drawString(textDisplay, x, y);
    }

    private class Listener extends MouseAdapter {

        @Override
        public void mousePressed(MouseEvent e) {
            JLabel temp = (JLabel) e.getSource();
            temp.setBorder(defaultBorder);
            moving = -2;
            repaint();
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            JLabel temp = (JLabel) e.getSource();
            temp.setBorder(hoverBorder);
            moving = 0;
            repaint();
        }

        @Override
        public void mouseEntered(MouseEvent e) {
            JLabel temp = (JLabel) e.getSource();
            temp.setBorder(hoverBorder);
        }

        @Override
        public void mouseExited(MouseEvent e) {
            JLabel temp = (JLabel) e.getSource();
            temp.setBorder(defaultBorder);
        }
    }
}
