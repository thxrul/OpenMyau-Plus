package myau.module.modules;

import myau.event.EventTarget;
import myau.events.UpdateEvent;
import myau.module.Module;
import myau.property.properties.FloatProperty;
import net.minecraft.client.Minecraft;

public class AutoLadder extends Module {
    private static final Minecraft mc = Minecraft.getMinecraft();
    public FloatProperty speed = new FloatProperty("Speed", 1.5f, 1.0f, 3.0f);

    public AutoLadder() {
        super("AutoLadder", false, false, "Automatically climbs ladders.");
    }

    @EventTarget
    public void onUpdate(UpdateEvent event) {
        if (!this.isEnabled() || mc.thePlayer == null) return;
        if (mc.thePlayer.isOnLadder() && mc.thePlayer.isCollidedHorizontally) {
            mc.thePlayer.motionY = 0.169 * speed.getValue();
        }
    }
}
