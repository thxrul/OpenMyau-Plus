package myau.module.modules;

import myau.module.Module;
import myau.property.properties.FloatProperty;
import net.minecraft.client.Minecraft;

public class VClip extends Module {
    private static final Minecraft mc = Minecraft.getMinecraft();
    public FloatProperty distance = new FloatProperty("Distance", 3.0f, -10.0f, 10.0f);

    public VClip() {
        super("VClip", false, false, "Teleports vertically up or down through blocks.");
    }

    @Override
    public void onEnabled() {
        if (mc.thePlayer != null) {
            mc.thePlayer.setPosition(mc.thePlayer.posX, mc.thePlayer.posY + distance.getValue(), mc.thePlayer.posZ);
        }
        this.setEnabled(false);
    }
}
