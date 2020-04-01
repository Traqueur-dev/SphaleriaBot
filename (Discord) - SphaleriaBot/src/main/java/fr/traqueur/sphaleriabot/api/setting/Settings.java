package fr.traqueur.sphaleriabot.api.setting;

import lombok.Data;

@Data
public class Settings {

    private String token;
    private long serverID;
    private String prefix;

    public Settings() {
        this.token = "";
        this.serverID = 0L;
        this.prefix = "";
    }

}
