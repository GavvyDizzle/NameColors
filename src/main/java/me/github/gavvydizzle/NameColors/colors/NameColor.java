package me.github.gavvydizzle.NameColors.colors;

import com.github.mittenmc.serverutils.Colors;
import com.github.mittenmc.serverutils.ConfigUtils;
import com.github.mittenmc.serverutils.ItemStackUtils;
import me.github.gavvydizzle.NameColors.pattern.GradientPattern;
import me.github.gavvydizzle.NameColors.pattern.Pattern;
import me.github.gavvydizzle.NameColors.pattern.SolidPattern;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class NameColor {

    private final Material lockedMaterial, unlockedMaterial;
    private final String id, displayName;
    private final List<String> lore;
    private final Pattern pattern;
    private ItemStack unlockedItem, lockedItem;
    private final boolean hidden;

    public NameColor(String id, String displayName, List<String> lore, String lockedItem, String unlockedItem, String pattern, boolean hidden) {
        this.id = id;
        this.lore = Colors.conv(lore);
        this.lockedMaterial = ConfigUtils.getMaterial(lockedItem, Material.GRAY_DYE);
        this.unlockedMaterial = ConfigUtils.getMaterial(unlockedItem, Material.LIME_DYE);
        this.hidden = hidden;

        if (pattern.toLowerCase().contains("gradient")) {
            this.pattern = new GradientPattern(pattern);
        }
        else {
            this.pattern = new SolidPattern(pattern);
        }

        // if the displayName contains color codes
        if (!Colors.conv(displayName).equals(displayName)) {
            this.displayName = Colors.conv(displayName);
        }
        else { // if not, name it the pattern of this NameColor
            this.displayName = this.pattern.withPattern(displayName);
        }

        generateItems();
    }

    protected void generateItems() {
        unlockedItem = new ItemStack(unlockedMaterial);
        ItemMeta meta = unlockedItem.getItemMeta();
        assert meta != null;
        meta.setDisplayName(displayName);
        meta.setLore(lore);
        unlockedItem.setItemMeta(meta);
        ItemStackUtils.addAllItemFlags(unlockedItem);

        lockedItem = new ItemStack(lockedMaterial);
        meta = lockedItem.getItemMeta();
        assert meta != null;
        meta.setDisplayName(displayName);
        meta.setLore(lore);
        lockedItem.setItemMeta(meta);
        ItemStackUtils.addAllItemFlags(lockedItem);
    }

    public String getPermission() {
        return "namecolors.color." + id;
    }

    public ItemStack getUnlockedItem() {
        return unlockedItem;
    }

    public ItemStack getLockedItem() {
        return lockedItem;
    }

    public String getId() {
        return id;
    }

    public Pattern getPattern() {
        return pattern;
    }

    public boolean isHidden() {
        return hidden;
    }
}
