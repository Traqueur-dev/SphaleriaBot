package fr.traqueur.sphaleriabot.managers.profiles;

import lombok.Data;

@Data
public class ProfileSettings {

    private long joinChannelID;

    public ProfileSettings() {
        this.joinChannelID = 0L;
    }

}
