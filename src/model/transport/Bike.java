package model.transport;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.Random;

public class Bike extends Transport
{
    public static int numberOfBikes = 0;

    private int routeY = 1;

    private static Image image_up;
    private static Image image_down;

    static
    {
        try {
            image_up = ImageIO.read(new File("src/resources/bike_up.png"));
            image_down = ImageIO.read(new File("src/resources/bike_down.png"));
        } catch (IOException e) {
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
        super(X, Y, image_up, birthTime, deathTime);
        numberOfBikes++;
        countAllTransports++;

        Random random = new Random();
        int route = random.nextInt(2);
        if(route == 0)
        {
            routeY = 1;
            setImage(image_down);
        }
        else
        {
            routeY = -1;
            setImage(image_up);
        }
    }

    public void move(int speed)
    {
        int bikeY = this.getY();
        if(bikeY + speed > 700)
        {
            routeY = -1;
            setImage(image_up);
        }


        if(bikeY - speed < 0)
        {
            routeY = 1;
            setImage(image_down);
        }


        this.setY(bikeY + speed * routeY);
    }
}
