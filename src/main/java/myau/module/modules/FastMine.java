package myau.module.modules;

import myau.event.EventTarget;
import myau.events.UpdateEvent;
import myau.mixin.IAccessorPlayerControllerMP;
import myau.module.Module;
import myau.property.properties.FloatProperty;
import net.minecraft.client.Minecraft;

public class FastMine extends Module {
    private static final Minecraft mc = Minecraft.getMinecraft();
    public FloatProperty speed = new FloatProperty("Speed", 1.4f, 1.0f, 3.0f);

    public FastMine() {
        super("FastMine", false, false, "Increases block breaking speed.");
    }

    @EventTarget
    public void onUpdate(UpdateEvent event) {
        if (!this.isEnabled() || mc.thePlayer == null || mc.playerController == null) return;
        IAccessorPlayerControllerMP pc = (IAccessorPlayerControllerMP) mc.playerController;
        if (pc.getCurBlockDamageMP() > 0) {
            float progress = pc.getCurBlockDamageMP() * (speed.getValue() - 1.0f);
            pc.setCurBlockDamageMP(Math.min(1.0f, pc.getCurBlockDamageMP() + progress * 0.1f));
        }
    }
}
