package fr.traqueur.sphaleriabot.managers.bad;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class BadSettings {

    private List<String> badWords;
    private List<String> badLinks;
    private List<Long> ignoredChannelID;

    public BadSettings() {
        this.badLinks = new ArrayList<>();
        this.badWords = new ArrayList<>();
        this.ignoredChannelID = new ArrayList<>();
    }

}
