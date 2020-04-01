package fr.traqueur.sphaleriabot.managers.staff.commands;

import fr.traqueur.sphaleriabot.api.commands.CommandArgs;
import fr.traqueur.sphaleriabot.api.commands.ICommand;
import fr.traqueur.sphaleriabot.api.commands.annotations.Command;
import fr.traqueur.sphaleriabot.managers.staff.StaffManager;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Role;
import net.dv8tion.jda.core.entities.TextChannel;

public class StaffRemoveCommand extends ICommand {

    @Command(name = {"staff.remove"}, permittedRoles = {"537682405893341185", "537682450407620637"},
            permittedUsers = "285375027480756224", description = "Permet d'enlever un role comme staff")
    public void onCommand(CommandArgs args) {
        TextChannel channel = args.getChannel();
        StaffManager manager = StaffManager.getInstance();
        if (args.size() != 1) {
            EmbedBuilder builder = args.getInfo();
            builder.addField("Usage:", args.getPrefix() + "staff add <role>", true);
            channel.sendMessage(builder.build()).queue();
            return;
        }
        Role role = args.asRole(0);
        if (role == null) {
            EmbedBuilder builder = args.getError();
            builder.addField("Erreur:", "Veuillez saisir un role valide.", true);
            channel.sendMessage(builder.build()).queue();
            return;
        }
        boolean isAdd = manager.removeRoleID(role.getIdLong());
        EmbedBuilder builder = null;
        if (isAdd) {
            builder = args.getSuccess();
            builder.addField("Succés:", args.getSender() + " vient d'enlever le role " + role.getName()
                    + " aux roles staff.", true);
        } else {
            builder = args.getError();
            builder.addField("Erreur:", "Une erreur est survenue.", true);
        }
        channel.sendMessage(builder.build()).queue();
    }
}
