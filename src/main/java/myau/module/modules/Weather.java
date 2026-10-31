package myau.module.modules;

import myau.event.EventTarget;
import myau.events.UpdateEvent;
import myau.module.Module;
import myau.property.properties.ModeProperty;
import net.minecraft.client.Minecraft;

public class Weather extends Module {
    private static final Minecraft mc = Minecraft.getMinecraft();
    public ModeProperty mode = new ModeProperty("Weather", 0, new String[]{"Clear", "Rain", "Thunder"});

    public Weather() {
        super("Weather", false, false, "Changes the weather rendered locally.");
    }

    @EventTarget
    public void onUpdate(UpdateEvent event) {
        if (!this.isEnabled() || mc.theWorld == null) return;
        int m = mode.getValue();
        if (m == 0) {
            mc.theWorld.setRainStrength(0.0f);
            mc.theWorld.setThunderStrength(0.0f);
        } else if (m == 1) {
            mc.theWorld.setRainStrength(1.0f);
            mc.theWorld.setThunderStrength(0.0f);
        } else {
            mc.theWorld.setRainStrength(1.0f);
            mc.theWorld.setThunderStrength(1.0f);
        }
    }
}
