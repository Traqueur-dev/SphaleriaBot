package fr.traqueur.sphaleriabot.commands;

import fr.traqueur.sphaleriabot.api.commands.CommandArgs;
import fr.traqueur.sphaleriabot.api.commands.ICommand;
import fr.traqueur.sphaleriabot.api.commands.annotations.Command;
import fr.traqueur.sphaleriabot.api.tasks.SaveTask;
import net.dv8tion.jda.core.EmbedBuilder;

public class SaveCommand extends ICommand {

    @Command(name = {"save", "reload", "rl"}, permittedRoles = {"537682405893341185", "537682450407620637"},
            permittedUsers = "285375027480756224", description = "Permet de sauvegarder les données du bot")
    public void onCommand(CommandArgs args) {
        long time = SaveTask.save();
        EmbedBuilder builder = args.getSuccess();
        builder.addField("Succés:", "Sauvegarde des données efféctuée avec succés. (" + time + "ms)", true);
        args.getChannel().sendMessage(builder.build()).queue();
    }
}
