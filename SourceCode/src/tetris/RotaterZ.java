package tetris;

/**
 *
 * @author Dam Linh
 */
public class RotaterZ extends Rotater {

    public RotaterZ(Token[][] tokens) {
        super(tokens);
    }

    @Override
    public boolean rotateRight(Token[][] tokens, int[][] currentTokens, int rotateCount) {
        if (rotateCount % 2 == 0) {
            x0 = currentTokens[0][0] + 2;
            y0 = currentTokens[0][1] - 1;
            x1 = currentTokens[1][0] + 1;
            y1 = currentTokens[1][1];
            x2 = currentTokens[2][0];
            y2 = currentTokens[2][1] - 1;
            x3 = currentTokens[3][0] - 1;
            y3 = currentTokens[3][1];
        } else {
            x0 = currentTokens[0][0] - 2;
            y0 = currentTokens[0][1] + 1;
            x1 = currentTokens[1][0] - 1;
            y1 = currentTokens[1][1];
            x2 = currentTokens[2][0];
            y2 = currentTokens[2][1] + 1;
            x3 = currentTokens[3][0] + 1;
            y3 = currentTokens[3][1];
        }
        return setCurrentTokens(currentTokens);
    }

    @Override
    public boolean rotateLeft(Token[][] tokens, int[][] currentTokens, int rotateCount) {
        if (rotateCount % 2 == 1) {
            x0 = currentTokens[0][0] - 2;
            y0 = currentTokens[0][1] + 1;
            x1 = currentTokens[1][0] - 1;
            y1 = currentTokens[1][1];
            x2 = currentTokens[2][0];
            y2 = currentTokens[2][1] + 1;
            x3 = currentTokens[3][0] + 1;
            y3 = currentTokens[3][1];
        } else {
            x0 = currentTokens[0][0] + 2;
            y0 = currentTokens[0][1] - 1;
            x1 = currentTokens[1][0] + 1;
            y1 = currentTokens[1][1];
            x2 = currentTokens[2][0];
            y2 = currentTokens[2][1] - 1;
            x3 = currentTokens[3][0] - 1;
            y3 = currentTokens[3][1];
        }
        return setCurrentTokens(currentTokens);
    }
}
