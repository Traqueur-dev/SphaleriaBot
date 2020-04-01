package fr.traqueur.sphaleriabot.managers.bad.commands.link;

import fr.traqueur.sphaleriabot.api.commands.CommandArgs;
import fr.traqueur.sphaleriabot.api.commands.ICommand;
import fr.traqueur.sphaleriabot.api.commands.annotations.Command;
import fr.traqueur.sphaleriabot.managers.bad.BadManager;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.TextChannel;

public class LinkAddCommand extends ICommand {
    @Command(name = {"bad.link.add","bad.links.add"}, permittedRoles = {"537682405893341185", "537682450407620637"},
            permittedUsers = "285375027480756224", description = "Permet d'ajouter un lien interdit")
    public void onCommand(CommandArgs args) {
        BadManager manager = BadManager.getInstance();
        TextChannel channel = args.getChannel();
        if (args.size() != 1) {
            EmbedBuilder builder = args.getInfo();
            builder.addField("Usage:", args.getPrefix() + "bad link add <link>", true);
            channel.sendMessage(builder.build()).queue();
            return;
        }
        String link = args.getArgs(0);
        boolean isAdd = manager.addLink(link);
        EmbedBuilder builder = null;
        if (isAdd) {
            builder = args.getSuccess();
            builder.addField("Succés:", args.getSender() + " vient d'ajouter un lien à la liste des liens interdits.", false);
        } else {
            builder = args.getError();
            builder.addField("Erreur:", "Ce lien existe déjà.", false);
        }
        channel.sendMessage(builder.build()).queue();
    }
}
