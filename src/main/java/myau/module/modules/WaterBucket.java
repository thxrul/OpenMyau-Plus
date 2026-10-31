package myau.module.modules;

import myau.event.EventTarget;
import myau.events.UpdateEvent;
import myau.module.Module;
import myau.property.properties.FloatProperty;
import net.minecraft.client.Minecraft;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;

public class WaterBucket extends Module {
    private static final Minecraft mc = Minecraft.getMinecraft();
    public FloatProperty minFallDist = new FloatProperty("Fall Distance", 4.0f, 2.0f, 20.0f);

    public WaterBucket() {
        super("WaterBucket", false, false, "Places water bucket to break falls automatically.");
    }

    @EventTarget
    public void onUpdate(UpdateEvent event) {
        if (!this.isEnabled() || mc.thePlayer == null || mc.theWorld == null) return;
        if (mc.thePlayer.fallDistance >= minFallDist.getValue() && !mc.thePlayer.capabilities.isFlying) {
            for (int i = 0; i < 9; i++) {
                ItemStack stack = mc.thePlayer.inventory.getStackInSlot(i);
                if (stack != null && stack.getItem() == Items.water_bucket) {
                    BlockPos below = new BlockPos(mc.thePlayer.posX, mc.thePlayer.posY - 1, mc.thePlayer.posZ);
                    if (!mc.theWorld.isAirBlock(below)) {
                        mc.thePlayer.inventory.currentItem = i;
                        mc.playerController.sendUseItem(mc.thePlayer, mc.theWorld, stack);
                    }
                    break;
                }
            }
        }
    }
}
