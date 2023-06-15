package com.tomacheese.discordterminal.Event;

import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class Event_Ready extends ListenerAdapter {
    @Override
    public void onReady(ReadyEvent event) {
        User user = event.getJDA().getSelfUser();
        String tag = user.getName() + (user.getDiscriminator().equals("0000") ? "" : "#" + user.getDiscriminator());
        System.out.println("Ready: " + tag);
    }
}
