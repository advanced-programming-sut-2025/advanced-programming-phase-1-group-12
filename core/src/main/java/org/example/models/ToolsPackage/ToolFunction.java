package org.example.models.ToolsPackage;

import org.example.models.Fundementals.Location;
import org.example.models.Fundementals.Result;

@FunctionalInterface
public interface ToolFunction {

    Result execute(Location targetLocation, int skillLeve, Tools tool);
}