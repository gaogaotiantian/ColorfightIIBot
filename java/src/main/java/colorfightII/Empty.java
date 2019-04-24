package colorfightII;

public class Empty extends Building {
    Empty(){
        super(new Pair(0,0),"empty");
    }

    @Override
    public boolean is_empty() {
        return true;
    }

    @Override
    public boolean is_home() {
        return false;
    }
}
