package model.transport;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.Random;

public class Car extends Transport
{
    public static int numberOfCars = 0;
    private int routeX = 1;

    private static Image image_right;
    private static Image image_left;

    static
    {
        try {
            image_right = ImageIO.read(new File("src/resources/car_left.png"));
            image_left = ImageIO.read(new File("src/resources/car_right.png"));
        } catch (IOException e) {
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
        super(X, Y, image_left, birthTime, deathTime);
        numberOfCars++;
        countAllTransports++;

        Random random = new Random();

        setY(random.nextInt(360) + 200);


        // Рандомное направление движения

        int route = random.nextInt(2);
        if(route == 0)
        {
            routeX = 1;
            setImage(image_left);
        }
        else
        {
            routeX = -1;
            setImage(image_right);
        }
    }

    public void move(int speed)
    {
        int carX = getX();
        if(carX + speed > 800)
        {
            routeX = -1;
            setImage(image_right);
        }


        if(carX - speed < 0)
        {
            routeX = 1;
            setImage(image_left);
        }

        this.setX(carX + speed * routeX);
    }
}
