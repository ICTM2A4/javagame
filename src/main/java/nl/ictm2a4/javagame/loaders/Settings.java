package nl.ictm2a4.javagame.loaders;

import nl.ictm2a4.javagame.raspberrypi.RaspberryPIController;
import nl.ictm2a4.javagame.screens.GameScreen;
import nl.ictm2a4.javagame.services.users.User;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javax.swing.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.Optional;

public class Settings {

    private static Settings instance;
    private boolean showShadows = true;
    private boolean animatedLights = true;
    private boolean useRPI;
    private String username, password, raspberryPiIp;

    /**
     * Create an instance of Settings and load all settings from the file
     */
    public Settings() {
        instance = this;
        load();
    }

    /**
     * Load all settings from the settings.json file
     */
    private void load() {
        Optional<File> file = getSettingsFile();

        Optional<JSONObject> settingsObject = Optional.empty();

        if (file.isPresent()) {
            try (InputStream is = new FileInputStream(file.get())) {
                JSONParser parser = new JSONParser();
                Reader rd = new InputStreamReader(is, StandardCharsets.UTF_8);
                Object object = parser.parse(rd);
                settingsObject = Optional.of((JSONObject) object);
            } catch (IOException | ParseException e) {
                e.printStackTrace();
            }
        }

        if (settingsObject.isPresent()) {
            showShadows = (Boolean) settingsObject.get().getOrDefault("showShadows", true);
            animatedLights = (Boolean) settingsObject.get().getOrDefault("animatedLights", true);
            username = (String) ((JSONObject)settingsObject.get().getOrDefault("user", new JSONObject())).getOrDefault("username", "");
            password = (String) ((JSONObject)settingsObject.get().getOrDefault("user", new JSONObject())).getOrDefault("password", "");
            useRPI = (Boolean) settingsObject.get().getOrDefault("useRPI", false);
            raspberryPiIp = (String) settingsObject.get().getOrDefault("rpiIP", "");

        }
    }

    /**
     * Save all settings to the settings.json file
     */
    public void save() {
        if (getSettingsFile().isPresent()) {
            File file = getSettingsFile().get();

            JSONObject object = new JSONObject();
            object.put("showShadows", showShadows);
            object.put("animatedLights", animatedLights);
            object.put("useRPI", useRPI);
            object.put("rpiIP", raspberryPiIp);

            JSONObject userObject = new JSONObject();
            if (GameScreen.getInstance().getCurrentUser().isPresent()) {
                userObject.put("username", username);
                userObject.put("password", password);
            }

            object.put("user", userObject);

            try(FileWriter writer = new FileWriter(file)) {
                writer.write(object.toJSONString());
                writer.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    /**
     * Check if the settings folder exists
     * @return The folder file
     */
    private File checkFolders() {
        File gameFolder = new File(String.valueOf((Path.of((new JFileChooser().getFileSystemView().getDefaultDirectory().toPath()
            + File.separator + GameScreen.GAMENAME).replaceAll("%20", "")))));
        if (!gameFolder.exists())
            gameFolder.mkdir();

        File settingsFolder = new File((Path.of((new JFileChooser().getFileSystemView().getDefaultDirectory().toPath()
            + File.separator + GameScreen.GAMENAME))
            + File.separator + "settings").replaceAll("%20", ""));
        if (!settingsFolder.exists())
            settingsFolder.mkdir();
        return settingsFolder;
    }

    /**
     * Create the settings.json file
     * @return Get the Optional File of settings.json
     */
    private Optional<File> createFile() {
        try {
            File customLevelsFolder = checkFolders();
            File file = new File(customLevelsFolder.getPath() + File.separator + "settings.json");
            file.createNewFile();
            JSONObject object = new JSONObject();
            object.put("showShadows", true);
            object.put("animatedLights", true);
            object.put("useRPI", false);
            object.put("rpiIP", "");
            object.put("user", new JSONObject());
            try(FileWriter writer = new FileWriter(file)) {
                writer.write(object.toJSONString());
                writer.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return Optional.of(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    /**
     * Check if the settingsFile exits
     * @return Return the Optional settings file
     */
    private Optional<File> getSettingsFile() {
        File customLevelsFolder = checkFolders();
        File file = new File(customLevelsFolder.getPath() + File.separator + "settings.json");
        if (!file.exists())
            return createFile();
        else
            return Optional.of(file);

    }

    /**
     * Get the instance of Settings
     * @return Settings instance
     */
    public static Settings getInstance() {
        if (instance == null)
            new Settings();
        return instance;
    }

    /**
     * Set the new setting value of the shadows
     * @param value New setting to set
     */
    public void setShowShadows(boolean value) {
        this.showShadows = value;
    }

    /**
     * Get the setting value of shadows
     * @return Shadow setting value
     */
    public boolean isShowShadows() {
        return showShadows;
    }

    /**
     * Set the setting value of animated lights
     * @param value New setting to set
     */
    public void setAnimatedLights(boolean value) {
        this.animatedLights = value;
    }

    /**
     * Get the setting value of animated lights
     * @return Animated lights setting value
     */
    public boolean isAnimatedLights() {
        return animatedLights;
    }

    /**
     * Set the setting value of raspberry pi usage
     * @param value New setting to set
     */
    public void setUseRPI(boolean value) {
        useRPI = value;
        if (!value)
            RaspberryPIController.getInstance().disconnect();
    }

    /**
     * Get the setting value of raspberry pi usage
     * @return Raspberry PI usage setting value
     */
    public boolean isUseRPI() {return useRPI;}

    /**
     * Update the user data setting
     * @param username Username to set
     * @param password Password to set
     */
    public void updateUser(String username, String password) {
        this.username = username;
        this.password = password;
    }

    /**
     * Get the username setting
     * @return Username setting value
     */
    public String getUsername() {
        return this.username;
    }

    /**
     * Get the password setting
     * @return Password setting value
     */
    public String getPassword() {
        return this.password;
    }

    /**
     * Set the rapsberry pi ip
     * @param ip Raspberry PI ip to set
     */
    public void setRaspberryPiIp(String ip) {
        boolean reconnect = (!ip.equals(this.raspberryPiIp) && !ip.equals(""));
        this.raspberryPiIp = ip;
        if(reconnect)
            new RaspberryPIController();
    }

    /**
     * Get the raspberry PI ip
     * @return Raspberry PI ip
     */
    public String getRaspberryPiIp() {
        return raspberryPiIp;
    }
}
