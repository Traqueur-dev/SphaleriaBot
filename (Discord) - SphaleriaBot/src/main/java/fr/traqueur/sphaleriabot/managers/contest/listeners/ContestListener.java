package fr.traqueur.sphaleriabot.managers.contest.listeners;

import fr.traqueur.sphaleriabot.managers.contest.ContestManager;
import fr.traqueur.sphaleriabot.managers.profiles.Profile;
import fr.traqueur.sphaleriabot.managers.profiles.ProfileManager;
import net.dv8tion.jda.core.entities.Invite;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.ShutdownEvent;
import net.dv8tion.jda.core.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.core.events.guild.member.GuildMemberLeaveEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

public class ContestListener extends ListenerAdapter {

    @Override
    public void onShutdown(ShutdownEvent event) {
        ContestManager manager = ContestManager.getInstance();
        manager.saveContest();
    }

    @Override
    public void onGuildMemberJoin(GuildMemberJoinEvent event) {
        ContestManager manager = ContestManager.getInstance();
        Invite invite = manager.getInvite(event.getGuild());
        User inviter = invite.getInviter();
        manager.addParticipant(inviter);
        manager.addInvite(inviter);
    }

    @Override
    public void onGuildMemberLeave(GuildMemberLeaveEvent event) {
        ContestManager manager = ContestManager.getInstance();
        ProfileManager profileManager = ProfileManager.getInstance();
        User user = event.getUser();
        Profile profile = profileManager.getProfile(user);
        Profile inviter = profileManager.getProfile(profile.getIdInviter());
        manager.removeInvite(event.getJDA().getUserById(inviter.getId()));
    }
}
