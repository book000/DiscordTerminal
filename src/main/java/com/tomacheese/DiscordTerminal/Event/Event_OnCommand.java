package com.tomacheese.DiscordTerminal.Event;

import java.util.Map;

import com.tomacheese.DiscordTerminal.Main;
import com.tomacheese.DiscordTerminal.Task.Task_ExecTerminalCommand;

import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.SubscribeEvent;

public class Event_OnCommand {
	@SubscribeEvent
	public void onMessageReceivedEvent(MessageReceivedEvent event) {
		MessageChannel channel = event.getChannel();
		String text = event.getMessage().getContentRaw();
		if (Main.getChannelID() != event.getChannel().getIdLong()) {
			return;
		}

		if (event.getAuthor().getIdLong() == event.getJDA().getSelfUser().getIdLong()) {
			return;
		}

		if (Main.getUserID() != -1L && Main.getUserID() != event.getAuthor().getIdLong()) {
			return;
		}

		if (Main.getRoleID() != -1L && event.getMember() != null) {
			boolean isAllowRole = event
					.getMember()
					.getRoles()
					.stream()
					.filter(role -> role != null && role.getIdLong() == Main.getRoleID())
					.count() != 0;
			if (!isAllowRole) {
				return;
			}
		}

		event.getMessage().addReaction("👁").queue();

		if (text.split("\n").length != 1) {
			channel.sendMessage("> Command: `" + text + "`\n```SYSTEM ERROR: Multi-line commands are not supported.```")
					.queue();
			return;
		}

		if (text.startsWith("%")) {
			// ex. `%restart`
			String aliasName = text.substring(1).trim();
			onAliasCommand(channel, aliasName);
		} else {
			onCommand(text);
		}
	}

	void onAliasCommand(MessageChannel channel, String aliasName) {
		Map<String, String> alias = Main.getAlias();

		if (!alias.containsKey(aliasName)) {
			channel.sendMessage("> Alias: `%" + aliasName + "`\n```SYSTEM ERROR: This alias is not supported.```")
					.queue();
			return;
		}

		new Task_ExecTerminalCommand(alias.get(aliasName)).start();
	}

	void onCommand(String command) {
		new Task_ExecTerminalCommand(command).start();
	}
}
