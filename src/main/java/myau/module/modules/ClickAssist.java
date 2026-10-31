package myau.module.modules;

import myau.event.EventTarget;
import myau.events.LeftClickMouseEvent;
import myau.module.Module;
import myau.property.properties.IntProperty;
import net.minecraft.client.Minecraft;

public class ClickAssist extends Module {
    private static final Minecraft mc = Minecraft.getMinecraft();
    public IntProperty chance = new IntProperty("Chance", 80, 0, 100);

    public ClickAssist() {
        super("ClickAssist", false, false, "Adds additional clicks when pressing mouse buttons.");
    }

    @EventTarget
    public void onLeftClick(LeftClickMouseEvent event) {
        if (!this.isEnabled() || mc.thePlayer == null) return;
        if (Math.random() * 100.0 < chance.getValue()) {
            mc.thePlayer.swingItem();
        }
    }
}
