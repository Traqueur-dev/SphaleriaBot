package fr.traqueur.sphaleriabot.managers.bad.listeners;

import fr.traqueur.sphaleriabot.api.commands.CommandFramework;
import fr.traqueur.sphaleriabot.managers.bad.BadManager;
import fr.traqueur.sphaleriabot.managers.bad.BadSettings;
import net.dv8tion.jda.core.entities.ChannelType;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class BadListener extends ListenerAdapter {

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if (event.getChannelType() != ChannelType.TEXT) return;
        Message message = event.getMessage();
        BadManager manager = BadManager.getInstance();
        BadSettings settings = manager.getSettings();
        String[] args = message.getContentRaw().split(" ");
        List<Long> channels = settings.getIgnoredChannelID();
        for (Long l : channels) {
            if (l == event.getChannel().getIdLong()) {
                return;
            }
        }
        for (String arg : args) {
            for (String badword : settings.getBadWords()) {
                if (arg.equalsIgnoreCase(badword) && !args[0].equalsIgnoreCase(CommandFramework.getPrefix() + "bad")) {
                    event.getMessage().delete().complete();
                    event.getChannel().sendMessage("Veillez à surveillez votre langage.").queue(m -> m.delete().queueAfter(5, TimeUnit.SECONDS));
                    return;
                }
            }
        }
        for (String link : settings.getBadLinks()) {
            if (message.getContentRaw().contains(link) && !event.getAuthor().isBot() && !args[0].equalsIgnoreCase(CommandFramework.getPrefix() + "bad")) {
                event.getMessage().delete().complete();
                event.getChannel().sendMessage("Vous avez saisi un lien qui n'est pas autorisé sur ce discord.").queue(m -> m.delete().queueAfter(5, TimeUnit.SECONDS));
                return;
            }
        }
    }
}
