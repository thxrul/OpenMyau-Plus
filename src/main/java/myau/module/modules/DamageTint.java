package myau.module.modules;

import myau.event.EventTarget;
import myau.events.Render2DEvent;
import myau.module.Module;
import myau.property.properties.IntProperty;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;

public class DamageTint extends Module {
    private static final Minecraft mc = Minecraft.getMinecraft();
    public IntProperty maxAlpha = new IntProperty("Max Alpha", 100, 10, 255);

    public DamageTint() {
        super("DamageTint", false, false, "Renders a red vignette on screen when taking damage.");
    }

    @EventTarget
    public void onRender2D(Render2DEvent event) {
        if (!this.isEnabled() || mc.thePlayer == null || mc.thePlayer.hurtTime <= 0) return;
        float progress = mc.thePlayer.hurtTime / 10.0f;
        int alpha = (int) (progress * maxAlpha.getValue());
        ScaledResolution sr = new ScaledResolution(mc);
        Gui.drawRect(0, 0, sr.getScaledWidth(), sr.getScaledHeight(), (alpha << 24) | 0xFF0000);
    }
}
