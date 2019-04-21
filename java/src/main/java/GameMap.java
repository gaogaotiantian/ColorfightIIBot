public class GameMap {
    //TODO: idk...it's just incomplete
    private int width;
    private int height;

    public GameMap(int width, int height){
        this.width = width;
        this.height = height;
    }

    public MapCell game_map(Position position){
        return new MapCell(position);
    }
}
