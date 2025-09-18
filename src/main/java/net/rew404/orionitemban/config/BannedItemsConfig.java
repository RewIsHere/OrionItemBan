package net.rew404.orionitemban.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import net.rew404.orionitemban.OrionItemBan;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.lang.reflect.Type;
import java.util.*;

public class BannedItemsConfig {

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final String CONFIG_PATH = "config/orionitemban/banned_items.json";
    private static final String BLOCK_CONFIG_PATH = "config/orionitemban/blocked_blocks.json";

    public static Set<String> DROP_FROM_INVENTORY = new HashSet<>();
    public static Set<String> DELETE_ON_PICKUP = new HashSet<>();
    public static Set<String> BAN_CRAFTING = new HashSet<>();
    public static Set<String> REMOVE_FROM_DUNGEONS = new HashSet<>();


    // Bloqueos de bloques
    public static boolean DISABLE_ENCHANTING_TABLE = false;
    public static boolean DISABLE_ANVIL = false;

    /** Cargar el JSON, crear archivo por defecto si no existe */
    public static void loadConfig() {
        try {
            File file = new File(CONFIG_PATH);
            if (!file.exists()) {
                OrionItemBan.LOGGER.info("Archivo de config no encontrado, creando uno por defecto...");
                file.getParentFile().mkdirs();
                saveDefaultConfig(file);
            }

            FileReader reader = new FileReader(file);
            Type type = new TypeToken<Map<String, Set<String>>>() {}.getType();
            Map<String, Set<String>> map = GSON.fromJson(reader, type);
            reader.close();

            DROP_FROM_INVENTORY = map.getOrDefault("drop_from_inventory", new HashSet<>());
            DELETE_ON_PICKUP = map.getOrDefault("delete_on_pickup", new HashSet<>());
            BAN_CRAFTING = map.getOrDefault("ban_crafting", new HashSet<>());
            REMOVE_FROM_DUNGEONS = map.getOrDefault("remove_from_dungeons", new HashSet<>());

            OrionItemBan.LOGGER.info("Configuración de items baneados cargada correctamente ✅");

        } catch (Exception e) {
            OrionItemBan.LOGGER.error("Error al cargar el JSON de items baneados", e);
        }
    }

    /** Crear archivo JSON por defecto */
    private static void saveDefaultConfig(File file) {
        try {
            Map<String, Set<String>> defaults = new LinkedHashMap<>();
            defaults.put("drop_from_inventory", new HashSet<>());
            defaults.put("delete_on_pickup", new HashSet<>());
            defaults.put("ban_crafting", new HashSet<>());
            defaults.put("remove_from_dungeons", new HashSet<>());

            try (FileWriter writer = new FileWriter(file)) {
                GSON.toJson(defaults, writer);
            }

            OrionItemBan.LOGGER.info("Archivo de configuración de items baneados creado automáticamente.");
        } catch (Exception e) {
            OrionItemBan.LOGGER.error("Error creando JSON por defecto", e);
        }
    }

    /** Guardar cambios en el JSON */
    public static void saveConfig() {
        try {
            Map<String, Set<String>> map = new LinkedHashMap<>();
            map.put("drop_from_inventory", DROP_FROM_INVENTORY);
            map.put("delete_on_pickup", DELETE_ON_PICKUP);
            map.put("ban_crafting", BAN_CRAFTING);
            map.put("remove_from_dungeons", REMOVE_FROM_DUNGEONS);

            File file = new File(CONFIG_PATH);
            file.getParentFile().mkdirs();
            try (FileWriter writer = new FileWriter(file)) {
                GSON.toJson(map, writer);
            }

            OrionItemBan.LOGGER.info("Configuración de items baneados guardada ✅");
        } catch (Exception e) {
            OrionItemBan.LOGGER.error("Error guardando JSON de items baneados", e);
        }
    }

    /** Métodos utilitarios para chequear categorías */
    public static boolean isDropFromInventory(String itemId) { return DROP_FROM_INVENTORY.contains(itemId); }
    public static boolean isDeleteOnPickup(String itemId) { return DELETE_ON_PICKUP.contains(itemId); }
    public static boolean isBanCrafting(String itemId) { return BAN_CRAFTING.contains(itemId); }
    public static boolean isRemoveFromDungeons(String itemId) { return REMOVE_FROM_DUNGEONS.contains(itemId); }

    /** Agregar item a categoría y guardar JSON */
    public static void banItem(String itemId, String category) {
        switch (category.toLowerCase()) {
            case "drop" -> DROP_FROM_INVENTORY.add(itemId);
            case "pickup" -> DELETE_ON_PICKUP.add(itemId);
            case "crafting" -> BAN_CRAFTING.add(itemId);
            case "dungeon" -> REMOVE_FROM_DUNGEONS.add(itemId);
            default -> OrionItemBan.LOGGER.warn("Categoría desconocida: " + category);
        }
        saveConfig();
    }

    /** Quitar item de todas las categorías y guardar JSON */
    public static void unbanItem(String itemId) {
        DROP_FROM_INVENTORY.remove(itemId);
        DELETE_ON_PICKUP.remove(itemId);
        BAN_CRAFTING.remove(itemId);
        REMOVE_FROM_DUNGEONS.remove(itemId);
        saveConfig();
    }

    /** Devuelve todos los items baneados como un String legible */
    public static String itemsListToString() {
        StringBuilder sb = new StringBuilder();

        if(!DROP_FROM_INVENTORY.isEmpty()) {
            sb.append("DROP_FROM_INVENTORY: ").append(String.join(", ", DROP_FROM_INVENTORY)).append("\n");
        }
        if(!DELETE_ON_PICKUP.isEmpty()) {
            sb.append("DELETE_ON_PICKUP: ").append(String.join(", ", DELETE_ON_PICKUP)).append("\n");
        }
        if(!BAN_CRAFTING.isEmpty()) {
            sb.append("BAN_CRAFTING: ").append(String.join(", ", BAN_CRAFTING)).append("\n");
        }
        if(!REMOVE_FROM_DUNGEONS.isEmpty()) {
            sb.append("REMOVE_FROM_DUNGEONS: ").append(String.join(", ", REMOVE_FROM_DUNGEONS)).append("\n");
        }

        if(sb.isEmpty()) return "No hay items baneados.";
        return sb.toString().trim();
    }

    /** -----------------------------------
     * Configuración de bloques
     * ----------------------------------- */

    public static void loadBlockConfig() {
        try {
            File file = new File(BLOCK_CONFIG_PATH);
            if (!file.exists()) {
                saveBlockConfig();
                return;
            }
            FileReader reader = new FileReader(file);
            Type type = new TypeToken<Map<String, Boolean>>(){}.getType();
            Map<String, Boolean> map = GSON.fromJson(reader, type);
            reader.close();

            DISABLE_ENCHANTING_TABLE = map.getOrDefault("disable_enchanting_table", false);
            DISABLE_ANVIL = map.getOrDefault("disable_anvil", false);
        } catch (Exception e) {
            OrionItemBan.LOGGER.error("Error cargando config de bloques", e);
        }
    }

    public static void saveBlockConfig() {
        try {
            Map<String, Boolean> map = new LinkedHashMap<>();
            map.put("disable_enchanting_table", DISABLE_ENCHANTING_TABLE);
            map.put("disable_anvil", DISABLE_ANVIL);

            File file = new File(BLOCK_CONFIG_PATH);
            file.getParentFile().mkdirs();
            try (FileWriter writer = new FileWriter(file)) {
                GSON.toJson(map, writer);
            }
        } catch (Exception e) {
            OrionItemBan.LOGGER.error("Error guardando config de bloques", e);
        }
    }

    public static void setDisableEnchantingTable(boolean value) {
        DISABLE_ENCHANTING_TABLE = value;
        saveBlockConfig();
    }

    public static void setDisableAnvil(boolean value) {
        DISABLE_ANVIL = value;
        saveBlockConfig();
    }
}
