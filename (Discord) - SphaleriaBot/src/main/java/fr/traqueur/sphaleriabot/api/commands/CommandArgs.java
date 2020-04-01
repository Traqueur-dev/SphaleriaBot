package fr.traqueur.sphaleriabot.api.commands;

import fr.traqueur.sphaleriabot.api.utils.EmbedHelper;
import lombok.Getter;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.entities.*;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.util.List;

@Getter
public class CommandArgs {

    private JDA jda;
    private Member member;
    private User user;
    private String sender;
    private Guild guild;
    private TextChannel channel;
    private String[] args;
    private String prefix;
    private Message message;

    public CommandArgs(MessageReceivedEvent event, String[] args, int sub, String prefix) {
        this.jda = event.getJDA();
        this.guild = event.getGuild();
        this.member = event.getMember();
        this.user = event.getAuthor();
        this.sender = event.getAuthor().getName();
        this.channel = event.getTextChannel();
        this.message = event.getMessage();
        this.prefix = prefix;
        String[] modArgs = new String[args.length - sub];
        for (int i = 0; i < args.length - sub; i++) {
            modArgs[i] = args[i+sub];
        }
        this.args = modArgs;
    }

    public String getArgs(int i) {
        return args[i];
    }

    public int size() {
        return args.length;
    }

    public Long asLong(int i) {
        try {
            return Long.parseLong(this.getArgs(i));
         } catch (Exception e) {
            return null;
        }
    }

    public Integer asInteger(int i) {
        try {
            return Integer.parseInt(this.getArgs(i));
        } catch (Exception e) {
            return null;
        }
    }

    public User asUser(int i) {
        List<Member> list = this.getGuild().getMembersByName(this.getArgs(i), true);
        Long l = this.asLong(i);
        if (l != null && this.getGuild().getMemberById(l) != null) {
           return this.getGuild().getMemberById(l).getUser();
        } else if (!list.isEmpty() && list.get(0) != null){
            return list.get(0).getUser();
        } else if (!this.getMessage().getMentionedMembers().isEmpty()){
            Member temp2 = this.getMessage().getMentionedMembers().get(0);
            return temp2.getUser();
        } else {
            return null;
        }
    }

    public Role asRole(int i) {
        String str = this.getArgs(i);
        Role role = null;
        Long l = this.asLong(i);
        if (l != null && this.getGuild().getRoleById(str) != null) {
            role = this.getGuild().getRoleById(str);
        } else if (!this.getGuild().getRolesByName(str, true).isEmpty()) {
            role = this.getGuild().getRolesByName(str, true).get(0);
        } else if (!this.getMessage().getMentionedRoles().isEmpty()) {
            role = this.getMessage().getMentionedRoles().get(0);
        }
        return role;
    }

    public TextChannel asChannel(int i) {
        String str = this.getArgs(i);
        TextChannel channel = null;
        Long l = this.asLong(i);
        if (l != null && this.getGuild().getTextChannelById(l) != null) {
            channel = this.getGuild().getTextChannelById(l);
        } else if (!this.getGuild().getTextChannelsByName(str, true).isEmpty()) {
            channel = this.getGuild().getTextChannelsByName(str, true).get(0);
        } else if (!this.getMessage().getMentionedChannels().isEmpty()) {
            channel = this.getMessage().getMentionedChannels().get(0);
        }
        return channel;
    }

    public EmbedBuilder getSuccess() { return EmbedHelper.getSuccessEmbed(); }
    public EmbedBuilder getError() { return EmbedHelper.getErrorEmbed(); }
    public EmbedBuilder getInfo() {
        return EmbedHelper.getInfoEmbed();
    }

}
