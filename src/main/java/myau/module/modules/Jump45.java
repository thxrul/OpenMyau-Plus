package myau.module.modules;

import myau.event.EventTarget;
import myau.events.JumpEvent;
import myau.module.Module;
import myau.util.MoveUtil;
import net.minecraft.client.Minecraft;

public class Jump45 extends Module {
    private static final Minecraft mc = Minecraft.getMinecraft();

    public Jump45() {
        super("Jump45", false, false, "Angles jumps at 45 degrees for optimized movement.");
    }

    @EventTarget
    public void onJump(JumpEvent event) {
        if (!this.isEnabled() || mc.thePlayer == null) return;
        if (MoveUtil.isForwardPressed()) {
            mc.thePlayer.rotationYaw += 45.0f;
        }
    }
}
