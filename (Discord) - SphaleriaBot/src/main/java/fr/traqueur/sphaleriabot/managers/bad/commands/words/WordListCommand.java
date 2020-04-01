package fr.traqueur.sphaleriabot.managers.bad.commands.words;

import fr.traqueur.sphaleriabot.api.commands.CommandArgs;
import fr.traqueur.sphaleriabot.api.commands.ICommand;
import fr.traqueur.sphaleriabot.api.commands.annotations.Command;
import fr.traqueur.sphaleriabot.managers.bad.BadManager;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.TextChannel;

public class WordListCommand extends ICommand {

    @Command(name = {"bad.words.list","bad.word.list"}, permittedRoles = {"537682405893341185", "537682450407620637"},
            permittedUsers = "285375027480756224", description = "Permet de voir la liste des mots interdits")
    public void onCommand(CommandArgs args) {
        BadManager manager = BadManager.getInstance();
        TextChannel channel = args.getChannel();
        EmbedBuilder builder = args.getSuccess();
        if (manager.getSettings().getBadWords().isEmpty()) {
            builder.addField("", "Il n'y a aucun mot interdit.", false);
        } else {
            int i = 1;
            for (String str : manager.getSettings().getBadWords()) {
                builder.addField("Mot n°" + i + ":", str, false);
            }
        }
        channel.sendMessage(builder.build()).queue();
    }
}

