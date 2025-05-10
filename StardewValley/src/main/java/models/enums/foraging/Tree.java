package models.enums.foraging;

public class Tree {
    private TreeType type;
    private foragingTrees foragingTrees;

    public Tree(TreeType type, foragingTrees foragingTrees) {
        this.type = type;
        this.foragingTrees = foragingTrees;
    }

    public TreeType getType() {
        return type;
    }

    public void setType(TreeType type) {
        this.type = type;
    }
}
