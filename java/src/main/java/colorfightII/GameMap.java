package colorfightII;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class GameMap {
    private int width;
    private int height;
    private static MapCell[][] game_map;

    public GameMap( int width, int height ){
        this.width = width;
        this.height = height;
        game_map = new MapCell[height][width];
        for ( int i = 0; i < width; i++ ) {
            for ( int j = 0; j < height; j++ ) {
                game_map[j][i] = new MapCell( new Position(i,j) );
            }
        }
    }

    public static MapCell get_cell( Position pos ){
        return game_map[pos.y][pos.x];
    }

    public void _update_info( JSONObject info ){
        for ( Object o:(JSONArray)info.get("data") ) {
            JSONArray row = (JSONArray) o;
            for ( Object m:row ) {
                JSONObject cell = unpack_cell( (JSONArray) info.get("headers"), (JSONArray) m );
                int x = ( (Long)( (JSONArray)cell.get("position") ).get(0) ).intValue();
                int y = ( (Long)( (JSONArray)cell.get("position") ).get(1) ).intValue();
                game_map[y][x]._update_info( cell );
            }
        }
    }

    private JSONObject unpack_cell( JSONArray headers, JSONArray cell ){
        JSONObject unpack_cell = new JSONObject();
        int i = 0;
        for ( Object o:headers ) {
            String header = (String) o;
            unpack_cell.put( header, cell.get(i) );
            i++;
        }
        return unpack_cell;
    }
}
