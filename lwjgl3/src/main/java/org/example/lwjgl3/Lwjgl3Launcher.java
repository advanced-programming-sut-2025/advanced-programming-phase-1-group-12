package org.example.lwjgl3;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import org.example.Client.Main;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.TimeUnit;

/** Launches the desktop (LWJGL3) application. */
public class Lwjgl3Launcher {
    private static Process serverProcess;
    private static final int SERVER_PORT = 8080;
    private static final int MAX_STARTUP_WAIT_TIME = 30000; // 30 seconds
    private static final int HEALTH_CHECK_INTERVAL = 1000; // 1 second

    public static void main(String[] args) {
        if (StartupHelper.startNewJvmIfRequired()) return; // This handles macOS support and helps on Windows.

        // Server startup disabled - start manually with: ./gradlew :core:runServer
        System.out.println("[Launcher] ℹ️  Server startup disabled. Start manually with: ./gradlew :core:runServer");
        System.out.println("[Launcher] ℹ️  Game will start without multiplayer server.");

        // Comment out automatic server startup
        // if (startServerProcess()) {
        //     System.out.println("[Launcher] ✅ Server started successfully!");
        // } else {
        //     System.err.println("[Launcher] ❌ Failed to start server. Game will continue without multiplayer.");
        // }

        // Comment out shutdown hook since server is not started automatically
        // Runtime.getRuntime().addShutdownHook(new Thread(() -> stopServerProcess()));

        createApplication();
    }

    private static boolean startServerProcess() {
        try {
            // Set the working directory to the project root
            String projectRoot = System.getProperty("user.dir");
            Path gradlewPath = getGradlewPath(projectRoot);

            if (!Files.exists(gradlewPath)) {
                System.err.println("[Launcher] ❌ Gradlew not found at: " + gradlewPath);
                return false;
            }

            System.out.println("[Launcher] 🚀 Starting server process...");
            System.out.println("[Launcher] 📁 Working directory: " + projectRoot);
            System.out.println("[Launcher] 🔧 Gradlew path: " + gradlewPath);

            ProcessBuilder pb = new ProcessBuilder(gradlewPath.toString(), ":core:runServer");
            pb.directory(new java.io.File(projectRoot));
            pb.inheritIO(); // Show server output in the console

            serverProcess = pb.start();
            System.out.println("[Launcher] ⏳ Waiting for server to start...");

            // Wait for server to be ready
            if (waitForServerReady()) {
                System.out.println("[Launcher] ✅ Server is ready and responding!");
                return true;
            } else {
                System.err.println("[Launcher] ❌ Server failed to start within timeout period");
                stopServerProcess();
                return false;
            }

        } catch (IOException e) {
            System.err.println("[Launcher] ❌ Failed to start server process: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    private static Path getGradlewPath(String projectRoot) {
        String os = System.getProperty("os.name").toLowerCase();
        String gradlewName = os.contains("win") ? "gradlew.bat" : "gradlew";
        return Paths.get(projectRoot, gradlewName);
    }

    private static boolean waitForServerReady() {
        long startTime = System.currentTimeMillis();

        while (System.currentTimeMillis() - startTime < MAX_STARTUP_WAIT_TIME) {
            try {
                // Check if server process is still alive
                if (serverProcess != null && !serverProcess.isAlive()) {
                    System.err.println("[Launcher] ❌ Server process died unexpectedly");
                    return false;
                }

                // Try to connect to server health endpoint
                if (isServerHealthy()) {
                    return true;
                }

                // Wait before next check
                Thread.sleep(HEALTH_CHECK_INTERVAL);

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return false;
            }
        }

        return false;
    }

    private static boolean isServerHealthy() {
        try {
            URL url = new URL("http://localhost:" + SERVER_PORT + "/health");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(2000);
            connection.setReadTimeout(2000);

            int responseCode = connection.getResponseCode();
            return responseCode == 200;

        } catch (Exception e) {
            // Server not ready yet, this is expected during startup
            return false;
        }
    }

    private static void stopServerProcess() {
        if (serverProcess != null && serverProcess.isAlive()) {
            System.out.println("[Launcher] 🛑 Stopping server process...");
            serverProcess.destroy();

            // Give it a chance to shutdown gracefully
            try {
                if (!serverProcess.waitFor(5, TimeUnit.SECONDS)) { // Wait up to 5 seconds
                    System.out.println("[Launcher] ⚠️ Server didn't stop gracefully, forcing shutdown...");
                    serverProcess.destroyForcibly();
                    serverProcess.waitFor();
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.err.println("[Launcher] ❌ Error waiting for server to stop: " + e.getMessage());
            }

            System.out.println("[Launcher] ✅ Server process stopped.");
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
