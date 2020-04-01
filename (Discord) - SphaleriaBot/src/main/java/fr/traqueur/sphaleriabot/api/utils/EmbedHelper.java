package fr.traqueur.sphaleriabot.api.utils;

import java.awt.*;
import java.time.Instant;

import net.dv8tion.jda.core.EmbedBuilder;

public class EmbedHelper {
	
	private static String footerText;
	
	public static void setFooterText(String text) {
		footerText = text;
	}
	
	public static EmbedBuilder getBasicEmbed() {
		EmbedBuilder builder = new EmbedBuilder();
		builder.setFooter(footerText, null);
		builder.setTimestamp(Instant.now());
		return builder;
	}

	public static EmbedBuilder getInfoEmbed() {
		EmbedBuilder builder = new EmbedBuilder();
		builder.setFooter(footerText, null);
		builder.setColor(Color.YELLOW);
		builder.setTimestamp(Instant.now());
		return builder;
	}

	public static EmbedBuilder getSuccessEmbed() {
		EmbedBuilder builder = new EmbedBuilder();
		builder.setFooter(footerText, null);
		builder.setColor(Color.GREEN);
		builder.setTimestamp(Instant.now());
		return builder;
	}

	public static EmbedBuilder getErrorEmbed() {
		EmbedBuilder builder = new EmbedBuilder();
		builder.setFooter(footerText, null);
		builder.setColor(Color.RED);
		builder.setTimestamp(Instant.now());
		return builder;
	}

}