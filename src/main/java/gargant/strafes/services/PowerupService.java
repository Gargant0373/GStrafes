package gargant.strafes.services;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.block.data.type.WallSign;
import org.bukkit.potion.PotionEffectType;

import gargant.strafes.classes.BlockSign;
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
    private Map<Block, BlockSign> signCache = new HashMap<>();
    private Map<Block, Long> cacheTime = new HashMap<>();

    public void load() {
        powerups.put("speed", new EffectPowerup("SPEED", PotionEffectType.SPEED));
        powerups.put("jump", new EffectPowerup("JUMP", PotionEffectType.JUMP));
    }

    /**
     * Gets a powerup by name
     * 
     * @param name The name of the powerup
     * @return {@link Powerup} if found, null if not
     */
    public Powerup getPowerup(String name) {
        return powerups.get(name.toLowerCase());
    }

    /**
     * Gets all powerups
     * 
     * @return {@link Collection<Powerup>} all powerups
     */
    public Collection<Powerup> getPowerups() {
        return powerups.values();
    }

    /**
     * Registers a powerup
     * 
     * @param {@link Powerup} the powerup to register
     * @return {@link Powerup} the powerup that was replaced, null if none
     */
    public Powerup registerPowerup(Powerup powerup) {
        return powerups.put(powerup.getName().toLowerCase(), powerup);
    }

    /**
     * Finds a sign on a block.
     * NOTE: Must be the block the player is stepping on.
     * 
     * @param b the {@link Block} to check
     * @return {@link BlockSign} if found, null if not
     */
    public BlockSign findSign(Block b) {
        if (b == null)
            return null;

        if (signCache.containsKey(b)) {
            if (cacheTime.get(b) + 5000 < System.currentTimeMillis()) {
                signCache.remove(b);
                cacheTime.remove(b);
            } else {
                return signCache.get(b);
            }
        }

        // Checking underneath
        if (b.getRelative(0, -1, 0).getState() instanceof Sign) {
            BlockSign sign = new BlockSign(lib, b,
                    ((Sign) b.getRelative(0, -1, 0).getState()).getLines());
            signCache.put(b, sign);
            cacheTime.put(b, System.currentTimeMillis());
            return sign;
        }

        // Checking on block
        for (int i = -1; i <= 1; i += 2) {
            Block rel = b.getRelative(i, 0, 0);
            if (rel.getState().getBlockData() instanceof WallSign) {
                WallSign sign = (WallSign) rel.getState().getBlockData();
                Block attached = rel.getRelative(sign.getFacing().getOppositeFace());
                if (!attached.getLocation().equals(b.getLocation())) {
                    return null;
                }
                BlockSign blockSign = new BlockSign(lib, b, ((Sign) rel.getState()).getLines());
                signCache.put(b, blockSign);
                cacheTime.put(b, System.currentTimeMillis());
                return blockSign;
            }
        }

        for (int i = -1; i <= 1; i += 2) {
            Block rel = b.getRelative(0, 0, i);
            if (rel.getState().getBlockData() instanceof WallSign) {
                WallSign sign = (WallSign) rel.getState().getBlockData();
                Block attached = rel.getRelative(sign.getFacing().getOppositeFace());
                if (!attached.getLocation().equals(b.getLocation())) {
                    return null;
                }
                BlockSign blockSign = new BlockSign(lib, b, ((Sign) rel.getState()).getLines());
                signCache.put(b, blockSign);
                cacheTime.put(b, System.currentTimeMillis());
                return blockSign;
            }
        }

        return null;
    }
}
