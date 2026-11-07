package my.mod.config;

import com.google.gson.*;
import my.mod.MyMod;
import my.mod.module.Module;
import my.mod.module.setting.Setting;
import net.fabricmc.loader.api.FabricLoader;

import java.nio.file.Files;
import java.nio.file.Path;

/** Сохраняет бинды и настройки модулей в .minecraft/config/mymod.json */
public final class Config {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    private Config() {}

    private static Path path() {
        return FabricLoader.getInstance().getConfigDir().resolve("mymod.json");
    }

    public static void save() {
        if (MyMod.MODULES == null) return;
        try {
            JsonObject root = new JsonObject();
            for (Module m : MyMod.MODULES.getAll()) {
                JsonObject o = new JsonObject();
                o.addProperty("key", m.getKey());
                JsonObject s = new JsonObject();
                for (Setting<?> st : m.getSettings()) s.add(st.getName(), st.toJson());
                o.add("settings", s);
                root.add(m.getName(), o);
            }
            Files.writeString(path(), GSON.toJson(root));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void load() {
        Path p = path();
        if (!Files.exists(p)) return;
        try {
            JsonObject root = JsonParser.parseString(Files.readString(p)).getAsJsonObject();
            for (Module m : MyMod.MODULES.getAll()) {
                JsonElement me = root.get(m.getName());
                if (me == null || !me.isJsonObject()) continue;
                JsonObject o = me.getAsJsonObject();
                if (o.has("key")) m.setKey(o.get("key").getAsInt());
                if (o.has("settings") && o.get("settings").isJsonObject()) {
                    JsonObject so = o.getAsJsonObject("settings");
                    for (Setting<?> st : m.getSettings()) {
                        if (so.has(st.getName())) st.fromJson(so.get(st.getName()));
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
