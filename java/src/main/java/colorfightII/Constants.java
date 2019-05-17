package colorfightII;

import java.util.Map;

public class Constants {
    public static int GAME_VERSION = 2;
    public static int GAME_WIDTH = 30;
    public static int GAME_HEIGHT = 30;
    public static int GAME_MAX_TURN = 500;
    public static int GAME_MAX_LEVEL = 4;

    public void update_globals( Map info ){
        GAME_WIDTH = (int)info.get("width");
        GAME_HEIGHT = (int)info.get("height");
        GAME_MAX_TURN = (int)info.get("max_turn");
    }

    public static final char CMD_ATTACK  = 'a';
    public static final char CMD_BUILD   = 'b';
    public static final char CMD_UPGRADE = 'u';

    public static final char BLD_HOME      = 'h';
    public static final char BLD_GOLD_MINE = 'g';
    public static final char BLD_ENERGY_WELL = 'e';
    public static final char BLD_FORTRESS = 'f';

    public static Pair BUILDING_COST = new Pair(100, 0);
}
