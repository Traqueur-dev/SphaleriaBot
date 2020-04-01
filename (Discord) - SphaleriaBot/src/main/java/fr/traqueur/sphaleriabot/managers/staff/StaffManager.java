package fr.traqueur.sphaleriabot.managers.staff;

import com.google.gson.reflect.TypeToken;
import fr.traqueur.sphaleriabot.api.DiscordBot;
import fr.traqueur.sphaleriabot.api.utils.jsons.DiscUtil;
import fr.traqueur.sphaleriabot.api.utils.jsons.Saveable;
import fr.traqueur.sphaleriabot.managers.staff.commands.StaffAddCommand;
import fr.traqueur.sphaleriabot.managers.staff.commands.StaffListCommand;
import fr.traqueur.sphaleriabot.managers.staff.commands.StaffRemoveCommand;
import fr.traqueur.sphaleriabot.managers.staff.listeners.StaffListener;
import lombok.Getter;

import java.io.File;
import java.lang.reflect.Type;
import java.util.List;

@Getter
public class StaffManager extends Saveable {

    private static @Getter StaffManager instance;
    private StaffSettings settings;

    public StaffManager(DiscordBot bot) {
        super(bot, "Staff");

        instance = this;
        this.settings = new StaffSettings();
        this.registerCommand(new StaffAddCommand());
        this.registerCommand(new StaffRemoveCommand());
        this.registerCommand(new StaffListCommand());
        this.registerListener(new StaffListener());
    }

    @Override
    public File getFile() {
        return new File(this.getBot().getDataFolder(), "/staff/settings.json");
    }

    public long getChannelID() {
        return this.settings.getChannelIDAnnoucementStaff();
    }

    public List<Long> getRolesID() {
        return this.settings.getStaffRolesID();
    }

    public boolean exist(Long id) {
        return this.getRolesID().contains(id);
    }

    public boolean addRoleID(Long id) {
        if (!exist(id)) {
            this.getRolesID().add(id);
            return true;
        }
        return false;
    }

    public boolean removeRoleID(Long id) {
        return this.getRolesID().removeIf(i -> i.equals(id));
    }

    @Override
    public void loadData() {
        String content = DiscUtil.readCatch(this.getFile());
        if (content != null) {
            Type type = new TypeToken<StaffSettings>() {}.getType();
            this.settings = this.getGson().fromJson(content, type);
        }
    }

    @Override
    public void saveData() {
        DiscUtil.writeCatch(this.getFile(), this.getGson().toJson(this.settings));
    }
}
