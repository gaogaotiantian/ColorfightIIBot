package colorfightII;
import java.util.ArrayList;

public abstract class Building {
    public Pair cost = new Pair(0,0);
    public String name;
    public int level = 1;
    public abstract boolean is_empty();
    public abstract boolean is_home();

    public ArrayList<Pair> upgrade_cost = new ArrayList<>();

    public Building(Pair cost, String name) {
        this.cost.assign(cost);
        this.name = name;
    }

    public int max_level() {
        if(upgrade_cost.size()!=0){
            return upgrade_cost.size() + 1;
        }
        return 0;
    }

    public boolean can_upgrade() {
        return (!is_empty()) && level < max_level();
    }

    public int upgrade_gold() {
        if(can_upgrade()){
            return upgrade_cost.get(level - 1).getX();
        }
        return Integer.parseInt(null);
    }

    public int upgrade_energy() {
        if(can_upgrade()){
            return upgrade_cost.get(level - 1).getY();
        }
        return Integer.parseInt(null);
    }
}

