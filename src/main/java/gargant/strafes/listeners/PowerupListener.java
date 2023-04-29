package gargant.strafes.listeners;

import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerMoveEvent;

import gargant.strafes.services.PowerupService;
import masecla.mlib.classes.Registerable;
import masecla.mlib.main.MLib;

public class PowerupListener extends Registerable {

    private PowerupService powerupService;

    public PowerupListener(MLib lib, PowerupService powerupService) {
        super(lib);
        this.powerupService = powerupService;
    }

    @EventHandler
    public void onMove(PlayerMoveEvent ev) {
        if (ev.getFrom().getBlockX() == ev.getTo().getBlockX()
                && ev.getFrom().getBlockY() == ev.getTo().getBlockY()
                && ev.getFrom().getBlockZ() == ev.getTo().getBlockZ())
            return;

        Block steppingOn = ev.getTo().getBlock().getRelative(0, -1, 0);
        if (!(steppingOn.getRelative(0, -1, 0).getState() instanceof Sign))
            return;
        Sign sign = (Sign) steppingOn.getRelative(0, -1, 0).getState();
        powerupService.applyPowerup(ev.getPlayer(), sign.getLines(), steppingOn.getLocation());
    }
}
