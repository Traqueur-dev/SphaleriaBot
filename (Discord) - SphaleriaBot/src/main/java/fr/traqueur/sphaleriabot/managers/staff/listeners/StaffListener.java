package fr.traqueur.sphaleriabot.managers.staff.listeners;

import fr.traqueur.sphaleriabot.managers.staff.StaffManager;
import net.dv8tion.jda.core.entities.*;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

public class StaffListener extends ListenerAdapter {

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if (!event.isFromType(ChannelType.TEXT)) { return; }
        StaffManager manager = StaffManager.getInstance();
        TextChannel channel = event.getTextChannel();
        if (!(channel.getIdLong() == manager.getChannelID())) { return; }
        User user = event.getAuthor();
        for (Member member : event.getGuild().getMembers()) {
            for (Role role: member.getRoles()) {
                if (manager.getRolesID().contains(role.getIdLong())) {
                    PrivateChannel pChan = user.openPrivateChannel().complete();
                    pChan.sendMessage("Vous venez de recevoir un message de la part de `"
                            + event.getAuthor().getName() + "` dans le channel réservé au staff veuillez y jeter un coup d'oeil.")
                            .queue();
                }
            }
        }
    }
}
