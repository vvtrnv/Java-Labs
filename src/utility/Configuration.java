package utility;

import model.transport.habitat.Habitat;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class Configuration
{
    private static String configFileName = "config.txt";
    private static File configFile = new File(configFileName);

    public static void saveConfig()
    {
        try (
                FileWriter writer = new FileWriter(configFileName, false)
        ) {
            String text = Habitat.getN1() + " // Generation time (sec) of Car\n" +
                    Habitat.getP1() + " // Probability (%) to born Car\n" +
                    Habitat.getN2() + " // Generation time (sec) of Bike\n" +
                    Habitat.getP2() + " // Probability (%) to born Bike\n" +
                    Habitat.getD1() + " // Death period of Car\n" +
                    Habitat.getD2() + " // Death period of Bike\n";
            writer.append(text);
            writer.flush();
        } catch (FileNotFoundException eFileNotFound) {
            System.err.println("Error: file was not found while saving config");
        } catch (IOException eIO) {
            System.err.println("Error: something went wrong with IO while saving config");
        }
    }

    public static ArrayList<Integer> loadConfig()
    {
        ArrayList<Integer> parameters = new ArrayList<>();

        try(
                Scanner scanner = new Scanner(configFile)
        ) {
            while (scanner.hasNextLine()) {
                String string = scanner.nextLine();
                Scanner intScanner = new Scanner(string);
                parameters.add(intScanner.nextInt());
            }
        } catch (IOException eIO) {
            System.err.println("Error: something went wrong while loading config");
        }

        return parameters;
    }

    public static File getConfigFile()
    {
        return configFile;
    }
}

