package fr.traqueur.sphaleriabot.managers.count;

import lombok.Data;

@Data
public class CountSettings {

    private Long countChannelId;
    private String nameCountChannel;

    public CountSettings() {
        this.countChannelId = 0L;
        this.nameCountChannel = "Membres:";
    }

}
