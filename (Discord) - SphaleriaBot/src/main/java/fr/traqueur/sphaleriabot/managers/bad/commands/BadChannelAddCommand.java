package fr.traqueur.sphaleriabot.managers.bad.commands;

import fr.traqueur.sphaleriabot.api.commands.CommandArgs;
import fr.traqueur.sphaleriabot.api.commands.ICommand;
import fr.traqueur.sphaleriabot.api.commands.annotations.Command;
import fr.traqueur.sphaleriabot.managers.bad.BadManager;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.TextChannel;

import javax.xml.soap.Text;

public class BadChannelAddCommand extends ICommand {

    @Command(name = {"bad.channel.add"}, permittedRoles = {"537682405893341185", "537682450407620637"},
            permittedUsers = "285375027480756224", description = "Permet d'ajouter un channel ignoré")
    public void onCommand(CommandArgs args) {
        BadManager manager = BadManager.getInstance();
        TextChannel channel = args.getChannel();
        if (args.size() != 1) {
            EmbedBuilder builder = args.getInfo();
            builder.addField("Usage:", args.getPrefix() + "bad channel add <channel>", true);
            channel.sendMessage(builder.build()).queue();
            return;
        }
        TextChannel channelAdd = args.asChannel(0);
        boolean isAdd = manager.addChannel(channelAdd.getIdLong());
        EmbedBuilder builder = null;
        if (isAdd) {
            builder = args.getSuccess();
            builder.addField("Succés:", args.getSender() + " vient d'ajouter un channel à la liste des channels ignorés.", false);
        } else {
            builder = args.getError();
            builder.addField("Erreur:", "Ce channel est déjà ignoré.", false);
        }
        channel.sendMessage(builder.build()).queue();
    }
}
