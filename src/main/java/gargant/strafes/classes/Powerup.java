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

    public abstract void apply(Player p, int level, int duration);
}
