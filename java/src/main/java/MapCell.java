import java.util.Map;

public class MapCell {
    private Position position;
    private Building building = new Empty();
    private int gold = 0;
    private int energy = 0;
    private int owner = 0;
    private int attack_cost = 0;
    private int natural_cost = 0;
    private int natural_gold = 0;
    private int natural_energy = 0;
    private int force_field = 0;

    public MapCell(Position position){
        this.position.assign(position);
    }

    public void _update_info( Map info ){/*TODO: ???*/}
}
