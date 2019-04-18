import java.util.ArrayList;
import java.util.Map;

public class Colorfight {
    public int uid = 0;
    public int turn = 0;
    public int max_turn = 0;
    public int round_time = 0;
    public User me = null;
    public Map<Integer, User> users;
    public Map<Integer, ArrayList<String>> error;
    public GameMap game_map = null;

    public void connect(String url){/*TODO*/}
    public void update_turn(){/*TODO*/}
    public boolean register(String username, String password){
        /*TODO*/
        return false;
    }
    public String attack(Position position, int energy){
        /*TODO*/
        return null;
    }
    public String build(Position position, char building){
        /*TODO*/
        return null;
    }
    public String upgrade(Position position){
        /*TODO*/
        return null;
    }
    public void send_cmd(ArrayList<String> cmd_list){/*TODO*/}
}
