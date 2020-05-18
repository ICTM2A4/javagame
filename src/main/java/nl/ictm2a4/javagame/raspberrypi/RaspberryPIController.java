package nl.ictm2a4.javagame.raspberrypi;

import nl.ictm2a4.javagame.loaders.LevelLoader;

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
        this.client = new Client("192.168.2.14", 8001);

        executor =
            Executors.newSingleThreadScheduledExecutor();

        Runnable periodicTask = () -> {
            if (client.isConnected() && LevelLoader.getInstance().getCurrentLevel().isPresent()) //TODO: only if not paused
                currentButton = client.getButtonStatus();
        };

        executor.scheduleAtFixedRate(periodicTask, 0, 2, TimeUnit.MILLISECONDS);
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