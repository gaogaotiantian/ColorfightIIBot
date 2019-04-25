import colorfightII.*;
import org.json.simple.parser.ParseException;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Random;

public class example_ai {
    public static void main(String[] args) {
        /*
         * example ai for java
         */
        Colorfight game = new Colorfight();
        try {
            game.connect("http://colorfightii.herokuapp.com/gameroom/public");
            String username = "ExampleAI"+(new Random().nextInt(100)+1);
            String password = "ExampleAI"+(new Random().nextInt(100)+1);
            if(!game.register(username, password)) return;
            ArrayList<String> cmd_list = new ArrayList<>();
            ArrayList<Position> my_attack_list = new ArrayList<>();
            while(true){
                cmd_list.clear();
                my_attack_list.clear();
                game.update_turn();
                if(game.me==null) continue;
                User me = game.me;
                for (MapCell cell:game.me.cells
                        ) {
                    for (Position pos:cell.position.get_surrounding_cardinals()
                            ) {
                        MapCell c = game.game_map.get_cell(pos);
                        if((c.attack_cost < game.me.energy) &&
                                (c.owner != game.uid) &&
                                (!my_attack_list.contains(c.position)) &&
                                (me.cells.size()<95)){
                            cmd_list.add(game.attack(pos, c.attack_cost));
                            game.me.energy -= c.attack_cost;
                            System.out.println("we are attacking {"+
                                    pos.x+","+pos.y+
                                    "} with "+c.attack_cost+" energy");
                            my_attack_list.add(c.position);
                        }
                    }
                    if( (cell.building.can_upgrade) &&
                            (cell.building.is_home || (cell.building.level < me.tech_level)) &&
                            (cell.building.upgrade_gold < me.gold) &&
                            (cell.building.upgrade_energy < me.energy)){
                        cmd_list.add(game.upgrade(cell.position));
                        System.out.println("we upgraded {"+cell.position.x+","+cell.position.y+"}");
                        me.gold -= cell.building.upgrade_gold;
                        me.energy -= cell.building.upgrade_energy;
                    }
                    if( (cell.owner==me.uid) && (cell.building.is_empty) && (me.gold >= 100) ){
                        char buildings[] = {
                                Constants.BLD_FORTRESS,
                                Constants.BLD_GOLD_MINE,
                                Constants.BLD_ENERGY_WELL};
                        char building = buildings[new Random().nextInt(3)];
                        cmd_list.add(game.build(cell.position, building));
                        System.out.println("we build " + building +
                                " on {" + cell.position.x+","+
                                cell.position.y+"}");
                        me.gold -= 100;
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
