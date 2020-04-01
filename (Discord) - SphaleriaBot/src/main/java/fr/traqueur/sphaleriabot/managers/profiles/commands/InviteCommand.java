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

public class InviteCommand extends ICommand {

    @Command(name = {"invite", "invites"},description = "Permet de voir ses invitations où celle d'un autre membre")
    public void onCommand(CommandArgs args) {
        ProfileManager manager = ProfileManager.getInstance();
        TextChannel channel = args.getChannel();
        if (args.size() > 1) {
            EmbedBuilder builder = args.getInfo();
            builder.addField("Information:", args.getPrefix() + "invite [user]", true);
            channel.sendMessage(builder.build()).queue();
            return;
        }
        User user = args.getUser();
        Profile profile = manager.getProfile(user);
        Profile target = profile;
        if (args.size() == 1) {
            List<Member> list = args.getGuild().getMembersByName(args.getArgs(0), true);
            Long l = args.asLong(0);
            if (l != null && args.getGuild().getMemberById(l) != null) {
                target = manager.getProfile(args.asLong(0));
            } else if (!list.isEmpty() && list.get(0) != null){
                target = manager.getProfile(list.get(0).getUser());
            } else if (!args.getMessage().getMentionedMembers().isEmpty()){
                Member temp2 = args.getMessage().getMentionedMembers().get(0);
                target = manager.getProfile(temp2.getUser());
            } else {
                EmbedBuilder builder = args.getError();
                builder.addField("Erreur:", "Veuillez saisir un joueur valide.", true);
                channel.sendMessage(builder.build()).queue();
                return;
            }
        }
        String userS = profile == target ? "Vous avez" : target.getName() + " a";
        channel.sendMessage(userS + " actuellement **" + manager.getInvites(target) + "** invitations (**"
                + target.getJoinInvite() + "** joins ; **"
                + target.getBonusInvite() + "** bonus ; **"
                + target.getLeaveInvite() + "** leaves)"
        ).queue();

    }
}
