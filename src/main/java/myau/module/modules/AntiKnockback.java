package myau.module.modules;

import myau.event.EventTarget;
import myau.events.PacketEvent;
import myau.mixin.IAccessorS12PacketEntityVelocity;
import myau.module.Module;
import myau.property.properties.FloatProperty;
import net.minecraft.client.Minecraft;
import net.minecraft.network.play.server.S12PacketEntityVelocity;
import net.minecraft.network.play.server.S27PacketExplosion;

public class AntiKnockback extends Module {
    private static final Minecraft mc = Minecraft.getMinecraft();
    public FloatProperty horizontal = new FloatProperty("Horizontal", 0.0f, 0.0f, 100.0f);
    public FloatProperty vertical = new FloatProperty("Vertical", 0.0f, 0.0f, 100.0f);

    public AntiKnockback() {
        super("AntiKnockback", false, false, "Reduces or eliminates taking knockback.");
    }

    @EventTarget
    public void onPacket(PacketEvent event) {
        if (!this.isEnabled() || mc.thePlayer == null) return;
        float h = horizontal.getValue() / 100.0f;
        float v = vertical.getValue() / 100.0f;

        if (event.getPacket() instanceof S12PacketEntityVelocity) {
            S12PacketEntityVelocity packet = (S12PacketEntityVelocity) event.getPacket();
            if (packet.getEntityID() == mc.thePlayer.getEntityId()) {
                if (h == 0 && v == 0) {
                    event.setCancelled(true);
                } else {
                    IAccessorS12PacketEntityVelocity acc = (IAccessorS12PacketEntityVelocity) packet;
                    acc.setMotionX((int) (packet.getMotionX() * h));
                    acc.setMotionY((int) (packet.getMotionY() * v));
                    acc.setMotionZ((int) (packet.getMotionZ() * h));
                }
            }
        } else if (event.getPacket() instanceof S27PacketExplosion) {
            if (h == 0 && v == 0) {
                event.setCancelled(true);
            }
        }
    }
}
