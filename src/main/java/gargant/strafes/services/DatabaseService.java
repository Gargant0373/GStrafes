package gargant.strafes.services;

import lombok.AllArgsConstructor;
import lombok.Getter;
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

    public double getVelocity(DatabaseType type) {
        return lib.getConfigurationAPI().getConfig().getDouble(type.name().toLowerCase() + ".velocity",
                type.getDefaultVelocity());
    }

    public void incrementVelocity(DatabaseType type, double velocity) {
        lib.getConfigurationAPI().getConfig().set(type.name().toLowerCase() + ".velocity",
                Double.sum(getVelocity(type), velocity));
    }

    public double getVerticalVelocity(DatabaseType type) {
        return lib.getConfigurationAPI().getConfig().getDouble(type.name().toLowerCase() + ".vertical_velocity",
                type.getDefaultVerticalVelocity());
    }

    public void incrementVerticalVelocity(DatabaseType type, double velocity) {
        lib.getConfigurationAPI().getConfig().set(type.name().toLowerCase() + ".vertical_velocity",
                Double.sum(getVerticalVelocity(type), velocity));
    }

    public void resetVelocity(DatabaseType type) {
        lib.getConfigurationAPI().getConfig().set(type.name().toLowerCase() + ".velocity", type.getDefaultVelocity());
        lib.getConfigurationAPI().getConfig().set(type.name().toLowerCase() + ".vertical_velocity",
                type.getDefaultVerticalVelocity());
    }

    @AllArgsConstructor
    public enum DatabaseType {
        STRAFES(1.78, 0.3), LEAP(1.5, 0.4);

        @Getter
        private double defaultVelocity;
        @Getter
        private double defaultVerticalVelocity;
    }
}
