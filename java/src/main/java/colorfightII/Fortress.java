package colorfightII;

public class Fortress extends Building {
    Fortress(){
        super( new Pair( 200, 0 ),"fortress" );
        upgrade_cost.add( new Pair( 400,0 ) );
        upgrade_cost.add( new Pair( 600,0 ) );
        is_empty = false;
        is_home = false;
    }
}
