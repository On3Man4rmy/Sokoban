package Resources;

import java.awt.*;

// https://stackoverflow.com/a/19858051
public enum Colors {
    PIED_PIPER_BUTTERLAND(253, 242, 212),
    CANARINHO(251, 220, 95),
    WILD_VIOLET_PETAL(189, 148, 214),
    SURRENDER_V2(210, 231, 166),
    GREENISH(76, 115, 24),
    // http://www.colourlovers.com/color/F8975A/A_Spring_Trumpet
    A_SWING_TRUMPET_V2(248, 151, 90);

    private final int r;
    private final int g;
    private final int b;
    private final String rgb;

    private Colors(final int r,final int g,final int b) {
        this.r = r;
        this.g = g;
        this.b = b;
        this.rgb = r + ", " + g + ", " + b;
    }

    public String getRGB() {
        return rgb;
    }

    //You can add methods like this too
    public int getRed(){
        return r;
    }

    public int getGreen(){
        return g;
    }

    public int getBlue(){
        return r;
    }

    //Or even these
    public Color getColor(){
        return new Color(r,g,b);
    }

    public int getARGB(){
        return 0xFF000000 | ((r << 16) & 0x00FF0000) | ((g << 8) & 0x0000FF00) | b;
    }
}