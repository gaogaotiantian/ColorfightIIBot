import org.json.simple.parser.ParseException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        /*
         * example ai for java
         */
        Colorfight game = new Colorfight();
        try {
            game.connect("http://colorfightii.herokuapp.com/gameroom/public");
            if(!game.register("delin1111", "delinnn")) return;
            ArrayList<String> cmd_list = new ArrayList<>();
            ArrayList<Position> my_attack_list = new ArrayList<>();
            while(true){
                cmd_list.clear();
                my_attack_list.clear();
                game.update_turn();
                if(game.me==null) continue;
                for (Position cell:game.me.cells
                     ) {
                    for (Position pos:cell.get_surrounding_cardinals()
                         ) {
                        MapCell c = game.game_map.get_cell(pos);
                        if((c.attack_cost < game.me.energy) && (c.owner != game.uid) && (!my_attack_list.contains(c.position))){
                            cmd_list.add(game.attack(pos, c.attack_cost));
                            game.me.energy -= c.attack_cost;
                            my_attack_list.add(c.position);
                        }
                    }
                }
                System.out.println(game.send_cmd(cmd_list).toString());
            }
        } catch (URISyntaxException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}
