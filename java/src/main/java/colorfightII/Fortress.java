package colorfightII;

public class Fortress extends Building {
    Fortress(){
        super(new Pair(100, 0),"fortress");
        upgrade_cost.add(new Pair(200,0));
        upgrade_cost.add(new Pair(400,0));
    }

    @Override
    public boolean is_empty() {
        return false;
    }

    @Override
    public boolean is_home() {
        return false;
    }
}
