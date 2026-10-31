package myau.module.modules;

import myau.event.EventTarget;
import myau.events.LivingUpdateEvent;
import myau.module.Module;
import myau.property.properties.FloatProperty;
import net.minecraft.client.Minecraft;

public class Dolphin extends Module {
    private static final Minecraft mc = Minecraft.getMinecraft();
    public FloatProperty swimSpeed = new FloatProperty("Speed", 1.5f, 1.0f, 4.0f);

    public Dolphin() {
        super("Dolphin", false, false, "Automatically swims upward in water.");
    }

    @EventTarget
    public void onLivingUpdate(LivingUpdateEvent event) {
        if (!this.isEnabled() || mc.thePlayer == null) return;
        if (mc.thePlayer.isInWater() && !mc.thePlayer.capabilities.isFlying) {
            mc.thePlayer.motionY += 0.04 * swimSpeed.getValue();
        }
    }
}
