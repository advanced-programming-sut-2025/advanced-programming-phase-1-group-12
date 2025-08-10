package org.example.Common.saveGame;

import org.example.Common.models.Fundementals.Game;

import java.io.FileWriter;
import java.nio.charset.StandardCharsets;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class GameDatabase {
    private static final String DB_URL = "jdbc:sqlite:game_saves.db";
    private GameDatabase() {}
    public static void init() throws SQLException {
        try (Connection c = DriverManager.getConnection(DB_URL);
             Statement s = c.createStatement()) {
            s.execute("""
                CREATE TABLE IF NOT EXISTS GameSaves (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    save_name TEXT NOT NULL UNIQUE,
                    data BLOB NOT NULL,
                    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
                );
            """);
        }
    }
    public static void save(Game game, String saveName) throws Exception {
        if (saveName == null || saveName.isBlank()) {
            throw new IllegalArgumentException("Save name cannot be null or empty");
        }
        String sql = "INSERT OR REPLACE INTO GameSaves (save_name, data) VALUES (?, ?)";
        try (Connection c = DriverManager.getConnection(DB_URL);
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, saveName);
            ps.setBytes(2, GameSaveManager.serializeAndCompress(game));
            ps.executeUpdate();
        }
    }

    public static List<String> list() throws SQLException {
        List<String> out = new ArrayList<>();
        String sql = "SELECT save_name FROM GameSaves ORDER BY created_at DESC";
        try (Connection c = DriverManager.getConnection(DB_URL);
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) out.add(rs.getString(1));
        }
        return out;
    }
    public static Game load(String saveName) throws Exception {
        String sql = "SELECT data FROM GameSaves WHERE save_name = ?";
        try (Connection c = DriverManager.getConnection(DB_URL);
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, saveName);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) throw new Exception("Save not found: " + saveName);
                return GameSaveManager.decompressAndDeserialize(rs.getBytes(1));
            }
        }
    }
    public static void exportAsJson(String saveName, String outPath) throws Exception {
        String sql = "SELECT data FROM GameSaves WHERE save_name = ?";
        try (Connection c = DriverManager.getConnection(DB_URL);
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, saveName);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) throw new Exception("Save not found: " + saveName);
                Game game = GameSaveManager.decompressAndDeserialize(rs.getBytes(1));
                try (FileWriter fw = new FileWriter(outPath, StandardCharsets.UTF_8)) {
                    GameSaveManager.getMapper().writeValue(fw, game);
                }
            }
        }
    }
}

