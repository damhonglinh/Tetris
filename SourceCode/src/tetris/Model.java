package tetris;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.Observable;
import java.util.Random;
import javax.swing.Timer;

/**
 *
 * @author Dam Linh
 */
public class Model extends Observable {

    private Token[][] tokens;
    private TokenNext[][] nextTokens1;
    private TokenNext[][] nextTokens2;
    private TokenNext[][] nextTokens3;
    private TokenNext[][] holdTokens;
    private int nextToken1Type;
    private int nextToken2Type;
    private int nextToken3Type;
    private int holdTokensType;
    /**
     * currentTokens and directingTokens and nextTokens1Position are just the
     * position of the current tokens it has 4 rows and 2 columns. The 0th
     * column is x position, 1st one is y position. Each row is a token.
     */
    private int[][] nextTokens3Position;
    private int[][] nextTokens2Position;
    private int[][] nextTokens1Position;
    private int[][] holdTokensPosition;
    private int[][] directingTokens;
    private int[][] currentTokens;
    private int currentType;
    private Timer timeCount;
    private Timer timeSpeed;
    private int time;
    private short speed;
    private short line;
    private byte level;
    private int rotateCount;
    private Rotater rotater;
    private boolean isPaused;
    private boolean holdable;
    private boolean freezing;
    private boolean isGameOn;
    private Random random = new Random();

    public Model() {
        initializeTokens();
        speed = 1000;
        level = 1;

        timeSpeed = new Timer(speed, new TimeSpeedListener());
        timeCount = new Timer(1000, new TimeCountListener());
    }

    private void initializeTokens() {
        tokens = new Token[21][10];
        for (int i = 0; i < tokens.length; i++) {
            for (int j = 0; j < tokens[i].length; j++) {
                tokens[i][j] = new Token();
            }
        }

        nextTokens1 = new TokenNext[2][4];
        nextTokens2 = new TokenNext[2][4];
        nextTokens3 = new TokenNext[2][4];
        holdTokens = new TokenNext[2][4];
        for (int i = 0; i < nextTokens1.length; i++) {
            for (int j = 0; j < nextTokens1[i].length; j++) {
                nextTokens1[i][j] = new TokenNext();
                nextTokens2[i][j] = new TokenNext();
                nextTokens3[i][j] = new TokenNext();
                holdTokens[i][j] = new TokenNext();
            }
        }
        currentTokens = new int[4][2];
        directingTokens = new int[4][2];
        nextTokens3Position = new int[4][2];
        nextTokens2Position = new int[4][2];
        nextTokens1Position = new int[4][2];
        holdTokensPosition = new int[4][2];
    }

    public void restart() {
        for (int i = 0; i < tokens.length; i++) {
            for (int j = 0; j < tokens[i].length; j++) {
                tokens[i][j].setType(0);
                tokens[i][j].setFrozen(false);
            }
        }

        for (int i = 0; i < nextTokens1.length; i++) {
            for (int j = 0; j < nextTokens1[i].length; j++) {
                nextTokens1[i][j].setType(0);
                nextTokens2[i][j].setType(0);
                nextTokens3[i][j].setType(0);
                holdTokens[i][j].setType(0);
            }
        }

        timeCount.stop();
        timeSpeed.stop();
        speed = 800;
        line = 0;
        level = 1;
        time = 0;
        rotateCount = 0;
        isPaused = false;
        isGameOn = false;
        freezing = false;
        currentTokens = new int[4][2];
        directingTokens = new int[4][2];
        nextTokens3Position = new int[4][2];
        nextTokens2Position = new int[4][2];
        nextTokens1Position = new int[4][2];
        holdTokensPosition = new int[4][2];
        timeSpeed.setDelay(speed);

        setChanged();
        notifyObservers(time);
        setChanged();
        notifyObservers(line);
        refresh();
        setChanged();
        notifyObservers(level);
    }

    public void start() {
        nextToken1Type = random.nextInt(7) + 1;
        nextToken2Type = random.nextInt(7) + 1;
        holdTokensType = 0;
        randomNextType3();
        setNextTokens(nextToken1Type, nextTokens1Position, nextTokens1);
        setNextTokens(nextToken2Type, nextTokens2Position, nextTokens2);
        setNextTokens(nextToken3Type, nextTokens3Position, nextTokens3);

        isGameOn = true;
        nextMove(0);
        timeCount.start();
        timeSpeed.start();
    }

    public void pause() {
        if (!isGameOn) {
            return;
        }
        if (!isPaused) {
            timeSpeed.stop();
            timeCount.stop();
            isPaused = true;
        } else {
            timeSpeed.start();
            timeCount.start();
            isPaused = false;
        }
    }

    private void nextMove(int type) {
        if (type == 0) {
            holdable = true;
            generateNextTokens();
        } else {
            currentType = type;
        }
        //set currentTokens
        switch (currentType) {
            case 1:// the I token
                setPositionForCase1(currentTokens, 3);
                rotater = new RotaterI(tokens);
                break;
            case 2:// the J token
                setPositionForCase2(currentTokens, 3);
                rotater = new RotaterJ(tokens);
                break;
            case 3:// the L token
                setPositionForCase3(currentTokens, 3);
                rotater = new RotaterL(tokens);
                break;
            case 4:// the O token
                setPositionForCase4(currentTokens, 3);
                rotater = new RotaterO(tokens);
                break;
            case 5:// the S token
                setPositionForCase5(currentTokens, 3);
                rotater = new RotaterS(tokens);
                break;
            case 6:// the T token
                setPositionForCase6(currentTokens, 3);
                rotater = new RotaterT(tokens);
                break;
            default:// the Z token
                setPositionForCase7(currentTokens, 3);
                rotater = new RotaterZ(tokens);
        }

        if (checkIsLose()) {
            lose();
            return;
        }

        //set type for the currentToken
        setCurrentTokens(currentType);
        setDirectingTokens();
        rotateCount = 0;
        refresh();
    }

    //set nextTokens1Position
    private void generateNextTokens() {
        currentType = nextToken1Type;
        nextToken1Type = nextToken2Type;
        nextToken2Type = nextToken3Type;
        randomNextType3();

        setNextTokens(nextToken1Type, nextTokens1Position, nextTokens1);
        setNextTokens(nextToken2Type, nextTokens2Position, nextTokens2);
        setNextTokens(nextToken3Type, nextTokens3Position, nextTokens3);
    }

    private void randomNextType3() {
        boolean flag = true;
        while (flag) {//avoid 3 identical nextTokens
            nextToken3Type = random.nextInt(7) + 1;
            if (nextToken3Type != nextToken2Type || nextToken3Type != nextToken1Type) {
                flag = false;
            }
        }
    }

    private void setNextTokens(int nextType, int[][] nextTokensPosition, TokenNext[][] nextTokens) {
        switch (nextType) {
            case 1:// the I token
                setPositionForCase1(nextTokensPosition, 0);
                break;
            case 2:// the J token
                setPositionForCase2(nextTokensPosition, 0);
                break;
            case 3:// the L token
                setPositionForCase3(nextTokensPosition, 0);
                break;
            case 4:// the O token
                setPositionForCase4(nextTokensPosition, 0);
                break;
            case 5:// the S token
                setPositionForCase5(nextTokensPosition, 0);
                break;
            case 6:// the T token
                setPositionForCase6(nextTokensPosition, 0);
                break;
            default:// the Z token
                setPositionForCase7(nextTokensPosition, 0);
        }

        //reset nextTokens
        for (int i = 0; i < nextTokens.length; i++) {
            for (int j = 0; j < nextTokens[i].length; j++) {
                nextTokens[i][j].setType(0);
            }
        }
        for (int i = 0; i < nextTokensPosition.length; i++) {
            int x = nextTokensPosition[i][0];
            int y = nextTokensPosition[i][1];
            nextTokens[y][x].setType(nextType);
        }
    }

    private void setPositionForCase1(int[][] position, int padding) {
        position[0][0] = 0 + padding;
        position[0][1] = 0;
        position[1][0] = 1 + padding;
        position[1][1] = 0;
        position[2][0] = 2 + padding;
        position[2][1] = 0;
        position[3][0] = 3 + padding;
        position[3][1] = 0;
    }

    private void setPositionForCase2(int[][] position, int padding) {
        position[0][0] = 0 + padding;
        position[0][1] = 0;
        position[1][0] = 0 + padding;
        position[1][1] = 1;
        position[2][0] = 1 + padding;
        position[2][1] = 1;
        position[3][0] = 2 + padding;
        position[3][1] = 1;
    }

    private void setPositionForCase3(int[][] position, int padding) {
        position[0][0] = 2 + padding;
        position[0][1] = 0;
        position[1][0] = 0 + padding;
        position[1][1] = 1;
        position[2][0] = 1 + padding;
        position[2][1] = 1;
        position[3][0] = 2 + padding;
        position[3][1] = 1;
    }

    private void setPositionForCase4(int[][] position, int padding) {
        position[0][0] = 1 + padding;
        position[0][1] = 0;
        position[1][0] = 2 + padding;
        position[1][1] = 0;
        position[2][0] = 1 + padding;
        position[2][1] = 1;
        position[3][0] = 2 + padding;
        position[3][1] = 1;
    }

    private void setPositionForCase5(int[][] position, int padding) {
        position[0][0] = 1 + padding;
        position[0][1] = 0;
        position[1][0] = 2 + padding;
        position[1][1] = 0;
        position[2][0] = 0 + padding;
        position[2][1] = 1;
        position[3][0] = 1 + padding;
        position[3][1] = 1;
    }

    private void setPositionForCase6(int[][] position, int padding) {
        position[0][0] = 1 + padding;
        position[0][1] = 0;
        position[1][0] = 0 + padding;
        position[1][1] = 1;
        position[2][0] = 1 + padding;
        position[2][1] = 1;
        position[3][0] = 2 + padding;
        position[3][1] = 1;
    }

    private void setPositionForCase7(int[][] position, int padding) {
        position[0][0] = 0 + padding;
        position[0][1] = 0;
        position[1][0] = 1 + padding;
        position[1][1] = 0;
        position[2][0] = 1 + padding;
        position[2][1] = 1;
        position[3][0] = 2 + padding;
        position[3][1] = 1;
    }

    private void setCurrentTokens(int type) {
        for (int i = 0; i < currentTokens.length; i++) {
            int x = currentTokens[i][0];
            int y = currentTokens[i][1];
            tokens[y][x].setType(type);
        }
    }

    private void setDirectingTokens() {
        for (int i = 0; i < 4; i++) {//re-setType(0) for tokens at directTokens
            if (!tokens[directingTokens[i][1]][directingTokens[i][0]].isFrozen()) {
                tokens[directingTokens[i][1]][directingTokens[i][0]].setType(0);
            }
        }

        for (int i = 0; i < tokens.length; i++) {
            if ((currentTokens[0][1] + i == tokens.length - 1)
                    || (currentTokens[1][1] + i == tokens.length - 1)
                    || (currentTokens[2][1] + i == tokens.length - 1)
                    || (currentTokens[3][1] + i == tokens.length - 1)
                    || tokens[currentTokens[0][1] + i + 1][currentTokens[0][0]].isFrozen()
                    || tokens[currentTokens[1][1] + i + 1][currentTokens[1][0]].isFrozen()
                    || tokens[currentTokens[2][1] + i + 1][currentTokens[2][0]].isFrozen()
                    || tokens[currentTokens[3][1] + i + 1][currentTokens[3][0]].isFrozen()) {
                directingTokens[0][0] = currentTokens[0][0];
                directingTokens[0][1] = currentTokens[0][1] + i;
                directingTokens[1][0] = currentTokens[1][0];
                directingTokens[1][1] = currentTokens[1][1] + i;
                directingTokens[2][0] = currentTokens[2][0];
                directingTokens[2][1] = currentTokens[2][1] + i;
                directingTokens[3][0] = currentTokens[3][0];
                directingTokens[3][1] = currentTokens[3][1] + i;
                break;
            }
        }

        for (int i = 0; i < 4; i++) {
            tokens[directingTokens[i][1]][directingTokens[i][0]].setType(-currentType);
        }
        for (int i = 0; i < 4; i++) {
            tokens[currentTokens[i][1]][currentTokens[i][0]].setType(currentType);
        }

        refresh();
    }

    public void moveDown() {
        if (freezing || !isGameOn) {
            return;
        }
        if (!checkMovable("DOWN")) {
            for (int i = 0; i < currentTokens.length; i++) {
                int x = currentTokens[i][0];
                int y = currentTokens[i][1];
                tokens[y][x].setFrozen(true);
            }

            if (!checkBlowLines()) {
                nextMove(0);
            }
            return;
        }
        setCurrentTokens(0);

        for (int i = 0; i < currentTokens.length; i++) {
            int x = currentTokens[i][0];
            int y = currentTokens[i][1] + 1;
            tokens[y][x].setType(currentType);
            currentTokens[i][1] = y;// move position down 1 point
        }
        refresh();
    }

    public void moveLeft() {
        if (freezing || !isGameOn) {
            return;
        }
        if (!checkMovable("LEFT")) {
            return;
        }
        setCurrentTokens(0);

        for (int i = 0; i < currentTokens.length; i++) {
            int x = currentTokens[i][0] - 1;
            int y = currentTokens[i][1];
            tokens[y][x].setType(currentType);
            currentTokens[i][0] = x;// move position left 1 point
        }

        setDirectingTokens();
        refresh();
    }

    public void moveRight() {
        if (freezing || !isGameOn) {
            return;
        }
        if (!checkMovable("RIGHT")) {
            return;
        }
        setCurrentTokens(0);

        for (int i = 0; i < currentTokens.length; i++) {
            int x = currentTokens[i][0] + 1;
            int y = currentTokens[i][1];
            tokens[y][x].setType(currentType);
            currentTokens[i][0] = x;// move position right 1 point
        }

        setDirectingTokens();
        refresh();
    }

    public void speedUp() {
        if (speed > 100) {
            speed = (short) (speed - 100);
            timeSpeed.setDelay(speed);
        }
    }

    public void speedDown() {
        if (speed < 1200) {
            speed = (short) (speed + 100);
            timeSpeed.setDelay(speed);
        }
    }

    public void drop() {
        if (freezing || !isGameOn) {
            return;
        }
        for (int i = 0; i < 4; i++) {
            tokens[currentTokens[i][1]][currentTokens[i][0]].setType(0);
            currentTokens[i][1] = directingTokens[i][1];
            currentTokens[i][0] = directingTokens[i][0];
        }
        //intentionally split into 2 loops
        //to prevent overlap of currentTokens and directTokes
        for (int i = 0; i < 4; i++) {
            tokens[directingTokens[i][1]][directingTokens[i][0]].setType(currentType);
            tokens[directingTokens[i][1]][directingTokens[i][0]].setFrozen(true);
        }
        refresh();
        bounce();

        if (!checkBlowLines()) {
            nextMove(0);
        }
    }

    public void rotateRight() {
        if (freezing || !isGameOn) {
            return;
        }
        setCurrentTokens(0);
        if (rotater.rotateRight(tokens, currentTokens, rotateCount)) {
            rotateCount++;
        }
        setCurrentTokens(currentType);

        setDirectingTokens();
        refresh();
    }

    public void rotateLeft() {
        if (freezing || !isGameOn) {
            return;
        }
        setCurrentTokens(0);
        if (rotater.rotateLeft(tokens, currentTokens, rotateCount)) {
            if (rotateCount <= 0) {// prevent negative module
                rotateCount = rotateCount + 4;
            }
            rotateCount--;
        }
        setCurrentTokens(currentType);

        setDirectingTokens();
        refresh();
    }

    public void levelUp() {
        level++;
        setChanged();
        notifyObservers(level);
        speedUp();
    }

    private boolean checkMovable(String dir) {
        switch (dir) {
            case "LEFT":
                for (int i = 0; i < currentTokens.length; i++) {
                    int x = currentTokens[i][0];
                    int y = currentTokens[i][1];
                    if (x == 0 || tokens[y][x - 1].isFrozen()) {
                        return false;
                    }
                }
                return true;
            case "RIGHT":
                for (int i = 0; i < currentTokens.length; i++) {
                    int x = currentTokens[i][0];
                    int y = currentTokens[i][1];
                    if (x == 9 || tokens[y][x + 1].isFrozen()) {
                        return false;
                    }
                }
                return true;
            default:
                for (int i = 0; i < currentTokens.length; i++) {
                    int x = currentTokens[i][0];
                    int y = currentTokens[i][1];
                    if (y == tokens.length - 1 || tokens[y + 1][x].isFrozen()) {
                        return false;
                    }
                }
                return true;
        }
    }

    public void hold() {
        if (!isGameOn || !holdable) {
            return;
        }
        setCurrentTokens(0);

        int typeTemp = holdTokensType;
        holdTokensType = currentType;
        //set position of holdTokens
        switch (holdTokensType) {
            case 1:// the I token
                setPositionForCase1(holdTokensPosition, 0);
                break;
            case 2:// the J token
                setPositionForCase2(holdTokensPosition, 0);
                break;
            case 3:// the L token
                setPositionForCase3(holdTokensPosition, 0);
                break;
            case 4:// the O token
                setPositionForCase4(holdTokensPosition, 0);
                break;
            case 5:// the S token
                setPositionForCase5(holdTokensPosition, 0);
                break;
            case 6:// the T token
                setPositionForCase6(holdTokensPosition, 0);
                break;
            default:// the Z token
                setPositionForCase7(holdTokensPosition, 0);
        }

        //reset holdTokens
        for (int i = 0; i < holdTokens.length; i++) {
            for (int j = 0; j < holdTokens[i].length; j++) {
                holdTokens[i][j].setType(0);
            }
        }
        for (int i = 0; i < holdTokensPosition.length; i++) {
            int x = holdTokensPosition[i][0];
            int y = holdTokensPosition[i][1];
            holdTokens[y][x].setType(holdTokensType);
        }
        holdable = false;
        nextMove(typeTemp);
    }

    private boolean checkBlowLines() {
        short countLineToBlow = 0;
        byte[] linesToBlow = {99, 99, 99, 99};//prevent default value of 0

        outer://set linesToBlow
        for (int i = 0; i < currentTokens.length; i++) {
            byte currentLine = (byte) currentTokens[i][1];
            for (int j = 0; j < 4; j++) {
                if (linesToBlow[j] == currentLine) {
                    //if currentLine is same as linesChecked then move to the next currentTokens
                    continue outer;
                }
            }

            //if currentLine is different from linesChecked then:
            for (int j = 0; j < 10; j++) {
                if (!tokens[currentLine][j].isFrozen()) {
                    continue outer;
                }
            }

            //if the whole line is filled and currentLine is different from linesChecked
            linesToBlow[i] = currentLine;
            countLineToBlow++;
        }

        //sort linesToBlow to make the top-most line to blow is at 1st index
        Arrays.sort(linesToBlow);

        if (countLineToBlow > 0) {
            freezing = true;
            blowLinesWithAnimation(linesToBlow);
            line = (short) (countLineToBlow + line);
            setChanged();
            notifyObservers(line);

            if (level == 1 && line >= 10) {
                levelUp();
            } else if (level == 2 && line >= 20) {
                levelUp();
            } else if (level == 3 && line >= 30) {
                levelUp();
            } else if (level == 4 && line >= 40) {
                levelUp();
            } else if (level == 5 && line >= 55) {
                levelUp();
            } else if (level == 6 && line >= 70) {
                levelUp();
            } else if (level == 7 && line >= 85) {
                levelUp();
            } else if (level == 8 && line >= 100) {
                levelUp();
            } else if (level == 9 && line >= 120) {
                levelUp();
            }
            return true;
        } else {
            return false;
        }
    }

    private void blowLinesWithFlash(byte[] linesToBlow) {
        Runnable flashLine = new FlashLinesToBlow(linesToBlow);
        Thread flashLineThread = new Thread(flashLine);
        flashLineThread.start();
    }

    private void blowLinesWithAnimation(byte[] linesToBlow) {
        BlowLinesAnimationTimer blowLinesAction = new BlowLinesAnimationTimer(linesToBlow);
        Timer blowLinesTimer = new Timer(40, blowLinesAction);
        blowLinesAction.setTimer(blowLinesTimer);

        blowLinesTimer.start();
    }

    private void moveAllDownStartFrom(int startLine) {
        for (int i = startLine; i >= 1; i--) {
            for (int j = 0; j < 10; j++) {
                tokens[i][j].setType(tokens[i - 1][j].getType());
                tokens[i][j].setFrozen(tokens[i - 1][j].isFrozen());
            }
        }

        //setType for row 0
        for (int j = 0; j < 10; j++) {
            tokens[0][j].setType(0);
        }
    }

    private void refresh() {
        setChanged();
        notifyObservers("Refresh");
    }

    protected void bounce() {
        setChanged();
        notifyObservers("Jump");
    }

    private boolean checkIsLose() {
        for (int i = 0; i < 4; i++) {
            int x = currentTokens[i][0];
            int y = currentTokens[i][1];
            if (tokens[y][x].isFrozen()) {
                return true;
            }
        }
        return false;
    }

    private void lose() {
        System.out.println("LOSE");
        timeSpeed.stop();
        timeCount.stop();
        isGameOn = false;

    }

    public Token[][] getTokens() {
        return tokens;
    }

    public TokenNext[][] getNextTokens1() {
        return nextTokens1;
    }

    public TokenNext[][] getNextTokens2() {
        return nextTokens2;
    }

    public TokenNext[][] getNextTokens3() {
        return nextTokens3;
    }

    public TokenNext[][] getHoldTokens() {
        return holdTokens;
    }

    private class TimeCountListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            time++;
            setChanged();
            notifyObservers(time);
        }
    }

    private class TimeSpeedListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            moveDown();
        }
    }

    private class BlowLinesAnimationTimer implements ActionListener {

        private byte[] linesToBlow;
        private Timer timer;
        private byte count;

        public BlowLinesAnimationTimer(byte[] linesToBlow) {
            this.linesToBlow = linesToBlow;
        }

        public void setTimer(Timer timer) {
            this.timer = timer;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if (count <= 4) {
                for (int i = 0; i < 4; i++) {//traverse lines
                    byte curLine = linesToBlow[i];
                    if (curLine != 99) {
                        for (int j = 0; j < 4 - count; j++) {//travers left side in a line
                            tokens[curLine][j].setType(tokens[curLine][j + 1].getType());
                        }

                        for (int j = 9; j > 5 + count; j--) {//travers right side in a line
                            tokens[curLine][j].setType(tokens[curLine][j - 1].getType());
                        }

                        //middle tokens
                        tokens[curLine][4 - count].setType(0);
                        tokens[curLine][4 - count].setFrozen(false);
                        tokens[curLine][5 + count].setType(0);
                        tokens[curLine][5 + count].setFrozen(false);
                    }
                }
                count++;
            } else {
                timer.stop();
                for (int i = 0; i < 4; i++) {
                    if (linesToBlow[i] != 99) {
                        moveAllDownStartFrom(linesToBlow[i]);
                    }
                }
                nextMove(0);
                freezing = false;
            }
            refresh();
        }
    }

    private class FlashLinesToBlow implements Runnable {

        private byte[] linesToBlow;

        public FlashLinesToBlow(byte[] linesToBlow) {
            this.linesToBlow = linesToBlow;
        }

        @Override
        public void run() {
            for (int i = 0; i < 4; i++) {
                if (linesToBlow[i] != 99) {
                    for (int j = 0; j < 10; j++) {
                        tokens[linesToBlow[i]][j].flash();
                    }
                }
            }
            refresh();

            try {
                Thread.sleep(120);
            } catch (Exception e) {
                System.out.println(e);
            }

            for (int i = 0; i < 4; i++) {
                if (linesToBlow[i] != 99) {
                    for (int j = 0; j < 10; j++) {
                        tokens[linesToBlow[i]][j].unFlash();
                    }
                }
            }
            refresh();

            for (int i = 0; i < 4; i++) {
                if (linesToBlow[i] != 99) {
                    moveAllDownStartFrom(linesToBlow[i]);
                }
            }
            nextMove(0);
            freezing = false;
        }
    }

    public void debugFrozenToken() {
        System.out.println();
        for (int i = 10; i < tokens.length; i++) {
            for (int j = 0; j < 10; j++) {
                if (tokens[i][j].isFrozen()) {
                    System.out.print("[" + i + " " + j + " T] ");
                } else {
                    System.out.print("[" + i + " " + j + " F] ");
                }
            }

            System.out.println();
        }
    }
}
