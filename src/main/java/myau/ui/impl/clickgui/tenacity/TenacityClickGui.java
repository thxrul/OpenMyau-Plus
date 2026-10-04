package myau.ui.impl.clickgui.tenacity;

import myau.Myau;
import myau.module.Module;
import myau.module.modules.ClickGUIModule;
import myau.module.modules.InvWalk;
import myau.property.Property;
import myau.property.properties.*;
import myau.ui.impl.clickgui.rise.RiseClickGUI;
import myau.ui.impl.clickgui.rise.RiseValueEditor;
import myau.util.KeyBindUtil;
import myau.util.RenderUtil;
import myau.util.font.FontManager;
import myau.util.font.impl.FontRenderer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.settings.KeyBinding;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/** Three-panel GUI inspired by the Tenacity reference, using the client's own modules. */
public class TenacityClickGui extends GuiScreen {
    private static final float WINDOW_W = 660, WINDOW_H = 340;
    private static final float SIDEBAR_W = 134, SETTINGS_W = 174;
    private static final float ROW_H = 46, ROW_STEP = 66;
    private static final int BACKGROUND = 0xFF202124, PANEL = 0xFF303136;
    private static final int MUTED = 0xFF888A92, WHITE = 0xFFF7F7FA;
    private static final int CYAN = 0xFF29B8DB, PINK = 0xFFE38FD5;
    private static final String[] CATEGORIES = {
            "Combat", "Movement", "Render", "Player", "Exploit", "Misc", "Scripts"
    };
    private static TenacityClickGui instance;

    private final List<Module> modules = new ArrayList<>();
    private final List<RiseValueEditor> editors = new ArrayList<>();
    private String category = "Movement";
    private Module selected;
    private Module binding;
    private float windowX = -1, windowY = -1, scale = 1;
    private float moduleScroll, settingsScroll, settingsHeight;
    private boolean dragging;
    private float dragX, dragY;
    private long lastFrame;

    public static TenacityClickGui getInstance() {
        if (instance == null) instance = new TenacityClickGui();
        return instance;
    }

    @Override
    public void initGui() {
        FontManager.initializeFonts();
        Keyboard.enableRepeatEvents(true);
        scale = Math.min(1, Math.min((width - 12f) / WINDOW_W, (height - 12f) / WINDOW_H));
        scale = Math.max(0.01f, scale);
        ClickGUIModule gui = guiModule();
        if (windowX < 0 || gui == null || !gui.saveGuiState.getValue()) {
            windowX = (width / scale - WINDOW_W) / 2;
            windowY = (height / scale - WINDOW_H) / 2;
        }
        clampWindow();
        rebuildModules();
        if (selected == null || !modules.contains(selected)) {
            Module noSlow = Myau.moduleManager.getModule("NoSlow");
            select(modules.contains(noSlow) ? noSlow : modules.isEmpty() ? null : modules.get(0));
        } else {
            select(selected);
        }
        lastFrame = System.nanoTime();
    }

    private ClickGUIModule guiModule() {
        return (ClickGUIModule) Myau.moduleManager.getModule("ClickGUI");
    }

    private void rebuildModules() {
        modules.clear();
        for (Module module : Myau.moduleManager.allModules()) {
            String tab = RiseClickGUI.getModuleCategoryName(module);
            // The reference groups inventory movement and scaffold with movement.
            if (module instanceof InvWalk || "Scaffold".equals(module.getName())) tab = "Movement";
            if ("Ghost".equals(tab)) tab = "Combat";
            if (category.equals(tab)) modules.add(module);
        }
        modules.sort(Comparator.comparing(Module::getName, String.CASE_INSENSITIVE_ORDER));
        moduleScroll = clamp(moduleScroll, 0, moduleScrollMax());
    }

    private void select(Module module) {
        releaseEditors();
        editors.clear();
        selected = module;
        binding = null;
        settingsScroll = 0;
        List<Property<?>> properties = module == null ? null : Myau.propertyManager.properties.get(module);
        if (properties != null) for (Property<?> property : properties) {
            if (property instanceof ModeProperty) editors.add(new PillModeEditor((ModeProperty) property));
            else if (property instanceof BooleanProperty) editors.add(new RiseValueEditor.BooleanEditor((BooleanProperty) property));
            else if (property instanceof IntProperty || property instanceof FloatProperty || property instanceof PercentProperty)
                editors.add(new RiseValueEditor.SliderEditor(property));
            else if (property instanceof ColorProperty) editors.add(new RiseValueEditor.ColorEditor((ColorProperty) property));
            else if (property instanceof TextProperty) editors.add(new RiseValueEditor.TextEditor((TextProperty) property));
        }
        updateSettingsHeight();
    }

    private void updateSettingsHeight() {
        settingsHeight = 28;
        for (RiseValueEditor editor : editors) if (editor.isVisible()) settingsHeight += editor.getHeight() + 6;
        settingsScroll = clamp(settingsScroll, 0, settingsScrollMax());
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        float mx = mouseX / scale, my = mouseY / scale;
        if (dragging) {
            windowX = mx - dragX;
            windowY = my - dragY;
            clampWindow();
        }
        float delta = Math.min(0.05f, (System.nanoTime() - lastFrame) / 1_000_000_000f);
        lastFrame = System.nanoTime();
        updateSettingsHeight();
        GlStateManager.pushMatrix();
        GlStateManager.scale(scale, scale, 1);
        try {
            rounded(windowX, windowY, WINDOW_W, WINDOW_H, 14, BACKGROUND);
            rounded(windowX, windowY, SIDEBAR_W, WINDOW_H, 14, PANEL);
            drawSidebar(mx, my);
            drawModules(mx, my);
            drawSettings(mx, my, partialTicks, delta);
        } finally {
            GL11.glDisable(GL11.GL_SCISSOR_TEST);
            GlStateManager.color(1, 1, 1, 1);
            GlStateManager.popMatrix();
        }
        updateMovement();
    }

    private void drawSidebar(float mx, float my) {
        rounded(windowX + 9, windowY + 10, 26, 26, 13, WHITE);
        rounded(windowX + 12, windowY + 13, 20, 20, 10, PANEL);
        text(FontManager.productSans24, "M", windowX + 16, windowY + 13, WHITE);
        text(FontManager.productSans24, "Myau+", windowX + 43, windowY + 15, WHITE);
        text(FontManager.productSans12, fit(FontManager.productSans12, Myau.version == null ? "dev" : Myau.version, 28),
                windowX + 94, windowY + 20, MUTED);
        rounded(windowX + 14, windowY + 47, SIDEBAR_W - 28, 1, 0, 0xFF42444B);
        for (int i = 0; i < CATEGORIES.length; i++) {
            float y = categoryY(i);
            boolean active = category.equals(CATEGORIES[i]);
            boolean hover = over(mx, my, windowX + 8, y, SIDEBAR_W - 16, 30);
            int color = active ? WHITE : hover ? 0xFFBFC1C8 : MUTED;
            if (hover) rounded(windowX + 7, y, SIDEBAR_W - 14, 30, 5, 0xFF37383E);
            drawCategoryIcon(i, windowX + 21, y + 15, color);
            text(FontManager.productSans28, CATEGORIES[i], windowX + 46, y + 7, color);
        }
    }

    private void drawCategoryIcon(int icon, float x, float y, int color) {
        if (icon == 5) {
            for (int i = -1; i <= 1; i++) {
                rounded(x - 9, y + i * 5 - 1, 3, 2, 1, color);
                rounded(x - 3, y + i * 5 - 1, 13, 2, 1, color);
            }
        } else if (icon == 2) {
            RenderUtil.drawRoundedRectOutline(x - 10, y - 6, 20, 12, 6, 1.5f, color, true, true, true, true);
            rounded(x - 3, y - 3, 6, 6, 3, color);
        } else if (icon == 1 || icon == 3) {
            rounded(x - 2, y - 12, 6, 6, 3, color);
            line(x, y - 4, x - 3, y + 3, 3, color);
            line(x - 3, y + 3, x - 7, y + 10, 3, color);
            line(x - 3, y + 3, x + 6, y + 10, 3, color);
            line(x - 1, y - 2, x + 7, y + (icon == 1 ? -4 : 3), 3, color);
            line(x - 1, y - 2, x - 8, y + 1, 3, color);
        } else if (icon == 6) {
            RenderUtil.drawRoundedRectOutline(x - 8, y - 11, 16, 22, 2, 1.5f, color, true, true, true, true);
            line(x - 3, y - 4, x - 6, y, 1.5f, color);
            line(x - 6, y, x - 3, y + 4, 1.5f, color);
            line(x + 3, y - 4, x + 6, y, 1.5f, color);
            line(x + 6, y, x + 3, y + 4, 1.5f, color);
        } else if (icon == 4) {
            rounded(x - 5, y - 8, 10, 17, 5, color);
            for (int i = -1; i <= 1; i++) line(x - 10, y + i * 6, x + 10, y + i * 6, 1.5f, color);
        } else {
            line(x - 8, y - 9, x + 8, y + 9, 3, color);
            line(x + 8, y - 9, x - 8, y + 9, 3, color);
            line(x - 10, y + 4, x - 4, y + 10, 2, color);
            line(x + 4, y + 10, x + 10, y + 4, 2, color);
        }
    }

    private void drawModules(float mx, float my) {
        clip(listX(), windowY + 6, listWidth(), WINDOW_H - 12);
        for (int i = 0; i < modules.size(); i++) {
            Module module = modules.get(i);
            float y = rowY(i);
            if (y + ROW_H < windowY + 6 || y > windowY + WINDOW_H - 6) continue;
            boolean hover = over(mx, my, listX(), y, listWidth(), ROW_H);
            rounded(listX(), y, listWidth(), ROW_H, 7, hover ? 0xFF37383F : PANEL);
            if (module.isEnabled()) {
                gradientToggle(listX(), y);
                line(listX() + 15, y + 24, listX() + 21, y + 30, 4, WHITE);
                line(listX() + 21, y + 30, listX() + 32, y + 18, 4, WHITE);
            } else {
                rounded(listX(), y, ROW_H, ROW_H, 7, 0xFF44474E);
                rounded(listX() + 17, y + 17, 12, 12, 6, 0xFF30333A);
            }
            text(FontManager.productSans32, fit(FontManager.productSans32, module.getName(), listWidth() - 83),
                    listX() + 56, y + 13, WHITE);
            if (selected == module) rounded(listX() + listWidth() - 20, y, 20, ROW_H, 7, 0xFFC38FD0);
            for (int dot = 0; dot < 3; dot++) rounded(listX() + listWidth() - 12.5f, y + 7 + dot * 13, 5, 5, 2.5f, WHITE);
        }
        if (modules.isEmpty()) text(FontManager.productSans16, "No modules in this category", listX() + 10, windowY + 20, MUTED);
        GL11.glDisable(GL11.GL_SCISSOR_TEST);
        scrollbar(listX() + listWidth() + 3, windowY + 6, WINDOW_H - 12, moduleScroll, moduleScrollMax());
    }

    private void drawSettings(float mx, float my, float partialTicks, float delta) {
        rounded(settingsX(), windowY, SETTINGS_W, WINDOW_H, 14, PANEL);
        String title = selected == null ? "Settings" : selected.getName();
        title = fit(FontManager.productSans32, title, SETTINGS_W - 20);
        text(FontManager.productSans32, title, settingsX() + (SETTINGS_W - textWidth(FontManager.productSans32, title)) / 2,
                windowY + 7, WHITE);
        rounded(settingsX() + 1, windowY + 31, SETTINGS_W - 2, 1, 0, 0xFF292A2F);
        clip(settingsX() + 5, settingsTop(), SETTINGS_W - 10, settingsViewport());
        float y = settingsTop() + 4 - settingsScroll;
        for (RiseValueEditor editor : editors) {
            if (!editor.isVisible()) continue;
            // Keep active drag updates even when an editor scrolls out of view.
            editor.draw(settingsX() + 10, y, SETTINGS_W - 20, (int) mx, (int) my, partialTicks, delta, 255);
            y += editor.getHeight() + 6;
        }
        if (selected != null) {
            text(FontManager.productSans16, "Keybind", settingsX() + 10, y + 5, WHITE);
            String key = binding == selected ? "Press a key..." : selected.getKey() == 0 ? "None" : KeyBindUtil.getKeyName(selected.getKey());
            text(FontManager.productSans12, key, settingsX() + SETTINGS_W - 10 - textWidth(FontManager.productSans12, key), y + 7, MUTED);
        }
        GL11.glDisable(GL11.GL_SCISSOR_TEST);
        scrollbar(settingsX() + SETTINGS_W - 4, settingsTop(), settingsViewport(), settingsScroll, settingsScrollMax());
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int button) throws IOException {
        float mx = mouseX / scale, my = mouseY / scale;
        if (!over(mx, my, windowX, windowY, WINDOW_W, WINDOW_H)) return;
        for (int i = 0; i < CATEGORIES.length; i++) if (button == 0 && over(mx, my, windowX + 8, categoryY(i), SIDEBAR_W - 16, 30)) {
            category = CATEGORIES[i];
            moduleScroll = 0;
            rebuildModules();
            select(modules.isEmpty() ? null : modules.get(0));
            return;
        }
        if (over(mx, my, listX(), windowY + 6, listWidth(), WINDOW_H - 12)) {
            for (int i = 0; i < modules.size(); i++) if (over(mx, my, listX(), rowY(i), listWidth(), ROW_H)) {
                Module module = modules.get(i);
                if (button == 2) { select(module); binding = module; }
                else if (button == 1 || (button == 0 && mx >= listX() + listWidth() - 22)) select(module);
                else if (button == 0) module.toggle();
                return;
            }
        }
        if (over(mx, my, settingsX() + 5, settingsTop(), SETTINGS_W - 10, settingsViewport())) {
            float y = settingsTop() + 4 - settingsScroll;
            for (RiseValueEditor editor : editors) if (editor.isVisible()) {
                if (editor.click((int) mx, (int) my, button, settingsX() + 10, y, SETTINGS_W - 20)) return;
                y += editor.getHeight() + 6;
            }
            if (selected != null && button == 0 && over(mx, my, settingsX() + 10, y, SETTINGS_W - 20, 24)) binding = selected;
            return;
        }
        if (button == 0 && over(mx, my, windowX, windowY, SIDEBAR_W, 47)) {
            dragging = true;
            dragX = mx - windowX;
            dragY = my - windowY;
        }
    }

    @Override
    public void handleMouseInput() throws IOException {
        super.handleMouseInput();
        int wheel = Mouse.getEventDWheel();
        if (wheel == 0) return;
        float mx = Mouse.getEventX() * width / (float) mc.displayWidth / scale;
        float my = (height - Mouse.getEventY() * height / (float) mc.displayHeight - 1) / scale;
        float amount = wheel > 0 ? -28 : 28;
        releaseEditors();
        if (over(mx, my, listX(), windowY + 6, listWidth(), WINDOW_H - 12)) moduleScroll = clamp(moduleScroll + amount, 0, moduleScrollMax());
        else if (over(mx, my, settingsX(), settingsTop(), SETTINGS_W, settingsViewport())) settingsScroll = clamp(settingsScroll + amount, 0, settingsScrollMax());
    }

    @Override
    protected void mouseReleased(int mouseX, int mouseY, int state) {
        dragging = false;
        releaseEditors();
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        if (binding != null) {
            if (keyCode != Keyboard.KEY_ESCAPE) binding.setKey(keyCode == Keyboard.KEY_DELETE || keyCode == Keyboard.KEY_BACK ? 0 : keyCode);
            binding = null;
            return;
        }
        if (isTyping()) {
            for (RiseValueEditor editor : editors) editor.key(typedChar, keyCode);
            return;
        }
        if (keyCode == Keyboard.KEY_ESCAPE) mc.displayGuiScreen(null);
    }

    private boolean isTyping() {
        if (binding != null) return true;
        for (RiseValueEditor editor : editors) if (editor.isTyping()) return true;
        return false;
    }

    private void updateMovement() {
        InvWalk invWalk = (InvWalk) Myau.moduleManager.getModule("InvWalk");
        if (invWalk != null && invWalk.isEnabled() && invWalk.guiEnabled.getValue()) {
            if (isTyping()) KeyBinding.unPressAllKeys();
            else invWalk.pressMovementKeys(true);
        }
    }

    private void releaseEditors() {
        for (RiseValueEditor editor : editors) editor.released();
    }

    @Override
    public void onGuiClosed() {
        Keyboard.enableRepeatEvents(false);
        dragging = false;
        binding = null;
        releaseEditors();
        KeyBinding.unPressAllKeys();
        ClickGUIModule gui = guiModule();
        if (gui != null && !gui.isSwitchingGuiStyle()) gui.setEnabled(false);
    }

    @Override
    public boolean doesGuiPauseGame() { return false; }

    private float listX() { return windowX + SIDEBAR_W + 12; }
    private float listWidth() { return WINDOW_W - SIDEBAR_W - SETTINGS_W - 30; }
    private float settingsX() { return windowX + WINDOW_W - SETTINGS_W; }
    private float settingsTop() { return windowY + 36; }
    private float settingsViewport() { return WINDOW_H - 44; }
    private float categoryY(int index) { return windowY + 60 + index * 38; }
    private float rowY(int index) { return windowY + 10 + index * ROW_STEP - moduleScroll; }
    private float moduleScrollMax() { return Math.max(0, modules.size() * ROW_STEP - (ROW_STEP - ROW_H) + 8 - (WINDOW_H - 12)); }
    private float settingsScrollMax() { return Math.max(0, settingsHeight + 8 - settingsViewport()); }
    private void clampWindow() {
        windowX = clamp(windowX, 0, width / scale - WINDOW_W);
        windowY = clamp(windowY, 0, height / scale - WINDOW_H);
    }
    private void clip(float x, float y, float w, float h) { RenderUtil.scissor(x * scale, y * scale, w * scale, h * scale); }
    private static float clamp(float value, float min, float max) { return Math.max(min, Math.min(value, Math.max(min, max))); }
    private static boolean over(float mx, float my, float x, float y, float w, float h) { return mx >= x && mx < x + w && my >= y && my < y + h; }
    private static void rounded(float x, float y, float w, float h, float radius, int color) { RenderUtil.drawRoundedRect(x, y, w, h, radius, color, true, true, true, true); }
    private static void gradientToggle(float x, float y) {
        RenderUtil.enableRenderState();
        GL11.glShadeModel(GL11.GL_SMOOTH);
        GL11.glBegin(GL11.GL_POLYGON);
        for (int corner = 0; corner < 4; corner++) {
            float cx = x + (corner == 0 || corner == 3 ? 7 : ROW_H - 7);
            float cy = y + (corner < 2 ? 7 : ROW_H - 7);
            for (int angle = 180 + corner * 90; angle <= 270 + corner * 90; angle += 6) {
                float vx = cx + (float) Math.cos(Math.toRadians(angle)) * 7;
                float vy = cy + (float) Math.sin(Math.toRadians(angle)) * 7;
                float t = ((vx - x) + (vy - y)) / (ROW_H * 2);
                float r = (((CYAN >> 16) & 255) * (1 - t) + ((PINK >> 16) & 255) * t) / 255f;
                float g = (((CYAN >> 8) & 255) * (1 - t) + ((PINK >> 8) & 255) * t) / 255f;
                float b = ((CYAN & 255) * (1 - t) + (PINK & 255) * t) / 255f;
                GL11.glColor4f(r, g, b, 1);
                GL11.glVertex2f(vx, vy);
            }
        }
        GL11.glEnd();
        GL11.glShadeModel(GL11.GL_FLAT);
        RenderUtil.disableRenderState();
        GlStateManager.resetColor();
    }
    private static void line(float x, float y, float x2, float y2, float width, int color) {
        GlStateManager.disableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        RenderUtil.drawLine(x, y, x2, y2, width, color);
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
    }
    private static float textWidth(FontRenderer font, String text) { return font == null ? net.minecraft.client.Minecraft.getMinecraft().fontRendererObj.getStringWidth(text) : (float) font.getStringWidth(text); }
    private static void text(FontRenderer font, String text, float x, float y, int color) {
        if (font != null) font.drawString(text, x, y, color);
        else net.minecraft.client.Minecraft.getMinecraft().fontRendererObj.drawString(text, x, y, color, false);
    }
    private static String fit(FontRenderer font, String text, float width) {
        if (textWidth(font, text) <= width) return text;
        while (!text.isEmpty() && textWidth(font, text + "...") > width) text = text.substring(0, text.length() - 1);
        return text + "...";
    }
    private static void scrollbar(float x, float y, float viewport, float scroll, float max) {
        if (max <= 0) return;
        float thumb = Math.max(18, viewport * viewport / (viewport + max));
        rounded(x, y + (viewport - thumb) * scroll / max, 2, thumb, 1, 0xFF777983);
    }

    /** Mode values live in outlined pills, matching the settings panel in the reference. */
    private static class PillModeEditor extends RiseValueEditor {
        private final ModeProperty mode;
        PillModeEditor(ModeProperty mode) { super(mode); this.mode = mode; }
        @Override public float getHeight() { return 22; }
        @Override public void draw(float x, float y, float w, int mx, int my, float partialTicks, float delta, int opacity) {
            float pillW = Math.min(w * 0.5f, Math.max(58, textWidth(FontManager.productSans16, mode.getModeString()) + 14));
            text(FontManager.productSans16, fit(FontManager.productSans16, mode.getName(), w - pillW - 6), x, y + 5, WHITE);
            float pillX = x + w - pillW;
            rounded(pillX, y + 2, pillW, 18, 6, 0xFF484A52);
            rounded(pillX + 2, y + 4, pillW - 4, 14, 4, BACKGROUND);
            String value = fit(FontManager.productSans16, mode.getModeString(), pillW - 10);
            text(FontManager.productSans16, value, pillX + (pillW - textWidth(FontManager.productSans16, value)) / 2, y + 5, WHITE);
        }
        @Override public boolean click(int mx, int my, int button, float x, float y, float w) {
            if ((button == 0 || button == 1) && over(x, y, w, getHeight(), mx, my)) {
                if (button == 0) mode.nextMode(); else mode.previousMode();
                return true;
            }
            return false;
        }
    }
}
