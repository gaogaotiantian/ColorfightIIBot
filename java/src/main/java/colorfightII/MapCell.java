package colorfightII;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.Map;

public class MapCell {
    public Position position;
    public Building building = new Empty();
    public boolean is_empty = true;
    public boolean is_home = false;
    public int gold = 0;
    public int energy = 0;
    public int owner = 0;
    public int attack_cost = 0;
    public int natural_cost = 0;
    public int natural_gold = 0;
    public int natural_energy = 0;
    public int force_field = 0;

    public MapCell( Position position ){
        this.position = new Position( position.x, position.y );
    }

    public void _update_info( Map info ){
        position.assign( new Position(
                ( (Long)( (JSONArray)info.get("position") ).get(0) ).intValue(),
                ( (Long)( (JSONArray)info.get("position") ).get(1) ).intValue()
        ) );
        gold = ( (Long)info.get("gold") ).intValue();
        energy = ( (Long)info.get("energy") ).intValue();
        owner = ( (Long)info.get("owner") ).intValue();
        attack_cost = ( (Long)info.get("attack_cost") ).intValue();
        natural_cost = ( (Long)info.get("natural_cost") ).intValue();
        natural_gold = ( (Long)info.get("natural_gold") ).intValue();
        natural_energy = ( (Long)info.get("natural_energy") ).intValue();
        force_field = ( (Long)info.get("force_field") ).intValue();
        building = letter_to_build_class( (String) ( (JSONArray)info.get("building") ).get(0) );
        building.level = ( (Long)( (JSONArray)info.get("building") ).get(1) ).intValue();
        building._update_member();
        is_empty = building.is_empty;
        is_home = building.is_home;
    }

    private Building letter_to_build_class( String name ){
        if ( name.equals("f") ) return new Fortress();
        if ( name.equals("h") ) return new Home();
        if ( name.equals("e") ) return new EnergyWell();
        if ( name.equals("g") ) return new GoldMine();
        else return new Empty();
    }
}
