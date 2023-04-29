package gargant.strafes.services;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.potion.PotionEffectType;

import gargant.strafes.classes.EffectPowerup;
import gargant.strafes.classes.Powerup;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import masecla.mlib.main.MLib;

/**
 * Service that handles powerups and their registration
 */
@RequiredArgsConstructor
public class PowerupService {

    @NonNull
    private MLib lib;
    private Map<String, Powerup> powerups = new HashMap<>();

    public void load() {
        powerups.put("speed", new EffectPowerup("SPEED", PotionEffectType.SPEED));
        powerups.put("jump", new EffectPowerup("JUMP", PotionEffectType.JUMP));
    }

    /**
     * Gets a powerup by name
     * @param name The name of the powerup
     * @return {@link Powerup} if found, null if not
     */
    public Powerup getPowerup(String name) {
        return powerups.get(name.toLowerCase());
    }

    /**
     * Registers a powerup
     * @param {@link Powerup} the powerup to register
     * @return {@link Powerup} the powerup that was replaced, null if none
     */
    public Powerup registerPowerup(Powerup powerup) {
        return powerups.put(powerup.getName().toLowerCase(), powerup);
    }
}
