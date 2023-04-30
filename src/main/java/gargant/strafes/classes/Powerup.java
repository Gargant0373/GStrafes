package gargant.strafes.classes;

import org.bukkit.entity.Player;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;

@AllArgsConstructor
public abstract class Powerup {
    @NonNull
    @Getter
    private String name;

    /**
     * Applies the powerup to a player
     * @param player the {@link Player} to apply to
     * @param level the level of the powerup
     * @param duration the duration of the powerup
     */
    public abstract void apply(Player p, int level, int duration);
}
