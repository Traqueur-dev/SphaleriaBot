package fr.traqueur.sphaleriabot.managers.contest.commands;

import fr.traqueur.sphaleriabot.api.commands.CommandArgs;
import fr.traqueur.sphaleriabot.api.commands.ICommand;
import fr.traqueur.sphaleriabot.api.commands.annotations.Command;
import fr.traqueur.sphaleriabot.managers.contest.ContestManager;
import fr.traqueur.sphaleriabot.managers.contest.Participant;
import fr.traqueur.sphaleriabot.managers.profiles.Profile;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.TextChannel;

import java.util.List;

public class ContestStopCommand extends ICommand {

    @Command(name = {"contest.stop"},  permittedRoles = {"537682405893341185", "537682450407620637"},
            permittedUsers = "285375027480756224", description = "Permet d'arrêter un concours")
    public void onCommand(CommandArgs args) {
        ContestManager manager = ContestManager.getInstance();
        TextChannel channel = args.getChannel();
        if (!manager.haveContest()) {
            EmbedBuilder builder = args.getError();
            builder.addField("Erreur:", "Il n'y a pas de concours en cours.", false);
            channel.sendMessage(builder.build()).queue();
            return;
        }

        boolean isDeleted = manager.stopContest();
        EmbedBuilder builderE = null;
        if (isDeleted) {
            builderE = args.getSuccess();
            builderE.addField("Succés:", args.getSender() + " vient de stoper le concours.", false);
        } else {
            builderE = args.getError();
            builderE.addField("Erreur:", "Une erreur est survenue lors de l'arrêt du concours.", false);
        }
        channel.sendMessage(builderE.build()).queue();
    }
}
