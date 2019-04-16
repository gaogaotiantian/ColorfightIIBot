import java.util.HashMap;
import java.util.Map;

public class User {
    private int uid = 0;
    private String username = "";
    private int energy = 0;
    private int gold = 0;
    private int energy_source = 0;
    private int gold_resource = 0;
    private boolean dead = false;
    private Map cells;

    public void _update_info( Map info ){
        //TODO
    }

    public Map info(){
        Map info = new HashMap<String, Object>();
        info.put("uid", uid);
        info.put("username", username);
        info.put("energy", energy);
        info.put("gold", gold);
        info.put("dead", dead);
        info.put("energy_source", energy_source);
        info.put("gold_source", gold_resource);
        return null;
        //TODO
    }
}
