package models.ToolsPackage;

import models.Fundementals.Location;
import models.Fundementals.Result;

public interface ToolFunction {
    Result execute(Location targetLocation, int skillLevel);
}
