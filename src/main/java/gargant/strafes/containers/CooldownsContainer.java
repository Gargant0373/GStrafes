package gargant.strafes.containers;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;

import masecla.mlib.classes.Replaceable;
import masecla.mlib.containers.generic.ImmutableContainer;
import masecla.mlib.main.MLib;
import net.md_5.bungee.api.ChatColor;
import revive.retab.classes.skin.Skin;

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
			p.playSound(p.getLocation(), Sound.ENTITY_ARROW_HIT_PLAYER, 0.8f, 1);
			return;
		}
		if (event.getSlot() == 40) {
			int cooldown = (int) lib.getConfigurationAPI().getConfig().get("leap.cooldown", 20);
			if (cooldown == -1)
				lib.getConfigurationAPI().getConfig().set("leap.cooldown", 20);
			else
				lib.getConfigurationAPI().getConfig().set("leap.cooldown", -1);
			p.playSound(p.getLocation(), Sound.ENTITY_ARROW_HIT_PLAYER, 0.8f, 1);
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
			p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BASS, 1, 0.8f);
			return;
		}

		if (cooldownValue + amount <= 0 || cooldownValue + amount > 64) {
			lib.getMessagesAPI().sendMessage("invalid-cooldown", p, new Replaceable("%value%", cooldownValue + amount));
			p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BASS, 1, 0.8f);
			return;
		}
		lib.getConfigurationAPI().getConfig().set(path, cooldownValue + amount);
		p.playSound(p.getLocation(), Sound.ENTITY_ARROW_HIT_PLAYER, 0.8f, 1);
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
		ItemStack result = new ItemStack(Material.SKULL_ITEM, 1, (byte) 3);
		SkullMeta theMeta = (SkullMeta) result.getItemMeta();
		GameProfile profile = new Skin(
				"ewogICJ0aW1lc3RhbXAiIDogMTYxNjI1ODY1NTExMiwKICAicHJvZmlsZUlkIiA6ICJiMGQ3MzJmZTAwZjc0MDdlOWU3Zjc0NjMwMWNkOThjYSIsCiAgInByb2ZpbGVOYW1lIiA6ICJPUHBscyIsCiAgInNpZ25hdHVyZVJlcXVpcmVkIiA6IHRydWUsCiAgInRleHR1cmVzIiA6IHsKICAgICJTS0lOIiA6IHsKICAgICAgInVybCIgOiAiaHR0cDovL3RleHR1cmVzLm1pbmVjcmFmdC5uZXQvdGV4dHVyZS9lNDRlYWUyNTAxMzk4M2E5MWQ2YTY5YjFiZjExMjdjMjY0NTk5MWExZDIwNWY2ZTUzODY1OTQ3ZjE0ZDVmNDVlIiwKICAgICAgIm1ldGFkYXRhIiA6IHsKICAgICAgICAibW9kZWwiIDogInNsaW0iCiAgICAgIH0KICAgIH0KICB9Cn0=",
				"XH3TZYlsF7xmneUF7Z5sKBFBSrMTI+tY64PbAeFJcH4MrHGz38Z5Lc9kub3qG20LJFa861mHFeIAq+Ca6aLFdE3+5hHrxGJalj+JoF8gFP2Fx0u9myy633IAU+Uj/MKCq1BXpEVeAcDBUMTn6S0/j5YUVi3Q9PWsBoBH5rQk4HYetW0LN0qmd9BJwxfB16xHJQxMrfrIGlpLfts8Md58p+Q1cvHU5ZiJjnmA5lP66YP15eduX6APwhW5somS8A7Cl/P4k+P08SjAudkvQzvM+TGj+Mz2jNFG3qSp2z2p/X/jooRByKWfPzwvQ/O1KuJrWKUC3kzXGPrMBHLXGv/Gti3EvMoKNM1+KKcq5s7T5ZYTlMhSnKBFKgB6W9fQtSyZtlmVN1MPI1tnUxOgU4lEYuVRAaSgM/VcYSSivHW+0+moRESIG0CpFluJsHtMQ/B4aMqEJXDVXxu+jC/SDkQ+MNJb62gleTGbEn8TUyHVLLLngVXMasGmh4HjZuazsLB1nCLjxXzXvMe72b9ovpwF0o3xRzDbcc6JvGk0ajshzneDbkl0ucStCv7xCfXP8zKXupGVfyo/0kuYhdMv5SpskVrfxSsFGoK/xjgY95hADQbCTuRoOyK+1PbQyYrHr9vfb/LmlonF0c/AbwCP7UrYfHVPuK+VM8HLT7Rk70oC5dA=")
						.createProfile();
		Field profileField = null;
		try {
			profileField = theMeta.getClass().getDeclaredField("profile");
			profileField.setAccessible(true);
			profileField.set(theMeta, profile);
		} catch (IllegalAccessException | IllegalArgumentException | NoSuchFieldException
				| SecurityException exception) {
			// empty catch block
		}
		theMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&cClose!"));
		ArrayList<String> thelore = new ArrayList<String>();
		thelore.add(" ");
		thelore.add(ChatColor.translateAlternateColorCodes('&', "&7Close this menu!"));
		thelore.add(" ");
		theMeta.setLore(thelore);
		result.setItemMeta(theMeta);
		return result;
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
		ItemStack result = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
		ItemMeta theMeta = result.getItemMeta();
		theMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&aStrafe Cooldown"));
		List<String> thelore = new ArrayList<String>();
		thelore.add("");
		int cooldown = (int) lib.getConfigurationAPI().getConfig().get("strafes.cooldown", 10);
		if (cooldown != -1)
			thelore.add(ChatColor.translateAlternateColorCodes('&',
					"&2" + cooldown + " &aSecond" + (cooldown == 1 ? "" : "s")));
		else
			thelore.add(ChatColor.translateAlternateColorCodes('&', "&c&lDISABLED"));
		thelore.add("");
		thelore.add(ChatColor.GRAY + "Click to toggle.");
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
		return result;
	}

	private ItemStack getLeap() {
		ItemStack result = new ItemStack(Material.FEATHER);
		ItemMeta theMeta = result.getItemMeta();
		theMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&aLeap Cooldown"));
		List<String> thelore = new ArrayList<String>();
		thelore.add("");
		int cooldown = (int) lib.getConfigurationAPI().getConfig().get("leap.cooldown", 10);
		if (cooldown != -1)
			thelore.add(ChatColor.translateAlternateColorCodes('&',
					"&2" + cooldown + " &aSecond" + (cooldown == 1 ? "" : "s")));
		else
			thelore.add(ChatColor.translateAlternateColorCodes('&', "&c&lDISABLED"));
		thelore.add(ChatColor.GRAY + "Click to reset to default!");
		theMeta.setLore(thelore);
		result.setItemMeta(theMeta);
		return result;
	}

}
