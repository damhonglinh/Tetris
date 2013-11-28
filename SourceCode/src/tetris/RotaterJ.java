package tetris;

/**
 *
 * @author Dam Linh
 */
public class RotaterJ extends Rotater {

    public RotaterJ(Token[][] tokens) {
        super(tokens);
    }

    @Override
    public boolean rotateRight(Token[][] tokens, int[][] currentTokens, int rotateCount) {
        if (rotateCount % 4 == 0) {
            x0 = currentTokens[0][0] + 2;
            y0 = currentTokens[0][1];
            x1 = currentTokens[1][0] + 1;
            y1 = currentTokens[1][1] - 1;
            x2 = currentTokens[2][0];
            y2 = currentTokens[2][1];
            x3 = currentTokens[3][0] - 1;
            y3 = currentTokens[3][1] + 1;

        } else if (rotateCount % 4 == 1) {
            x0 = currentTokens[0][0];
            y0 = currentTokens[0][1] + 2;
            x1 = currentTokens[1][0] + 1;
            y1 = currentTokens[1][1] + 1;
            x2 = currentTokens[2][0];
            y2 = currentTokens[2][1];
            x3 = currentTokens[3][0] - 1;
            y3 = currentTokens[3][1] - 1;

        } else if (rotateCount % 4 == 2) {
            x0 = currentTokens[0][0] - 2;
            y0 = currentTokens[0][1];
            x1 = currentTokens[1][0] - 1;
            y1 = currentTokens[1][1] + 1;
            x2 = currentTokens[2][0];
            y2 = currentTokens[2][1];
            x3 = currentTokens[3][0] + 1;
            y3 = currentTokens[3][1] - 1;

        } else {
            x0 = currentTokens[0][0];
            y0 = currentTokens[0][1] - 2;
            x1 = currentTokens[1][0] - 1;
            y1 = currentTokens[1][1] - 1;
            x2 = currentTokens[2][0];
            y2 = currentTokens[2][1];
            x3 = currentTokens[3][0] + 1;
            y3 = currentTokens[3][1] + 1;
        }
        return setCurrentTokens(currentTokens);
    }

    @Override
    public boolean rotateLeft(Token[][] tokens, int[][] currentTokens, int rotateCount) {
        if (rotateCount % 4 == 1) {
            x0 = currentTokens[0][0] - 2;
            y0 = currentTokens[0][1];
            x1 = currentTokens[1][0] - 1;
            y1 = currentTokens[1][1] + 1;
            x2 = currentTokens[2][0];
            y2 = currentTokens[2][1];
            x3 = currentTokens[3][0] + 1;
            y3 = currentTokens[3][1] - 1;

        } else if (rotateCount % 4 == 2) {
            x0 = currentTokens[0][0];
            y0 = currentTokens[0][1] - 2;
            x1 = currentTokens[1][0] - 1;
            y1 = currentTokens[1][1] - 1;
            x2 = currentTokens[2][0];
            y2 = currentTokens[2][1];
            x3 = currentTokens[3][0] + 1;
            y3 = currentTokens[3][1] + 1;

        } else if (rotateCount % 4 == 3) {
            x0 = currentTokens[0][0] + 2;
            y0 = currentTokens[0][1];
            x1 = currentTokens[1][0] + 1;
            y1 = currentTokens[1][1] - 1;
            x2 = currentTokens[2][0];
            y2 = currentTokens[2][1];
            x3 = currentTokens[3][0] - 1;
            y3 = currentTokens[3][1] + 1;

        } else {
            x0 = currentTokens[0][0];
            y0 = currentTokens[0][1] + 2;
            x1 = currentTokens[1][0] + 1;
            y1 = currentTokens[1][1] + 1;
            x2 = currentTokens[2][0];
            y2 = currentTokens[2][1];
            x3 = currentTokens[3][0] - 1;
            y3 = currentTokens[3][1] - 1;
        }
        return setCurrentTokens(currentTokens);
    }
}
