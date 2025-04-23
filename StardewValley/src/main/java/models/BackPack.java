package models;

import models.enums.ToolEnums.BackPackTypes;

import javax.tools.Tool;
import java.util.ArrayList;
import java.util.List;

public class BackPack {
    private List<Tools> tools;
    private BackPackTypes type;
    private int maxAmount;
    public BackPack(BackPackTypes type) {
        this.tools = new ArrayList<>();
        this.type = type;
    }
    public void setMaxAmount(int maxAmount) {
        this.maxAmount = maxAmount;
    }

    public List<Tools> getTools() {
        return tools;
    }

    public void setTools(List<Tools> tools) {
        this.tools = tools;
    }

    public BackPackTypes getType() {
        return type;
    }

    public void setType(BackPackTypes type) {
        this.type = type;
    }

    public int getMaxAmount() {
        return maxAmount;
    }
}
