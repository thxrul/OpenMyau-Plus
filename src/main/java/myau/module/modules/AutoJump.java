package myau.module.modules;

import myau.event.EventTarget;
import myau.events.UpdateEvent;
import myau.module.Module;
import myau.util.MoveUtil;
import net.minecraft.client.Minecraft;

public class AutoJump extends Module {
    private static final Minecraft mc = Minecraft.getMinecraft();

    public AutoJump() {
        super("AutoJump", false, false, "Automatically jumps when standing on ground.");
    }

    @EventTarget
    public void onUpdate(UpdateEvent event) {
        if (!this.isEnabled() || mc.thePlayer == null) return;
        if (mc.thePlayer.onGround && MoveUtil.isMoving()) {
            mc.thePlayer.jump();
        }
    }
}
