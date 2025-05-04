package models.ToolsPackage;

import models.Fundementals.Result;


@FunctionalInterface
public interface UpgradeFunction {

    Result execute(int currentLevel);
}