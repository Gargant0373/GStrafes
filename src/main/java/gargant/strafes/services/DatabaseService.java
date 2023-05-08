package gargant.strafes.services;

import lombok.AllArgsConstructor;
import lombok.Getter;
import masecla.mlib.main.MLib;

/**
 * Class responsible for handling all database related operations.
 */
@AllArgsConstructor
public class DatabaseService {

    private MLib lib;

    public int getCooldown(DatabaseType type) {
        return (int) lib.getConfigurationAPI().getConfig().get(type.name().toLowerCase() + ".cooldown", 20);
    }

    public void setCooldown(DatabaseType type, int cooldown) {
        lib.getConfigurationAPI().getConfig().set(type.name().toLowerCase() + ".cooldown", cooldown);
    }

    public void setVelocity(DatabaseType type, double velocity) {
        lib.getConfigurationAPI().getConfig().set(type.name().toLowerCase() + ".velocity", velocity);
    }

    public double getVelocity(DatabaseType type) {
        return lib.getConfigurationAPI().getConfig().getDouble(type.name().toLowerCase() + ".velocity",
                type.getDefaultVelocity());
    }

    public void incrementVelocity(DatabaseType type, double velocity) {
        int vel = (int) (getVelocity(type) * 100);
        vel += velocity * 100;
        double newVel = vel / 100d;
        lib.getConfigurationAPI().getConfig().set(type.name().toLowerCase() + ".velocity",
                newVel);
    }

    public void setVerticalVelocity(DatabaseType type, double velocity) {
        lib.getConfigurationAPI().getConfig().set(type.name().toLowerCase() + ".vertical_velocity", velocity);
    }

    public double getVerticalVelocity(DatabaseType type) {
        return lib.getConfigurationAPI().getConfig().getDouble(type.name().toLowerCase() + ".vertical_velocity",
                type.getDefaultVerticalVelocity());
    }

    public void incrementVerticalVelocity(DatabaseType type, double velocity) {
        int vel = (int) (getVerticalVelocity(type) * 100);
        vel += velocity * 100;
        double newVel = vel / 100d;
        lib.getConfigurationAPI().getConfig().set(type.name().toLowerCase() + ".vertical_velocity",
                newVel);
    }

    public void resetVelocity(DatabaseType type) {
        lib.getConfigurationAPI().getConfig().set(type.name().toLowerCase() + ".velocity", type.getDefaultVelocity());
        lib.getConfigurationAPI().getConfig().set(type.name().toLowerCase() + ".vertical_velocity",
                type.getDefaultVerticalVelocity());
    }

    /**
     * Enum containing all the database types (strafes and leap).
     */
    @AllArgsConstructor
    public enum DatabaseType {
        STRAFES(1.78, 0.3), LEAP(1.5, 0.4);

        @Getter
        private double defaultVelocity;
        @Getter
        private double defaultVerticalVelocity;
    }
}
