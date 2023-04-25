package gargant.strafes.services;

import lombok.AllArgsConstructor;
import masecla.mlib.main.MLib;

@AllArgsConstructor
public class DatabaseService {

    private MLib lib;

    public int getCooldown(DatabaseType type) {
        return (int) lib.getConfigurationAPI().getConfig().get(type.name().toLowerCase() + ".cooldown", 20);
    }

    public void setCooldown(DatabaseType type, int cooldown) {
        lib.getConfigurationAPI().getConfig().set(type.name().toLowerCase() + ".cooldown", cooldown);
    }

    public enum DatabaseType {
        STRAFE, LEAP;
    }
}
