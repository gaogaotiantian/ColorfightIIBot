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

        // Create a Colorfight Instance. This will be the object that you interact
        // with.
        Colorfight game = new Colorfight();

        // Include all the code in a try-catch to handle exceptions.
        // This would help you debug your program.
        try {
            // Connect to the server. This will connect to the public room. If you want to
            // join other rooms, you need to change the argument.
            game.connect( "public" );
            String username = "ExampleAI"+( new Random().nextInt(100)+1 );
            String password = "ExampleAI"+( new Random().nextInt(100)+1 );

            // game.register should return True if succeed.
            // As no duplicate usernames are allowed, a random integer string is appended
            // to the example username. You don't need to do this, change the username
            // to your ID.
            // You need to set a password. For the example AI, a random password is used
            // as the password. You should change it to something that will not change
            // between runs so you can continue the game if disconnected.
            if ( !game.register( username, password ) ) return;

            // The command list we will send to the server
            ArrayList<String> cmd_list = new ArrayList<>();
            // The list of cells that we want to attack
            ArrayList<Position> my_attack_list = new ArrayList<>();

            // This is the game loop
            while( true ){
                // empty the two lists in the start of each round.
                cmd_list.clear();
                my_attack_list.clear();

                int last_turn = game.turn;

                // update_turn() is required to get the latest information from the
                // server. This will halt the program until it receives the updated
                // information.
                // After update_turn(), game object will be updated.
                game.update_turn();

                // Time does not go back. If it does, it means there's a new game.
                // Potentially you can add a infinite loop to take usage of this
                // to run your bot infinitely
                if game.turn < last_turn:
                    break

                // Check if you exist in the game. If not, wait for the next round.
                // You may not appear immediately after you join. But you should be
                // in the game after one round.
                if (game.me==null) continue;

                User me = game.me;

                // game.me.cells is a Arraylist of MapCells.
                // The outer loop gets all my cells.
                for ( MapCell cell:game.me.cells ) {
                    // The inner loop checks the surrounding positions.
                    for ( Position pos:cell.position.get_surrounding_cardinals() ) {
                        // Get the MapCell object of that position
                        MapCell c = game.game_map.get_cell( pos );

                        // Attack if the cost is less than what I have, and the owner
                        // is not mine, and I have not attacked it in this round already
                        // We also try to keep our cell number under 100 to avoid tax
                        if ( ( c.attack_cost < game.me.energy ) &&
                                ( c.owner != game.uid ) &&
                                ( !my_attack_list.contains( c.position ) ) &&
                                ( me.cells.size()<95 ) ) {
                            // Add the attack command in the command list
                            // Subtract the attack cost manually so I can keep track
                            // of the energy I have.
                            // Add the position to the attack list so I won't attack
                            // the same cell
                            cmd_list.add( game.attack( pos, c.attack_cost ) );
                            game.me.energy -= c.attack_cost;
                            System.out.println( "we are attacking {" +
                                    pos.x + "," + pos.y +
                                    "} with " + c.attack_cost + " energy" );
                            my_attack_list.add( c.position );
                        }
                    }

                    // If we can upgrade the building, upgrade it.
                    // Notice can_update only checks for upper bound. You need to check
                    // tech_level by yourself.
                    if ( ( cell.building.can_upgrade ) &&
                            ( cell.building.is_home || ( cell.building.level < me.tech_level ) ) &&
                            ( cell.building.upgrade_gold < me.gold ) &&
                            ( cell.building.upgrade_energy < me.energy ) ) {
                        cmd_list.add( game.upgrade( cell.position ) );
                        System.out.println( "we upgraded {"+cell.position.x+","+cell.position.y+"}" );
                        me.gold -= cell.building.upgrade_gold;
                        me.energy -= cell.building.upgrade_energy;
                    }

                    // Build a random building if we have enough gold
                    if ( ( cell.owner==me.uid ) && ( cell.building.is_empty ) && ( me.gold >= Constants.BUILDING_COST.getX() ) ) {
                        char buildings[] = {
                                Constants.BLD_FORTRESS,
                                Constants.BLD_GOLD_MINE,
                                Constants.BLD_ENERGY_WELL};
                        char building = buildings[ new Random().nextInt(3) ];
                        cmd_list.add( game.build( cell.position, building ) );
                        System.out.println( "we build " + building +
                                " on {" + cell.position.x+","+
                                cell.position.y+"}" );
                        me.gold -= 100;
                    }
                }

                // Send the command list to the server
                // and print out the message from server
                System.out.println( game.send_cmd( cmd_list ).toString() );
            }
        } catch ( URISyntaxException e ) {
            e.printStackTrace();
        } catch ( InterruptedException e ) {
            e.printStackTrace();
        } catch ( ParseException e ) {
            e.printStackTrace();
        }
    }
}
