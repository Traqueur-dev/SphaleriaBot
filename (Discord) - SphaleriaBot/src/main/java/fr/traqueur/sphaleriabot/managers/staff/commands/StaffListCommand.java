package fr.traqueur.sphaleriabot.managers.staff.commands;

import fr.traqueur.sphaleriabot.api.commands.CommandArgs;
import fr.traqueur.sphaleriabot.api.commands.ICommand;
import fr.traqueur.sphaleriabot.api.commands.annotations.Command;
import fr.traqueur.sphaleriabot.managers.staff.StaffManager;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Role;

import java.util.concurrent.TimeUnit;

public class StaffListCommand extends ICommand {

    @Command(name = {"staff.list"}, permittedRoles = {"537682405893341185", "537682450407620637"},
            permittedUsers = "285375027480756224", description = "Permet de voir la liste des roles staff")
    public void onCommand(CommandArgs args) {
        StaffManager manager = StaffManager.getInstance();
        EmbedBuilder builder = args.getSuccess();
        if (manager.getRolesID().isEmpty()) {
            builder.addField("", "Aucun role staff existant.", false);
        } else {
            for (Long id : manager.getRolesID()) {
                Role role = args.getGuild().getRoleById(id);
                builder.addField("Role: " + role.getName(), "Identifiant: " + role.getId(), false);
            }
        }
        builder.addField("", "", false);
        builder.addField("Information:", "Ce message se supprime dans 15 secondes.", false);
        args.getChannel().sendMessage(builder.build()).queue(
                m -> m.delete().queueAfter(15, TimeUnit.SECONDS)
        );
    }
}
