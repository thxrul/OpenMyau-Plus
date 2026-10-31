package myau.module.modules;

import myau.event.EventTarget;
import myau.events.PlayerUpdateEvent;
import myau.module.Module;
import myau.property.properties.BooleanProperty;
import myau.util.MoveUtil;
import net.minecraft.client.Minecraft;

public class InstantStop extends Module {
    private static final Minecraft mc = Minecraft.getMinecraft();
    public BooleanProperty onlyOnGround = new BooleanProperty("Only on Ground", true);

    public InstantStop() {
        super("InstantStop", false, false, "Stops player momentum instantly when key released.");
    }

    @EventTarget
    public void onPlayerUpdate(PlayerUpdateEvent event) {
        if (!this.isEnabled() || mc.thePlayer == null) return;
        if (onlyOnGround.getValue() && !mc.thePlayer.onGround) return;
        if (!MoveUtil.isForwardPressed()) {
            mc.thePlayer.motionX = 0.0;
            mc.thePlayer.motionZ = 0.0;
        }
    }
}
