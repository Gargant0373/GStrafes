package gargant.strafes.classes;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import masecla.mlib.apis.CompatibilityAPI.Versions;
import masecla.mlib.classes.builders.ItemBuilder;
import masecla.mlib.main.MLib;

public class Items {

	private MLib lib;

	public Items(MLib lib) {
		this.lib = lib;
	}

	public ItemStack getLeftStrafe() {
		return new ItemBuilder(getHead()).skull(lib.getConfigurationAPI().getConfig().getString("heads.left.active"))
				.name("&a&lLeft Strafe &7[Right Click]")
				.lore("", "&7Stop reading this", "&7and go play!", "", "&b\u25BA Right click to use!")
				.tagString("StrafeDirection", "LEFT")
				.build(lib);
	}

	public ItemStack getRightStrafe() {
		return new ItemBuilder(getHead()).amount(1).name("&a&lRight Strafe &7[Right Click]")
				.lore("", "&7Stop reading this", "&7and go play!", "", "&b\u25BA Right click to use!")
				.tagString("StrafeDirection", "RIGHT")
				.skull(lib.getConfigurationAPI().getConfig().getString("heads.right.active")).build(lib);
	}

	public ItemStack getBackStrafe() {
		return new ItemBuilder(getHead()).amount(1).name("&a&lBack Strafe &7[Right Click]")
				.lore("", "&7Stop reading this", "&7and go play!", "", "&b\u25BA Right click to use!")
				.tagString("StrafeDirection", "BACK")
				.skull(lib.getConfigurationAPI().getConfig().getString("heads.backwards.active")).build(lib);
	}

	public ItemStack getRightCooldown(int cooldown) {
		return new ItemBuilder(getHead()).amount(cooldown).name("&a&lRight Strafe &7[On Cooldown]")
				.lore("", "&7Stop reading this", "&7and go play!", "", "&b\u25BA Right click to use!")
				.tagString("StrafeDirection", "COOLDOWN")
				.skull(lib.getConfigurationAPI().getConfig().getString("heads.right.cooldown")).build(lib);
	}

	public ItemStack getLeftCooldown(int cooldown) {
		return new ItemBuilder(getHead()).amount(cooldown).name("&a&lLeft Strafe &7[On Cooldown]")
				.lore("", "&7Stop reading this", "&7and go play!", "", "&b\u25BA Right click to use!")
				.tagString("StrafeDirection", "COOLDOWN")
				.skull(lib.getConfigurationAPI().getConfig().getString("heads.left.cooldown")).build(lib);
	}

	public ItemStack getBackCooldown(int cooldown) {
		return new ItemBuilder(getHead()).amount(cooldown).name("&a&lBack Strafe &7[On Cooldown]")
				.lore("", "&7Stop reading this", "&7and go play!", "", "&b\u25BA Right click to use!")
				.tagString("StrafeDirection", "COOLDOWN")
				.skull(lib.getConfigurationAPI().getConfig().getString("heads.backwards.cooldown")).build(lib);
	}

	public ItemStack getLeap() {
		return new ItemBuilder(Material.FEATHER).name("&f&lLeap &7[Right Click]").lore("", "&7Stop reading this",
				"&7and go play!", "", "&b\u25BA Right click to use!").tagString("StrafeDirection", "LEAP").build(lib);
	}

	public ItemStack getCooldownLeap(int cooldown) {
		return new ItemBuilder(Material.FEATHER).amount(cooldown).name("&f&lLeap &7[On Cooldown]")
				.lore("", "&7Stop reading this",
						"&7and go play!", "", "&b\u25BA Right click to use!")
				.tagString("StrafeDirection", "COOLDOWN").build(lib);
	}

	public boolean containsBoostTag(String tag) {		//Todo: Sorry. No idea where else to put this...
		if (tag == null)
			return false;
		switch (tag) {
			case "BACK":
			case "RIGHT":
			case "LEFT":
			case "LEAP":
			case "COOLDOWN":
				return true;
			default:
				return false;
		}
	}

	private Material head;

	private Material getHead() {
		if (head == null)
			if (lib.getCompatibilityApi().getServerVersion().lowerThanOr(Versions.v1_12_2))
				head = Material.matchMaterial("SKULL_ITEM");
			else
				head = Material.PLAYER_HEAD;
		return head;
	}
}
