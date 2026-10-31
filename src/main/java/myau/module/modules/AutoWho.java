package myau.module.modules;

import myau.event.EventTarget;
import myau.events.LoadWorldEvent;
import myau.module.Module;
import net.minecraft.client.Minecraft;

public class AutoWho extends Module {
    private static final Minecraft mc = Minecraft.getMinecraft();

    public AutoWho() {
        super("AutoWho", false, false, "Automatically sends /who upon joining games.");
    }

    @EventTarget
    public void onLoadWorld(LoadWorldEvent event) {
        if (!this.isEnabled() || mc.thePlayer == null) return;
        new Thread(() -> {
            try {
                Thread.sleep(1500L);
                if (mc.thePlayer != null) {
                    mc.thePlayer.sendChatMessage("/who");
                }
            } catch (InterruptedException ignored) {}
        }).start();
    }
}
