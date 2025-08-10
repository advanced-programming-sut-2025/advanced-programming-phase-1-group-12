package org.example.Common.models.Utils;

import io.github.cdimascio.dotenv.Dotenv;
import org.example.Common.models.NPC.NPC;
import org.json.JSONArray;
import org.json.JSONObject;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

public class NpcAI {
    private static Dotenv dotenv = Dotenv.configure().directory("../") // :)
        .ignoreIfMissing()
        .load();

    private static final String API_URL = "https://openrouter.ai/api/v1/chat/completions";
    private static final String API_KEY = dotenv.get("OPENROUTER_API_KEY");

    private static final HttpClient HTTP_CLIENT = HttpClient.newBuilder()
        .version(HttpClient.Version.HTTP_2)
        .connectTimeout(Duration.ofSeconds(10))
        .build();


    public static String generateDialogue(NPC npc, String context) {
        try {
            String prompt = buildPrompt(npc, context);
            return requestNpcDialogue(prompt);
        } catch (Exception e) {
            System.err.println("Dialogue generation failed: " + e.getMessage());
            return fallbackDialogue(npc);
        }
    }

    private static String buildPrompt(NPC npc, String context) {
        return new StringBuilder()
            .append("You are ").append(npc.getName())
            .append(", an NPC in a farming game similar to Stardew Valley.\n")
            .append("Your personality: ").append(npc.getPersonality()).append("\n")
            .append("Your job: ").append(npc.getJob()).append("\n\n")
            .append("Current Context:\n")
            .append(context).append("\n\n")
            .append("Instructions:\n")
            .append("- Reply with one short, natural response (1–2 sentences)\n")
            .append("- Stay true to your personality\n")
            .append("- Mention time, weather, or season when relevant\n")
            .append("- Use all prior conversation history\n")
            .append("- Recall past topics and interactions when possible\n")
            .append("- Show relationship growth\n")
            .append("- Build upon shared experiences\n")
            .append("- Avoid repeating past phrases\n")
            .append("- Keep it friendly but in-character\n\n")
            .append("Your response: ")
            .toString();
    }


    private static String sendAIRequest(String userMessage) throws Exception {
        JSONObject requestPayload = new JSONObject()
            .put("model", "deepseek/deepseek-chat-v3-0324:free")
            .put("messages", new JSONArray().put(new JSONObject()
                .put("role", "user")
                .put("content", userMessage)));

        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(API_URL))
            .header("Authorization", "Bearer " + API_KEY)
            .header("Content-Type", "application/json")
            .POST(HttpRequest.BodyPublishers.ofString(requestPayload.toString()))
            .build();

        HttpResponse<String> response = HTTP_CLIENT.send(request, HttpResponse.BodyHandlers.ofString());
        JSONObject jsonResponse = new JSONObject(response.body());

        if (jsonResponse.has("choices")) {
            return jsonResponse.getJSONArray("choices")
                .getJSONObject(0)
                .getJSONObject("message")
                .getString("content");
        }

        if (jsonResponse.has("error")) {
            throw new Exception("API Error: " + jsonResponse.getJSONObject("error").getString("message"));
        }

        throw new Exception("Unexpected API response: " + response.body());
    }


    private static String requestNpcDialogue(String message) {
        try {
            return sendAIRequest(message);
        } catch (Exception e) {
            System.err.println("Error retrieving NPC dialogue: " + e.getMessage());
            e.printStackTrace();
            return "Oh! Didn't expect to see you here today.";
        }
    }


    private static String fallbackDialogue(NPC npc) {
        String personality = npc.getPersonality().toLowerCase();
        if (personality.contains("introverted") || personality.contains("thoughtful")) {
            return "Hey there! You always brighten my day.";
        } else if (personality.contains("hardworking") || personality.contains("friendly")) {
            return "Every seed planted is a promise for tomorrow.";
        } else if (personality.contains("lazy")) {
            return "Ah… let's just watch the clouds for a while.";
        } else if (personality.contains("jealous")) {
            return "Looks like you've been busy… maybe too busy.";
        } else if (personality.contains("greedy")) {
            return "If it's valuable, I'm interested. Let's talk business.";
        } else {
            return "Nice to see you around. Feels like a good day.";
        }
    }
}
