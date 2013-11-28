package tetris;

/**
 *
 * @author Dam Linh
 */
public abstract class Rotater {

    protected int x0;
    protected int x1;
    protected int x2;
    protected int x3;
    protected int y0;
    protected int y1;
    protected int y2;
    protected int y3;
    protected int y4;
    protected Token[][] tokens;

    public Rotater(Token[][] tokens) {
        this.tokens = tokens;
    }

    public abstract boolean rotateRight(Token[][] tokens, int[][] currentTokens, int rotateCount);

    public abstract boolean rotateLeft(Token[][] tokens, int[][] currentTokens, int rotateCount);

    private boolean checkLocationAvailable() {
        if (y0 < 0 || y1 < 0 || y2 < 0 || y3 < 0
                || y0 > 19 || y1 > 19 || y2 > 19 || y3 > 19
                || tokens[y0][x0].getType() > 0
                || tokens[y1][x1].getType() > 0
                || tokens[y2][x2].getType() > 0
                || tokens[y3][x3].getType() > 0) {
            return false;
        }
        return true;
    }

    protected boolean setCurrentTokens(int[][] currentTokens) {
        avoidFromEdge(currentTokens);
        if (checkLocationAvailable()) {
            currentTokens[0][0] = x0;
            currentTokens[0][1] = y0;
            currentTokens[1][0] = x1;
            currentTokens[1][1] = y1;
            currentTokens[2][0] = x2;
            currentTokens[2][1] = y2;
            currentTokens[3][0] = x3;
            currentTokens[3][1] = y3;
            return true;
        } else {
            return false;
        }
    }

    private void avoidFromEdge(int[][] currentTokens) {
        //move currentTokens to the right if they are at right edge
        while (x0 < 0 || x1 < 0 || x2 < 0 || x3 < 0) {
            currentTokens[0][0]++;
            currentTokens[1][0]++;
            currentTokens[2][0]++;
            currentTokens[3][0]++;
            x0++;
            x1++;
            x2++;
            x3++;
        }

        //move currentTokens to the left if they are at left edge
        while (x0 > 9 || x1 > 9 || x2 > 9 || x3 > 9) {
            currentTokens[0][0]--;
            currentTokens[1][0]--;
            currentTokens[2][0]--;
            currentTokens[3][0]--;
            x0--;
            x1--;
            x2--;
            x3--;
        }
    }
}
