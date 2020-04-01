package fr.traqueur.sphaleriabot.managers.profiles.listeners;

import fr.traqueur.sphaleriabot.api.setting.Settings;
import fr.traqueur.sphaleriabot.api.setting.SettingsManager;
import fr.traqueur.sphaleriabot.api.utils.Utils;
import fr.traqueur.sphaleriabot.managers.count.CountManager;
import fr.traqueur.sphaleriabot.managers.count.CountSettings;
import fr.traqueur.sphaleriabot.managers.profiles.Profile;
import fr.traqueur.sphaleriabot.managers.profiles.ProfileManager;
import fr.traqueur.sphaleriabot.managers.profiles.ProfileSettings;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.Event;
import net.dv8tion.jda.core.events.ReadyEvent;
import net.dv8tion.jda.core.events.guild.GenericGuildEvent;
import net.dv8tion.jda.core.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.core.events.guild.member.GuildMemberLeaveEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

public class ProfileListener extends ListenerAdapter {

    @Override
    public void onReady(ReadyEvent event) {
        JDA jda = event.getJDA();
        SettingsManager manager = SettingsManager.getInstance();
        ProfileManager profileManager = ProfileManager.getInstance();
        Settings settings = manager.getSettings();
        Guild guild = jda.getGuildById(settings.getServerID());
        int nbCreated = 0;
        for (Member member: Utils.getMembersWithoutBots(guild.getMembers())) {
            User user = member.getUser();
            boolean isCreated = profileManager.createProfile(user, jda.getSelfUser());
            if (isCreated) {
                System.out.println(user.getName() + "'s data created.");
                nbCreated++;
            }
        }
        if (nbCreated != 0) {
            System.out.println("Total: " + nbCreated);
        }
        ProfileManager.setInvites(guild.getInvites().complete());
    }

    @Override
    public void onGuildMemberJoin(GuildMemberJoinEvent event) {
        User user = event.getUser();
        ProfileManager manager = ProfileManager.getInstance();
        ProfileSettings settings = manager.getSettings();
        User inviter = manager.getInvite(event.getGuild()).getInviter();
        boolean isCreated = manager.createProfile(user, inviter);
        if (isCreated) {
            System.out.println(user.getName() + "'s data created because he joins.");
        }
        event.getGuild().getTextChannelById(settings.getJoinChannelID()).sendMessage(
                "**" + user.getName() + "** vient de rejoindre le Discord <:sphaleria:553680757147697180> "
                        + " -- invité par __" + inviter.getAsTag() + "__ - **(" + manager.getInvites(inviter) + " invitations)**").queue();
    }

    @Override
    public void onGuildMemberLeave(GuildMemberLeaveEvent event) {
        User user = event.getUser();
        ProfileManager manager = ProfileManager.getInstance();
        boolean isDeleted = manager.deleteProfile(user);
        if (isDeleted) {
            System.out.println(user.getName() + "'s data deleted because he leaves.");
        }
    }
}
