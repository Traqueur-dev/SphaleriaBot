package fr.traqueur.sphaleriabot.managers.contest.commands;

import fr.traqueur.sphaleriabot.api.commands.CommandArgs;
import fr.traqueur.sphaleriabot.api.commands.ICommand;
import fr.traqueur.sphaleriabot.api.commands.annotations.Command;
import fr.traqueur.sphaleriabot.api.setting.SettingsManager;
import fr.traqueur.sphaleriabot.managers.contest.ContestManager;
import fr.traqueur.sphaleriabot.managers.contest.Participant;
import fr.traqueur.sphaleriabot.managers.profiles.Profile;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.TextChannel;

import java.util.List;

public class ContestLeaderboardCommand extends ICommand {

    @Command(name = {"contest.leaderboard", "contest.top", "contest.classement"},
            description = "Permet de voir le classement d'un concours")
    public void onCommand(CommandArgs args) {
        ContestManager manager = ContestManager.getInstance();
        TextChannel channel = args.getChannel();
        if (!manager.haveContest()) {
            EmbedBuilder builder = args.getError();
            builder.addField("Erreur:", "Il n'y a pas de concours en cours.", false);
            channel.sendMessage(builder.build()).queue();
            return;
        }
        List<Participant> top = manager.getClassement();
        StringBuilder builder = new StringBuilder();
        builder.append("Classement du concours **" + manager.getContest().getName() + "**:\n");
        int size = top.size() == 1 ? 0 : top.size() - 1;
        for (int i = 0; i < Math.min(top.size(), 10); i++) {
            Participant profile = top.get(size - i);
            builder.append("[#").append(i + 1).append("] **").append(profile.getName()).append("** - **").append(profile.getInvites()).append("** invitations.\n");
        }
        for (int i = 0; i < top.size(); i++) {
            Participant p = top.get(size - i);
            if (p.getId() != args.getUser().getIdLong()) { continue; }
            builder.append("**Votre position:** ").append((i + 1) == 1 ? (i + 1) + "er" : (i + 1) + "ème").append(" - **").append(p.getInvites()).append("** invitations.\n");
        }
       channel.sendMessage(builder.toString()).queue();
    }
}
