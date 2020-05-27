package nl.ictm2a4.javagame.raspberrypi;

import nl.ictm2a4.javagame.loaders.LevelLoader;
import nl.ictm2a4.javagame.loaders.Settings;
import nl.ictm2a4.javagame.screens.GameScreen;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class RaspberryPIController {

    private static RaspberryPIController instance;
    private Client client;
    private String currentButton = "";
    private ScheduledExecutorService executor;

    public RaspberryPIController() {
        instance = this;
        String ip = Settings.getInstance().getRaspberryPiIp();
        if (Settings.getInstance().isUseRPI() && !ip.equals("")) {

            String[] ipSplit = ip.split(":");

            this.client = new Client(ipSplit[0], Integer.parseInt(ipSplit[1]));

            executor =
                Executors.newSingleThreadScheduledExecutor();

            Runnable periodicTask = () -> {
                if (client.isConnected() && LevelLoader.getInstance().getCurrentLevel().isPresent() && !LevelLoader.getInstance().isPaused())
                    currentButton = client.getButtonStatus();
            };

            executor.scheduleAtFixedRate(periodicTask, 0, 2, TimeUnit.MILLISECONDS);
        }
    }

    public String getPressedButton() {
        return currentButton;
    }

    public static RaspberryPIController getInstance() {
        if (instance == null) {
            new RaspberryPIController();
        }
        return instance;
    }

    public void disconnect() {
        if (Settings.getInstance().isUseRPI() && client.isConnected()) {
            this.executor.shutdown();

            new java.util.Timer().schedule(
                new java.util.TimerTask() {
                    @Override
                    public void run() {
                        client.disconnect();
                    }
                },
                1000
            );
        }

    }


}
