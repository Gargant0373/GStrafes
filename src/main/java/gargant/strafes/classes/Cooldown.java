package gargant.strafes.classes;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import masecla.mlib.main.MLib;

public class Cooldown {

	private MLib lib;
	private Items items;
	private Player player;
	private CooldownType type;
	private int slot;
	private int cooldown;

	public enum CooldownType {
		STRAFES_LEFT, STRAFES_RIGHT, STRAFES_BACK, LEAP_FORWARD;
	}

	public Cooldown(MLib lib, Player player, CooldownType type, Items items, int slot, int cooldown) {
		this.lib = lib;
		this.player = player;
		this.type = type;
		this.items = items;
		this.slot = slot;
		this.cooldown = cooldown;
	}

	public void register() {
		if ((cooldown <= 0 || cooldown > 64) && cooldown != -1) {
			lib.getLoggerAPI().error("Cooldown for " + type.name() + " is invalid. (" + cooldown + ")");
			return;
		}

		if(cooldown == -1)
			return;

		ItemStack cr = this.getCooldownItemFor(type).clone();
		Bukkit.getScheduler().scheduleSyncDelayedTask(lib.getPlugin(), () -> {
			player.getInventory().setItem(slot, cr);
		}, 1);


		Bukkit.getScheduler().scheduleSyncDelayedTask(lib.getPlugin(), () -> {
			this.updateItem();
		}, 20);
	}

	private void updateItem() {
		if (player.getInventory().getItem(slot) == null
				|| player.getInventory().getItem(slot).getType().equals(Material.AIR))
			return;
		ItemStack held = player.getInventory().getItem(slot);
		int amount = player.getInventory().getItem(slot).getAmount();
		String tag = lib.getNmsAPI().read(held).getString("StrafeDirection").getValue();
		if (tag == null || !tag.equals("COOLDOWN"))
			return;
		if (amount <= 1) {
			player.getInventory().setItem(slot, this.getItemFor(type));
			return;
		}
		player.getInventory().getItem(slot).setAmount(amount - 1);
		Bukkit.getScheduler().scheduleSyncDelayedTask(lib.getPlugin(), () -> {
			this.updateItem();
		}, 20);
	}

	private ItemStack getCooldownItemFor(CooldownType type) {
		switch (type) {
			case STRAFES_LEFT:
				return items.getLeftCooldown(cooldown);
			case STRAFES_RIGHT:
				return items.getRightCooldown(cooldown);
			case STRAFES_BACK:
				return items.getBackCooldown(cooldown);
			case LEAP_FORWARD:
				return items.getCooldownLeap(cooldown);
		}
		return items.getLeap();
	}

	private ItemStack getItemFor(CooldownType type) {
		switch (type) {
			case STRAFES_LEFT:
				return items.getLeftStrafe();
			case STRAFES_RIGHT:
				return items.getRightStrafe();
			case STRAFES_BACK:
				return items.getBackStrafe();
			case LEAP_FORWARD:
				return items.getLeap();
		}
		return items.getLeap();
	}

}
