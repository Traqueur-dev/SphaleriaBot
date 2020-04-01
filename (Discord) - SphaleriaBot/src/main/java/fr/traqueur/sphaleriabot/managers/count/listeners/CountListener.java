package fr.traqueur.sphaleriabot.managers.count.listeners;

import fr.traqueur.sphaleriabot.api.setting.Settings;
import fr.traqueur.sphaleriabot.api.setting.SettingsManager;
import fr.traqueur.sphaleriabot.managers.count.CountManager;
import fr.traqueur.sphaleriabot.managers.count.CountSettings;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.VoiceChannel;
import net.dv8tion.jda.core.events.ReadyEvent;
import net.dv8tion.jda.core.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.core.events.guild.member.GuildMemberLeaveEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

import java.util.ArrayList;
import java.util.List;

public class CountListener extends ListenerAdapter {

    @Override
    public void onReady(ReadyEvent event) {
        JDA jda = event.getJDA();
        Settings settings = SettingsManager.getInstance().getSettings();
        CountManager manager = CountManager.getInstance();
        Guild guild = jda.getGuildById(settings.getServerID());
        manager.updateMembersCount(guild);
    }

    @Override
    public void onGuildMemberJoin(GuildMemberJoinEvent event) {
        JDA jda = event.getJDA();
        Settings settings = SettingsManager.getInstance().getSettings();
        CountManager manager = CountManager.getInstance();
        Guild guild = jda.getGuildById(settings.getServerID());
        manager.updateMembersCount(guild);
    }

    @Override
    public void onGuildMemberLeave(GuildMemberLeaveEvent event) {
        JDA jda = event.getJDA();
        Settings settings = SettingsManager.getInstance().getSettings();
        CountManager manager = CountManager.getInstance();
        Guild guild = jda.getGuildById(settings.getServerID());
        manager.updateMembersCount(guild);
    }
}
