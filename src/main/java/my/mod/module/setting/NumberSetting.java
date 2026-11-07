package my.mod.module.setting;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;

import java.util.Locale;

public class NumberSetting extends Setting<Double> {
    private final double min, max, step;

    public NumberSetting(String name, double def, double min, double max, double step) {
        super(name, def);
        this.min = min;
        this.max = max;
        this.step = step;
    }

    public double getMin() { return min; }
    public double getMax() { return max; }

    @Override
    public void set(Double v) {
        double c = Math.max(min, Math.min(max, v));
        if (step > 0) c = Math.round(c / step) * step;
        c = Math.max(min, Math.min(max, c));
        value = Math.round(c * 1000.0) / 1000.0;
    }

    public float f() { return value.floatValue(); }
    public int i() { return (int) Math.round(value); }

    @Override public String display() {
        return step >= 1 ? String.valueOf(i()) : String.format(Locale.ROOT, "%.2f", value);
    }
    @Override public JsonElement toJson() { return new JsonPrimitive(value); }
    @Override public void fromJson(JsonElement e) {
        if (e.isJsonPrimitive()) set(e.getAsDouble());
    }
}
