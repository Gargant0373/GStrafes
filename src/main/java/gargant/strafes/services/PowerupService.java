package gargant.strafes.services;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;

import gargant.strafes.classes.EffectPowerup;
import gargant.strafes.classes.Powerup;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import masecla.mlib.main.MLib;

@RequiredArgsConstructor
public class PowerupService {

    @NonNull
    private MLib lib;
    private Map<String, Powerup> powerups = new HashMap<>();

    public void load() {
        powerups.put("speed", new EffectPowerup("SPEED", PotionEffectType.SPEED));
        powerups.put("jump", new EffectPowerup("JUMP", PotionEffectType.JUMP));
    }

    public Powerup getPowerup(String name) {
        return powerups.get(name.toLowerCase());
    }

    public void applyPowerup(Player p, String[] lines, Location loc) {
        if (getPowerup(lines[0]) == null) {
            return;
        }
        try {
            getPowerup(lines[0]).apply(p, Integer.parseInt(lines[1]) - 1, 20 * Integer.parseInt(lines[2]));
        } catch (NumberFormatException ex) {
            lib.getLoggerAPI().error("Invalid number formatted in powerup sign! [" + loc.toString() + "]");
        } catch (NullPointerException ex) {
            lib.getLoggerAPI().error("Invalid powerup sign! [" + loc.toString() + "]");
        }
    }
}
