package colorfightII;

public class Pair {
    private int x;
    private int y;

    public Pair( int x, int y ){
        this.x = x;
        this.y = y;
    }

    public void assign( Pair other ){
        this.x = other.x;
        this.y = other.y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
