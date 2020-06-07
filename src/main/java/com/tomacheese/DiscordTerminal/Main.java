package com.tomacheese.DiscordTerminal;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import com.tomacheese.DiscordTerminal.Event.Event_OnCommand;
import com.tomacheese.DiscordTerminal.Event.Event_Ready;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.hooks.AnnotatedEventManager;

public class Main {
	static JDA jda;
	static long userid = -1L;
	static long roleid = -1L;
	static long channelid = -1L;
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
			userid = json.getLong("userid");
		} else if (json.has("roleid")) {
			roleid = json.getLong("roleid");
		} else {
			System.out.println("'userid' or 'roleid' not found in config.json. Checks are made for channels only.");
		}

		if (!json.has("channelid")) {
			System.out.println("'channelid' not found in config.json.");
			System.exit(1);
			return;
		}
		channelid = json.getLong("channelid");

		if (json.has("alias")) {
			for (Map.Entry<String, Object> entry : json.getJSONObject("alias").toMap().entrySet()) {
				System.out.println("Added alias: " + entry.getKey() + " -> " + entry.getValue());
				alias.put(entry.getKey(), entry.getValue().toString());
			}
		}

		try {
			JDABuilder jdabuilder = JDABuilder.createDefault(token)
					.setAutoReconnect(true)
					.setBulkDeleteSplittingEnabled(false)
					.setContextEnabled(false)
					.setEventManager(new AnnotatedEventManager());

			jdabuilder.addEventListeners(new Event_Ready());
			jdabuilder.addEventListeners(new Event_OnCommand());

			jda = jdabuilder.build().awaitReady();
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}

	}

	public static long getUserID() {
		return userid;
	}

	public static long getRoleID() {
		return roleid;
	}

	public static long getChannelID() {
		return channelid;
	}

	public static Map<String, String> getAlias() {
		return alias;
	}

	public static JDA getJDA() {
		return jda;
	}

	public static String getHostName() {
		try {
			return InetAddress.getLocalHost().getHostName();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
