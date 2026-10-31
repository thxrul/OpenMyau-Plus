package myau.module.modules;

import myau.event.EventTarget;
import myau.events.AttackEvent;
import myau.module.Module;
import myau.property.properties.FloatProperty;
import net.minecraft.client.Minecraft;

public class Reduce extends Module {
    private static final Minecraft mc = Minecraft.getMinecraft();
    public FloatProperty factor = new FloatProperty("Factor", 0.6f, 0.0f, 1.0f);

    public Reduce() {
        super("Reduce", false, false, "Reduces taken knockback when attacking.");
    }

    @EventTarget
    public void onAttack(AttackEvent event) {
        if (!this.isEnabled() || mc.thePlayer == null) return;
        mc.thePlayer.motionX *= factor.getValue();
        mc.thePlayer.motionZ *= factor.getValue();
    }
}
