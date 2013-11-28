package tetris;

import java.awt.Color;

/**
 *
 * @author Dam Linh
 */
public class TokenNext extends Token {

    @Override
    public void setType(int type) {
        super.setType(type);
        if (type == 0) {
            setBackgroundColor(new Color(255, 255, 255, 0));
            setBorder(null);
        }
    }
}
