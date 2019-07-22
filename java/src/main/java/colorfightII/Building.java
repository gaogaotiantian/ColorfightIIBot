package colorfightII;
import java.util.ArrayList;

public abstract class Building {
    public Pair cost = new Pair( 0,0 );
    public String name;
    public int level = 1;
    public boolean is_empty = true;
    public boolean is_home = false;
    public int max_level = 0;
    public boolean can_upgrade = false;
    public int upgrade_gold = 0;
    public int upgrade_energy = 0;
    public int destroy_gold = 0;
    public int destroy_forcefield = 0;

    public ArrayList<Pair> upgrade_cost = new ArrayList<>();
    public ArrayList<Integer>  destroy_bonus = new ArrayList<>();

    public Building( Pair cost, String name ) {
        this.cost.assign( cost );
        this.name = name;
        this.destroy_bonus.add(100);
        this.destroy_bonus.add(300);
        this.destroy_bonus.add(600);
    }

    public void _update_member(){
        if ( upgrade_cost.size() != 0 ) {
            max_level = upgrade_cost.size() + 1;
        }

        can_upgrade = ( !is_empty ) && level < max_level;

        if ( can_upgrade ) {
            upgrade_gold = upgrade_cost.get( level - 1 ).getX();
        }

        if ( can_upgrade ) {
            upgrade_energy = upgrade_cost.get( level - 1 ).getY();
        }

        if (name == "energy_well") {
            destroy_forcefield = destroy_bonus.get(level - 1);
        } else {
            destroy_forcefield = 0;
        }

        if (name == "gold_mine") {
            destroy_gold = destroy_bonus.get(level - 1);
        } else {
            destroy_gold = 0;
        }
    }
}

