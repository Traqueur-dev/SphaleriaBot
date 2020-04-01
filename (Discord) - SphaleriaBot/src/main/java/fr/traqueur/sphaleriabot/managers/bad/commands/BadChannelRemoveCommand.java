package fr.traqueur.sphaleriabot.managers.bad.commands;

import fr.traqueur.sphaleriabot.api.commands.CommandArgs;
import fr.traqueur.sphaleriabot.api.commands.ICommand;
import fr.traqueur.sphaleriabot.api.commands.annotations.Command;
import fr.traqueur.sphaleriabot.managers.bad.BadManager;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.TextChannel;

public class BadChannelRemoveCommand extends ICommand {

    @Command(name = {"bad.channel.remove"}, permittedRoles = {"537682405893341185", "537682450407620637"},
            permittedUsers = "285375027480756224",description = "Permet d'enlever un channel ignoré")
    public void onCommand(CommandArgs args) {
        BadManager manager = BadManager.getInstance();
        TextChannel channel = args.getChannel();
        if (args.size() != 1) {
            EmbedBuilder builder = args.getInfo();
            builder.addField("Usage:", args.getPrefix() + "bad channel remove <channel>", true);
            channel.sendMessage(builder.build()).queue();
            return;
        }
        TextChannel channelRemove = args.asChannel(0);
        boolean isAdd = manager.removeChannel(channelRemove.getIdLong());
        EmbedBuilder builder = null;
        if (isAdd) {
            builder = args.getSuccess();
            builder.addField("Succés:", args.getSender() + " vient d'enlever un channel à la liste des channels ignorés.", false);
        } else {
            builder = args.getError();
            builder.addField("Erreur:", "Ce channel n'est pas ignoré.", false);
        }
        channel.sendMessage(builder.build()).queue();
    }
}
