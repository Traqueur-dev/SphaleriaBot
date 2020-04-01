package fr.traqueur.sphaleriabot.managers.bad.commands;

import fr.traqueur.sphaleriabot.api.commands.CommandArgs;
import fr.traqueur.sphaleriabot.api.commands.ICommand;
import fr.traqueur.sphaleriabot.api.commands.annotations.Command;
import fr.traqueur.sphaleriabot.managers.bad.BadManager;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.TextChannel;

import javax.xml.soap.Text;

public class BadChannelListCommand extends ICommand {

    @Command(name = {"bad.channel.list"}, permittedRoles = {"537682405893341185", "537682450407620637"},
            permittedUsers = "285375027480756224", description = "Permet de voir les channels ignorés")
    public void onCommand(CommandArgs args) {
        BadManager manager = BadManager.getInstance();
        TextChannel channel = args.getChannel();
        EmbedBuilder builder = args.getSuccess();
        if (manager.getSettings().getIgnoredChannelID().isEmpty()) {
            builder.addField("", "Il n'y a aucun channel ignoré.", false);
        } else {
            for (Long l : manager.getSettings().getIgnoredChannelID()) {
                TextChannel t =args.getGuild().getTextChannelById(l);
                builder.addField("Channel " + t.getName() + ":", "Identifiant: " + t.getIdLong(), false);
            }
        }
        channel.sendMessage(builder.build()).queue();
    }
}
