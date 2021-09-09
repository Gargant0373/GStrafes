package gargant.strafes.classes;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;

import masecla.mlib.main.MLib;
import net.md_5.bungee.api.ChatColor;

public class Items {

	private MLib lib;

	public Items(MLib lib) {
		this.lib = lib;
	}

	public ItemStack getLeftStrafe() {
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

	public ItemStack getRightStrafe() {
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

	public ItemStack getBackStrafe() {
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

	public ItemStack getRightCooldown(int cooldown) {
		ItemStack result = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
		result.setAmount(cooldown);
		ItemMeta theMeta = result.getItemMeta();
		theMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&a&lRight Strafe &7[On Cooldown]"));
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
					new Property("textures", lib.getConfigurationAPI().getConfig().getString("heads.right.cooldown")));
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
		return lib.getNmsAPI().write().tagString("StrafeDirection", "COOLDOWN").applyOn(result);
	}

	public ItemStack getLeftCooldown(int cooldown) {
		ItemStack result = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
		result.setAmount(cooldown);
		ItemMeta theMeta = result.getItemMeta();
		theMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&a&lLeft Strafe &7[On Cooldown]"));
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
					new Property("textures", lib.getConfigurationAPI().getConfig().getString("heads.left.cooldown")));
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
		return lib.getNmsAPI().write().tagString("StrafeDirection", "COOLDOWN").applyOn(result);
	}

	public ItemStack getBackCooldown(int cooldown) {
		ItemStack result = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
		result.setAmount(cooldown);
		ItemMeta theMeta = result.getItemMeta();
		theMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&a&lBack Strafe &7[On Cooldown]"));
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
					lib.getConfigurationAPI().getConfig().getString("heads.backwards.cooldown")));
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
		return lib.getNmsAPI().write().tagString("StrafeDirection", "COOLDOWN").applyOn(result);
	}

	public ItemStack getLeap() {
		ItemStack result = new ItemStack(Material.FEATHER);
		ItemMeta theMeta = result.getItemMeta();
		theMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&f&lLeap &7[Right Click]"));
		List<String> thelore = new ArrayList<String>();
		thelore.add("");
		thelore.add(ChatColor.translateAlternateColorCodes('&', "&7Stop reading this"));
		thelore.add(ChatColor.translateAlternateColorCodes('&', "&7and go play!"));
		thelore.add("");
		thelore.add(ChatColor.translateAlternateColorCodes('&', "&b\u25BA Right click to use!"));
		theMeta.setLore(thelore);

		result.setItemMeta(theMeta);
		return lib.getNmsAPI().write().tagString("StrafeDirection", "LEAP").applyOn(result);
	}

	public ItemStack getCooldownLeap(int cooldown) {
		ItemStack result = new ItemStack(Material.FEATHER);
		result.setAmount(cooldown);
		ItemMeta theMeta = result.getItemMeta();
		theMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&f&lLeap &7[On Cooldown]"));
		List<String> thelore = new ArrayList<String>();
		thelore.add("");
		thelore.add(ChatColor.translateAlternateColorCodes('&', "&7Stop reading this"));
		thelore.add(ChatColor.translateAlternateColorCodes('&', "&7and go play!"));
		thelore.add("");
		thelore.add(ChatColor.translateAlternateColorCodes('&', "&b\u25BA Right click to use!"));
		theMeta.setLore(thelore);

		result.setItemMeta(theMeta);
		return lib.getNmsAPI().write().tagString("StrafeDirection", "COOLDOWN").applyOn(result);
	}
}
