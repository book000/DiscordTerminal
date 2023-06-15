package com.tomacheese.discordterminal.Event;

import com.tomacheese.discordterminal.Main;
import com.tomacheese.discordterminal.Task.Task_ExecTerminalCommand;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.Map;

public class Event_OnCommand extends ListenerAdapter {
    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
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
            boolean isAllowRole = event.getMember().getRoles().stream().anyMatch(role -> role != null && role.getIdLong() == Main.getRoleID());
            if (!isAllowRole) {
                return;
            }
        }

        event.getMessage().addReaction(Emoji.fromUnicode("👁")).queue();

        if (text.split("\n").length != 1) {
            channel.sendMessage("> Command: `" + text + "`\n```SYSTEM ERROR: Multi-line commands are not supported.```").queue();
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
            channel.sendMessage("> Alias: `%" + aliasName + "`\n```SYSTEM ERROR: This alias is not defined.```").queue();
            return;
        }

        new Task_ExecTerminalCommand(alias.get(aliasName)).start();
    }

    void onCommand(String command) {
        new Task_ExecTerminalCommand(command).start();
    }
}
