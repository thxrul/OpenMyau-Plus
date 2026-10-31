package myau.module.modules;

import myau.event.EventTarget;
import myau.events.LivingUpdateEvent;
import myau.module.Module;
import myau.property.properties.IntProperty;
import net.minecraft.client.Minecraft;

public class JumpReset extends Module {
    private static final Minecraft mc = Minecraft.getMinecraft();
    public IntProperty hurtTime = new IntProperty("Hurt Time", 9, 1, 10);

    public JumpReset() {
        super("JumpReset", false, false, "Jumps automatically when hit to reduce knockback.");
    }

    @EventTarget
    public void onLivingUpdate(LivingUpdateEvent event) {
        if (!this.isEnabled() || mc.thePlayer == null) return;
        if (mc.thePlayer.hurtTime == hurtTime.getValue() && mc.thePlayer.onGround) {
            mc.thePlayer.jump();
        }
    }
}
