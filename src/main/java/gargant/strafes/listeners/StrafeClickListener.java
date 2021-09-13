package gargant.strafes.listeners;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.Vector;

import gargant.strafes.classes.Cooldown;
import gargant.strafes.classes.Cooldown.CooldownType;
import gargant.strafes.classes.Items;
import masecla.mlib.classes.Registerable;
import masecla.mlib.main.MLib;

public class StrafeClickListener extends Registerable {

	private Items items;

	public StrafeClickListener(MLib lib, Items items) {
		super(lib);
		this.items = items;
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

		@SuppressWarnings("deprecation")
		String tag = lib.getNmsAPI().getNBTTagValueString(held, "StrafeDirection");
		if (tag == null)
			return;
		if (event.getAction().equals(Action.LEFT_CLICK_AIR) || event.getAction().equals(Action.LEFT_CLICK_BLOCK)) {
			return;
		} else if (event.getAction().equals(Action.RIGHT_CLICK_AIR)
				|| event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
			switch (tag) {
			case "RIGHT":
				this.applyRightStrafe(p);
				this.playStrafeSound(p);
				new Cooldown(lib, p, CooldownType.STRAFES_RIGHT, items,
						event.getPlayer().getInventory().getHeldItemSlot(), this.getStrafeCooldown()).register();
				break;
			case "LEFT":
				this.applyLeftStrafe(p);
				this.playStrafeSound(p);
				new Cooldown(lib, p, CooldownType.STRAFES_LEFT, items,
						event.getPlayer().getInventory().getHeldItemSlot(), this.getStrafeCooldown()).register();
				break;
			case "BACK":
				this.applyBackStrafe(p);
				this.playStrafeSound(p);
				new Cooldown(lib, p, CooldownType.STRAFES_BACK, items,
						event.getPlayer().getInventory().getHeldItemSlot(), this.getStrafeCooldown()).register();
				break;
			case "LEAP":
				this.applyLeap(p);
				this.applyLeapSound(p);
				new Cooldown(lib, p, CooldownType.LEAP_FORWARD, items,
						event.getPlayer().getInventory().getHeldItemSlot(), this.getLeapCooldown()).register();
				break;
			}
			this.justClicked.add(p.getUniqueId());
			Bukkit.getScheduler().scheduleSyncDelayedTask(lib.getPlugin(), () -> {
				this.justClicked.remove(p.getUniqueId());
			}, 10);
		}
	}

	private void applyBackStrafe(Player player) {
		Location locVec = player.getLocation().clone();
		locVec = this.snapYaw(locVec);
		locVec.setPitch(0);
		Vector velocityVector = locVec.getDirection().multiply(-1.0).multiply(this.getStrafeVelocity());
		velocityVector = velocityVector.setY(this.getStrafeVerticalVelocity());
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
		velocityVector = velocityVector.multiply(this.getStrafeVelocity());
		velocityVector = velocityVector.setY(this.getStrafeVerticalVelocity());
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
		velocityVector = velocityVector.multiply(this.getStrafeVelocity());
		velocityVector = velocityVector.setY(this.getStrafeVerticalVelocity());
		player.setVelocity(velocityVector);
	}

	private void applyLeap(Player p) {
		Vector v = p.getLocation().getDirection();
		v.multiply(this.getLeapVelocity());
		v.setY(this.getLeapVerticalVelocity());
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

	private double getStrafeVelocity() {
		return (double) lib.getConfigurationAPI().getConfig().get("strafes.strafe_velocity", 1.78);
	}

	private double getStrafeVerticalVelocity() {
		return (double) lib.getConfigurationAPI().getConfig().get("strafes.vertical_velocity", 0.3);
	}

	private double getLeapVelocity() {
		return (double) lib.getConfigurationAPI().getConfig().get("leap.leap_velocity", 1.5);
	}

	private double getLeapVerticalVelocity() {
		return (double) lib.getConfigurationAPI().getConfig().get("leap.vertical_velocity", 0.4);
	}

	private int getStrafeCooldown() {
		return lib.getConfigurationAPI().getConfig().getInt("strafes.cooldown", 20);
	}

	private int getLeapCooldown() {
		return lib.getConfigurationAPI().getConfig().getInt("leap.cooldown", 8);
	}

	private void playStrafeSound(Player player) {
		player.playSound(player.getLocation(), Sound.ENTITY_ITEM_PICKUP, 1, 0.6f);
	}

	private void applyLeapSound(Player p) {
		p.playSound(p.getLocation(), Sound.ENTITY_ENDERDRAGON_FLAP, 0.6f, 1);
	}

}
