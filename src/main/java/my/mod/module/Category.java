package my.mod.module;

public enum Category {
    COMBAT("Combat"),
    MOVEMENT("Movement"),
    PLAYER("Player"),
    RENDER("Render"),
    MISC("Misc");

    private final String label;
    Category(String label) { this.label = label; }
    public String getLabel() { return label; }
}
