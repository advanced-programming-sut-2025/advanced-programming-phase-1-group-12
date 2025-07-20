package org.example.Common.models.ToolsPackage;

import org.example.Common.models.Fundementals.Location;
import org.example.Common.models.Fundementals.Result;

@FunctionalInterface
public interface ToolFunction {

    Result execute(Location targetLocation, int skillLeve, Tools tool);
}
