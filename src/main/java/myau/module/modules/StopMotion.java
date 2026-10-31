package myau.module.modules;

import myau.event.EventTarget;
import myau.events.PlayerUpdateEvent;
import myau.module.Module;
import myau.util.MoveUtil;
import net.minecraft.client.Minecraft;

public class StopMotion extends Module {
    private static final Minecraft mc = Minecraft.getMinecraft();

    public StopMotion() {
        super("StopMotion", false, false, "Halts motion immediately under set conditions.");
    }

    @EventTarget
    public void onPlayerUpdate(PlayerUpdateEvent event) {
        if (!this.isEnabled() || mc.thePlayer == null) return;
        if (!MoveUtil.isForwardPressed()) {
            mc.thePlayer.motionX = 0;
            mc.thePlayer.motionZ = 0;
        }
    }
}
