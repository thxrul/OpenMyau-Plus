package myau.module.modules;

import myau.event.EventTarget;
import myau.events.PlayerUpdateEvent;
import myau.module.Module;
import net.minecraft.client.Minecraft;

public class Freeze extends Module {
    private static final Minecraft mc = Minecraft.getMinecraft();

    public Freeze() {
        super("Freeze", false, false, "Freezes movement and position updates.");
    }

    @EventTarget
    public void onPlayerUpdate(PlayerUpdateEvent event) {
        if (!this.isEnabled() || mc.thePlayer == null) return;
        mc.thePlayer.motionX = 0;
        mc.thePlayer.motionY = 0;
        mc.thePlayer.motionZ = 0;
    }
}
