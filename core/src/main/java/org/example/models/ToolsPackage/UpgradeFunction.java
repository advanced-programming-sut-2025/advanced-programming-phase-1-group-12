package org.example.models.ToolsPackage;

import org.example.models.Fundementals.Result;


@FunctionalInterface
public interface UpgradeFunction {

    Result execute(int currentLevel, Tools tools);
}