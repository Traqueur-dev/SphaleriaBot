package fr.traqueur.sphaleriabot.managers.profiles.commands;

import fr.traqueur.sphaleriabot.api.commands.CommandArgs;
import fr.traqueur.sphaleriabot.api.commands.ICommand;
import fr.traqueur.sphaleriabot.api.commands.annotations.Command;
import fr.traqueur.sphaleriabot.managers.profiles.Profile;
import fr.traqueur.sphaleriabot.managers.profiles.ProfileManager;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.TextChannel;

import java.util.List;

public class LeaderboardCommand extends ICommand {

    @Command(name = {"leaderboard","classement", "top"}, description = "Permet de voir le classement des invitations")
    public void onCommand(CommandArgs args) {
        TextChannel channel = args.getChannel();
        ProfileManager manager = ProfileManager.getInstance();
        List<Profile> top = manager.getClassement();
        StringBuilder builder = new StringBuilder();
        builder.append("Classement général:\n");
        int size = top.size() == 1 ? 0 : top.size() - 1;
        for (int i = 0; i < Math.min(top.size(), 15); i++) {
            Profile profile = top.get(size - i);
            builder.append("[#").append(i + 1).append("] **").append(profile.getName()).append("** - **").append(profile.getInvites()).append("** invitations.\n");
        }
        for (int i = 0; i < top.size(); i++) {
            Profile p = top.get(size - i);
            if (p.getId() != args.getUser().getIdLong()) { continue; }
            builder.append("**Votre position:** ").append((i + 1) == 1 ? (i + 1) + "er" : (i + 1) + "ème").append(" - **").append(p.getInvites()).append("** invitations.\n");
        }
        channel.sendMessage(builder.toString()).queue();
    }
}
