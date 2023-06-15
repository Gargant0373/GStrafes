package gargant.strafes.containers;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import gargant.strafes.services.DatabaseService;
import gargant.strafes.services.DatabaseService.DatabaseType;
import masecla.mlib.apis.CompatibilityAPI.Versions;
import masecla.mlib.apis.SoundAPI.Sound;
import masecla.mlib.classes.Replaceable;
import masecla.mlib.classes.builders.ItemBuilder;
import masecla.mlib.containers.generic.ImmutableContainer;
import masecla.mlib.main.MLib;
import net.md_5.bungee.api.ChatColor;

public class CooldownsContainer extends ImmutableContainer {

	private DatabaseService databaseService;

	public CooldownsContainer(MLib lib, DatabaseService databaseService) {
		super(lib);
		this.databaseService = databaseService;
	}

	@Override
	public void onTopClick(InventoryClickEvent event) {
		event.setCancelled(true);
		Player p = (Player) event.getWhoClicked();
		if (event.getSlot() == 49) {
			p.closeInventory();
			return;
		}
		if (event.getSlot() == 22) {
			int cooldown = databaseService.getCooldown(DatabaseType.STRAFES);
			if (cooldown == -1)
				databaseService.setCooldown(DatabaseType.STRAFES, 20);
			else
				databaseService.setCooldown(DatabaseType.STRAFES, -1);
		}
		if (event.getSlot() == 40) {
			int cooldown = databaseService.getCooldown(DatabaseType.LEAP);
			if (cooldown == -1)
				databaseService.setCooldown(DatabaseType.LEAP, 20);
			else
				databaseService.setCooldown(DatabaseType.LEAP, -1);
			p.playSound(p.getLocation(), Sound.LEVEL_UP.bukkitSound(), 0.8f, 1);
			return;
		}

		String tag = lib.getNmsAPI().read(event.getCurrentItem()).getString("INCREMENT").getValue();
		if (tag == null || tag.isEmpty())
			return;

		int amount = Integer.parseInt(tag.split("_")[0]);
		DatabaseType type = DatabaseType.valueOf(tag.split("_")[1].toUpperCase());

		int cooldownValue = databaseService.getCooldown(type);
		if (cooldownValue == -1) {
			lib.getMessagesAPI().sendMessage("cooldown-disabled", p);
			p.playSound(p.getLocation(), Sound.NOTE_BASS.bukkitSound(), 1, 0.8f);
			return;
		}

		if (cooldownValue + amount <= 0 || cooldownValue + amount > 64) {
			lib.getMessagesAPI().sendMessage("invalid-cooldown", p, new Replaceable("%value%", cooldownValue + amount));
			p.playSound(p.getLocation(), Sound.NOTE_BASS.bukkitSound(), 1, 0.8f);
			return;
		}

		databaseService.setCooldown(type, cooldownValue + amount);
		p.playSound(p.getLocation(), Sound.LEVEL_UP.bukkitSound(), 0.8f, 1);
	}

	@Override
	public int getSize(Player p) {
		return 54;
	}

	@Override
	public int getUpdatingInterval() {
		return 10;
	}

	@Override
	public boolean requiresUpdating() {
		return true;
	}

	@Override
	public Inventory getInventory(Player p) {
		Inventory inv = Bukkit.createInventory(p, getSize(p), ChatColor.DARK_GREEN + "Strafe Cooldowns");
		inv.setItem(19, this.getIncrement(-10, "Strafes"));
		inv.setItem(20, this.getIncrement(-5, "Strafes"));
		inv.setItem(21, this.getIncrement(-1, "Strafes"));
		inv.setItem(22, this.getStrafeItem());
		inv.setItem(23, this.getIncrement(1, "Strafes"));
		inv.setItem(24, this.getIncrement(5, "Strafes"));
		inv.setItem(25, this.getIncrement(10, "Strafes"));

		inv.setItem(37, this.getIncrement(-10, "Leap"));
		inv.setItem(38, this.getIncrement(-5, "Leap"));
		inv.setItem(39, this.getIncrement(-1, "Leap"));
		inv.setItem(40, this.getLeap());
		inv.setItem(41, this.getIncrement(1, "Leap"));
		inv.setItem(42, this.getIncrement(5, "Leap"));
		inv.setItem(43, this.getIncrement(10, "Leap"));

		inv = this.applyMarginalBars(inv);
		inv.setItem(49, this.getInventoryClose());
		return inv;
	}

	private ItemStack getIncrement(int amount, String type) {
		ItemBuilder builder = new ItemBuilder();
		if (lib.getCompatibilityApi().getServerVersion().lowerThanOr(Versions.v1_12_2))
			builder.item(Material.matchMaterial("WOOL")).data(this.getColorFor(amount));
		else
			builder.item(Material.matchMaterial(convertColor(amount) + "_WOOL"));
		return builder.name("&2" + (amount > 0 ? "+" : "") + amount
				+ " &aSecond" + (Math.abs(amount) == 1 ? "" : "s") + " to Cooldown").lore("").lore("&7Click to use!")
				.tagString("INCREMENT", amount + "_" + type.toLowerCase()).build(lib);
	}

	private int getColorFor(int count) {
		count = Math.abs(count);
		if (count == 1)
			return 13;
		if (count == 5)
			return 1;
		return 14;
	}

	private String convertColor(int color) {
		color = getColorFor(color);
		switch (color) {
			case 13:
				return "GREEN";
			case 1:
				return "YELLOW";
			case 14:
				return "RED";
		}
		return "WHITE";
	}

	private ItemStack getInventoryClose() {
		return new ItemBuilder().name("&cClose!").lore("", "&7Close this menu.").skull(
				"ewogICJ0aW1lc3RhbXAiIDogMTY1MzAyMzMxMTcxNywKICAicHJvZmlsZUlkIiA6ICJjYmFkZmRmNTRkZTM0N2UwODQ3MjUyMDIyYTFkNGRkZCIsCiAgInByb2ZpbGVOYW1lIiA6ICJmaXdpcGVlIiwKICAic2lnbmF0dXJlUmVxdWlyZWQiIDogdHJ1ZSwKICAidGV4dHVyZXMiIDogewogICAgIlNLSU4iIDogewogICAgICAidXJsIiA6ICJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlLzUzYjQ4MDNkZjZmMDM5NWZkZGQ4NWUyN2ZhODM3YTVmMDExMjQ2NDA2YjAxZmZlNTVhYzJlOTJmYTc0OWNhNzkiLAogICAgICAibWV0YWRhdGEiIDogewogICAgICAgICJtb2RlbCIgOiAic2xpbSIKICAgICAgfQogICAgfQogIH0KfQ==")
				.build(lib);
	}

	private Inventory applyMarginalBars(Inventory inv) {
		ItemStack marginalBar = this.getMarginalBar();
		for (int i = 0; i < 5; i++) {
			// Top bar
			inv.setItem(i, marginalBar);
			inv.setItem(9 - i, marginalBar);

			// Bottom bar
			inv.setItem(i + this.getSize(null) - 9, marginalBar);
			inv.setItem(this.getSize(null) - i - 1, marginalBar);

			// Side bars
			inv.setItem(i * 9, marginalBar);
			inv.setItem(i * 9 + 8, marginalBar);
		}

		return inv;
	}

	private ItemStack getMarginalBar() {
		ItemBuilder builder = new ItemBuilder();
		if (lib.getCompatibilityApi().getServerVersion().lowerThanOr(Versions.v1_12_2))
			builder.item(Material.matchMaterial("STAINED_GLASS_PANE")).data(15);
		else
			builder.item(Material.matchMaterial("GRAY_STAINED_GLASS_PANE"));

		return builder.empty().build(lib);
	}

	private ItemStack getStrafeItem() {
		List<String> lore = new ArrayList<String>();
		lore.add("");
		int cooldown = (int) lib.getConfigurationAPI().getConfig().get("strafes.cooldown", 10);
		if (cooldown != -1)
			lore.add(ChatColor.translateAlternateColorCodes('&',
					"&2" + cooldown + " &aSecond" + (cooldown == 1 ? "" : "s")));
		else
			lore.add(ChatColor.translateAlternateColorCodes('&', "&c&lDISABLED"));
		lore.add("");
		lore.add(ChatColor.GRAY + "Click to toggle.");

		return new ItemBuilder().name("&aStrafe Cooldown").lore(lore)
				.skull(lib.getConfigurationAPI().getConfig().getString("heads.left.active")).build(lib);
	}

	private ItemStack getLeap() {
		List<String> lore = new ArrayList<String>();
		lore.add("");
		int cooldown = (int) lib.getConfigurationAPI().getConfig().get("leap.cooldown", 10);
		if (cooldown != -1)
			lore.add(ChatColor.translateAlternateColorCodes('&',
					"&2" + cooldown + " &aSecond" + (cooldown == 1 ? "" : "s")));
		else
			lore.add(ChatColor.translateAlternateColorCodes('&', "&c&lDISABLED"));
		lore.add(ChatColor.GRAY + "Click to reset to default!");
		return new ItemBuilder(Material.FEATHER).name("&aLeap Cooldown").lore(lore).build(lib);
	}

}
