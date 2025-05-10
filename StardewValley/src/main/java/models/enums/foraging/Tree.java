package models.enums.foraging;

public class Tree {
    private TreeType type;

    public Tree(TreeType type) {
        this.type = type;
    }

    public TreeType getType() {
        return type;
    }

    public void setType(TreeType type) {
        this.type = type;
    }
}
