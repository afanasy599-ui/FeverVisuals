package dev.fevervisuals;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.lwjgl.glfw.GLFW;

public final class FeverVisuals implements ClientModInitializer {
    public static final String MOD_ID = "fevervisuals";
    public static final ModuleManager MODULES = new ModuleManager();
    private static KeyBinding openMenuKey;
    private static double previousGamma = 1.0;

    @Override
    public void onInitializeClient() {
        MinecraftClient mc = MinecraftClient.getInstance();
        if (mc.options != null) previousGamma = mc.options.getGamma().getValue();
        MODULES.load();

        openMenuKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.fevervisuals.open",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_RIGHT_SHIFT,
                new KeyBinding.Category(Identifier.of(MOD_ID, "main"))
        ));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (openMenuKey.wasPressed()) {
                if (client.currentScreen == null) {
                    client.setScreen(new FeverScreen());
                } else if (client.currentScreen instanceof FeverScreen) {
                    client.setScreen(null);
                }
            }
            applyFullbright(client);
        });

        HudRenderCallback.EVENT.register((drawContext, tickCounter) -> renderHud(MinecraftClient.getInstance(), drawContext));
    }

    private static void applyFullbright(MinecraftClient client) {
        if (client.options == null) return;
        Module fullbright = MODULES.get("Fullbright");
        if (fullbright != null && fullbright.isEnabled()) {
            client.options.getGamma().setValue(16.0);
        }
    }

    private static void renderHud(MinecraftClient client, net.minecraft.client.gui.DrawContext ctx) {
        if (client.options == null || client.player == null) return;
        int width = client.getWindow().getScaledWidth();
        int y = 8;

        if (MODULES.enabled("Watermark")) {
            String title = "FEVER";
            String suffix = "VISUALS";
            int w = client.textRenderer.getWidth(title) + client.textRenderer.getWidth(suffix) + 18;
            ctx.fill(8, 8, 8 + w, 28, 0xD9100D19);
            ctx.fill(8, 8, 10, 28, 0xFFB45CFF);
            ctx.drawTextWithShadow(client.textRenderer, title, 16, 14, 0xFFE7D4FF);
            ctx.drawTextWithShadow(client.textRenderer, suffix, 16 + client.textRenderer.getWidth(title) + 4, 14, 0xFFB45CFF);
        }

        if (MODULES.enabled("ArrayList")) {
            for (Module module : MODULES.all()) {
                if (!module.isEnabled() || !module.isHudVisible() || module.getName().equals("ArrayList")) continue;
                int textWidth = client.textRenderer.getWidth(module.getName());
                int x = width - textWidth - 12;
                ctx.fill(x - 7, y - 3, width - 7, y + 11, 0xA8100D19);
                ctx.fill(width - 9, y - 3, width - 7, y + 11, 0xFFB45CFF);
                ctx.drawTextWithShadow(client.textRenderer, module.getName(), x, y, 0xFFEADFFF);
                y += 15;
            }
        }

        int infoY = client.getWindow().getScaledHeight() - 18;
        StringBuilder info = new StringBuilder();
        if (MODULES.enabled("FPS")) info.append("FPS ").append(client.getCurrentFps()).append("   ");
        if (MODULES.enabled("Coordinates")) {
            info.append(String.format("XYZ %.1f / %.1f / %.1f", client.player.getX(), client.player.getY(), client.player.getZ())).append("   ");
        }
        if (MODULES.enabled("Ping") && client.getNetworkHandler() != null) {
            var entry = client.getNetworkHandler().getPlayerListEntry(client.player.getUuid());
            if (entry != null) info.append("PING ").append(entry.getLatency()).append("ms");
        }
        if (!info.isEmpty()) {
            String value = info.toString().trim();
            ctx.fill(8, infoY - 4, 8 + client.textRenderer.getWidth(value) + 12, infoY + 12, 0xB8100D19);
            ctx.fill(8, infoY - 4, 10, infoY + 12, 0xFFB45CFF);
            ctx.drawTextWithShadow(client.textRenderer, value, 15, infoY, 0xFFEADFFF);
        }
    }

    public static void onModuleToggled(Module module) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (module.getName().equals("Fullbright") && client.options != null) {
            client.options.getGamma().setValue(module.isEnabled() ? 16.0 : previousGamma);
        }
        MODULES.save();
    }

    public static void notify(String message) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player != null) client.player.sendMessage(Text.literal("§5[FeverVisuals] §f" + message), true);
    }
}
