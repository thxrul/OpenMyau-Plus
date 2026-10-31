package myau.module.modules;

import myau.event.EventTarget;
import myau.events.Render2DEvent;
import myau.module.Module;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;

public class PotionHUD extends Module {
    private static final Minecraft mc = Minecraft.getMinecraft();

    public PotionHUD() {
        super("PotionHUD", false, false, "Displays active potion effects on the screen.");
    }

    @EventTarget
    public void onRender2D(Render2DEvent event) {
        if (!this.isEnabled() || mc.thePlayer == null) return;
        int y = 20;
        for (PotionEffect effect : mc.thePlayer.getActivePotionEffects()) {
            Potion potion = Potion.potionTypes[effect.getPotionID()];
            if (potion != null) {
                String name = I18n.format(potion.getName()) + " " + Potion.getDurationString(effect);
                mc.fontRendererObj.drawStringWithShadow(name, 4, y, potion.getLiquidColor());
                y += 10;
            }
        }
    }
}
