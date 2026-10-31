package myau.module.modules;

import myau.event.EventTarget;
import myau.events.PlayerUpdateEvent;
import myau.module.Module;
import myau.property.properties.FloatProperty;
import net.minecraft.client.Minecraft;

public class Freecam extends Module {
    private static final Minecraft mc = Minecraft.getMinecraft();
    public FloatProperty speed = new FloatProperty("Speed", 1.0f, 0.1f, 5.0f);
    private double startX, startY, startZ;

    public Freecam() {
        super("Freecam", false, false, "Allows moving camera freely outside the player body.");
    }

    @Override
    public void onEnabled() {
        if (mc.thePlayer != null) {
            startX = mc.thePlayer.posX;
            startY = mc.thePlayer.posY;
            startZ = mc.thePlayer.posZ;
            mc.thePlayer.capabilities.isFlying = true;
        }
    }

    @Override
    public void onDisabled() {
        if (mc.thePlayer != null) {
            mc.thePlayer.capabilities.isFlying = false;
            mc.thePlayer.setPosition(startX, startY, startZ);
        }
    }

    @EventTarget
    public void onPlayerUpdate(PlayerUpdateEvent event) {
        if (!this.isEnabled() || mc.thePlayer == null) return;
        mc.thePlayer.capabilities.isFlying = true;
    }
}
