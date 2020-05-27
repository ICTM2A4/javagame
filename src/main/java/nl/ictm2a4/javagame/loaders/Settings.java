package nl.ictm2a4.javagame.loaders;

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

    public Settings() {
        instance = this;
        load();
    }

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

        }
    }

    public void save() {
        if (getSettingsFile().isPresent()) {
            File file = getSettingsFile().get();

            JSONObject object = new JSONObject();
            object.put("showShadows", showShadows);
            object.put("animatedLights", animatedLights);

            JSONObject userObject = new JSONObject();
            if (GameScreen.getInstance().getCurrentUser().isPresent()) {
                User user = GameScreen.getInstance().getCurrentUser().get();
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

    private File checkFolders() {
        File customLevelsFolder = new File((Path.of((new JFileChooser().getFileSystemView().getDefaultDirectory().toPath()
            + File.separator + GameScreen.GAMENAME).replaceAll("%20", " "))
            + File.separator + "settings").replaceAll("%20", ""));
        if (!customLevelsFolder.exists())
            customLevelsFolder.mkdir();
        return customLevelsFolder;
    }

    private Optional<File> createFile() {
        try {
            File customLevelsFolder = checkFolders();
            File file = new File(customLevelsFolder.getPath() + File.separator + "settings.json");
            file.createNewFile();
            JSONObject object = new JSONObject();
            object.put("showShadows", true);
            object.put("animatedLights", true);
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

    private Optional<File> getSettingsFile() {
        File customLevelsFolder = checkFolders();
        File file = new File(customLevelsFolder.getPath() + File.separator + "settings.json");
        if (!file.exists())
            return createFile();
        else
            return Optional.of(file);

    }

    public static Settings getInstance() {
        if (instance == null)
            new Settings();
        return instance;
    }

    public void setShowShadows(boolean value) {
        this.showShadows = value;
        save();
    }

    public boolean isShowShadows() {
        return showShadows;
    }

    public void setAnimatedLights(boolean value) {
        this.animatedLights = value;
        save();
    }

    public boolean isAnimatedLights() {
        return animatedLights;
    }

    public void setUseRPI(boolean value) {useRPI = value;}

    public boolean isUseRPI() {return useRPI;}

    public void updateUser(String username, String password) {
        this.username = username;
        this.password = password;
        save();
    }

    public String getUsername() {
        return this.username;
    }

    public String getPassword() {
        return this.password;
    }

    public void setRaspberryPiIp(String ip) {this.raspberryPiIp = ip;}

    public String getRaspberryPiIp() {
        return raspberryPiIp;
    }
}
