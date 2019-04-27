package colorfightII;

public class Direction {
    private static Pair North = new Pair( 0, -1 );
    private static Pair South = new Pair( 0, 1 );
    private static Pair West = new Pair( -1, 0 );
    private static Pair East = new Pair( 1, 0 );

    public static Pair[] get_all_cardinals(){
        Pair[] arr = new Pair[4];
        arr[0] = North;
        arr[1] = South;
        arr[2] = West;
        arr[3] = East;
        return arr;
    }
}
