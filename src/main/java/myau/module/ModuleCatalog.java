package myau.module;

import myau.Myau;
import myau.module.modules.ScriptModule;
import java.util.*;

/** One module catalog shared by every GUI style. */
public final class ModuleCatalog {
    public static final String[] CATEGORIES = {
            "Combat", "Movement", "Render", "Player", "World", "Minemen", "Bedwars", "Duels", "Skywars", "Settings", "Configs"
    };

    private static final Map<String, String> CATEGORIES_BY_NAME = new HashMap<>();
    private static final Map<String, String> DESCRIPTIONS = new HashMap<>();

    static {
        // Combat
        add("AimAssist", "Combat", "Adjusts aim toward nearby targets while attacking.");
        add("AntiKnockback", "Combat", "Reduces or eliminates taking knockback.");
        add("AutoClicker", "Combat", "Repeats attack clicks while the mouse is held.");
        add("Backtrack", "Combat", "Delays entity updates for selected targets.");
        add("BlockHit", "Combat", "Times sword blocking around attacks.");
        add("ClickAssist", "Combat", "Adds additional clicks when pressing mouse buttons.");
        add("ClickAssits", "Combat", "Adds configured assistance to attack clicks.");
        add("Criticals", "Combat", "Changes attack movement for critical hits.");
        add("Displace", "Combat", "Changes movement or timing around a target.");
        add("HitBox", "Combat", "Changes entity targeting box sizes.");
        add("HitSelect", "Combat", "Controls attack timing around knockback.");
        add("Hitflick", "Combat", "Adjusts aim during attacks on nearby players.");
        add("JumpReset", "Combat", "Jumps automatically when hit to reduce knockback.");
        add("KillAura", "Combat", "Automatically selects and attacks nearby targets.");
        add("KnockbackDelay", "Combat", "Delays the application of knockback packets.");
        add("MoreKB", "Combat", "Changes sprint timing around attacks.");
        add("NoHitDelay", "Combat", "Removes the local attack click delay.");
        add("Piercing", "Combat", "Changes targeting behavior around intervening entities.");
        add("Reach", "Combat", "Changes the local targeting reach distance.");
        add("Reduce", "Combat", "Reduces taken knockback when attacking.");
        add("RodAimbot", "Combat", "Aims fishing rod at targets automatically.");
        add("SprintReset", "Combat", "Resets sprinting around attacks.");
        add("Stasis", "Combat", "Controls movement and packet timing during combat.");
        add("TargetStrafe", "Combat", "Moves around the selected combat target.");
        add("ThrowAura", "Combat", "Uses throwable items against nearby targets.");
        add("TickBase", "Combat", "Changes movement timing near combat targets.");
        add("TimerRange", "Combat", "Changes local tick timing around target distance.");
        add("TPAura", "Combat", "Teleports to and attacks targets rapidly.");
        add("Velocity", "Combat", "Changes the application of knockback.");
        add("WTap", "Combat", "Resets forward movement around attacks.");
        add("Wtap", "Combat", "Resets forward movement around attacks.");

        // Movement
        add("AntiAFK", "Movement", "Makes small movements to prevent idle kicks.");
        add("AntiVoid", "Movement", "Attempts to recover when falling into the void.");
        add("AutoHeadHitter", "Movement", "Times jumps beneath overhead blocks.");
        add("AutoJump", "Movement", "Automatically jumps when standing on ground.");
        add("AutoLadder", "Movement", "Automatically climbs ladders.");
        add("BHop", "Movement", "Continuously hops to maintain fast forward movement.");
        add("Boost", "Movement", "Gives a temporary movement speed boost.");
        add("Dolphin", "Movement", "Automatically swims upward in water.");
        add("Eagle", "Movement", "Sneaks near block edges while bridging.");
        add("Fly", "Movement", "Changes player movement for flight modes.");
        add("InstantStop", "Movement", "Stops player momentum instantly when key released.");
        add("InvWalk", "Movement", "Allows movement while selected screens are open.");
        add("Jesus", "Movement", "Changes movement on water surfaces.");
        add("Jump45", "Movement", "Angles jumps at 45 degrees for optimized movement.");
        add("KeepSprint", "Movement", "Retains sprinting through attacks.");
        add("LongJump", "Movement", "Changes movement to extend jump distance.");
        add("MoveFix", "Movement", "Aligns movement input with modified rotations.");
        add("MovementFix", "Movement", "Fixes movement input direction relative to silent rotations.");
        add("NoFall", "Movement", "Changes fall-related movement packets.");
        add("NoJumpDelay", "Movement", "Removes the delay between held jumps.");
        add("NoSlow", "Movement", "Changes movement slowdown while using items.");
        add("NullMove", "Movement", "Prevents sending movement packets.");
        add("SafeWalk", "Movement", "Prevents movement over block edges.");
        add("Speed", "Movement", "Changes movement speed using selected modes.");
        add("Sprint", "Movement", "Keeps sprint active while moving forward.");
        add("StopMotion", "Movement", "Halts motion immediately under set conditions.");
        add("Teleport", "Movement", "Teleports player to targeted position.");
        add("Timer", "Movement", "Changes the local game tick rate.");
        add("VClip", "Movement", "Teleports vertically up or down through blocks.");

        // Render
        add("Ambience", "Render", "Changes local time and weather appearance.");
        add("Animations", "Render", "Customizes swinging and blocking animations.");
        add("AntiDebuff", "Render", "Hides blindness and nausea visual effects.");
        add("AntiObfuscate", "Render", "Removes obfuscated text formatting.");
        add("AntiShuffle", "Render", "Prevents chat text scrambling.");
        add("Arrows", "Render", "Draws direction arrows toward nearby players.");
        add("BedESP", "Render", "Highlights bed blocks in the world.");
        add("BlockESP", "Render", "Highlights configured blocks through terrain.");
        add("BlockOverlay", "Render", "Customizes the targeted block outline.");
        add("BreakProgress", "Render", "Shows progress on the block being mined.");
        add("Capes", "Render", "Loads and displays local cape textures.");
        add("Chams", "Render", "Renders highlighted player models.");
        add("ChestESP", "Render", "Highlights storage blocks in the world.");
        add("DamageTags", "Render", "Displays damage indicators on hit entities.");
        add("DamageTint", "Render", "Renders a red vignette on screen when taking damage.");
        add("DynamicIsland", "Render", "Displays server and player status in the HUD.");
        add("ESP", "Render", "Highlights players with configurable overlays.");
        add("ESP2D", "Render", "Draws screen-space boxes around entities.");
        add("ExtendCamera", "Render", "Extends the third-person camera distance.");
        add("ExtraBobbing", "Render", "Increases camera view bobbing effect.");
        add("FPScounter", "Render", "Displays frame rate in a compact overlay.");
        add("FreeLook", "Render", "Moves the camera independently of player rotation.");
        add("FullBright", "Render", "Brightens dark areas without changing the world.");
        add("Fullbright", "Render", "Brightens dark areas without changing the world.");
        add("HitParticleEffects", "Render", "Adds particles when attacks hit a target.");
        add("Hotbar", "Render", "Replaces the hotbar with a customizable overlay.");
        add("HUD", "Render", "Configures the module list and HUD colors.");
        add("Indicators", "Render", "Shows player and combat status indicators.");
        add("ItemESP", "Render", "Highlights dropped items in the world.");
        add("ItemPhysics", "Render", "Changes the appearance of dropped items.");
        add("MobESP", "Render", "Highlights mob entities in the world.");
        add("NameTags", "Render", "Displays configurable player name labels.");
        add("NoCameraClip", "Render", "Prevents the third-person camera from clipping into blocks.");
        add("NoHurtCam", "Render", "Removes the camera shake when damaged.");
        add("Notifications", "Render", "Displays module and client notifications.");
        add("PlayerESP", "Render", "Highlights player entities through walls.");
        add("PotionHUD", "Render", "Displays active potion effects on the screen.");
        add("Radar", "Render", "Shows nearby entities on a compact radar.");
        add("RenderFixes", "Render", "Styles chat and scoreboard backgrounds.");
        add("Saturation", "Render", "Enhances color saturation of the world render.");
        add("Statistics", "Render", "Displays match and player statistics.");
        add("TargetESP", "Render", "Highlights the current combat target.");
        add("TargetHUD", "Render", "Displays the current target's health and status.");
        add("TeamHealthDisplay", "Render", "Displays teammates' health.");
        add("TNTTimer", "Render", "Displays countdown timers on primed TNT entities.");
        add("Tracers", "Render", "Draws lines toward nearby entities.");
        add("Trajectories", "Render", "Previews the path of thrown items and arrows.");
        add("ViewClip", "Render", "Allows the third-person camera through blocks.");
        add("WaterMark", "Render", "Displays a customizable client watermark.");
        add("Xray", "Render", "Shows selected blocks through surrounding terrain.");

        // Player
        add("AntiFireball", "Player", "Attacks nearby fireballs to deflect them.");
        add("AntiObbyTrap", "Player", "Helps escape blocks trapping the player.");
        add("AutoAnduril", "Player", "Selects the Anduril sword when needed.");
        add("AutoBedDef", "Player", "Places blocks to defend a bed.");
        add("AutoBlockIn", "Player", "Places protective blocks around the player.");
        add("AutoBlockin", "Player", "Surrounds player in protective blocks automatically.");
        add("AutoGapple", "Player", "Uses golden apples according to health settings.");
        add("Gapple", "Player", "Uses golden apples according to health settings.");
        add("AutoHeal", "Player", "Uses healing items when health is low.");
        add("AutoSwap", "Player", "Switches held items according to configured rules.");
        add("AutoTool", "Player", "Selects a suitable tool for the block being mined.");
        add("BedNuker", "Player", "Finds and breaks nearby beds and covering blocks.");
        add("BedTracker", "Player", "Alerts when players or pearls approach your bed.");
        add("BridgeAssist", "Player", "Assists with bridging speed and placement.");
        add("ChestAura", "Player", "Opens nearby storage containers automatically.");
        add("ChestStealer", "Player", "Moves items from open chests into inventory.");
        add("Clutch", "Player", "Places blocks when falling into void or off ledges.");
        add("DelayRemover", "Player", "Removes left-click and right-click delays.");
        add("FastMine", "Player", "Increases block breaking speed.");
        add("FastPlace", "Player", "Reduces the delay between item-use clicks.");
        add("Freecam", "Player", "Allows moving camera freely outside the player body.");
        add("GhostHand", "Player", "Ignores selected players when targeting blocks.");
        add("InvManager", "Player", "Sorts inventory and removes unwanted items.");
        add("InventoryClicker", "Player", "Repeats inventory clicks while held.");
        add("NoRotate", "Player", "Changes handling of server rotation corrections.");
        add("Refill", "Player", "Moves items from inventory into hotbar slots.");
        add("Scaffold", "Player", "Places blocks beneath the player while moving.");
        add("SpeedMine", "Player", "Changes block-breaking timing.");
        add("Tower", "Player", "Builds upward rapidly by placing blocks beneath feet.");
        add("WaterBucket", "Player", "Places water bucket to break falls automatically.");

        // World
        add("AntiBot", "World", "Filters suspected bot entities from targeting.");
        add("Weather", "World", "Changes the weather rendered locally.");

        // Minemen
        add("AutoWho", "Minemen", "Automatically sends /who upon joining games.");
        add("AutoRequeue", "Minemen", "Automatically requeues after match ends.");
        add("BridgeInfo", "Minemen", "Displays statistics and indicators for Bridge games.");
        add("MurderMystery", "Minemen", "Identifies the murderer and detective in Murder Mystery games.");
        add("SpeedBuilders", "Minemen", "Assists with building structures in Speed Builders.");
        add("SumoFences", "Minemen", "Creates visual barriers around sumo platforms.");
        add("WoolWars", "Minemen", "Utilities and helpers for Wool Wars matches.");

        // Bedwars
        add("BedWars", "Bedwars", "Minigame helpers and alerts for BedWars matches.");
        add("BedwarUtils", "Bedwars", "Adds utilities for Bed Wars matches.");

        // Duels
        add("DuelsStats", "Duels", "Displays opponent statistics in Duels matches.");

        // Skywars
        add("SkyWars", "Skywars", "Utilities and automation for SkyWars matches.");

        // Settings / Network / Misc
        add("ClickGUI", "Settings", "Opens the module browser and settings.");
        add("ClickGUIModule", "Settings", "Opens the module browser and settings.");
        add("ClientSpoofer", "Settings", "Changes the client brand sent to the server.");
        add("NameHider", "Settings", "Hides player names in chat, tablist, and scoreboard.");
        add("NickHider", "Settings", "Replaces player names in displayed text.");
        add("Spammer", "Settings", "Repeats configured chat messages.");
        add("MCF", "Settings", "Adds or removes friends with the middle mouse button.");
        add("FlagDetector", "Settings", "Reports server movement corrections.");
        add("LightningTracker", "Settings", "Reports lightning positions in chat.");
        add("AutoAuth", "Settings", "Sends configured login commands on supported servers.");
        add("AutoHypixel", "Settings", "Automates selected Hypixel-specific actions.");
        add("RawInput", "Settings", "Uses raw mouse input for camera movement.");
        add("MouseRawInput", "Settings", "Uses raw mouse input for camera movement.");
        add("Freeze", "Settings", "Freezes movement and position updates.");
        add("ServerLag", "Settings", "Delays incoming packets to simulate latency.");
        add("FakeLag", "Settings", "Queues packets to simulate network lag.");
        add("Disabler", "Settings", "Changes packet handling using server-specific modes.");
        add("Blink", "Settings", "Queues outgoing movement packets until released.");
        add("LagRange", "Settings", "Controls packet delay based on target distance.");
    }

    private static void add(String name, String category, String description) {
        CATEGORIES_BY_NAME.put(name.toLowerCase(Locale.ROOT), category);
        DESCRIPTIONS.put(name.toLowerCase(Locale.ROOT), description);
    }

    public static String category(Module module) {
        if (module instanceof ScriptModule) return "Settings";
        return CATEGORIES_BY_NAME.getOrDefault(module.getName().toLowerCase(Locale.ROOT), "Settings");
    }

    public static String description(String name) {
        return DESCRIPTIONS.getOrDefault(name.toLowerCase(Locale.ROOT), "");
    }

    public static List<Module> modules(String category) {
        List<Module> result = new ArrayList<>();
        for (Module module : Myau.moduleManager.allModules()) {
            if (category.equalsIgnoreCase(category(module))) {
                result.add(module);
            }
        }
        result.sort(Comparator.comparing(Module::getName, String.CASE_INSENSITIVE_ORDER));
        return result;
    }

    private ModuleCatalog() { }
}
