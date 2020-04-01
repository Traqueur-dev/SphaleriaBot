package fr.traqueur.sphaleriabot.managers.profiles.commands;

import fr.traqueur.sphaleriabot.api.commands.CommandArgs;
import fr.traqueur.sphaleriabot.api.commands.ICommand;
import fr.traqueur.sphaleriabot.api.commands.annotations.Command;
import fr.traqueur.sphaleriabot.managers.profiles.Profile;
import fr.traqueur.sphaleriabot.managers.profiles.ProfileManager;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;

import java.util.List;

public class InviteAddCommand extends ICommand {

    @Command(name = {"invite.add","invites.add"},  permittedRoles = {"537682405893341185", "537682450407620637"},
            permittedUsers = "285375027480756224",description = "Permet d'ajouter des invitations bonus à un joueur")
    public void onCommand(CommandArgs args) {
        ProfileManager manager = ProfileManager.getInstance();
        TextChannel channel = args.getChannel();
        if (args.size() != 2) {
            EmbedBuilder builder = args.getInfo();
            builder.addField("Usage:", args.getPrefix() + "invite add <user> <nombre>", true);
            channel.sendMessage(builder.build()).queue();
            return;
        }
        Profile target = null;
        User targetU = args.asUser(0);
        if (targetU == null) {
            EmbedBuilder builder = args.getError();
            builder.addField("Erreur:", "Veuillez saisir un joueur valide.", true);
            channel.sendMessage(builder.build()).queue();
            return;
        }
        target = manager.getProfile(targetU);
        Integer num = args.asInteger(1);
        if (num == null) {
            EmbedBuilder builder = args.getError();
            builder.addField("Erreur:", "Veuillez saisir un nombre valide.", true);
            channel.sendMessage(builder.build()).queue();
            return;
        }
        boolean isAdd = manager.addInvite(target, num);
        EmbedBuilder builder;
        if (isAdd) {
            builder = args.getSuccess();
            builder.addField("Succés:", args.getSender() + " vient d'ajouter " + num
                    + " invitations à " + target.getName() + ".", true);
        } else {
            builder = args.getError();
            builder.addField("Erreur:", "Une erreur est survenue.", true);
        }
        channel.sendMessage(builder.build()).queue();
    }
}
