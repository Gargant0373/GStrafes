package gargant.strafes.classes;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import masecla.mlib.classes.builders.ItemBuilder;
import masecla.mlib.main.MLib;

public class Items {

	private MLib lib;

	public Items(MLib lib) {
		this.lib = lib;
	}

	public ItemStack getLeftStrafe() {
		return new ItemBuilder(Material.SKULL_ITEM).amount(1).name("&a&lLeft Strafe &7[Right Click]")
				.lore("", "&7Stop reading this", "&7and go play!", "", "&b\u25BA Right click to use!")
				.tagString("StrafeDirection", "LEFT")
				.skull(lib.getConfigurationAPI().getConfig().getString("heads.left.active")).build(lib);
	}

	public ItemStack getRightStrafe() {
		return new ItemBuilder(Material.SKULL_ITEM).amount(1).name("&a&lRight Strafe &7[Right Click]")
				.lore("", "&7Stop reading this", "&7and go play!", "", "&b\u25BA Right click to use!")
				.tagString("StrafeDirection", "RIGHT")
				.skull(lib.getConfigurationAPI().getConfig().getString("heads.right.active")).build(lib);
	}

	public ItemStack getBackStrafe() {
		return new ItemBuilder(Material.SKULL_ITEM).amount(1).name("&a&lBack Strafe &7[Right Click]")
				.lore("", "&7Stop reading this", "&7and go play!", "", "&b\u25BA Right click to use!")
				.tagString("StrafeDirection", "BACK")
				.skull(lib.getConfigurationAPI().getConfig().getString("heads.backwards.active")).build(lib);
	}

	public ItemStack getRightCooldown(int cooldown) {
		return new ItemBuilder(Material.SKULL_ITEM).amount(cooldown).name("&a&lRight Strafe &7[On Cooldown]")
				.lore("", "&7Stop reading this", "&7and go play!", "", "&b\u25BA Right click to use!")
				.tagString("StrafeDirection", "COOLDOWN")
				.skull(lib.getConfigurationAPI().getConfig().getString("heads.right.cooldown")).build(lib);
	}

	public ItemStack getLeftCooldown(int cooldown) {
		return new ItemBuilder(Material.SKULL_ITEM).amount(cooldown).name("&a&lLeft Strafe &7[On Cooldown]")
				.lore("", "&7Stop reading this", "&7and go play!", "", "&b\u25BA Right click to use!")
				.tagString("StrafeDirection", "COOLDOWN")
				.skull(lib.getConfigurationAPI().getConfig().getString("heads.left.cooldown")).build(lib);
	}

	public ItemStack getBackCooldown(int cooldown) {
		return new ItemBuilder(Material.SKULL_ITEM).amount(cooldown).name("&a&lBack Strafe &7[On Cooldown]")
				.lore("", "&7Stop reading this", "&7and go play!", "", "&b\u25BA Right click to use!")
				.tagString("StrafeDirection", "COOLDOWN")
				.skull(lib.getConfigurationAPI().getConfig().getString("heads.backwards.cooldown")).build(lib);
	}

	public ItemStack getLeap() {
		return new ItemBuilder(Material.FEATHER).name("&f&lLeap &7[Right Click]").lore("", "&7Stop reading this",
				"&7and go play!", "", "&b\u25BA Right click to use!").tagString("StrafeDirection", "LEAP").build(lib);
	}

	public ItemStack getCooldownLeap(int cooldown) {
		return new ItemBuilder(Material.FEATHER).amount(cooldown).name("&f&lLeap &7[On Cooldown]").lore("", "&7Stop reading this",
				"&7and go play!", "", "&b\u25BA Right click to use!").tagString("StrafeDirection", "COOLDOWN").build(lib);
	}
}
