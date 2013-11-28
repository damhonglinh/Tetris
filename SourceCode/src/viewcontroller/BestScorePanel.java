package viewcontroller;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ResourceBundle;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.border.LineBorder;
import main.Main;
import model.Model;
import model.Score;

/**
 *
 * @author Dam Linh
 */
public class BestScorePanel extends JPanel {

    private Model model;
    private MainPanel parentPanel;
    private CardLayout card = new CardLayout();
    private Box resultBox;
    private Box bestScoresBox;
    private ResourceBundle language;
    private Font f = Template.getFont().deriveFont(30f);
    private JTextField name;
    private KulButton ok;
    private boolean isActive;
    private JLabel score;
    private JLabel line;
    private JLabel lvl;
    private JLabel time;
    private Box nameBox = new Box(BoxLayout.X_AXIS);

    public BestScorePanel(Model model, MainPanel parentPanel) {
        this.model = model;
        this.parentPanel = parentPanel;
        this.language = model.getLanguage();
        this.setLayout(card);
        this.setBackground(Template.getBackground());

        resultBox = new Box(BoxLayout.Y_AXIS);
        bestScoresBox = new Box(BoxLayout.Y_AXIS);

        add(bestScoresBox, "bestScoresBox");
        add(resultBox, "resultBox");

        resultBox.add(Box.createVerticalStrut(15));
        resultBox.add(drawTitle());
        resultBox.add(Box.createVerticalStrut(100));
        resultBox.add(drawScore());
        resultBox.add(Box.createVerticalStrut(50));
        resultBox.add(drawLine());
        resultBox.add(Box.createVerticalStrut(25));
        resultBox.add(drawTime());
        resultBox.add(Box.createVerticalStrut(25));
        resultBox.add(drawLevel());
        resultBox.add(Box.createVerticalStrut(55));
        resultBox.add(nameBox);
        if (model.getBestScores() != null) {
            drawName();
        }
        resultBox.add(Box.createVerticalGlue());
        drawButton();
        resultBox.add(Box.createVerticalStrut(50));
    }

    //<editor-fold defaultstate="collapsed" desc="drawTitle">
    private Box drawTitle() {
        JLabel title = new JLabel("Game Over");
        title.setFont(f.deriveFont(90f));
        title.setForeground(Color.WHITE);
        Box titleBox = new Box(BoxLayout.X_AXIS);
        titleBox.add(Box.createHorizontalGlue());
        titleBox.add(title);
        titleBox.add(Box.createHorizontalGlue());

        return titleBox;
    }
    //</editor-fold>  

    //<editor-fold defaultstate="collapsed" desc="drawScore">
    private Box drawScore() {
        JLabel scoreTitle = new JLabel(s("yourScore"));
        scoreTitle.setFont(f.deriveFont(40f));
        scoreTitle.setForeground(Color.WHITE);

        score = new JLabel(model.getScore() + "");
        score.setFont(Template.getFontAlter().deriveFont(40f));
        score.setForeground(Template.getForegroundContrast());

        Box scoreBox = new Box(BoxLayout.X_AXIS);
        scoreBox.add(Box.createHorizontalGlue());
        scoreBox.add(scoreTitle);
        scoreBox.add(Box.createRigidArea(new Dimension(20, 5)));
        scoreBox.add(score);
        scoreBox.add(Box.createHorizontalGlue());

        return scoreBox;
    }
    //</editor-fold>  

    //<editor-fold defaultstate="collapsed" desc="drawLine">
    private Box drawLine() {
        JLabel lineTitle = new JLabel(s("line"));
        lineTitle.setFont(f.deriveFont(30f));
        lineTitle.setForeground(Color.WHITE);

        line = new JLabel(model.getLine() + "");
        line.setFont(Template.getFontAlter().deriveFont(30f));
        line.setForeground(Template.getForegroundContrast());

        Box lineBox = new Box(BoxLayout.X_AXIS);
        lineBox.add(Box.createHorizontalGlue());
        lineBox.add(lineTitle);
        lineBox.add(Box.createRigidArea(new Dimension(20, 5)));
        lineBox.add(line);
        lineBox.add(Box.createHorizontalGlue());

        return lineBox;
    }
    //</editor-fold>  

    //<editor-fold defaultstate="collapsed" desc="drawTime">
    private Box drawTime() {
        JLabel timeTitle = new JLabel(s("time"));
        timeTitle.setFont(f.deriveFont(25f));
        timeTitle.setForeground(Color.WHITE);

        time = new JLabel(model.getScore() + "");
        time.setFont(Template.getFontAlter().deriveFont(25f));
        time.setForeground(Template.getForegroundContrast());

        Box timeBox = new Box(BoxLayout.X_AXIS);
        timeBox.add(Box.createHorizontalGlue());
        timeBox.add(timeTitle);
        timeBox.add(Box.createRigidArea(new Dimension(20, 5)));
        timeBox.add(time);
        timeBox.add(Box.createHorizontalGlue());

        return timeBox;
    }
    //</editor-fold>  

    //<editor-fold defaultstate="collapsed" desc="drawLevel">
    private Box drawLevel() {
        JLabel lvlTitle = new JLabel("Level:");
        lvlTitle.setFont(f.deriveFont(25f));
        lvlTitle.setForeground(Color.WHITE);

        lvl = new JLabel(model.getScore() + "");
        lvl.setFont(Template.getFontAlter().deriveFont(25f));
        lvl.setForeground(Template.getForegroundContrast());

        Box lvlBox = new Box(BoxLayout.X_AXIS);
        lvlBox.add(Box.createHorizontalGlue());
        lvlBox.add(lvlTitle);
        lvlBox.add(Box.createRigidArea(new Dimension(20, 5)));
        lvlBox.add(lvl);
        lvlBox.add(Box.createHorizontalGlue());

        return lvlBox;
    }
    //</editor-fold>  

    //<editor-fold defaultstate="collapsed" desc="drawName">
    private void drawName() {
        JLabel nameTitle = new JLabel(s("name"));
        nameTitle.setFont(f.deriveFont(30f));
        nameTitle.setForeground(Color.WHITE);

        name = new JTextField(10);
        name.setFont(f.deriveFont(22f));
        name.setPreferredSize(new Dimension(130, 30));
        name.setMaximumSize(new Dimension(130, 30));

        nameBox.removeAll();
        nameBox.add(nameTitle);
        nameBox.add(Box.createRigidArea(new Dimension(20, 5)));
        nameBox.add(name);
    }
    //</editor-fold>  

    //<editor-fold defaultstate="collapsed" desc="drawButton">
    private void drawButton() {
        ok = new KulButton("Ok");
        ok.setPreferredSize(new Dimension(125, 40));
        ok.setMaximumSize(new Dimension(125, 40));
        ok.setForeground(Color.WHITE);
        ok.setFont(f.deriveFont(26f));

        Box buttonBox = new Box(BoxLayout.X_AXIS);
        buttonBox.add(Box.createHorizontalGlue());
        buttonBox.add(ok);
        buttonBox.add(Box.createHorizontalGlue());

        resultBox.add(buttonBox);

        ok.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (SwingUtilities.isLeftMouseButton(e)) {
                    if (model.getBestScores() == null) {
                        slideUp();
                    } else {
                        if (validateName()) {
                            Score s = new Score("0", name.getText(), model.getScore());
                            model.addScore(s);
                            showBestScores();
                        } else {
                        }
                    }
                }
            }
        });
    }
    //</editor-fold>  

    //<editor-fold defaultstate="collapsed" desc="slideUp/slideDown">
    protected void slideUp() {
        isActive = false;

        int x1 = 0;
        int x2 = 0;
        int y1 = 0;
        int y2 = -Main.HEIGHT_MAIN;
        int w = Main.WIDTH_MAIN;
        int h = Main.HEIGHT_MAIN;
        new KulAnimator(parentPanel, this, x1, x2, y1, y2, w, h).slideVertical();
    }

    protected void slideDown() {
        isActive = true;
        card.show(this, "resultBox");

        line.setText(model.getLine() + "");
        lvl.setText(model.getLevel() + "");
        time.setText(model.getTime() + "");
        score.setText(model.getScore() + "");
        if (model.getBestScores() != null) {
            drawName();
            name.setText(System.getProperty("user.name"));
        } else {
            nameBox.removeAll();
        }

        int x1 = 0;
        int x2 = 0;
        int y1 = -Main.HEIGHT_MAIN;
        int y2 = 0;
        int w = Main.WIDTH_MAIN;
        int h = Main.HEIGHT_MAIN;
        new KulAnimator(parentPanel, this, x1, x2, y1, y2, w, h).slideVertical();
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="showBestScores">
    private void showBestScores() {
        bestScoresBox.removeAll();

        JLabel title = new JLabel(s("highScore"));
        title.setFont(f.deriveFont(80f));
        title.setForeground(Color.WHITE);
        Box titleBox = new Box(BoxLayout.X_AXIS);
        titleBox.add(Box.createHorizontalGlue());
        titleBox.add(title);
        titleBox.add(Box.createHorizontalGlue());

        bestScoresBox.add(Box.createVerticalStrut(15));
        bestScoresBox.add(titleBox);
        bestScoresBox.add(Box.createVerticalStrut(50));

        for (int i = 0; i < model.getBestScores().size(); i++) {
            Score scoreInner = model.getBestScores().get(i);

            Box lineBox = new Box(BoxLayout.X_AXIS);
            lineBox.add(Box.createRigidArea(new Dimension(170, 1)));

            JLabel nameLabel = new JLabel(scoreInner.getName());
            JLabel scoreLabel = new JLabel(scoreInner.getScore() + "");
            scoreLabel.setFont(f);
            nameLabel.setFont(f);

            if (scoreInner.getId().length() == 1) {
                nameLabel.setForeground(Template.getContrast());
                scoreLabel.setForeground(Template.getContrast());
            } else {
                nameLabel.setForeground(Color.WHITE);
                scoreLabel.setForeground(Color.WHITE);
            }

            lineBox.add(nameLabel);
            lineBox.add(Box.createHorizontalGlue());
            lineBox.add(scoreLabel);
            lineBox.add(Box.createHorizontalStrut(250));

            bestScoresBox.add(lineBox);
            bestScoresBox.add(Box.createRigidArea(new Dimension(1, 15)));
        }

        bestScoresBox.add(Box.createVerticalGlue());
        bestScoresBox.add(drawButtonBestScores());
        bestScoresBox.add(Box.createVerticalStrut(30));

        card.show(this, "bestScoresBox");
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="drawButtonBestScores">
    private Box drawButtonBestScores() {
        KulButton ok1 = new KulButton("Ok");
        ok1.setPreferredSize(new Dimension(125, 40));
        ok1.setMaximumSize(new Dimension(125, 40));
        ok1.setForeground(Color.WHITE);
        ok1.setFont(f.deriveFont(26f));

        Box buttonBox = new Box(BoxLayout.X_AXIS);
        buttonBox.add(Box.createHorizontalGlue());
        buttonBox.add(ok1);
        buttonBox.add(Box.createHorizontalGlue());

        ok1.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (SwingUtilities.isLeftMouseButton(e)) {
                    slideUp();
                }
            }
        });
        return buttonBox;
    }
    //</editor-fold>  

    public boolean isActive() {
        return isActive;
    }

    //<editor-fold defaultstate="collapsed" desc="validateName">
    private boolean validateName() {
        String nameString = name.getText();

        if (nameString.matches("^[a-zA-Z0-9_ ]{1,12}$")) {
            return true;
        } else {
            return false;
        }
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="get string from resource bundle">
    private String s(String key) {
        return language.getString(key);
    }
    //</editor-fold>  
}
