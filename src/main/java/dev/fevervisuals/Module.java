package dev.fevervisuals;

public final class Module {
    private final String name;
    private final String category;
    private final String description;
    private final boolean hudVisible;
    private boolean enabled;

    public Module(String name, String category, String description, boolean hudVisible, boolean enabled) {
        this.name = name;
        this.category = category;
        this.description = description;
        this.hudVisible = hudVisible;
        this.enabled = enabled;
    }

    public String getName() { return name; }
    public String getCategory() { return category; }
    public String getDescription() { return description; }
    public boolean isHudVisible() { return hudVisible; }
    public boolean isEnabled() { return enabled; }
    public void toggle() { enabled = !enabled; }
    public void setEnabled(boolean value) { enabled = value; }
}
