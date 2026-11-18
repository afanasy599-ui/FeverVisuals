package dev.fevervisuals;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.fabricmc.loader.api.FabricLoader;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class ModuleManager {
    private final List<Module> modules = new ArrayList<>();

    public ModuleManager() {
        modules.add(new Module("Watermark", "HUD", "FeverVisuals brand watermark", false, true));
        modules.add(new Module("ArrayList", "HUD", "Animated list of enabled modules", false, true));
        modules.add(new Module("FPS", "HUD", "Display client frame rate", false, true));
        modules.add(new Module("Coordinates", "HUD", "Display player coordinates", false, true));
        modules.add(new Module("Ping", "HUD", "Display connection latency", false, true));
        modules.add(new Module("Fullbright", "World", "Increase client gamma", true, false));
    }

    public List<Module> all() { return Collections.unmodifiableList(modules); }
    public List<Module> byCategory(String category) {
        return modules.stream().filter(m -> m.getCategory().equals(category)).toList();
    }
    public Module get(String name) {
        return modules.stream().filter(m -> m.getName().equals(name)).findFirst().orElse(null);
    }
    public boolean enabled(String name) {
        Module module = get(name);
        return module != null && module.isEnabled();
    }

    public void load() {
        Path file = configFile();
        if (!Files.exists(file)) return;
        try {
            JsonObject json = JsonParser.parseString(Files.readString(file)).getAsJsonObject();
            for (Module module : modules) {
                if (json.has(module.getName())) module.setEnabled(json.get(module.getName()).getAsBoolean());
            }
        } catch (Exception ignored) {
            // A malformed config should never prevent the client from starting.
        }
    }

    public void save() {
        JsonObject json = new JsonObject();
        for (Module module : modules) json.addProperty(module.getName(), module.isEnabled());
        try {
            Files.createDirectories(configFile().getParent());
            Files.writeString(configFile(), json.toString());
        } catch (IOException ignored) {
            // Config persistence is best-effort; gameplay remains unaffected.
        }
    }

    private Path configFile() {
        return FabricLoader.getInstance().getConfigDir().resolve("fevervisuals.json");
    }
}
