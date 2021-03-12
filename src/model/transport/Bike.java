package model.transport;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class Bike extends Transport
{
    public static int numberOfBikes = 0;
    static public Image image;

    // Запуск единоразового присвоения картинки объектам Car
    static
    {
        try
        {
            image = ImageIO.read(new File("src/resources/bike.png"));
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

     /*
     * Конструктор, который вызывает конструктор супер класса
     * и передаёт туда параметры.
     * Так же увеличивает кол-во мотоциклов и транспорта в целом.
     */
    Bike(int X, int Y, String path)
    {
        super(X, Y, path);
        numberOfBikes++;
        countAllTransports++;
    }
}
