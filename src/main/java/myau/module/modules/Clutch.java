package myau.module.modules;

import myau.event.EventTarget;
import myau.events.UpdateEvent;
import myau.module.Module;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;

public class Clutch extends Module {
    private static final Minecraft mc = Minecraft.getMinecraft();

    public Clutch() {
        super("Clutch", false, false, "Places blocks when falling into void or off ledges.");
    }

    @EventTarget
    public void onUpdate(UpdateEvent event) {
        if (!this.isEnabled() || mc.thePlayer == null || mc.theWorld == null) return;
        if (!mc.thePlayer.onGround && mc.thePlayer.motionY < -0.1) {
            BlockPos ground = new BlockPos(mc.thePlayer.posX, mc.thePlayer.posY - 1, mc.thePlayer.posZ);
            if (mc.theWorld.isAirBlock(ground)) {
                for (int i = 0; i < 9; i++) {
                    ItemStack stack = mc.thePlayer.inventory.getStackInSlot(i);
                    if (stack != null && stack.getItem() instanceof ItemBlock) {
                        mc.thePlayer.inventory.currentItem = i;
                        break;
                    }
                }
            }
        }
    }
}
