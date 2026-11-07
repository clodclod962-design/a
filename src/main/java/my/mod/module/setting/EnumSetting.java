package my.mod.module.setting;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;

public class EnumSetting<E extends Enum<E>> extends Setting<E> {
    private final E[] values;

    public EnumSetting(String name, E def) {
        super(name, def);
        this.values = def.getDeclaringClass().getEnumConstants();
    }

    public void next() { value = values[(value.ordinal() + 1) % values.length]; }

    @Override public String display() { return value.name(); }
    @Override public JsonElement toJson() { return new JsonPrimitive(value.name()); }
    @Override public void fromJson(JsonElement e) {
        if (!e.isJsonPrimitive()) return;
        String s = e.getAsString();
        for (E v : values) if (v.name().equalsIgnoreCase(s)) value = v;
    }
}
