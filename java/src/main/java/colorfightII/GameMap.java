package colorfightII;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class GameMap {
    private int width;
    private int height;
    private MapCell[][] game_map;

    public GameMap(int width, int height){
        this.width = width;
        this.height = height;
        game_map = new MapCell[height][width];
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                game_map[j][i] = new MapCell(new Position(i,j));
            }
        }
    }

    public MapCell get_cell(Position pos){
        return game_map[pos.getY()][pos.getX()];
    }

    public void _update_info(JSONArray info){
        for (Object o:info
             ) {
            JSONArray row = (JSONArray) o;
            for (Object m:row
                 ) {
                JSONObject cell = (JSONObject) m;
                int x = ((Long)((JSONArray)cell.get("position")).get(0)).intValue();
                int y = ((Long)((JSONArray)cell.get("position")).get(1)).intValue();
                game_map[y][x]._update_info(cell);
            }
        }
    }
}
