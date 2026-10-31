package myau.module.modules;

import myau.event.EventTarget;
import myau.events.PlayerUpdateEvent;
import myau.module.Module;
import myau.property.properties.FloatProperty;
import myau.util.MoveUtil;
import net.minecraft.client.Minecraft;

public class BHop extends Module {
    private static final Minecraft mc = Minecraft.getMinecraft();
    public FloatProperty speed = new FloatProperty("Speed", 1.2f, 0.5f, 3.0f);

    public BHop() {
        super("BHop", false, false, "Continuously hops to maintain fast forward movement.");
    }

    @EventTarget
    public void onPlayerUpdate(PlayerUpdateEvent event) {
        if (!this.isEnabled() || mc.thePlayer == null) return;
        if (MoveUtil.isForwardPressed()) {
            if (mc.thePlayer.onGround) {
                mc.thePlayer.jump();
                MoveUtil.setSpeed(MoveUtil.getBaseMoveSpeed() * speed.getValue());
            } else {
                MoveUtil.setSpeed(MoveUtil.getSpeed());
            }
        }
    }
}
