package fr.traqueur.sphaleriabot.managers.contest.commands;

import fr.traqueur.sphaleriabot.api.commands.CommandArgs;
import fr.traqueur.sphaleriabot.api.commands.ICommand;
import fr.traqueur.sphaleriabot.api.commands.annotations.Command;
import fr.traqueur.sphaleriabot.managers.contest.ContestManager;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.TextChannel;

public class ContestCreateCommand extends ICommand {

    @Command(name = {"contest.start"},  permittedRoles = {"537682405893341185", "537682450407620637"},
            permittedUsers = "285375027480756224", description = "Permet de créer un concours")
    public void onCommand(CommandArgs args) {
        ContestManager manager = ContestManager.getInstance();
        TextChannel channel = args.getChannel();
        if (manager.haveContest()) {
            EmbedBuilder builder = args.getError();
            builder.addField("Erreur:", "Il y a déjà un concours en cours.", false);
            channel.sendMessage(builder.build()).queue();
            return;
        }
        if (args.size() != 1) {
            EmbedBuilder builder = args.getInfo();
            builder.addField("usage:", args.getPrefix() +"contest start <name>", false);
            channel.sendMessage(builder.build()).queue();
            return;
        }

        boolean isCreated = manager.initContest(args.getArgs(0));
        EmbedBuilder builder = null;
        if (isCreated) {
            builder = args.getSuccess();
            builder.addField("Succés:", args.getSender() + " vient de lancer un concours.", false);
        } else {
            builder = args.getError();
            builder.addField("Erreur:", "Une erreur est survenue lors de la création du concours.", false);
        }
        channel.sendMessage(builder.build()).queue();
    }
}
