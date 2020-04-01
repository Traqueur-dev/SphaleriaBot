package fr.traqueur.sphaleriabot.managers.count.commands;

import fr.traqueur.sphaleriabot.api.commands.CommandArgs;
import fr.traqueur.sphaleriabot.api.commands.ICommand;
import fr.traqueur.sphaleriabot.api.commands.annotations.Command;
import fr.traqueur.sphaleriabot.managers.count.CountManager;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.TextChannel;

public class CountNameCommand extends ICommand {

    @Command(name = {"count.setname", "count.name"},  permittedRoles = {"537682405893341185", "537682450407620637"},
            permittedUsers = "285375027480756224", description = "Permet de changer le nom du compteur de membres.")
    public void onCommand(CommandArgs args) {
        TextChannel channel = args.getChannel();
        CountManager manager = CountManager.getInstance();
        if (args.size() != 1) {
            EmbedBuilder builder = args.getInfo();
            builder.addField("Usage:", args.getPrefix() + "count setname <name>", true);
            channel.sendMessage(builder.build()).queue();
            return;
        }

        String name = args.getArgs(0);
        manager.updateNameChannel(name);
        EmbedBuilder builder = args.getSuccess();
        builder.addField("Succés:", args.getSender() + " vient de modifier l'affichage du compteur de membres:\n" +
                name + " <nombre>.", true);
        channel.sendMessage(builder.build()).queue();
        manager.updateMembersCount(args.getGuild());
    }
}
