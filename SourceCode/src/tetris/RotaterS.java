package tetris;

/**
 *
 * @author Dam Linh
 */
public class RotaterS extends Rotater {

    public RotaterS(Token[][] tokens) {
        super(tokens);
    }

    @Override
    public boolean rotateRight(Token[][] tokens, int[][] currentTokens, int rotateCount) {
        if (rotateCount % 2 == 0) {
            x0 = currentTokens[0][0] + 1;
            y0 = currentTokens[0][1];
            x1 = currentTokens[1][0];
            y1 = currentTokens[1][1] + 1;
            x2 = currentTokens[2][0] + 1;
            y2 = currentTokens[2][1] - 2;
            x3 = currentTokens[3][0];
            y3 = currentTokens[3][1] - 1;
        } else {
            x0 = currentTokens[0][0] - 1;
            y0 = currentTokens[0][1];
            x1 = currentTokens[1][0];
            y1 = currentTokens[1][1] - 1;
            x2 = currentTokens[2][0] - 1;
            y2 = currentTokens[2][1] + 2;
            x3 = currentTokens[3][0];
            y3 = currentTokens[3][1] + 1;
        }
        return setCurrentTokens(currentTokens);
    }

    @Override
    public boolean rotateLeft(Token[][] tokens, int[][] currentTokens, int rotateCount) {
        if (rotateCount % 2 == 1) {
            x0 = currentTokens[0][0] - 1;
            y0 = currentTokens[0][1];
            x1 = currentTokens[1][0];
            y1 = currentTokens[1][1] - 1;
            x2 = currentTokens[2][0] - 1;
            y2 = currentTokens[2][1] + 2;
            x3 = currentTokens[3][0];
            y3 = currentTokens[3][1] + 1;
        } else {
            x0 = currentTokens[0][0] + 1;
            y0 = currentTokens[0][1];
            x1 = currentTokens[1][0];
            y1 = currentTokens[1][1] + 1;
            x2 = currentTokens[2][0] + 1;
            y2 = currentTokens[2][1] - 2;
            x3 = currentTokens[3][0];
            y3 = currentTokens[3][1] - 1;
        }
        return setCurrentTokens(currentTokens);
    }
}
