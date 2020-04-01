package fr.traqueur.sphaleriabot.api.commands;

import fr.traqueur.sphaleriabot.api.commands.annotations.Command;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.PrivateChannel;

public class HelpCommand extends ICommand {

    @Command(name = {"help", "aide"}, description = "Affiche la liste des commandes.")
    public void onCommand(CommandArgs args) {
        EmbedBuilder builder = CommandFramework.getHelper();
        builder.addField("Prefix des commandes:", args.getPrefix(), false);
        PrivateChannel channel = args.getUser().openPrivateChannel().complete();
        channel.sendMessage(builder.build()).queue();
        args.getChannel().sendMessage(args.getUser().getAsMention() + ", vous venez de recevoir la liste des commandes en privé.").queue();
    }
}
