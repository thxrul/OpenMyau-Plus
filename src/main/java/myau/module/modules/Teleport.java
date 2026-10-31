package myau.module.modules;

import myau.module.Module;
import net.minecraft.client.Minecraft;
import net.minecraft.util.MovingObjectPosition;

public class Teleport extends Module {
    private static final Minecraft mc = Minecraft.getMinecraft();

    public Teleport() {
        super("Teleport", false, false, "Teleports player to targeted position.");
    }

    @Override
    public void onEnabled() {
        if (mc.thePlayer != null && mc.objectMouseOver != null && mc.objectMouseOver.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK) {
            mc.thePlayer.setPosition(
                    mc.objectMouseOver.getBlockPos().getX() + 0.5,
                    mc.objectMouseOver.getBlockPos().getY() + 1.0,
                    mc.objectMouseOver.getBlockPos().getZ() + 0.5
            );
        }
        this.setEnabled(false);
    }
}
