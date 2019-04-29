package colorfightII;

import org.json.simple.JSONArray;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class User {
    public int uid = 0;
    public int energy = 0;
    public int gold = 0;
    public int energy_source = 0;
    public int gold_source = 0;
    public boolean dead = false;
    public int tech_level = 1;
    public int tax_level = 0;
    public int tax_amount = 0;
    public ArrayList<MapCell> cells = new ArrayList<>();

    public void _update_info( Map info ){
        uid = ( (Long) info.get("uid") ).intValue();
        energy = ( (Long)info.get("energy") ).intValue();
        gold = ( (Long)info.get("gold") ).intValue();
        energy_source = ( (Long)info.get("energy_source") ).intValue();
        gold_source = ( (Long)info.get("gold_source") ).intValue();
        dead = (boolean) info.get("dead");
        tech_level = ( (Long)info.get("tech_level") ).intValue();
        tax_level = ( (Long)info.get("tax_level") ).intValue();
        tax_amount = ( (Long)info.get("tax_amount") ).intValue();
        cells.clear();
        for ( Object o:(JSONArray)info.get("cells") ) {
            JSONArray cell = (JSONArray) o;
            int x = ( (Long)cell.get(0) ).intValue();
            int y = ( (Long)cell.get(1) ).intValue();
            cells.add( GameMap.get_cell( new Position(x,y) ) );
        }
    }

    public Map info(){
        Map<String, Object> info = new HashMap<>();
        info.put( "uid", uid );
        info.put( "energy", energy );
        info.put( "gold", gold );
        info.put( "dead", dead );
        info.put( "energy_source", energy_source );
        info.put( "gold_source", gold_source );
        info.put( "tech_level", tech_level );
        info.put( "tax_level", tax_level );
        info.put( "cells", cells );
        return info;
    }
}
