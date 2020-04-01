package fr.traqueur.sphaleriabot.commands;

import fr.traqueur.sphaleriabot.api.commands.CommandArgs;
import fr.traqueur.sphaleriabot.api.commands.ICommand;
import fr.traqueur.sphaleriabot.api.commands.annotations.Command;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.exceptions.PermissionException;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class ClearCommand extends ICommand {

    @Command(name = "clear", permittedRoles = {"537682405893341185", "537682450407620637"},
            permittedUsers = "285375027480756224", description = "Permet d'effacer les messages d'un salon")
    public void onCommand(CommandArgs args) {
        User user = args.getUser();
        TextChannel channel = args.getChannel();
        if (!args.getGuild().getSelfMember().hasPermission(Permission.MESSAGE_MANAGE)) {
            EmbedBuilder builder = args.getInfo();
            builder.addField("Erreur:", "Le bot doit avoir les permissions `Manage Message` et `Message History`...", true);
            channel.sendMessage(builder.build()).queue();
            return;
        }
        if (args.size() != 1) {
            EmbedBuilder builder = args.getInfo();
            builder.addField("Usage:", args.getPrefix() + "clear <nombre|all>", true);
            channel.sendMessage(builder.build()).queue();
            return;
        }
        Integer num = args.asInteger(0);
        if (num == null && args.getArgs(0).equalsIgnoreCase("all")) {
            TextChannel newChan = (TextChannel) args.getGuild().getController().createCopyOfChannel(channel).complete();
            int position = channel.getPosition();
            newChan.getManager().setPosition(position + 1).complete();
            channel.delete().complete();
            EmbedBuilder builder = args.getSuccess();
            builder.addField("Succés:", args.getSender() + " vient de supprimer tout les messages du salon.", true);
            newChan.sendMessage(builder.build()).queue();
            return;
        }

        if (num < 2 || num > 100) {
            EmbedBuilder builder = args.getInfo();
            builder.addField("Information:", "Vous ne pouvez que supprimer que 1 à 100 messages.", true);
            channel.sendMessage(builder.build()).queue();
            return;
        }

        channel.deleteMessageById(args.getMessage().getId()).complete();
        channel.getHistory().retrievePast(num).queue((List<Message> mess) -> {
            try {
                channel.deleteMessages(mess).queue(
                        error -> channel.sendMessage("Une erreur est survenue!").queue(m -> m.delete().queueAfter(5, TimeUnit.SECONDS)),
                        success -> {
                            EmbedBuilder builder = args.getSuccess();
                            builder.addField("Succés", " `" + num + "` messages supprimés.", true);
                            channel.sendMessage(builder.build()).queue(m -> m.delete().queueAfter(5, TimeUnit.SECONDS));
                        });
            }  catch (IllegalArgumentException iae) {
                EmbedBuilder builder = args.getError();
                builder.addField("Erreur:", "Impossible de supprimer un message de plus de 2 semaines.", true);
                channel.sendMessage(builder.build()).queue();
            } catch (PermissionException pe) {
                throw pe;
            }
        });

    }
}
