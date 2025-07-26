package org.example.lwjgl3;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import org.example.Client.Main;

import java.io.IOException;

/** Launches the desktop (LWJGL3) application. */
public class Lwjgl3Launcher {
    private static Process serverProcess;

    public static void main(String[] args) {
        if (StartupHelper.startNewJvmIfRequired()) return; // This handles macOS support and helps on Windows.

        // Start the server process using Gradle
        startServerProcess();

        // Add shutdown hook to stop the server when the game exits
        Runtime.getRuntime().addShutdownHook(new Thread(() -> stopServerProcess()));

        createApplication();
    }

    private static void startServerProcess() {
        try {
            // Set the working directory to the project root
            String projectRoot = System.getProperty("user.dir");
            String gradlewPath = projectRoot + "/gradlew";
            ProcessBuilder pb = new ProcessBuilder(gradlewPath, ":core:runServer");
            pb.directory(new java.io.File(projectRoot));

//            ProcessBuilder pb = new ProcessBuilder("./gradlew", ":core:runServer");
//            pb.directory(new java.io.File(projectRoot)); // <-- Add this line
            pb.inheritIO(); // Optional: show server output in the console
            serverProcess = pb.start();
            System.out.println("[Launcher] Server process started.");
            Thread.sleep(3000);
        } catch (IOException | InterruptedException e) {
            System.err.println("[Launcher] Failed to start server process: " + e.getMessage());
        }
    }

    private static void stopServerProcess() {
        if (serverProcess != null && serverProcess.isAlive()) {
            System.out.println("[Launcher] Stopping server process...");
            serverProcess.destroy();
            try {
                serverProcess.waitFor();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            System.out.println("[Launcher] Server process stopped.");
        }
    }

    private static Lwjgl3Application createApplication() {
        return new Lwjgl3Application(new Main(), getDefaultConfiguration());
    }

    private static Lwjgl3ApplicationConfiguration getDefaultConfiguration() {
        Lwjgl3ApplicationConfiguration configuration = new Lwjgl3ApplicationConfiguration();
        configuration.setTitle("StardewValley");
        configuration.useVsync(true);
        configuration.setForegroundFPS(Lwjgl3ApplicationConfiguration.getDisplayMode().refreshRate + 1);
        configuration.setWindowedMode(1920, 1080);
        configuration.setWindowIcon("libgdx128.png", "libgdx64.png", "libgdx32.png", "libgdx16.png");
        return configuration;
    }
}
