package me.maximus1027.NameColors.Utils;

import de.tr7zw.nbtapi.NBTItem;
import me.maximus1027.NameColors.Main;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class Misc {
    public static String conv(String msg) {
        return ChatColor.translateAlternateColorCodes('&', msg);
    }

    public static ItemStack getNameColorStack(String id) {
        FileConfiguration config = Main.getInstance().getConfig();
        String path = "namecolors."+id;

        String materialName = config.getString(path+".item-locked");

        ItemStack NameColor;

        if (materialName.contains(":")) {
            NameColor = new ItemStack(Material.valueOf(materialName.toUpperCase().split(":")[0]), 1, Short.parseShort(materialName.split(":")[1]));
        }
        else {
            NameColor = new ItemStack(Material.valueOf(materialName.toUpperCase()));
        }

        ItemMeta NameColorMeta = NameColor.getItemMeta();
        NameColorMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        NameColorMeta.setDisplayName(conv(config.getString(path+".display-name")));

        List<String> lore = new ArrayList<>();
        for (String tLore : config.getStringList(path+".lore")) {
            lore.add(conv(tLore));
        }

        NameColorMeta.setLore(lore);
        NameColor.setItemMeta(NameColorMeta);

        NBTItem nbtItem = new NBTItem(NameColor);
        nbtItem.setString("namecolor", id);
        nbtItem.setString("unlock", config.getString(path+".item"));
        nbtItem.applyNBT(NameColor);

        return NameColor;
    }


}
