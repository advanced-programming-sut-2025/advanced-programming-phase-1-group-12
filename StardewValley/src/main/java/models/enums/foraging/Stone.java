package models.enums.foraging;

public class Stone {

    private MineralTypes mineralTypes;

    public Stone(MineralTypes mineralTypes) {
        this.mineralTypes = mineralTypes;
    }

    public MineralTypes getMineralTypes() {
        return mineralTypes;
    }

    public void setMineralTypes(MineralTypes mineralTypes) {
        this.mineralTypes = mineralTypes;
    }
}
