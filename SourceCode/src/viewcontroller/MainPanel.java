package viewcontroller;

import java.awt.Color;
import model.Token;
import model.Model;
import model.TokenNext;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedInputStream;
import java.io.InputStream;
import java.util.Observable;
import java.util.Observer;
import java.util.ResourceBundle;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.FloatControl;
import javax.swing.AbstractAction;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.SpringLayout;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.border.CompoundBorder;
import javax.swing.border.LineBorder;
import main.Main;

/**
 *
 * @author Dam Linh
 */
public class MainPanel extends JPanel implements Observer {

    private ResourceBundle language;
    private int bounceCount;
    private Timer bounceTimer;
    private JLayeredPane layer;
    private BestScorePanel bestScorePanel;
    private Box container;
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
    private JLabel scoreLabel;
    private JLabel lvl;
    private JLabel line;
    private JLabel time;
    private JLabel score;
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
    private Clip backgroundSound;
    private Clip bounceSound;
    private Clip stuckSound;
    private Clip moveSound;
    private Clip lvlUpSound;
    private Clip blowSound;
    private Clip placeSound;
    private Clip loseSound;
    private Clip tetrisSound;
    private boolean isMute;
    private Timer moveDownTimer;
//    private Timer moveLeftTimer;
//    private Timer moveRightTimer;

    //<editor-fold defaultstate="collapsed" desc="constructor">
    public MainPanel(Model model) {
        this.model = model;
        this.tokens = model.getTokens();
        this.nextTokens1 = model.getNextTokens1();
        this.nextTokens2 = model.getNextTokens2();
        this.nextTokens3 = model.getNextTokens3();
        this.holdTokens = model.getHoldTokens();
        this.language = model.getLanguage();
        bounceTimer = new Timer(4, new TimeListenerToBounce());

        setBackground(Template.getBackground());
        setBorder(new CompoundBorder(
                new LineBorder(Template.getBackground(), 2),
                new LineBorder(Template.getLineBorderColor(), 4)));
        setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));

        layer = new JLayeredPane();
        container = new Box(BoxLayout.X_AXIS);
        bestScorePanel = new BestScorePanel(model, this);
        layer.add(container, new Integer(1));
        layer.add(bestScorePanel, new Integer(2));
        container.setBounds(0, 0, Main.WIDTH_MAIN - 12, Main.HEIGHT_MAIN - 12);
        add(layer);

        moveDownTimer = new Timer(27, new MoveDownActionListener());
//        moveLeftTimer = new Timer(40, new MoveLeftActionListener());
//        moveRightTimer = new Timer(40, new MoveRightActionListener());

        drawNextPanel();
        drawCenterPanel();
        drawStatPanel();
        setKeyBinding();
        initSounds();
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="init Sounds">
    private void initSounds() {
        try {
            backgroundSound = AudioSystem.getClip();
            bounceSound = AudioSystem.getClip();
            stuckSound = AudioSystem.getClip();
            moveSound = AudioSystem.getClip();
            lvlUpSound = AudioSystem.getClip();
            blowSound = AudioSystem.getClip();
            placeSound = AudioSystem.getClip();
            loseSound = AudioSystem.getClip();
            tetrisSound = AudioSystem.getClip();

            //init background sound
            InputStream backgroundBufferedInput = new BufferedInputStream(getClass().getResourceAsStream("/Sounds/background.wav"));
            AudioInputStream backgroundInputStream = AudioSystem.getAudioInputStream(backgroundBufferedInput);
            backgroundSound.open(backgroundInputStream);
            FloatControl bgControl =
                    (FloatControl) backgroundSound.getControl(FloatControl.Type.MASTER_GAIN);
            bgControl.setValue(-5.0f);

            //init bounce sound
            InputStream bounceBufferedInput = new BufferedInputStream(getClass().getResourceAsStream("/Sounds/bounce.wav"));
            AudioInputStream bounceInputStream = AudioSystem.getAudioInputStream(bounceBufferedInput);
            AudioFormat format = bounceInputStream.getFormat();
            DataLine.Info info = new DataLine.Info(Clip.class, format);
            bounceSound = (Clip) AudioSystem.getLine(info);
            bounceSound.open(bounceInputStream);
            FloatControl bounceControl =
                    (FloatControl) bounceSound.getControl(FloatControl.Type.MASTER_GAIN);
            bounceControl.setValue(-6.5f);

            //init stuck sound
            InputStream stuckBufferedInput = new BufferedInputStream(getClass().getResourceAsStream("/Sounds/stuck.wav"));
            AudioInputStream stuckInputStream = AudioSystem.getAudioInputStream(stuckBufferedInput);
            stuckSound.open(stuckInputStream);
            FloatControl stuckControl =
                    (FloatControl) stuckSound.getControl(FloatControl.Type.MASTER_GAIN);
            stuckControl.setValue(-7.0f);//Reduce volume by 7 decibels.

            //init move sound
            InputStream moveBufferedInput = new BufferedInputStream(getClass().getResourceAsStream("/Sounds/move.wav"));
            AudioInputStream moveInputStream = AudioSystem.getAudioInputStream(moveBufferedInput);
            moveSound.open(moveInputStream);
            FloatControl moveControl =
                    (FloatControl) moveSound.getControl(FloatControl.Type.MASTER_GAIN);
            moveControl.setValue(-15.0f);

            //init lvl up sound
            InputStream lvlUpBufferedInput = new BufferedInputStream(getClass().getResourceAsStream("/Sounds/lvlUp.wav"));
            AudioInputStream lvlUpInputStream = AudioSystem.getAudioInputStream(lvlUpBufferedInput);
            lvlUpSound.open(lvlUpInputStream);
            FloatControl lvlUpContrl =
                    (FloatControl) lvlUpSound.getControl(FloatControl.Type.MASTER_GAIN);
            lvlUpContrl.setValue(6.0f);

            //init blow sound
            InputStream blowBufferedInput = new BufferedInputStream(getClass().getResourceAsStream("/Sounds/blow.wav"));
            AudioInputStream blowInputStream = AudioSystem.getAudioInputStream(blowBufferedInput);
            blowSound.open(blowInputStream);
            FloatControl blowControl =
                    (FloatControl) blowSound.getControl(FloatControl.Type.MASTER_GAIN);
            blowControl.setValue(-3.0f);


            //init place sound
            InputStream placeBufferedInput = new BufferedInputStream(getClass().getResourceAsStream("/Sounds/place.wav"));
            AudioInputStream placeInputStream = AudioSystem.getAudioInputStream(placeBufferedInput);
            placeSound.open(placeInputStream);
            FloatControl placeControl =
                    (FloatControl) placeSound.getControl(FloatControl.Type.MASTER_GAIN);
            placeControl.setValue(5.0f);

            //init place sound
            InputStream loseBufferedInput = new BufferedInputStream(getClass().getResourceAsStream("/Sounds/lose.wav"));
            AudioInputStream loseInputStream = AudioSystem.getAudioInputStream(loseBufferedInput);
            loseSound.open(loseInputStream);
            FloatControl loseControl =
                    (FloatControl) loseSound.getControl(FloatControl.Type.MASTER_GAIN);
            loseControl.setValue(3.0f);

            //init tetris sound
            InputStream tetrisBufferedInput = new BufferedInputStream(getClass().getResourceAsStream("/Sounds/tetris.wav"));
            AudioInputStream tetrisInputStream = AudioSystem.getAudioInputStream(tetrisBufferedInput);
            tetrisSound.open(tetrisInputStream);
            FloatControl tetrisControl =
                    (FloatControl) tetrisSound.getControl(FloatControl.Type.MASTER_GAIN);
            tetrisControl.setValue(4.0f);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="drawNextPanel">
    private void drawNextPanel() {
        nextBox = new Box(BoxLayout.Y_AXIS);
        nextBox.setPreferredSize(new Dimension(NEXT_PANEL_WIDTH, 5000));
        nextBox.setMaximumSize(new Dimension(NEXT_PANEL_WIDTH, 5000));
        nextBox.setMinimumSize(new Dimension(NEXT_PANEL_WIDTH, 5000));
        nextBox.setBackground(Template.getBackground());
        nextBox.setOpaque(true);
        container.add(nextBox);

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
        nextLabel = new JLabel(s("next"));
        holdLabel = new JLabel(s("hold"));
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
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="drawCenterPanel">
    private void drawCenterPanel() {
        center = new JPanel();
        center.setBackground(Template.getBackground());
        container.add(center);

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
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="draw Stat Panel">
    private void drawStatPanel() {
        statBox = new Box(BoxLayout.Y_AXIS);
        statBox.setOpaque(true);
        statBox.setPreferredSize(new Dimension(STAT_PANEL_WIDTH, 5000));
        statBox.setMaximumSize(new Dimension(STAT_PANEL_WIDTH, 5000));
        container.add(statBox);

        statBox.add(Box.createVerticalStrut(20));
        drawLvlBoxInStatBox();
        statBox.add(Box.createVerticalStrut(15));
        drawLineInStatBox();
        drawTimeInStatBox();
        drawScoreInStatBox();
        statBox.add(Box.createVerticalGlue());
        drawRestartAndPlayButton();
        statBox.add(Box.createVerticalStrut(30));
        if (!Main.isApplet()) {
            drawQuitButton();
            statBox.add(Box.createVerticalStrut(40));
        }
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
        lineBox.setPreferredSize(new Dimension(STAT_PANEL_WIDTH, 65));
        lineBox.setMaximumSize(new Dimension(STAT_PANEL_WIDTH, 65));
        statBox.add(lineBox);

        lineLabel = new JLabel(s("line"));
        lineLabel.setFont(font.deriveFont(24f));
        lineLabel.setForeground(Template.getForeground());

        line = new JLabel("0");
        line.setFont(font.deriveFont(24f));
        line.setForeground(Template.getForeground());

        lineBox.add(Box.createHorizontalStrut(17));
        lineBox.add(lineLabel);
        lineBox.add(Box.createHorizontalGlue());
        lineBox.add(line);
        lineBox.add(Box.createHorizontalStrut(17));
    }

    private void drawTimeInStatBox() {
        Box timeBox = new Box(BoxLayout.X_AXIS);
        timeBox.setPreferredSize(new Dimension(STAT_PANEL_WIDTH, 65));
        timeBox.setMaximumSize(new Dimension(STAT_PANEL_WIDTH, 65));
        statBox.add(timeBox);

        timeLabel = new JLabel(s("time"));
        timeLabel.setFont(font.deriveFont(24f));
        timeLabel.setForeground(Template.getForeground());

        time = new JLabel("0");
        time.setFont(font.deriveFont(24f));
        time.setHorizontalAlignment(SwingConstants.RIGHT);
        time.setForeground(Template.getForeground());

        timeBox.add(Box.createHorizontalStrut(17));
        timeBox.add(timeLabel);
        timeBox.add(Box.createHorizontalGlue());
        timeBox.add(time);
        timeBox.add(Box.createHorizontalStrut(17));
    }

    private void drawScoreInStatBox() {
        Box scoreBox = new Box(BoxLayout.X_AXIS);
        scoreBox.setPreferredSize(new Dimension(STAT_PANEL_WIDTH, 65));
        scoreBox.setMaximumSize(new Dimension(STAT_PANEL_WIDTH, 65));
        statBox.add(scoreBox);

        scoreLabel = new JLabel(s("score"));
        scoreLabel.setFont(font.deriveFont(24f));
        scoreLabel.setForeground(Template.getForeground());

        score = new JLabel("0");
        score.setFont(font.deriveFont(24f));
        score.setHorizontalAlignment(SwingConstants.RIGHT);
        score.setForeground(Template.getForeground());

        scoreBox.add(Box.createHorizontalStrut(17));
        scoreBox.add(scoreLabel);
        scoreBox.add(Box.createHorizontalGlue());
        scoreBox.add(score);
        scoreBox.add(Box.createHorizontalStrut(17));
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="draw buttons">
    private void drawRestartAndPlayButton() {
        Box tempBox = new Box(BoxLayout.X_AXIS);
        statBox.add(tempBox);

        restart = new KulButton(s("restart"));
        restart.setPreferredSize(new Dimension(140, 50));
        restart.setMaximumSize(new Dimension(140, 50));
        restart.setToolTipText(s("restartHint"));
        restart.setVisible(false);
        restart.setForeground(Color.WHITE);
        restart.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (SwingUtilities.isLeftMouseButton(e)) {
                    String[] options = {s("yes"), s("no")};
                    int choice = JOptionPane.showOptionDialog(MainPanel.this,
                            s("restartConfirm"), s("restart"), JOptionPane.YES_NO_OPTION,
                            JOptionPane.WARNING_MESSAGE, null, options, 0);
                    if (choice == JOptionPane.OK_OPTION) {
                        restart();
                    }
                }
            }
        });

        play = new KulButton(s("play"));
        play.setPreferredSize(new Dimension(140, 50));
        play.setMaximumSize(new Dimension(140, 50));
        play.setForeground(Color.WHITE);
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

        quit = new KulButton(s("quit"));
        quit.setPreferredSize(new Dimension(140, 50));
        quit.setMaximumSize(new Dimension(140, 50));
        quit.setForeground(Color.WHITE);
        quit.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (SwingUtilities.isLeftMouseButton(e) && !bestScorePanel.isActive()) {
                    String[] options = {s("yes"), s("no")};
                    int choice = JOptionPane.showOptionDialog(MainPanel.this,
                            s("quitConfirm"), s("quit"), JOptionPane.YES_NO_OPTION,
                            JOptionPane.WARNING_MESSAGE, null, options, 0);
                    if (choice == JOptionPane.OK_OPTION) {
                        main.Main.closeWindow();
                    }
//              bestScorePanel.slideDown();//asssssssssssssssssssssssssssssssssssssssssssssssss
//              System.out.println("line 527 MainPanel needs re-writing");
                }
            }
        });
        tempBox.add(quit);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="start - restart">
    private void start() {
        model.start();
        play.setVisible(false);
        restart.setVisible(true);

        backgroundSound.start();
        backgroundSound.loop(Clip.LOOP_CONTINUOUSLY);
    }

    private void restart() {
        model.restart();
        play.setVisible(true);
        restart.setVisible(false);

        backgroundSound.stop();
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="setKeyBinding">
    private void setKeyBinding() {
        //<editor-fold defaultstate="collapsed" desc="close window hot key">
        getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put(
                KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0, true), "exit");
        getActionMap().put("exit", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String[] options = {s("yes"), s("no")};
                int choice = JOptionPane.showOptionDialog(MainPanel.this,
                        s("quitConfirm"), s("quit"), JOptionPane.YES_NO_OPTION,
                        JOptionPane.WARNING_MESSAGE, null, options, 0);
                if (choice == JOptionPane.OK_OPTION) {
                    main.Main.closeWindow();
                }
            }
        });
        //</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="LEFT">
        this.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0, false), "startMoveLeft");
        this.getActionMap().put("startMoveLeft", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!bestScorePanel.isActive()) {
                    model.moveLeft();
                }
            }
        });
//        this.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0, true), "stopMoveLeft");
//        this.getActionMap().put("stopMoveLeft", new AbstractAction() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                if (!bestScorePanel.isActive()) {
//                    if (moveLeftTimer.isRunning()) {
//                        moveLeftTimer.stop();
//                    }
//                }
//            }
//        });
        //</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="RIGHT">
        this.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0, false), "startMoveRight");
        this.getActionMap().put("startMoveRight", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!bestScorePanel.isActive()) {
                   model.moveRight();
                }
            }
        });
//        this.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0, true), "stopMoveRight");
//        this.getActionMap().put("stopMoveRight", new AbstractAction() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                if (!bestScorePanel.isActive()) {
//                    if (moveRightTimer.isRunning()) {
//                        moveRightTimer.stop();
//                    }
//                }
//            }
//        });
        //</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="DOWN">
        this.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0, false), "startMoveDown");
        this.getActionMap().put("startMoveDown", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!bestScorePanel.isActive()) {
                    if (!moveDownTimer.isRunning()) {
                        moveDownTimer.start();
                    }
                }
            }
        });
        this.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0, true), "stopMoveDown");
        this.getActionMap().put("stopMoveDown", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!bestScorePanel.isActive()) {
                    if (moveDownTimer.isRunning()) {
                        moveDownTimer.stop();
                    }
                }
            }
        });
        //</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="DROP">
        this.getInputMap().put(KeyStroke.getKeyStroke(' '), "drop");
        this.getInputMap().put(KeyStroke.getKeyStroke("C"), "drop");
        this.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "drop");
        this.getActionMap().put("drop", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!bestScorePanel.isActive()) {
                    model.drop();
                }
            }
        });
        //</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="ROTATE">
        //ROTATE LEFT
        this.getInputMap().put(KeyStroke.getKeyStroke("UP"), "rotateLeft");
        this.getInputMap().put(KeyStroke.getKeyStroke("Z"), "rotateLeft");
        this.getActionMap().put("rotateLeft", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!bestScorePanel.isActive()) {
                    model.rotateLeft();
                }
            }
        });

        //ROTATE RIGHT
        this.getInputMap().put(KeyStroke.getKeyStroke("X"), "rotateRight");
        this.getActionMap().put("rotateRight", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!bestScorePanel.isActive()) {
                    model.rotateRight();
                }
            }
        });
        //</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="start - restart">
        //START NEW
        this.getInputMap().put(KeyStroke.getKeyStroke("S"), "start");
        this.getActionMap().put("start", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (play.isVisible()) {
                    if (!bestScorePanel.isActive()) {
                        start();
                    }
                }
            }
        });

        //RE-START
        this.getInputMap().put(KeyStroke.getKeyStroke("R"), "restart");
        this.getActionMap().put("restart", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (restart.isVisible()) {
                    if (!bestScorePanel.isActive()) {
                        restart();
                    }
                }
            }
        });
        //</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="HOLD">
        //HOLD
        this.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_CONTROL, InputEvent.CTRL_DOWN_MASK), "hold");
        this.getActionMap().put("hold", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!bestScorePanel.isActive()) {
                    model.hold();
                }
            }
        });
        //</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="PAUSE">
        //PAUSE
        this.getInputMap().put(KeyStroke.getKeyStroke('P',
                InputEvent.ALT_DOWN_MASK + InputEvent.SHIFT_DOWN_MASK), "pause");
        this.getActionMap().put("pause", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!bestScorePanel.isActive()) {
                    model.pause();
                }
            }
        });
        //</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="SPEED UP - SPEED DOWN">
        //SPEED UP
        this.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_UP,
                InputEvent.CTRL_DOWN_MASK + InputEvent.SHIFT_DOWN_MASK), "speedUp");
        this.getActionMap().put("speedUp", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!bestScorePanel.isActive()) {
                    model.speedUp();
                }
            }
        });

        //SPEED DOWN
        this.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN,
                InputEvent.CTRL_DOWN_MASK + InputEvent.SHIFT_DOWN_MASK), "speedDown");
        this.getActionMap().put("speedDown", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!bestScorePanel.isActive()) {
                    model.speedDown();
                }
            }
        });        //</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="MUTE">
        this.getInputMap().put(KeyStroke.getKeyStroke('M', 0), "mute");
        this.getActionMap().put("mute", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                isMute = !isMute;
                if (isMute) {
                    backgroundSound.stop();
                } else {
                    backgroundSound.loop(-1);
                }
            }
        });
        //</editor-fold>
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="update()">
    @Override
    public void update(Observable obs, Object o) {
        if (o instanceof String) {
            String s = (String) o;
            switch (s) {
                case "Refresh":
                    repaint();
                    break;
                case "Bounce":
                    bounceTimer.start();
                    playBounceSound();
                    break;
                case "Move":
                    playMoveSound();
                    break;
                case "Stuck":
                    playStuckSound();
                    break;
                case "Place":
                    playPlaceSound();
                    break;
                case "Lose":
                    backgroundSound.stop();
                    playLoseSound();
                    bestScorePanel.slideDown();
                    break;
                case "Blow":
                    playBlowSound();
                    break;
                case "Tetris":
                    playTetrisSound();
                    break;
            }

        } else if (o instanceof Integer) {// if o is time
            String timeString = (Integer) o + "";
            time.setText(timeString);

        } else if (o instanceof Byte) {
            String lvlString = (Byte) o + "";
            lvl.setText(lvlString);
            playLvlUpSound();

        } else if (o instanceof Short) {
            String lineString = (Short) o + "";
            line.setText(lineString);

        } else if (o instanceof Long) {
            String scoreString = (Long) o + "";
            score.setText(scoreString);
        }
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Bounce">
    private class TimeListenerToBounce implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            bounceCount++;
            if (bounceCount > 8) {
                bounceCount = 0;
                bounceTimer.stop();
            } else {
                if (bounceCount <= 4) {
                    sprLayoutCenter.putConstraint(SpringLayout.NORTH, subCenterPanel, bounceCount * 2, SpringLayout.NORTH, center);
                    sprLayoutCenter.putConstraint(SpringLayout.SOUTH, subCenterPanel, -(8 - bounceCount * 2), SpringLayout.SOUTH, center);
                    center.revalidate();
                    center.repaint();
                } else {
                    sprLayoutCenter.putConstraint(SpringLayout.NORTH, subCenterPanel, 16 - bounceCount * 2, SpringLayout.NORTH, center);
                    sprLayoutCenter.putConstraint(SpringLayout.SOUTH, subCenterPanel, -(bounceCount * 2 - 8), SpringLayout.SOUTH, center);
                    center.revalidate();
                    center.repaint();
                }
            }
        }
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="play Sounds">
    private void playStuckSound() {
        if (isMute) {
            return;
        }
        stuckSound.setFramePosition(0);
        stuckSound.start();
    }

    private void playMoveSound() {
        if (isMute) {
            return;
        }
        moveSound.setFramePosition(0);
        moveSound.start();
    }

    private void playBlowSound() {
        if (isMute) {
            return;
        }
        blowSound.setFramePosition(0);
        blowSound.start();
    }

    private void playLvlUpSound() {
        if (isMute) {
            return;
        }
        lvlUpSound.setFramePosition(0);
        lvlUpSound.start();
    }

    private void playBounceSound() {
        if (isMute) {
            return;
        }
        bounceSound.setFramePosition(0);
        bounceSound.start();
    }

    private void playPlaceSound() {
        if (isMute) {
            return;
        }
        placeSound.setFramePosition(0);
        placeSound.start();
    }

    private void playLoseSound() {
        if (isMute) {
            return;
        }
        loseSound.setFramePosition(0);
        loseSound.start();
    }

    private void playTetrisSound() {
        if (isMute) {
            return;
        }
        tetrisSound.setFramePosition(0);
        tetrisSound.start();
    }
    //</editor-fold>  

    //<editor-fold defaultstate="collapsed" desc="get string from resource bundle">
    private String s(String key) {
        return language.getString(key);
    }
    //</editor-fold>  

    //<editor-fold defaultstate="collapsed" desc="MoveXXXActionListener">
    private class MoveDownActionListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            model.moveDown(true);
        }
    }

//    private class MoveLeftActionListener implements ActionListener {
//
//        @Override
//        public void actionPerformed(ActionEvent e) {
//            model.moveLeft();
//        }
//    }
//
//    private class MoveRightActionListener implements ActionListener {
//
//        @Override
//        public void actionPerformed(ActionEvent e) {
//            model.moveRight();
//        }
//    }
    //</editor-fold>
}
