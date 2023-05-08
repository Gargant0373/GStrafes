package gargant.strafes.services;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Sign;
import org.bukkit.block.data.type.WallSign;
import org.bukkit.potion.PotionEffectType;

import gargant.strafes.classes.BlockSign;
import gargant.strafes.classes.EffectPowerup;
import gargant.strafes.classes.Powerup;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import masecla.mlib.apis.CompatibilityAPI.Versions;
import masecla.mlib.main.MLib;

/**
 * Service that handles powerups and their registration
 */
@RequiredArgsConstructor
public class PowerupService {

    @NonNull
    private MLib lib;
    @NonNull
    private DatabaseService databaseService;
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
            if (cacheTime.get(b) + databaseService.getPowerupCacheTime() < System.currentTimeMillis()) {
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
            BlockSign sign = checkSign(b, rel);
            if (sign != null)
                return sign;
        }

        for (int i = -1; i <= 1; i += 2) {
            Block rel = b.getRelative(0, 0, i);
            BlockSign sign = checkSign(b, rel);
            if (sign != null)
                return sign;
        }

        return null;
    }

    @SuppressWarnings("deprecation")
    private BlockSign checkSign(Block b, Block rel) {
        if (rel.getState() instanceof Sign
                || (lib.getCompatibilityApi().getServerVersion().higherThanOr(Versions.v1_13)
                        && rel.getState().getBlockData() instanceof WallSign)) {
            BlockFace face = null;
            String[] lines = ((Sign) rel.getState()).getLines();
            if (lib.getCompatibilityApi().getServerVersion().lowerThanOr(Versions.v1_13)) {
                org.bukkit.material.Sign sign = (org.bukkit.material.Sign) rel.getState().getData();
                Method facing;
                try {
                    facing = sign.getClass().getMethod("getFacing");
                    face = (BlockFace) facing.invoke(sign);
                } catch (NoSuchMethodException | SecurityException | IllegalAccessException
                        | IllegalArgumentException | InvocationTargetException e) {
                    e.printStackTrace();
                }
            } else {
                face = ((WallSign) rel.getState().getBlockData()).getFacing();
            }
            Block attached = rel.getRelative(face.getOppositeFace());
            if (!attached.getLocation().equals(b.getLocation())) {
                return null;
            }

            BlockSign blockSign = new BlockSign(lib, b, lines);
            signCache.put(b, blockSign);
            cacheTime.put(b, System.currentTimeMillis());
            return blockSign;
        }
        return null;
    }
}
