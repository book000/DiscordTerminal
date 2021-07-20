package com.tomacheese.DiscordTerminal.Event;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.tomacheese.DiscordTerminal.Main;

import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.hooks.SubscribeEvent;

public class Event_Ready {

	@SubscribeEvent
	public void onReadyEvent(ReadyEvent event) {
		System.out.println("Ready: " + event.getJDA().getSelfUser().getAsTag());

		event.getJDA().getTextChannelById(Main.getChannelID()).sendMessage(
				"**[" + new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date()) + " | "
						+ Main.getHostName() + "]** "
						+ "Start DiscordTerminal");
		Runtime.getRuntime().addShutdownHook(new Thread(
				() -> event.getJDA().getTextChannelById(Main.getChannelID())
						.sendMessage("**[" + new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date()) + " | "
								+ Main.getHostName() + "]** " + "End DiscordTerminal")));
	}
}
