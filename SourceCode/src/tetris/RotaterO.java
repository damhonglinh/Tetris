package tetris;

/**
 *
 * @author Dam Linh
 */
public class RotaterO extends Rotater {

    public RotaterO(Token[][] tokens) {
        super(tokens);
    }

    @Override
    public boolean rotateRight(Token[][] tokens, int[][] currentTokens, int rotateCount) {
        return true;
    }

    @Override
    public boolean rotateLeft(Token[][] tokens, int[][] currentTokens, int rotateCount) {
        return true;
    }
}
