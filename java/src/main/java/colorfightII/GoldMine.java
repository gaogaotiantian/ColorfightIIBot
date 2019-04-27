package colorfightII;

public class GoldMine extends Building {
    GoldMine(){
        super( new Pair( 100, 0 ),"gold_mine" );
        upgrade_cost.add( new Pair( 200,0 ) );
        upgrade_cost.add( new Pair( 400,0 ) );
        is_empty = false;
        is_home = false;
    }
}
