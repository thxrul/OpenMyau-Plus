package myau.module.modules;

import myau.event.EventTarget;
import myau.events.PlayerUpdateEvent;
import myau.module.Module;
import myau.property.properties.FloatProperty;
import myau.util.MoveUtil;
import net.minecraft.client.Minecraft;

public class Boost extends Module {
    private static final Minecraft mc = Minecraft.getMinecraft();
    public FloatProperty multiplier = new FloatProperty("Multiplier", 2.0f, 1.0f, 5.0f);

    public Boost() {
        super("Boost", false, false, "Gives a temporary movement speed boost.");
    }

    @EventTarget
    public void onPlayerUpdate(PlayerUpdateEvent event) {
        if (!this.isEnabled() || mc.thePlayer == null) return;
        if (MoveUtil.isForwardPressed() && mc.thePlayer.onGround) {
            MoveUtil.setSpeed(MoveUtil.getBaseMoveSpeed() * multiplier.getValue());
        }
    }
}
