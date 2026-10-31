package myau.module.modules;

import myau.event.EventTarget;
import myau.events.UpdateEvent;
import myau.module.Module;
import myau.property.properties.FloatProperty;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public class Tower extends Module {
    private static final Minecraft mc = Minecraft.getMinecraft();
    public FloatProperty speed = new FloatProperty("Speed", 1.2f, 0.5f, 3.0f);

    public Tower() {
        super("Tower", false, false, "Builds upward rapidly by placing blocks beneath feet.");
    }

    @EventTarget
    public void onUpdate(UpdateEvent event) {
        if (!this.isEnabled() || mc.thePlayer == null) return;
        ItemStack held = mc.thePlayer.getHeldItem();
        if (held != null && held.getItem() instanceof ItemBlock && mc.gameSettings.keyBindJump.isKeyDown()) {
            if (mc.thePlayer.onGround) {
                mc.thePlayer.motionY = 0.42 * speed.getValue();
            }
        }
    }
}
