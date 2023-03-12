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

import masecla.mlib.apis.CompatibilityAPI.Versions;
import masecla.mlib.apis.SoundAPI.Sound;
import masecla.mlib.classes.builders.ItemBuilder;
import masecla.mlib.containers.generic.ImmutableContainer;
import masecla.mlib.main.MLib;
import net.md_5.bungee.api.ChatColor;

public class VelocityContainer extends ImmutableContainer {

	public VelocityContainer(MLib lib) {
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

		// Reset Strafe Velocities
		if (event.getSlot() == 22) {
			lib.getConfigurationAPI().getConfig().set("strafes.strafe_velocity", 1.78);
			lib.getConfigurationAPI().getConfig().set("strafes.vertical_velocity", 0.3);
			p.playSound(p.getLocation(), Sound.ARROW_HIT.bukkitSound(), 0.8f, 1);
			return;
		}

		// Reset Leap Velocities
		if (event.getSlot() == 40) {
			lib.getConfigurationAPI().getConfig().set("leap.leap_velocity", 1.5);
			lib.getConfigurationAPI().getConfig().set("leap.vertical_velocity", 0.4);
			p.playSound(p.getLocation(), Sound.ARROW_HIT.bukkitSound(), 0.8f, 1);
			return;
		}
		if (event.getCurrentItem() == null || event.getCurrentItem().getType().equals(Material.AIR))
			return;

		// Incrementing and decrementing
		String tag = lib.getNmsAPI().getNBTTagValueString(event.getCurrentItem(), "Increment");
		if (tag != null) {
			String type = tag.split("_")[0];
			double value = Double.parseDouble(tag.split("_")[1]);
			switch (type) {
				case "Leap":
					lib.getConfigurationAPI().getConfig().set("leap.leap_velocity",
							(double) lib.getConfigurationAPI().getConfig().get("leap.leap_velocity", 1.5) + value);
					p.playSound(p.getLocation(), Sound.ARROW_HIT.bukkitSound(), 0.8f, 1);
					return;
				case "Strafe":
					lib.getConfigurationAPI().getConfig().set("strafes.strafe_velocity",
							(double) lib.getConfigurationAPI().getConfig().get("strafes.strafe_velocity", 1.78)
									+ value);
					p.playSound(p.getLocation(), Sound.ARROW_HIT.bukkitSound(), 0.8f, 1);
					return;
				case "LeapV":
					lib.getConfigurationAPI().getConfig().set("leap.vertical_velocity",
							(double) lib.getConfigurationAPI().getConfig().get("leap.vertical_velocity", 0.4) + value);
					p.playSound(p.getLocation(), Sound.ARROW_HIT.bukkitSound(), 0.8f, 1);
					return;
				case "StrafeV":
					lib.getConfigurationAPI().getConfig().set("strafes.vertical_velocity",
							(double) lib.getConfigurationAPI().getConfig().get("strafes.vertical_velocity", 0.3)
									+ value);
					p.playSound(p.getLocation(), Sound.ARROW_HIT.bukkitSound(), 0.8f, 1);
					return;
			}
		}
	}

	@Override
	public int getSize(Player p) {
		return 54;
	}

	@Override
	public int getUpdatingInterval() {
		return 5;
	}

	@Override
	public boolean requiresUpdating() {
		return true;
	}

	@Override
	public Inventory getInventory(Player p) {
		Inventory inv = Bukkit.createInventory(p, getSize(p), ChatColor.DARK_GREEN + "Strafe Settings");

		inv.setItem(19, this.getIncrementItem(-1, "Strafe Velocity"));
		inv.setItem(20, this.getIncrementItem(-0.5, "Strafe Velocity"));
		inv.setItem(21, this.getIncrementItem(-0.05, "Strafe Velocity"));
		inv.setItem(22, this.getStrafeItem());
		inv.setItem(23, this.getIncrementItem(0.05, "Strafe Velocity"));
		inv.setItem(24, this.getIncrementItem(0.5, "Strafe Velocity"));
		inv.setItem(25, this.getIncrementItem(1, "Strafe Velocity"));

		inv.setItem(37, this.getIncrementItem(-1, "Leap Velocity"));
		inv.setItem(38, this.getIncrementItem(-0.5, "Leap Velocity"));
		inv.setItem(39, this.getIncrementItem(-0.05, "Leap Velocity"));
		inv.setItem(40, this.getLeap());
		inv.setItem(41, this.getIncrementItem(0.05, "Leap Velocity"));
		inv.setItem(42, this.getIncrementItem(0.5, "Leap Velocity"));
		inv.setItem(43, this.getIncrementItem(1, "Leap Velocity"));

		inv.setItem(12, this.getVerticalIncrement(-0.05, "Strafe Vertical Velocity"));
		inv.setItem(14, this.getVerticalIncrement(0.05, "Strafe Vertical Velocity"));

		inv.setItem(30, this.getVerticalIncrement(-0.05, "Leap Vertical Velocity"));
		inv.setItem(32, this.getVerticalIncrement(0.05, "Leap Vertical Velocity"));

		inv = this.applyMarginalBars(inv);
		inv.setItem(49, this.getInventoryClose());
		return inv;
	}

	private ItemStack getVerticalIncrement(double count, String type) {
		Material itemMaterial = (count > 0 ? Material.BLAZE_ROD : Material.STICK);
		ItemStack result = new ItemStack(itemMaterial);
		ItemMeta meta = result.getItemMeta();
		meta.setDisplayName(
				ChatColor.translateAlternateColorCodes('&', "&2" + (count < 0 ? count : "+" + count) + " &a" + type));
		meta.setLore(Arrays.asList("", ChatColor.GRAY + "Click!"));
		result.setItemMeta(meta);
		return lib.getNmsAPI().write().tagString("Increment", type.split(" ")[0] + "V" + "_" + count).applyOn(result);
	}

	private ItemStack getIncrementItem(double count, String type) {
		ItemBuilder builder = new ItemBuilder();
		if (lib.getCompatibilityApi().getServerVersion().lowerThanOr(Versions.v1_12_2))
			builder.item(Material.matchMaterial("WOOL")).data(this.getColorFor(count));
		else
			builder.item(Material.matchMaterial(convertColor(count) + "_WOOL"));
		return builder.name("&2" + (count < 0 ? count : "+" + count) + " &a" + type).lore("").lore("&7Click!").tagString("Increment", type.split(" ")[0] + "_" + count).build(lib);
	}

	private String convertColor(double count) {
		int color = getColorFor(count);
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

	private ItemStack getStrafeItem() {
		List<String> lore = new ArrayList<String>();
		lore.add("");
		lore.add(
				"&2" + (double) lib.getConfigurationAPI().getConfig().get("strafes.strafe_velocity", 1.78)
						+ " &aStrafe Velocity");
		lore.add(
				"&2" + (double) lib.getConfigurationAPI().getConfig().get("strafes.vertical_velocity", 0.3)
						+ " &aVertical Velocity");
		lore.add("");
		lore.add(ChatColor.GRAY + "Click to reset to default!");
		return new ItemBuilder()
				.skull(lib.getConfigurationAPI().getConfig().getString("heads.left.active")).name("&aStrafe").lore(lore)
				.build(lib);
	}

	private ItemStack getLeap() {
		List<String> lore = new ArrayList<String>();
		lore.add("");
		lore.add(ChatColor.translateAlternateColorCodes('&', "&2"
				+ (double) lib.getConfigurationAPI().getConfig().get("leap.leap_velocity", 1.5) + " &aLeap Velocity"));
		lore.add(ChatColor.translateAlternateColorCodes('&',
				"&2" + (double) lib.getConfigurationAPI().getConfig().get("leap.vertical_velocity", 0.4)
						+ " &aVertical Velocity"));
		lore.add("");
		lore.add(ChatColor.GRAY + "Click to reset to default!");
		return new ItemBuilder(Material.FEATHER).name("&aLeap Velocities").lore(lore).build(lib);
	}

	private int getColorFor(double count) {
		count = Math.abs(count);
		if (count == 0.05)
			return 13;
		if (count == 0.5)
			return 1;
		return 14;
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

}
