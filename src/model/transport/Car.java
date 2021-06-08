package model.transport;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.Random;

public class Car extends Transport
{
    public static int numberOfCars = 0;


    private static Image image_right;
    private static Image image_left;

    static
    {
        try {
            image_left = ImageIO.read(new File("src/resources/car_left.png"));
            image_right = ImageIO.read(new File("src/resources/car_right.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
     /*
     * Конструктор, который вызывает конструктор супер класса
     * и передаёт туда параметры.
     * Так же увеличивает кол-во машин и транспорта в целом.
     */
    public Car(int X, int Y, String path, int birthTime, int deathTime)
    {
        super(X, Y, image_left, birthTime, deathTime);
        numberOfCars++;
        countAllTransports++;

        Random random = new Random();

        setY(random.nextInt(330) + 200);

        // Рандомное направление движения

        int route = random.nextInt(2);
        if(route == 0)
        {
            setRoute(1);
            setImage(image_right);
        }
        else
        {
            setRoute(-1);
            setImage(image_left);
        }
    }

    public void setIMG()
    {
        if(getRoute() == 1)
            setImage(image_right);
        else
            setImage(image_left);
    }

    public void move(int speed)
    {
        int carX = getX();
        if(carX + speed > 680)
        {
            setRoute(-1);
            setImage(image_left);
        }

        if(carX - speed < 0)
        {
            setRoute(1);
            setImage(image_right);
        }

        this.setX(carX + speed * getRoute());
    }
}
