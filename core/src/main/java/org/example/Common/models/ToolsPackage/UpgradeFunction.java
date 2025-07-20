package org.example.Common.models.ToolsPackage;

import org.example.Common.models.Fundementals.Result;


@FunctionalInterface
public interface UpgradeFunction {

    Result execute(int currentLevel, Tools tools);
}
