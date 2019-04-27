package colorfightII;

public class Home extends Building {
    Home(){
        super( new Pair( 1000, 0 ),"home" );
        upgrade_cost.add( new Pair( 1000,1000 ) );
        upgrade_cost.add( new Pair( 2000,2000 ) );
        is_empty = false;
        is_home = true;
    }
}
