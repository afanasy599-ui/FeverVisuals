package dev.fevervisuals;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

public final class FeverScreen extends Screen {
    private static final int PANEL_WIDTH = 310;
    private static final int ROW_HEIGHT = 27;

    public FeverScreen() {
        super(Text.literal("FeverVisuals"));
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        MinecraftClient client = MinecraftClient.getInstance();

        context.fill(0, 0, this.width, this.height, 0x99070010);

        int left = (this.width - PANEL_WIDTH) / 2;
        int top = (this.height - 250) / 2;
        int right = left + PANEL_WIDTH;

        context.fill(left, top, right, top + 250, 0xF20E0A18);
        context.fill(left, top, right, top + 2, 0xFFB45CFF);
        context.fill(left, top + 34, right, top + 35, 0xFF49305E);

        context.drawTextWithShadow(client.textRenderer, "FEVER", left + 14, top + 13, 0xFFEADFFF);
        int feverWidth = client.textRenderer.getWidth("FEVER");
        context.drawTextWithShadow(client.textRenderer, "VISUALS", left + 17 + feverWidth, top + 13, 0xFFB45CFF);
        context.drawTextWithShadow(client.textRenderer, "RIGHT SHIFT  |  ESC TO CLOSE",
                left + 14, top + 43, 0xFF9B8AA8);

        int rowY = top + 67;
        for (Module module : FeverVisuals.MODULES.all()) {
            boolean hovered = mouseX >= left + 10 && mouseX <= right - 10
                    && mouseY >= rowY && mouseY < rowY + ROW_HEIGHT - 3;

            int rowColor = hovered ? 0xFF292035 : 0xFF191321;
            context.fill(left + 10, rowY, right - 10, rowY + ROW_HEIGHT - 3, rowColor);

            context.drawTextWithShadow(client.textRenderer, module.getName(),
                    left + 19, rowY + 7, 0xFFEADFFF);
            context.drawTextWithShadow(client.textRenderer, module.getCategory(),
                    left + 19, rowY + 17, 0xFF9B8AA8);

            String state = module.isEnabled() ? "ON" : "OFF";
            int stateColor = module.isEnabled() ? 0xFFB45CFF : 0xFF777080;
            int stateWidth = client.textRenderer.getWidth(state);
            context.drawTextWithShadow(client.textRenderer, state,
                    right - 22 - stateWidth, rowY + 9, stateColor);

            rowY += ROW_HEIGHT;
        }

        String footer = "NEON PURPLE  •  FeverVisuals 0.1.0";
        int footerWidth = client.textRenderer.getWidth(footer);
        context.drawTextWithShadow(client.textRenderer, footer,
                (this.width - footerWidth) / 2, top + 230, 0xFF8D7D9A);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (button != 0) {
            return super.mouseClicked(mouseX, mouseY, button);
        }

        int left = (this.width - PANEL_WIDTH) / 2;
        int top = (this.height - 250) / 2;
        int right = left + PANEL_WIDTH;

        int rowY = top + 67;
        for (Module module : FeverVisuals.MODULES.all()) {
            if (mouseX >= left + 10 && mouseX <= right - 10
                    && mouseY >= rowY && mouseY < rowY + ROW_HEIGHT - 3) {
                module.toggle();
                FeverVisuals.onModuleToggled(module);
                FeverVisuals.notify(module.getName() + (module.isEnabled() ? " enabled" : " disabled"));
                return true;
            }
            rowY += ROW_HEIGHT;
        }

        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean shouldPause() {
        return false;
    }
}
