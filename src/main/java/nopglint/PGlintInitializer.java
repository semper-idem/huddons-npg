package nopglint;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.options.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;

public class PGlintInitializer implements ModInitializer {
    private static KeyBinding toggleKeyBinding;
    private final static String propertiesPath =  (Paths.get("").toAbsolutePath().toString() + "/config/nopglint.properties");
    private static Properties settings = new Properties();
    private static boolean enabled;
    @Override
    public void onInitialize() {
        initConfig();
        registerKeyBind();
    }
    private void registerKeyBind(){
        toggleKeyBinding = new KeyBinding("key.nopglint.toggle", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_KP_3, "key.categories.misc");
        KeyBindingHelper.registerKeyBinding(toggleKeyBinding);
        ClientTickEvents.START_CLIENT_TICK.register(client ->{
            if(toggleKeyBinding.wasPressed()) {
                toggle();
                updateConfig();
            }
        });
    }

    private void initConfig(){
        if(Files.exists(Paths.get(propertiesPath))){
            loadConfig();
        }else{
            createDefaultConfig();
        }
    }

    private void updateConfig(){
        try (OutputStream outputStream = new FileOutputStream(propertiesPath)) {
            settings.setProperty("enabled", String.valueOf(enabled));
            settings.store(outputStream, null);
        } catch (IOException io) {
            io.printStackTrace();
        }
    }

    private void loadConfig(){
        try (InputStream inputStream = new FileInputStream(propertiesPath)) {
            Properties settings = new Properties();
            settings.load(inputStream);
            enabled = settings.getProperty("enabled", "true").equals("true");
        } catch (IOException io) {
            io.printStackTrace();
        }
    }

    private void createDefaultConfig(){
        try (OutputStream outputStream = new FileOutputStream(propertiesPath)) {
            Properties settings = new Properties();
            settings.put("enabled", "true");
            enabled = true;
            settings.store(outputStream, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void toggle(){
        enabled = !enabled;
    }

    public static boolean getEnabled(){
        return enabled;
    }
}
