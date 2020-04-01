package fr.traqueur.sphaleriabot.commands;

import fr.traqueur.sphaleriabot.SphaleriaBot;
import fr.traqueur.sphaleriabot.api.commands.CommandArgs;
import fr.traqueur.sphaleriabot.api.commands.ICommand;
import fr.traqueur.sphaleriabot.api.commands.annotations.Command;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

public class StopCommand extends ICommand {

    @Command(name = "stop",  permittedRoles = {"537682405893341185", "537682450407620637"},
            permittedUsers = "285375027480756224", description = "Permet d'éteindre le bot")
    public void onCommand(CommandArgs args) {
        EmbedBuilder builder = args.getSuccess();
        builder.addField("Succés:", "Bot disconnected...", true);
        args.getChannel().sendMessage(builder.build()).queue();
        SphaleriaBot.getInstance().getClient().stop();
    }
}
