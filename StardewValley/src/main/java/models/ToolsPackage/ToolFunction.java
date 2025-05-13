package models.ToolsPackage;

import models.Fundementals.Location;
import models.Fundementals.Result;

@FunctionalInterface
public interface ToolFunction {

    Result execute(Location targetLocation, int skillLeve, Tools tool);
}