package gargant.strafes.commands;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.util.Vector;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;

import gargant.strafes.containers.VelocityContainer;
import masecla.mlib.annotations.RegisterableInfo;
import masecla.mlib.classes.Registerable;
import masecla.mlib.main.MLib;
import net.md_5.bungee.api.ChatColor;

@RegisterableInfo(command = "strafes")
public class StrafesCommand extends Registerable {

	public StrafesCommand(MLib lib) {
		super(lib);
	}

	@Override
	public void fallbackCommand(CommandSender sender, String[] args) {
		if (!(sender instanceof Player))
			return;
		Player p = (Player) sender;

		if (sender.hasPermission("strafes.settings") && args.length > 0 && args[0].equalsIgnoreCase("settings")) {
			lib.getContainerAPI().openFor(p, VelocityContainer.class);
			return;
		}
		if (!sender.hasPermission("strafes.strafes")) {
			lib.getMessagesAPI().sendMessage("no-permission", sender);
			return;
		}
		if (!this.hasSlots(p, 3)) {
			lib.getMessagesAPI().sendMessage("no-space", sender);
			return;
		}
		if (!this.setItem(getBackStrafe(), 4, p)) {
			lib.getMessagesAPI().sendMessage("no-space", sender);
			return;
		}
		if (!this.setItem(getLeftStrafe(), 3, p)) {
			lib.getMessagesAPI().sendMessage("no-space", sender);
			return;
		}
		if (!this.setItem(getRightStrafe(), 5, p)) {
			lib.getMessagesAPI().sendMessage("no-space", sender);
			return;
		}
		lib.getMessagesAPI().sendMessage("strafes-activated", sender);
	}

	private boolean hasSlots(Player player, int slots) {
		for (int i = 0; i < player.getInventory().getSize(); i++) {
			if (player.getInventory().getItem(i) == null
					|| player.getInventory().getItem(i).getType().equals(Material.AIR))
				slots--;
			if (slots == 0)
				return true;
		}
		return false;
	}

	private boolean setItem(ItemStack is, int slot, Player p) {
		PlayerInventory inv = p.getInventory();
		if (inv.getItem(slot) == null || inv.getItem(slot).getType().equals(Material.AIR)) {
			inv.setItem(slot, is);
			return true;
		}
		if (p.getInventory().firstEmpty() == -1)
			return false;

		ItemStack previousItem = inv.getItem(slot).clone();
		boolean moved = false;
		for (int i = 0; i <= inv.getSize(); i++) {
			if (inv.getItem(i) == null || inv.getItem(i).getType().equals(Material.AIR)) {
				moved = true;
				p.getInventory().setItem(i, previousItem);
				p.getInventory().setItem(slot, is);
				break;
			}
		}
		if (!moved) {
			p.getInventory().addItem(is);
		}
		return true;
	}

	@EventHandler
	public void onPlace(BlockPlaceEvent event) {
		if (event.getPlayer().getInventory().getItemInMainHand() == null)
			return;
		ItemStack held = event.getItemInHand();
		@SuppressWarnings("deprecation")
		String tag = lib.getNmsAPI().getNBTTagValueString(held, "StrafeDirection");
		if (tag == null)
			return;
		switch (tag) {
		case "RIGHT":
		case "LEFT":
		case "BACK":
			event.setCancelled(true);
			break;
		}

	}

	private double getStrafeVelocity() {
		return (double) lib.getConfigurationAPI().getConfig().get("strafes.strafe_velocity", 1.78);
	}

	private double getVerticalVelocity() {
		return (double) lib.getConfigurationAPI().getConfig().get("strafes.vertical_velocity", 0.3);
	}

	@EventHandler
	public void onClick(PlayerInteractEvent event) {
		ItemStack held = event.getPlayer().getInventory().getItemInMainHand();
		if (held == null || held.getType().equals(Material.AIR))
			return;

		ItemMeta meta = held.getItemMeta();
		if (meta == null)
			return;

		Player p = event.getPlayer();

		@SuppressWarnings("deprecation")
		String tag = lib.getNmsAPI().getNBTTagValueString(held, "StrafeDirection");
		if (tag == null)
			return;
		if (event.getAction().equals(Action.LEFT_CLICK_AIR) || event.getAction().equals(Action.LEFT_CLICK_BLOCK)) {
			event.setCancelled(true);
			return;
		} else if (event.getAction().equals(Action.RIGHT_CLICK_AIR)
				|| event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
			switch (tag) {
			case "RIGHT":
				this.applyRightStrafe(p);
				this.playStrafeSound(p);
				break;
			case "LEFT":
				this.applyLeftStrafe(p);
				this.playStrafeSound(p);
				break;
			case "BACK":
				this.applyBackStrafe(p);
				this.playStrafeSound(p);
				break;
			}
		}
	}

	private void applyBackStrafe(Player player) {
		Location locVec = player.getLocation().clone();
		locVec = this.snapYaw(locVec);
		locVec.setPitch(0);
		Vector velocityVector = locVec.getDirection().multiply(-1.0).multiply(this.getStrafeVelocity());
		velocityVector = velocityVector.setY(this.getVerticalVelocity());
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
		velocityVector = velocityVector.setY(this.getVerticalVelocity());
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
		velocityVector = velocityVector.setY(this.getVerticalVelocity());
		player.setVelocity(velocityVector);
	}

	private ItemStack getLeftStrafe() {
		ItemStack result = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
		ItemMeta theMeta = result.getItemMeta();
		theMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&a&lLeft Strafe &7[Right Click]"));
		List<String> thelore = new ArrayList<String>();
		thelore.add("");
		thelore.add(ChatColor.translateAlternateColorCodes('&', "&7Stop reading this"));
		thelore.add(ChatColor.translateAlternateColorCodes('&', "&7and go play!"));
		thelore.add("");
		thelore.add(ChatColor.translateAlternateColorCodes('&', "&b\u25BA Right click to use!"));
		theMeta.setLore(thelore);

		SkullMeta sk = (SkullMeta) theMeta;
		try {
			GameProfile profile = new GameProfile(UUID.randomUUID(), "");
			profile.getProperties().put("textures",
					new Property("textures", lib.getConfigurationAPI().getConfig().getString("heads.left.active")));
			Field profileField = null;
			try {
				profileField = sk.getClass().getDeclaredField("profile");
				profileField.setAccessible(true);
				profileField.set(sk, profile);
			} catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException | SecurityException e) {
			}
		} catch (Exception e) {
		}

		result.setItemMeta(sk);
		return lib.getNmsAPI().write().tagString("StrafeDirection", "LEFT").applyOn(result);
	}

	private ItemStack getRightStrafe() {
		ItemStack result = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
		ItemMeta theMeta = result.getItemMeta();
		theMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&a&lRight Strafe &7[Right Click]"));
		List<String> thelore = new ArrayList<String>();
		thelore.add("");
		thelore.add(ChatColor.translateAlternateColorCodes('&', "&7Stop reading this"));
		thelore.add(ChatColor.translateAlternateColorCodes('&', "&7and go play!"));
		thelore.add("");
		thelore.add(ChatColor.translateAlternateColorCodes('&', "&b\u25BA Right click to use!"));
		theMeta.setLore(thelore);
		SkullMeta sk = (SkullMeta) theMeta;
		try {
			GameProfile profile = new GameProfile(UUID.randomUUID(), "");
			profile.getProperties().put("textures",
					new Property("textures", lib.getConfigurationAPI().getConfig().getString("heads.right.active")));
			Field profileField = null;
			try {
				profileField = sk.getClass().getDeclaredField("profile");
				profileField.setAccessible(true);
				profileField.set(sk, profile);
			} catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException | SecurityException e) {
			}
		} catch (Exception e) {
		}

		result.setItemMeta(sk);
		return lib.getNmsAPI().write().tagString("StrafeDirection", "RIGHT").applyOn(result);
	}

	private ItemStack getBackStrafe() {
		ItemStack result = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
		ItemMeta theMeta = result.getItemMeta();
		theMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&a&lBack Strafe &7[Right Click]"));
		List<String> thelore = new ArrayList<String>();
		thelore.add("");
		thelore.add(ChatColor.translateAlternateColorCodes('&', "&7Stop reading this"));
		thelore.add(ChatColor.translateAlternateColorCodes('&', "&7and go play!"));
		thelore.add("");
		thelore.add(ChatColor.translateAlternateColorCodes('&', "&b\u25BA Right click to use!"));
		theMeta.setLore(thelore);
		SkullMeta sk = (SkullMeta) theMeta;
		try {
			GameProfile profile = new GameProfile(UUID.randomUUID(), "");
			profile.getProperties().put("textures", new Property("textures",
					lib.getConfigurationAPI().getConfig().getString("heads.backwards.active")));
			Field profileField = null;
			try {
				profileField = sk.getClass().getDeclaredField("profile");
				profileField.setAccessible(true);
				profileField.set(sk, profile);
			} catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException | SecurityException e) {
			}
		} catch (Exception e) {
		}

		result.setItemMeta(sk);
		return lib.getNmsAPI().write().tagString("StrafeDirection", "BACK").applyOn(result);
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

	private void playStrafeSound(Player player) {
		player.playSound(player.getLocation(), Sound.ENTITY_ITEM_PICKUP, 1, 0.6f);
	}

}
