import java.util.ArrayList;

abstract class Building {
    Pair cost = new Pair(0,0);
    String name;
    int level = 1;
    abstract boolean is_empty();
    abstract boolean is_home();

    ArrayList<Pair> upgrade_cost = new ArrayList<>();

    public Building(Pair cost, String name) {
        this.cost.assign(cost);
        this.name = name;
    }

    int max_level() {
        if(upgrade_cost.size()!=0){
            return upgrade_cost.size() + 1;
        }
        return 0;
    }

    boolean can_upgrade() {
        return level < max_level();
    }

    int upgrade_gold() {
        if(can_upgrade()){
            return upgrade_cost.get(level - 1).getX();
        }
        return Integer.parseInt(null);
    }

    int upgrade_energy() {
        if(can_upgrade()){
            return upgrade_cost.get(level - 1).getY();
        }
        return Integer.parseInt(null);
    }
}

class Empty extends Building {
    Empty(){
        super(new Pair(0,0),"empty");
    }

    @Override
    boolean is_empty() {
        return true;
    }

    @Override
    boolean is_home() {
        return false;
    }
}

class Home extends Building {
    Home(){
        super(new Pair(1000, 0),"home");
        upgrade_cost.add(new Pair(1000,1000));
        upgrade_cost.add(new Pair(2000,2000));
    }

    @Override
    boolean is_empty() {
        return false;
    }

    @Override
    boolean is_home() {
        return true;
    }
}

class EnergyWell extends Building {
    EnergyWell(){
        super(new Pair(100, 0),"energy_well");
        upgrade_cost.add(new Pair(200,0));
        upgrade_cost.add(new Pair(400,0));
    }

    @Override
    boolean is_empty() {
        return false;
    }

    @Override
    boolean is_home() {
        return false;
    }
}

class GoldMine extends Building {
    GoldMine(){
        super(new Pair(100, 0),"gold_mine");
        upgrade_cost.add(new Pair(200,0));
        upgrade_cost.add(new Pair(400,0));
    }

    @Override
    boolean is_empty() {
        return false;
    }

    @Override
    boolean is_home() {
        return false;
    }
}

class Fortress extends Building {
    Fortress(){
        super(new Pair(100, 0),"fortress");
        upgrade_cost.add(new Pair(200,0));
        upgrade_cost.add(new Pair(400,0));
    }

    @Override
    boolean is_empty() {
        return false;
    }

    @Override
    boolean is_home() {
        return false;
    }
}