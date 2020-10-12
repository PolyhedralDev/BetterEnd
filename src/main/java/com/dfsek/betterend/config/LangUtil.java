package com.dfsek.betterend.config;

import com.dfsek.betterend.BetterEnd;
import com.dfsek.betterend.util.Util;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.InvalidConfigurationException;
import org.polydev.gaea.lang.Language;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.jar.JarFile;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LangUtil {
    private static final BetterEnd main = BetterEnd.getInstance();
    private static Language language;
    private static Logger logger;

    private LangUtil() {
    }

    public static Language getLanguage() {
        return language;
    }

    public static void loadlang(String id, Logger log) {
        File file = new File(main.getDataFolder(), "lang");
        logger = log;
        try(JarFile jar = new JarFile(new File(BetterEnd.class.getProtectionDomain().getCodeSource().getLocation().toURI()))) {
            Util.copyResourcesToDirectory(jar, "lang", file.toString());
        } catch(IOException | URISyntaxException e) {
            e.printStackTrace();
        }
        logger.info("Loading language " + id);

        File configFile = new File(main.getDataFolder() + File.separator + "lang" + File.separator + id + ".yml");
        try {
            language = new Language(configFile);
        } catch(IOException e) {
            main.getLogger().severe("Unable to load " + file.toString() + ". Defaulting to language en_us.");
            try {
                language = new Language(new File(main.getDataFolder() + File.separator + "lang" + File.separator + "en_us.yml"));
            } catch(IOException | InvalidConfigurationException e1) {
                e1.printStackTrace();
            }
        } catch(InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }
    public static void log(String messageID, Level level, String... args) {
        language.getMessage(messageID).log(logger, level, args);
    }
    public static void send(String messageID, CommandSender sender, String... args) {
        language.getMessage(messageID).send(sender, args);
    }
}
