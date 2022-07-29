package gargant.strafes.containers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import masecla.mlib.apis.SoundAPI.Sound;
import masecla.mlib.classes.Replaceable;
import masecla.mlib.classes.builders.ItemBuilder;
import masecla.mlib.containers.generic.ImmutableContainer;
import masecla.mlib.main.MLib;
import net.md_5.bungee.api.ChatColor;

public class CooldownsContainer extends ImmutableContainer {

	public CooldownsContainer(MLib lib) {
		super(lib);
	}

	@SuppressWarnings("deprecation")
	@Override
	public void onTopClick(InventoryClickEvent event) {
		event.setCancelled(true);
		Player p = (Player) event.getWhoClicked();
		if (event.getSlot() == 49) {
			p.closeInventory();
			return;
		}
		if (event.getSlot() == 22) {
			int cooldown = (int) lib.getConfigurationAPI().getConfig().get("strafes.cooldown", 20);
			if (cooldown == -1)
				lib.getConfigurationAPI().getConfig().set("strafes.cooldown", 20);
			else
				lib.getConfigurationAPI().getConfig().set("strafes.cooldown", -1);
			p.playSound(p.getLocation(), Sound.ARROW_HIT.bukkitSound(), 0.8f, 1);
			return;
		}
		if (event.getSlot() == 40) {
			int cooldown = (int) lib.getConfigurationAPI().getConfig().get("leap.cooldown", 20);
			if (cooldown == -1)
				lib.getConfigurationAPI().getConfig().set("leap.cooldown", 20);
			else
				lib.getConfigurationAPI().getConfig().set("leap.cooldown", -1);
			p.playSound(p.getLocation(), Sound.ARROW_HIT.bukkitSound(), 0.8f, 1);
			return;
		}

		String tag = lib.getNmsAPI().getNBTTagValueString(event.getCurrentItem(), "INCREMENT");
		if (tag == null || !event.getCurrentItem().getType().equals(Material.WOOL))
			return;

		int amount = Integer.parseInt(tag.split("_")[0]);
		String type = tag.split("_")[1];
		String path = type + ".cooldown";

		int cooldownValue = (int) lib.getConfigurationAPI().getConfig().get(path, 10);
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
		lib.getConfigurationAPI().getConfig().set(path, cooldownValue + amount);
		p.playSound(p.getLocation(), Sound.ARROW_HIT.bukkitSound(), 0.8f, 1);
	}

	@Override
	public int getSize() {
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
		Inventory inv = Bukkit.createInventory(p, getSize(), ChatColor.DARK_GREEN + "Strafe Cooldowns");
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
		ItemStack result = new ItemStack(Material.WOOL, 1, (byte) this.getColorFor(amount));
		ItemMeta meta = result.getItemMeta();
		meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&2" + (amount > 0 ? "+" : "") + amount
				+ " &aSecond" + (Math.abs(amount) == 1 ? "" : "s") + " to Cooldown"));
		meta.setLore(Arrays.asList("", ChatColor.GRAY + "Click to use!"));
		result.setItemMeta(meta);
		return lib.getNmsAPI().write().tagString("INCREMENT", amount + "_" + type.toLowerCase()).applyOn(result);
	}

	private int getColorFor(int count) {
		count = Math.abs(count);
		if (count == 1)
			return 13;
		if (count == 5)
			return 1;
		return 14;
	}

	private ItemStack getInventoryClose() {
		return new ItemBuilder(Material.SKULL_ITEM).name("&cClose!").lore("", "&7Close this menu.").skull(
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
			inv.setItem(i + this.getSize() - 9, marginalBar);
			inv.setItem(this.getSize() - i - 1, marginalBar);

			// Side bars
			inv.setItem(i * 9, marginalBar);
			inv.setItem(i * 9 + 8, marginalBar);
		}

		return inv;
	}

	private ItemStack getMarginalBar() {
		ItemStack s = new ItemStack(Material.STAINED_GLASS_PANE, 1, (byte) 15);
		ItemMeta meta = s.getItemMeta();
		meta.setDisplayName(ChatColor.BLACK + "+");
		s.setItemMeta(meta);
		return s;
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

		return new ItemBuilder(Material.SKULL_ITEM).name("&aStrafe Cooldown").lore(lore)
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
