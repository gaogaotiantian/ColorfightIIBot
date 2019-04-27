package colorfightII;

public class EnergyWell extends Building {
    EnergyWell(){
        super( new Pair( 100, 0 ),"energy_well" );
        upgrade_cost.add( new Pair( 200,0 ) );
        upgrade_cost.add( new Pair( 400,0 ) );
        is_empty = false;
        is_home = false;
    }
}
