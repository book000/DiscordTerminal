package com.tomacheese.discordterminal;

import com.tomacheese.discordterminal.Event.Event_OnCommand;
import com.tomacheese.discordterminal.Event.Event_Ready;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.requests.GatewayIntent;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;

public class Main {
    static JDA jda;
    static long userId = -1L;
    static long roleId = -1L;
    static long channelId = -1L;
    static Map<String, String> alias = new HashMap<>();

    public static void main(String[] args) {
        System.out.println("DiscordTerminal created by Tomachi (Twitter: @book000)");
        System.out.println("ProjectPage: https://github.com/book000/DiscordTerminal");

        File file = new File("config.json");
        if (!file.exists()) {
            System.out.println("config.json not found. Quit the application.");
            System.exit(1);
            return;
        } else if (!file.canRead()) {
            System.out.println("config.json not readable. Quit the application.");
            System.exit(1);
            return;
        }

        String source;
        try {
            source = String.join("\n", Files.readAllLines(file.toPath()));
        } catch (IOException e) {
            System.out.println("config.json can't load (" + e.getMessage() + "). Quit the application.");
            System.exit(1);
            return;
        }
        JSONObject json = new JSONObject(source);

        if (!json.has("token")) {
            System.out.println("'token' not found in config.json.");
            System.exit(1);
            return;
        }
        String token = json.getString("token");

        if (json.has("userid")) {
            userId = json.getLong("userid");
        } else if (json.has("roleid")) {
            roleId = json.getLong("roleid");
        } else {
            System.out.println("'userid' or 'roleid' not found in config.json. Checks are made for channels only.");
        }

        if (!json.has("channelid")) {
            System.out.println("'channelid' not found in config.json.");
            System.exit(1);
            return;
        }
        channelId = json.getLong("channelid");

        if (json.has("alias")) {
            for (Map.Entry<String, Object> entry : json.getJSONObject("alias").toMap().entrySet()) {
                System.out.println("Added alias: " + entry.getKey() + " -> " + entry.getValue());
                alias.put(entry.getKey(), entry.getValue().toString());
            }
        }

        try {
            jda = JDABuilder
                    .createDefault(token)
                    .enableIntents(GatewayIntent.MESSAGE_CONTENT)
                    .setAutoReconnect(true)
                    .setBulkDeleteSplittingEnabled(false)
                    .setContextEnabled(false)
                    .addEventListeners(new Event_Ready())
                    .addEventListeners(new Event_OnCommand())
                    .build()
                    .awaitReady();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static long getUserID() {
        return userId;
    }

    public static long getRoleID() {
        return roleId;
    }

    public static long getChannelID() {
        return channelId;
    }

    public static Map<String, String> getAlias() {
        return alias;
    }

    public static JDA getJDA() {
        return jda;
    }
}
