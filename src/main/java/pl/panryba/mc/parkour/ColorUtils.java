package pl.panryba.mc.parkour;

import org.bukkit.ChatColor;

/**
 * @author PanRyba.pl
 */
public class ColorUtils {
    public static String replaceColors(String message) {
        message = message.replaceAll("(?i)&([a-f0-9])", "§$1");
        message = message.replaceAll("(?i)&l", ChatColor.BOLD.toString());
        message = message.replaceAll("(?i)&r", ChatColor.RESET.toString());

        return message;
    }

    public static String removeColors(String message) {
        return message.replaceAll("(?i)§([a-f0-9klmnor])", "");
    }
}
