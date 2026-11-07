package my.mod.module.setting;

import com.google.gson.JsonElement;

public abstract class Setting<T> {
    private final String name;
    protected T value;

    protected Setting(String name, T value) {
        this.name = name;
        this.value = value;
    }

    public String getName() { return name; }
    public T get() { return value; }
    public void set(T v) { this.value = v; }

    public abstract String display();
    public abstract JsonElement toJson();
    public abstract void fromJson(JsonElement e);
}
