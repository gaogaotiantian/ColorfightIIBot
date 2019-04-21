import org.json.simple.JSONArray;

import java.util.Map;

public class MapCell {
    public Position position;
    //TODO: building
    public Building building = new Empty();
    public int gold = 0;
    public int energy = 0;
    public int owner = 0;
    public int attack_cost = 0;
    public int natural_cost = 0;
    public int natural_gold = 0;
    public int natural_energy = 0;
    public int force_field = 0;

    public MapCell(Position position){
        this.position = new Position(position.getX(), position.getY());
    }

    public void _update_info( Map info ){
        /*position = new Position(
                ((Long)((JSONArray)info.get("position")).get(0)).intValue(),
                ((Long)((JSONArray)info.get("position")).get(1)).intValue()
        );*/
        gold = ((Long)info.get("gold")).intValue();
        energy = ((Long)info.get("energy")).intValue();
        owner = ((Long)info.get("owner")).intValue();
        attack_cost = ((Long)info.get("attack_cost")).intValue();
        natural_cost = ((Long)info.get("natural_cost")).intValue();
        natural_gold = ((Long)info.get("natural_gold")).intValue();
        natural_energy = ((Long)info.get("natural_energy")).intValue();
        force_field = ((Long)info.get("force_field")).intValue();
    }
}
