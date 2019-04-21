abstract class Building {
    Pair cost;
    String name;
    int level = 1;
    Building(String name){
        this.name = name;
    }
}

class Empty extends Building {
    Empty(){
        super("empty");
    }
}

class Home extends Building {
    Home(){
        super("home");
        cost.assign(new Pair(1000, 0));
    }
}

class EnergyWell extends Building {
    EnergyWell(){
        super("energy_well");
        cost.assign(new Pair(100, 0));
    }
}

class GoldMine extends Building {
    GoldMine(){
        super("gold_mine");
        cost.assign(new Pair(100, 0));
    }
}