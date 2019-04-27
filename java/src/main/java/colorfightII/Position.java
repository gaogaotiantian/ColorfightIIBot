package colorfightII;

import java.util.Objects;

public class Position {
    public int x;
    public int y;

    public Position( int x, int y ){
        this.x = x;
        this.y = y;
    }

    public Position( Pair pos ){
        this.x = pos.getX();
        this.y = pos.getY();
    }

    @Override
    public boolean equals( Object o ) {
        if ( this == o ) return true;
        if ( o == null || getClass() != o.getClass() ) return false;
        Position position = (Position) o;
        return x == position.x &&
                y == position.y;
    }

    @Override
    public int hashCode() {

        return Objects.hash(x, y);
    }

    public Position add( Position other ){
        return new Position( this.x + other.x, this.y + other.y );
    }

    public Position subtract( Position other ){
        return new Position( this.x + other.x, this.y + other.y );
    }

    public void assign( Position other ){
        this.x = other.x;
        this.y = other.y;
    }

    @Override
    public String toString() {
        return "colorfightII.Position(" + x + ", " + y + ')';
    }

    public boolean is_valid(){
        return ( 0 <= x ) && ( x < Constants.GAME_WIDTH ) &&
                ( 0 <= y ) && ( y < Constants.GAME_HEIGHT );
    }

    public Position directional_offset( Pair direction ){
        return add( new Position(direction) );
    }

    public Position[] _get_all_surrounding_cardinals(){
        Position[] rtn = new Position[4];
        int temp = 0;
        for ( Pair pair:Direction.get_all_cardinals() ) {
            rtn[temp++] = directional_offset( pair );
        }
        return rtn;
    }

    public Position[] get_surrounding_cardinals(){
        int len = 0;
        for ( Position pos:_get_all_surrounding_cardinals() ) {
            if( pos.is_valid() ){
                len++;
            }
        }

        Position[] rtn = new Position[len];
        int temp = 0;
        for ( Position pos:_get_all_surrounding_cardinals() ) {
            if ( pos.is_valid() ) {
                rtn[temp++] = pos;
            }
        }
        return rtn;
    }

    public Pair info(){
        return new Pair( x, y );
    }
}
