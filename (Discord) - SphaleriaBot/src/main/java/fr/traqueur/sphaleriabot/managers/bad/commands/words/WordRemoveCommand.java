package fr.traqueur.sphaleriabot.managers.bad.commands.words;

import fr.traqueur.sphaleriabot.api.commands.CommandArgs;
import fr.traqueur.sphaleriabot.api.commands.ICommand;
import fr.traqueur.sphaleriabot.api.commands.annotations.Command;
import fr.traqueur.sphaleriabot.managers.bad.BadManager;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.TextChannel;

public class WordRemoveCommand extends ICommand {

    @Command(name = {"bad.words.remove","bad.word.remove"}, permittedRoles = {"537682405893341185", "537682450407620637"},
            permittedUsers = "285375027480756224", description = "Permet d'enlever un mot interdit")
    public void onCommand(CommandArgs args) {
        BadManager manager = BadManager.getInstance();
        TextChannel channel = args.getChannel();
        if (args.size() != 1) {
            EmbedBuilder builder = args.getInfo();
            builder.addField("Usage:", args.getPrefix() + "bad word remove <word>", true);
            channel.sendMessage(builder.build()).queue();
            return;
        }
        String word = args.getArgs(0);
        boolean isRemove = manager.removeWord(word);
        EmbedBuilder builder = null;
        if (isRemove) {
            builder = args.getSuccess();
            builder.addField("Succés:", args.getSender() + " vient d'enlever un mot à la liste des mots interdits.", false);
        } else {
            builder = args.getError();
            builder.addField("Erreur:", "Ce mot n'existe pas.", false);
        }
        channel.sendMessage(builder.build()).queue();
    }
}
