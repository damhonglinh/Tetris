package tetris;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Observable;
import java.util.Observer;
import javax.swing.AbstractAction;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.SpringLayout;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

/**
 *
 * @author Dam Linh
 */
public class MainPanel extends Box implements Observer {

    private int jumpCount;
    private Timer jumpTimer;
    private JPanel subCenterPanel;
    private JPanel subNextPanel1 = new JPanel(new GridLayout(2, 4));
    private JPanel subNextPanel2 = new JPanel(new GridLayout(2, 4));
    private JPanel subNextPanel3 = new JPanel(new GridLayout(2, 4));
    private JPanel subHoldPanel = new JPanel(new GridLayout(2, 4));
    private JPanel nextPanel1;
    private JPanel nextPanel2;
    private JPanel nextPanel3;
    private JPanel holdPanel;
    private Box statBox;
    private Box nextBox;
    private JPanel center;
    private SpringLayout sprLayoutCenter;
    private JLabel nextLabel;
    private JLabel holdLabel;
    private JLabel lvlLabel;
    private JLabel lineLabel;
    private JLabel timeLabel;
    private JLabel lvl;
    private JLabel line;
    private JLabel time;
    private KulButton quit;
    private KulButton restart;
    private KulButton play;
    private Model model;
    private Token[][] tokens;
    private TokenNext[][] nextTokens1;
    private TokenNext[][] nextTokens2;
    private TokenNext[][] nextTokens3;
    private TokenNext[][] holdTokens;
    private Font font = Template.getFont();
    private final int STAT_PANEL_WIDTH = 230;
    private final int NEXT_PANEL_WIDTH = 205;

    public MainPanel(Model model) {
        super(BoxLayout.X_AXIS);
        this.model = model;
        this.tokens = model.getTokens();
        this.nextTokens1 = model.getNextTokens1();
        this.nextTokens2 = model.getNextTokens2();
        this.nextTokens3 = model.getNextTokens3();
        this.holdTokens = model.getHoldTokens();
        jumpTimer = new Timer(4, new TimerListener());

        setBackground(Template.getBackground());

        drawNextPanel();
        drawCenterPanel();
        drawStatPanel();
        setKeyBinding();
    }

    private void drawNextPanel() {
        nextBox = new Box(BoxLayout.Y_AXIS);
        nextBox.setPreferredSize(new Dimension(NEXT_PANEL_WIDTH, 5000));
        nextBox.setMaximumSize(new Dimension(NEXT_PANEL_WIDTH, 5000));
        nextBox.setMinimumSize(new Dimension(NEXT_PANEL_WIDTH, 5000));
        nextBox.setBackground(Template.getBackground());
        nextBox.setOpaque(true);
        add(nextBox);

        //draw sub-panels
        subNextPanel1 = new JPanel(new GridLayout(2, 4));
        subNextPanel2 = new JPanel(new GridLayout(2, 4));
        subNextPanel3 = new JPanel(new GridLayout(2, 4));
        subHoldPanel = new JPanel(new GridLayout(2, 4));//for holdTokens
        subNextPanel1.setBackground(Template.getBackground());
        subNextPanel2.setBackground(Template.getBackground());
        subNextPanel3.setBackground(Template.getBackground());
        subHoldPanel.setBackground(Template.getBackground());
        for (int i = 0; i < nextTokens1.length; i++) {
            for (int j = 0; j < nextTokens1[i].length; j++) {
                subNextPanel1.add(nextTokens1[i][j]);
                subNextPanel2.add(nextTokens2[i][j]);
                subNextPanel3.add(nextTokens3[i][j]);
                subHoldPanel.add(holdTokens[i][j]);
            }
        }

        //draw panels that contain sub-panels
        SpringLayout sprLayout = new SpringLayout();
        nextPanel1 = new JPanel(sprLayout);
        nextPanel2 = new JPanel(sprLayout);
        nextPanel3 = new JPanel(sprLayout);
        holdPanel = new JPanel(sprLayout);
        nextPanel1.setBackground(Template.getBackground());
        nextPanel2.setBackground(Template.getBackground());
        nextPanel3.setBackground(Template.getBackground());
        holdPanel.setBackground(Template.getBackground());
        nextPanel1.add(subNextPanel1);
        nextPanel2.add(subNextPanel2);
        nextPanel3.add(subNextPanel3);
        holdPanel.add(subHoldPanel);

        //set size for panels
        nextPanel1.setPreferredSize(new Dimension(NEXT_PANEL_WIDTH, 120));
        nextPanel1.setMaximumSize(new Dimension(NEXT_PANEL_WIDTH, 120));

        nextPanel2.setPreferredSize(new Dimension(NEXT_PANEL_WIDTH, 110));
        nextPanel2.setMaximumSize(new Dimension(NEXT_PANEL_WIDTH, 110));

        nextPanel3.setPreferredSize(new Dimension(NEXT_PANEL_WIDTH, 110));
        nextPanel3.setMaximumSize(new Dimension(NEXT_PANEL_WIDTH, 110));

        //30 pixels greater than that of next1 because of SOUTH spring
        holdPanel.setPreferredSize(new Dimension(NEXT_PANEL_WIDTH, 150));
        holdPanel.setMaximumSize(new Dimension(NEXT_PANEL_WIDTH, 150));

        // spring for nextTokens1
        sprLayout.putConstraint(SpringLayout.NORTH, subNextPanel1, 25, SpringLayout.NORTH, nextPanel1);
        sprLayout.putConstraint(SpringLayout.WEST, subNextPanel1, 26, SpringLayout.WEST, nextPanel1);
        sprLayout.putConstraint(SpringLayout.EAST, subNextPanel1, -10, SpringLayout.EAST, nextPanel1);
        sprLayout.putConstraint(SpringLayout.SOUTH, subNextPanel1, -10, SpringLayout.SOUTH, nextPanel1);

        // spring for nextTokens2
        sprLayout.putConstraint(SpringLayout.NORTH, subNextPanel2, 40, SpringLayout.NORTH, nextPanel2);
        sprLayout.putConstraint(SpringLayout.WEST, subNextPanel2, 40, SpringLayout.WEST, nextPanel2);
        sprLayout.putConstraint(SpringLayout.EAST, subNextPanel2, -30, SpringLayout.EAST, nextPanel2);
        sprLayout.putConstraint(SpringLayout.SOUTH, subNextPanel2, -10, SpringLayout.SOUTH, nextPanel2);

        // spring for nextTokens3
        sprLayout.putConstraint(SpringLayout.NORTH, subNextPanel3, 25, SpringLayout.NORTH, nextPanel3);
        sprLayout.putConstraint(SpringLayout.WEST, subNextPanel3, 40, SpringLayout.WEST, nextPanel3);
        sprLayout.putConstraint(SpringLayout.EAST, subNextPanel3, -30, SpringLayout.EAST, nextPanel3);
        sprLayout.putConstraint(SpringLayout.SOUTH, subNextPanel3, -25, SpringLayout.SOUTH, nextPanel3);

        // spring for holdTokens
        sprLayout.putConstraint(SpringLayout.NORTH, subHoldPanel, 25, SpringLayout.NORTH, holdPanel);
        sprLayout.putConstraint(SpringLayout.WEST, subHoldPanel, 29, SpringLayout.WEST, holdPanel);
        sprLayout.putConstraint(SpringLayout.EAST, subHoldPanel, -10, SpringLayout.EAST, holdPanel);
        sprLayout.putConstraint(SpringLayout.SOUTH, subHoldPanel, -40, SpringLayout.SOUTH, holdPanel);

        //draw text JLabel
        nextLabel = new JLabel("Cục tiếp theo");
        holdLabel = new JLabel("Tạm giữ");
        nextLabel.setFont(font);
        holdLabel.setFont(font);
        nextLabel.setForeground(Template.getForeground());
        holdLabel.setForeground(Template.getForeground());
        //draw Box that contain text
        Box nextLabelBox = new Box(BoxLayout.X_AXIS);
        nextLabelBox.add(nextLabel);
        Box holdLabelBox = new Box(BoxLayout.X_AXIS);
        holdLabelBox.add(holdLabel);

        nextBox.add(nextLabelBox);
        nextBox.add(nextPanel1);
        nextBox.add(nextPanel2);
        nextBox.add(nextPanel3);
        nextBox.add(Box.createVerticalGlue());
        nextBox.add(holdLabelBox);
        nextBox.add(holdPanel);
    }

    private void drawCenterPanel() {
        center = new JPanel();
        center.setBackground(Template.getBackground());
        add(center);

        subCenterPanel = new JPanel(new GridLayout(tokens.length - 1, 10));
        subCenterPanel.setBackground(Template.getBackground());
        center.add(subCenterPanel);
        for (int i = 1; i < tokens.length; i++) { //first line is hidden
            for (int j = 0; j < tokens[i].length; j++) {
                subCenterPanel.add(tokens[i][j]);
            }
        }

        sprLayoutCenter = new SpringLayout();
        center.setLayout(sprLayoutCenter);
        sprLayoutCenter.putConstraint(SpringLayout.NORTH, subCenterPanel, 0, SpringLayout.NORTH, center);
        sprLayoutCenter.putConstraint(SpringLayout.WEST, subCenterPanel, 0, SpringLayout.WEST, center);
        sprLayoutCenter.putConstraint(SpringLayout.EAST, subCenterPanel, 0, SpringLayout.EAST, center);
        sprLayoutCenter.putConstraint(SpringLayout.SOUTH, subCenterPanel, -8, SpringLayout.SOUTH, center);
    }

    private void drawStatPanel() {
        statBox = new Box(BoxLayout.Y_AXIS);
        statBox.setOpaque(true);
        statBox.setPreferredSize(new Dimension(STAT_PANEL_WIDTH, 5000));
        statBox.setMaximumSize(new Dimension(STAT_PANEL_WIDTH, 5000));
        add(statBox);

        statBox.add(Box.createVerticalStrut(20));
        drawLvlBoxInStatBox();
        statBox.add(Box.createVerticalStrut(15));
        drawLineInStatBox();
        drawTimeInStatBox();
        statBox.add(Box.createVerticalGlue());
        drawRestartAndPlayButton();
        statBox.add(Box.createVerticalStrut(20));
        drawQuitButton();
        statBox.add(Box.createVerticalStrut(50));
    }

    private void drawLvlBoxInStatBox() {
        Box lvlBox = new Box(BoxLayout.X_AXIS);
        lvlBox.setPreferredSize(new Dimension(STAT_PANEL_WIDTH, 100));
        lvlBox.setMaximumSize(new Dimension(STAT_PANEL_WIDTH, 100));
        statBox.add(lvlBox);

        lvlLabel = new JLabel("Level:");
        lvlLabel.setFont(font.deriveFont(0, 56));
        lvlLabel.setForeground(Template.getForeground());

        lvl = new JLabel("1");
        lvl.setFont(font.deriveFont(0, 56));
        lvl.setForeground(Template.getForeground());

        lvlBox.add(Box.createHorizontalGlue());
        lvlBox.add(lvlLabel);
        lvlBox.add(Box.createHorizontalStrut(10));
        lvlBox.add(lvl);
        lvlBox.add(Box.createHorizontalGlue());
    }

    private void drawLineInStatBox() {
        Box lineBox = new Box(BoxLayout.X_AXIS);
        lineBox.setPreferredSize(new Dimension(STAT_PANEL_WIDTH, 80));
        lineBox.setMaximumSize(new Dimension(STAT_PANEL_WIDTH, 80));
        statBox.add(lineBox);

        lineLabel = new JLabel("Hàng thứ:");
        lineLabel.setFont(font);
        lineLabel.setForeground(Template.getForeground());

        line = new JLabel("0");
        line.setFont(font);
        line.setForeground(Template.getForeground());

        lineBox.add(Box.createHorizontalStrut(17));
        lineBox.add(lineLabel);
        lineBox.add(Box.createHorizontalGlue());
        lineBox.add(line);
        lineBox.add(Box.createHorizontalStrut(17));
    }

    private void drawTimeInStatBox() {
        Box timeBox = new Box(BoxLayout.X_AXIS);
        timeBox.setPreferredSize(new Dimension(STAT_PANEL_WIDTH, 80));
        timeBox.setMaximumSize(new Dimension(STAT_PANEL_WIDTH, 80));
        statBox.add(timeBox);

        timeLabel = new JLabel("Thời gian:");
        timeLabel.setFont(font);
        timeLabel.setForeground(Template.getForeground());

        time = new JLabel("0");
        time.setFont(font);
        time.setHorizontalAlignment(SwingConstants.RIGHT);
        time.setForeground(Template.getForeground());

        timeBox.add(Box.createHorizontalStrut(17));
        timeBox.add(timeLabel);
        timeBox.add(Box.createHorizontalGlue());
        timeBox.add(time);
        timeBox.add(Box.createHorizontalStrut(17));
    }

    private void drawRestartAndPlayButton() {
        Box tempBox = new Box(BoxLayout.X_AXIS);
        statBox.add(tempBox);

        restart = new KulButton("Chơi lại");
        restart.setPreferredSize(new Dimension(180, 60));
        restart.setMaximumSize(new Dimension(180, 60));
        restart.setToolTipText("Chơi lại từ đầu");
        restart.setVisible(false);
        restart.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (SwingUtilities.isLeftMouseButton(e)) {
                    restart();
                }
            }
        });

        play = new KulButton("Chơi");
        play.setPreferredSize(new Dimension(180, 60));
        play.setMaximumSize(new Dimension(180, 60));
        play.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (SwingUtilities.isLeftMouseButton(e)) {
                    start();
                }
            }
        });
        tempBox.add(play);
        tempBox.add(restart);
    }

    private void drawQuitButton() {
        Box tempBox = new Box(BoxLayout.X_AXIS);
        statBox.add(tempBox);

        quit = new KulButton("Thoát");
        quit.setPreferredSize(new Dimension(180, 60));
        quit.setMaximumSize(new Dimension(180, 60));
        quit.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (SwingUtilities.isLeftMouseButton(e)) {
                    String[] options = {"Phải", "Không"};
                    int choice = JOptionPane.showOptionDialog(MainPanel.this,
                            "Thoát?", "Thoát", JOptionPane.YES_NO_OPTION,
                            JOptionPane.WARNING_MESSAGE, null, options, 0);
                    if (choice == JOptionPane.OK_OPTION) {
                        System.exit(0);
                    }
                }
            }
        });
        tempBox.add(quit);
    }

    private void start() {
        model.start();
        play.setVisible(false);
        restart.setVisible(true);
    }

    private void restart() {
        model.restart();
        play.setVisible(true);
        restart.setVisible(false);
    }

    private void setKeyBinding() {
        //LEFT
        this.getInputMap().put(KeyStroke.getKeyStroke("LEFT"), "moveLeft");
        this.getActionMap().put("moveLeft", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                model.moveLeft();
            }
        });

        //RIGHT
        this.getInputMap().put(KeyStroke.getKeyStroke("RIGHT"), "moveRight");
        this.getActionMap().put("moveRight", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                model.moveRight();
            }
        });

        //DOWN
        this.getInputMap().put(KeyStroke.getKeyStroke("DOWN"), "moveDown");
        this.getActionMap().put("moveDown", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                model.moveDown();
            }
        });

        //DROP
        this.getInputMap().put(KeyStroke.getKeyStroke(' '), "drop");
        this.getInputMap().put(KeyStroke.getKeyStroke("C"), "drop");
        this.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "drop");
        this.getActionMap().put("drop", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                model.drop();
            }
        });

        //ROTATE LEFT
        this.getInputMap().put(KeyStroke.getKeyStroke("UP"), "rotateLeft");
        this.getInputMap().put(KeyStroke.getKeyStroke("Z"), "rotateLeft");
        this.getActionMap().put("rotateLeft", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                model.rotateLeft();
            }
        });

        //ROTATE RIGHT
        this.getInputMap().put(KeyStroke.getKeyStroke("X"), "rotateRight");
        this.getActionMap().put("rotateRight", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                model.rotateRight();
            }
        });

        //START NEW
        this.getInputMap().put(KeyStroke.getKeyStroke("S"), "start");
        this.getActionMap().put("start", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (play.isVisible()) {
                    start();
                }
            }
        });

        //RE-START
        this.getInputMap().put(KeyStroke.getKeyStroke("R"), "restart");
        this.getActionMap().put("restart", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (restart.isVisible()) {
                    restart();
                }
            }
        });

        //HOLD
        this.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_CONTROL, InputEvent.CTRL_DOWN_MASK), "hold");
        this.getActionMap().put("hold", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                model.hold();
            }
        });

        //PAUSE
        this.getInputMap().put(KeyStroke.getKeyStroke('P',
                InputEvent.CTRL_DOWN_MASK + InputEvent.SHIFT_DOWN_MASK), "pause");
        this.getActionMap().put("pause", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                model.pause();
            }
        });

        //SPEED UP
        this.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_UP,
                InputEvent.CTRL_DOWN_MASK + InputEvent.SHIFT_DOWN_MASK), "speedUp");
        this.getActionMap().put("speedUp", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                model.speedUp();
            }
        });

        //SPEED DOWN
        this.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN,
                InputEvent.CTRL_DOWN_MASK + InputEvent.SHIFT_DOWN_MASK), "speedDown");
        this.getActionMap().put("speedDown", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                model.speedDown();
            }
        });
    }

    private void changeBackgroundForLvl(int lvl) {
        lvl = lvl % 11;
        float h = 12;
        float s = lvl * 0.07f;
        float b = 1f;
        Color color = Color.getHSBColor(h, s, b);

        Template.setBackground(color);
        setBackground(color);
        center.setBackground(color);
        statBox.setBackground(color);
        nextBox.setBackground(color);
        nextPanel1.setBackground(color);
        nextPanel2.setBackground(color);
        nextPanel3.setBackground(color);
        holdPanel.setBackground(color);
        subCenterPanel.setBackground(color);
        subNextPanel1.setBackground(color);
        subNextPanel2.setBackground(color);
        subNextPanel3.setBackground(color);
        subHoldPanel.setBackground(color);
    }

    private void changeForegroundForLvl(int level) {
        float h = 0;
        float s = 1f;
        float b = 0.40f;
        Color color = Color.getHSBColor(h, s, b);

        Template.setForeground(color);
        nextLabel.setForeground(color);
        holdLabel.setForeground(color);
        lvlLabel.setForeground(color);
        timeLabel.setForeground(color);
        lineLabel.setForeground(color);
        lvl.setForeground(color);
        time.setForeground(color);
        line.setForeground(color);
        play.setForeground(color);
        quit.setForeground(color);
    }

    @Override
    public void update(Observable obs, Object o) {
        if (o instanceof String) {
            String s = (String) o;
            if (s.equals("Refresh")) {
                repaint();
            }
            if (s.equals("Jump")) {
                jumpTimer.start();
            }

        } else if (o instanceof Integer) {// if o is time
            String timeString = (Integer) o + "";
            time.setText(timeString);

        } else if (o instanceof Byte) {
            String lvlString = (Byte) o + "";
            changeBackgroundForLvl((Byte) o);
            changeForegroundForLvl((Byte) o);
            lvl.setText(lvlString);

        } else if (o instanceof Short) {
            String lineString = (Short) o + "";
            line.setText(lineString);
        }
    }

    private class TimerListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            jumpCount++;
            if (jumpCount > 8) {
                jumpCount = 0;
                jumpTimer.stop();
            } else {
                if (jumpCount <= 4) {
                    sprLayoutCenter.putConstraint(SpringLayout.NORTH, subCenterPanel, jumpCount * 2, SpringLayout.NORTH, center);
                    sprLayoutCenter.putConstraint(SpringLayout.SOUTH, subCenterPanel, -(8 - jumpCount * 2), SpringLayout.SOUTH, center);
                    center.revalidate();
                    center.repaint();
                } else {
                    sprLayoutCenter.putConstraint(SpringLayout.NORTH, subCenterPanel, 16 - jumpCount * 2, SpringLayout.NORTH, center);
                    sprLayoutCenter.putConstraint(SpringLayout.SOUTH, subCenterPanel, -(jumpCount * 2 - 8), SpringLayout.SOUTH, center);
                    center.revalidate();
                    center.repaint();
                }
            }
        }
    }
}
