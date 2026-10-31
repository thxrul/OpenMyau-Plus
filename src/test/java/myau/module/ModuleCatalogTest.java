package myau.module;

import myau.Myau;
import org.junit.Test;
import java.util.Arrays;
import java.util.List;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class ModuleCatalogTest {
    private Module module(String name) {
        return new Module(name, false) { };
    }

    @Test
    public void categoriesFollowFunctionRatherThanOldGuiLists() {
        assertEquals("Render", ModuleCatalog.category(module("AntiObfuscate")));
        assertEquals("Render", ModuleCatalog.category(module("Hotbar")));
        assertEquals("Movement", ModuleCatalog.category(module("InvWalk")));
        assertEquals("Combat", ModuleCatalog.category(module("TargetStrafe")));
        assertEquals("Combat", ModuleCatalog.category(module("TimerRange")));
        assertEquals("Settings", ModuleCatalog.category(module("ServerLag")));
        assertEquals("Player", ModuleCatalog.category(module("BedNuker")));
        assertEquals("World", ModuleCatalog.category(module("AntiBot")));
        assertEquals("Minemen", ModuleCatalog.category(module("AutoWho")));
        assertEquals("Bedwars", ModuleCatalog.category(module("BedWars")));
        assertEquals("Duels", ModuleCatalog.category(module("DuelsStats")));
        assertEquals("Skywars", ModuleCatalog.category(module("SkyWars")));
    }

    @Test
    public void containsAllExpectedCategories() {
        List<String> categories = Arrays.asList(ModuleCatalog.CATEGORIES);
        assertTrue(categories.contains("Combat"));
        assertTrue(categories.contains("Movement"));
        assertTrue(categories.contains("Render"));
        assertTrue(categories.contains("Player"));
        assertTrue(categories.contains("World"));
        assertTrue(categories.contains("Minemen"));
        assertTrue(categories.contains("Bedwars"));
        assertTrue(categories.contains("Duels"));
        assertTrue(categories.contains("Skywars"));
        assertTrue(categories.contains("Settings"));
        assertTrue(categories.contains("Configs"));
        assertEquals(11, ModuleCatalog.CATEGORIES.length);
    }

    @Test
    public void descriptionsExplainBehaviorAndPreserveExternalDescriptions() {
        assertTrue(module("NoSlow").getDescription().contains("slowdown"));
        assertTrue(module("Capes").getDescription().contains("cape textures"));
        assertFalse(ModuleCatalog.description("BedWars").isEmpty());
        assertFalse(ModuleCatalog.description("SkyWars").isEmpty());
        Module external = new Module("CustomScript", false, false, "A custom script.") { };
        assertEquals("A custom script.", external.getDescription());
    }

    @Test
    public void groupsContainEveryMatchingModuleExactlyOnceAndAreSorted() {
        Module timer = module("FakeLag");
        Module lag = module("ServerLag");
        Module hud = module("HUD");
        Myau.moduleManager = mock(ModuleManager.class);
        when(Myau.moduleManager.allModules()).thenReturn(Arrays.asList(timer, hud, lag));
        assertEquals(Arrays.asList(timer, lag), ModuleCatalog.modules("Settings"));
        assertEquals(Arrays.asList(hud), ModuleCatalog.modules("Render"));
    }
}
