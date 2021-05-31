package model.transport;

import model.transport.habitat.Habitat;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class Bike extends Transport
{
    public static int numberOfBikes = 0;
    static public Image image;
    private int routeY = 1;

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
    Bike(int X, int Y, String path, int birthTime, int deathTime)
    {
        super(X, Y, path, birthTime, deathTime);
        numberOfBikes++;
        countAllTransports++;
    }

    public void move(int speed)
    {
        int bikeY = this.getY();
        if(bikeY + speed > Habitat.SIZEWINDOW)
            routeY = -1;

        if(bikeY - speed < 0)
            routeY = 1;

        this.setY(bikeY + speed * routeY);
    }

}
