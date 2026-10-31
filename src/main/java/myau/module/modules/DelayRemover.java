package myau.module.modules;

import myau.event.EventTarget;
import myau.events.TickEvent;
import myau.mixin.IAccessorMinecraft;
import myau.module.Module;
import net.minecraft.client.Minecraft;

public class DelayRemover extends Module {
    private static final Minecraft mc = Minecraft.getMinecraft();

    public DelayRemover() {
        super("DelayRemover", false, false, "Removes left-click and right-click delays.");
    }

    @EventTarget
    public void onTick(TickEvent event) {
        if (!this.isEnabled() || mc.thePlayer == null) return;
        ((IAccessorMinecraft) mc).setRightClickDelayTimer(0);
    }
}
