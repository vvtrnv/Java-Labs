package model.transport;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class Car extends Transport
{
    public static int numberOfCars = 0;
    static public Image image;

    // Запуск единоразового присвоения картинки объектам Car
    static
    {
        try
        {
            image = ImageIO.read(new File("src/resources/car.png"));
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

     /*
     * Конструктор, который вызывает конструктор супер класса
     * и передаёт туда параметры.
     * Так же увеличивает кол-во машин и транспорта в целом.
     */
    Car(int X, int Y, String path, int birthTime, int deathTime)
    {
        super(X, Y, path, birthTime, deathTime);
        numberOfCars++;
        countAllTransports++;
    }
}
