package model.transport;

import model.transport.habitat.Habitat;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class Car extends Transport
{
    public static int numberOfCars = 0;
    static public Image image;
    private int routeX = 1;

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

    public void move(int speed)
    {
        int carX = getX();
        if(carX + speed > Habitat.SIZEWINDOW)
            routeX = -1;

        if(carX - speed < 0)
            routeX = 1;

        this.setX(carX + speed * routeX);
    }
}
