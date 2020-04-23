
package nl.ictm2a4.javagame;

import nl.ictm2a4.javagame.loaders.FileLoader;
import nl.ictm2a4.javagame.screens.GameScreen;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Path;

public class Main {

    public static void main(String[] args) {
        new GameScreen();


        URI path = Path.of((new JFileChooser().getFileSystemView().getDefaultDirectory().toPath()
            + File.separator + GameScreen.gameName + File.separator + "images").replaceAll("%20", " ")).toUri();

        if ((new File(path).exists()))
            new File(path).mkdir();

        BufferedImage bImage = FileLoader.getInstance().getLevelThumbnail(0);
        try {
            ImageIO.write(bImage, "png", new File(path.getPath() + File.separator + "image.png"));
        } catch (IOException e) {
            System.out.println("Exception occured :" + e.getMessage());
        }
        System.out.println("Images were written succesfully.");

    }
}