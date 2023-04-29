package gargant.strafes.listeners;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.Vector;

import gargant.strafes.classes.Cooldown;
import gargant.strafes.classes.Cooldown.CooldownType;
import gargant.strafes.services.DatabaseService;
import gargant.strafes.services.DatabaseService.DatabaseType;
import gargant.strafes.classes.Items;
import masecla.mlib.apis.SoundAPI.Sound;
import masecla.mlib.classes.Registerable;
import masecla.mlib.main.MLib;

public class StrafeClickListener extends Registerable {

	private Items items;
	private DatabaseService databaseService;

	public StrafeClickListener(MLib lib, Items items, DatabaseService databaseService) {
		super(lib);
		this.items = items;
		this.databaseService = databaseService;
	}

	private Set<UUID> justClicked = new HashSet<>();

	@EventHandler
	public void onClick(PlayerInteractEvent event) {
		ItemStack held = event.getPlayer().getInventory().getItemInMainHand();
		if (held == null || held.getType().equals(Material.AIR))
			return;

		ItemMeta meta = held.getItemMeta();
		if (meta == null)
			return;

		Player p = event.getPlayer();
		if (this.justClicked.contains(p.getUniqueId()))
			return;

		String tag = lib.getNmsAPI().read(held).getString("StrafeDirection").getValue();
		if (tag == null)
			return;
		if (event.getAction().equals(Action.LEFT_CLICK_AIR) || event.getAction().equals(Action.LEFT_CLICK_BLOCK)) {
			switch (tag) {
				case "RIGHT":
				case "LEFT":
				case "BACK":
				case "LEAP":
					event.setCancelled(true);
					return;
			}
			return;
		} else if (event.getAction().equals(Action.RIGHT_CLICK_AIR)
				|| event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
			switch (tag) {
				case "RIGHT":
					this.applyRightStrafe(p);
					this.playStrafeSound(p);
					new Cooldown(lib, p, CooldownType.STRAFES_RIGHT, items,
							event.getPlayer().getInventory().getHeldItemSlot(),
							databaseService.getCooldown(DatabaseType.STRAFES)).register();
					break;
				case "LEFT":
					this.applyLeftStrafe(p);
					this.playStrafeSound(p);
					new Cooldown(lib, p, CooldownType.STRAFES_LEFT, items,
							event.getPlayer().getInventory().getHeldItemSlot(),
							databaseService.getCooldown(DatabaseType.STRAFES)).register();
					break;
				case "BACK":
					this.applyBackStrafe(p);
					this.playStrafeSound(p);
					new Cooldown(lib, p, CooldownType.STRAFES_BACK, items,
							event.getPlayer().getInventory().getHeldItemSlot(),
							databaseService.getCooldown(DatabaseType.STRAFES)).register();
					break;
				case "LEAP":
					this.applyLeap(p);
					this.applyLeapSound(p);
					new Cooldown(lib, p, CooldownType.LEAP_FORWARD, items,
							event.getPlayer().getInventory().getHeldItemSlot(),
							databaseService.getCooldown(DatabaseType.LEAP)).register();
					break;
			}
			this.justClicked.add(p.getUniqueId());
			Bukkit.getScheduler().scheduleSyncDelayedTask(lib.getPlugin(), () -> {
				this.justClicked.remove(p.getUniqueId());
			}, 2);
		}
	}

	private void applyBackStrafe(Player player) {
		Location locVec = player.getLocation().clone();
		locVec = this.snapYaw(locVec);
		locVec.setPitch(0);
		Vector velocityVector = locVec.getDirection().multiply(-1.0)
				.multiply(databaseService.getVelocity(DatabaseType.STRAFES));
		velocityVector = velocityVector.setY(databaseService.getVerticalVelocity(DatabaseType.STRAFES));
		player.setVelocity(velocityVector);
	}

	private void applyLeftStrafe(Player player) {
		Location locVec = player.getLocation().clone();

		locVec = this.snapYaw(locVec);

		locVec.setPitch(0);
		Vector velocityVector = locVec.getDirection();
		double x = velocityVector.getX(), z = velocityVector.getZ();
		double aux = -x;
		x = z;
		z = aux;
		velocityVector.setX(x);
		velocityVector.setZ(z);
		velocityVector = velocityVector.multiply(databaseService.getVelocity(DatabaseType.STRAFES));
		velocityVector = velocityVector.setY(databaseService.getVerticalVelocity(DatabaseType.STRAFES));
		player.setVelocity(velocityVector);
	}

	private void applyRightStrafe(Player player) {
		Location locVec = player.getLocation().clone();

		locVec = this.snapYaw(locVec);

		locVec.setPitch(0);
		Vector velocityVector = locVec.getDirection();
		double x = velocityVector.getX(), z = velocityVector.getZ();
		double aux = x;
		x = -z;
		z = aux;
		velocityVector.setX(x);
		velocityVector.setZ(z);
		velocityVector = velocityVector.multiply(databaseService.getVelocity(DatabaseType.STRAFES));
		velocityVector = velocityVector.setY(databaseService.getVerticalVelocity(DatabaseType.STRAFES));
		player.setVelocity(velocityVector);
	}

	private void applyLeap(Player p) {
		Vector v = p.getLocation().getDirection();
		v.multiply(databaseService.getVelocity(DatabaseType.LEAP));
		v.setY(databaseService.getVerticalVelocity(DatabaseType.LEAP));
		p.setVelocity(v);
	}

	private Location snapYaw(Location location) {
		double rot = (location.getYaw() - 90) % 360;
		if (rot < 0)
			rot += 360.0;

		float settableYaw = -1;
		if (45 <= rot && rot < 135)
			settableYaw = 90;
		else if (135 <= rot && rot < 225)
			settableYaw = 180;
		else if (225 <= rot && rot < 315)
			settableYaw = 270;
		else
			settableYaw = 0;

		location.setYaw(settableYaw + 90);
		return location;
	}

	private org.bukkit.Sound strafeSound = null;
	private org.bukkit.Sound leapSound = null;

	private void playStrafeSound(Player player) {
		if (strafeSound == null)
			strafeSound = Sound.CHICKEN_EGG_POP.bukkitSound();
		player.playSound(player.getLocation(), strafeSound, 1, 0.6f);
	}

	private void applyLeapSound(Player p) {
		if (leapSound == null)
			leapSound = Sound.ENDERDRAGON_WINGS.bukkitSound();
		p.playSound(p.getLocation(), leapSound, 0.6f, 1);
	}

}
