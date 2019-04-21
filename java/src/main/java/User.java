import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class User {
    private int uid = 0;
    private int energy = 0;
    private int gold = 0;
    private int energy_source = 0;
    private int gold_source = 0;
    private boolean dead = false;
    private int tech_level = 1;
    private int tax_level = 0;
    private Map<Position, MapCell> cells;

    public void _update_info( Map info ){
        uid = (int)info.get("uid");
        energy = (int)info.get("energy");
        gold = (int)info.get("gold");
        energy_source = (int)info.get("energy_source");
        gold_source = (int)info.get("gold_source");
        dead = (boolean)info.get("dead");
        tech_level = (int)info.get("tech_level");
        tax_level = (int)info.get("tax_level");
        cells = (Map<Position, MapCell>)info.get("cells");
    }

    public Map info(){
        Map<String, Object> info = new HashMap<>();
        info.put("uid", uid);
        info.put("energy", energy);
        info.put("gold", gold);
        info.put("dead", dead);
        info.put("energy_source", energy_source);
        info.put("gold_source", gold_source);
        info.put("tech_level", tech_level);
        info.put("tax_level", tax_level);
        ArrayList cellinfos = new ArrayList();
        cellinfos.addAll(cells.values());
        info.put("cells", cellinfos);
        return info;
    }
}
