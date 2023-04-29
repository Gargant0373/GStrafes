package gargant.strafes.classes;

import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class EffectPowerup extends Powerup {

    private PotionEffectType effect;

    public EffectPowerup(String name, PotionEffectType effect) {
        super(name);
        this.effect = effect;
    }

    @Override
    public void apply(Player p, int level, int duration) {
        p.addPotionEffect(new PotionEffect(this.effect, duration, level));
    }

}
