package colorfightII;

public class Empty extends Building {
    Empty(){
        super( new Pair( 0,0 ),"empty" );
        is_empty = true;
        is_home = false;
    }
}
