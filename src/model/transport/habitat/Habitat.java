package model.transport.habitat;

import bornProcess.BornProcess;
import controller.Controller;
import model.transport.*;
import view.MyFrame;

import java.awt.*;
import java.util.ArrayList;
import java.util.Timer;

public class Habitat
{
    /*
    * СВОЙСТВА ОБЪЕКТА
    * */
    private int timeCar;

    private int timeBike;
    private int probabilityCar;
    private int probabilityBike;

    // Размер окна
    final public int SIZEWINDOW = 600;

    // Путь к файлам
    final private String pathToCar = "src/resources/car.png";
    final private String pathToBike = "src/resources/bike.png";

    // Список для хранения объектов и таймер
    private ArrayList<Transport> transportList = new ArrayList<>();
    private Timer timer = new Timer();

    // Состояние процесса(On/Off)
    private boolean bornProcessOn = false;

    private Controller controller;
    MyFrame myframe;
    BornProcess bornProcess = new BornProcess(this);

    /*
    * МЕТОДЫ КЛАССА
    * */

    // Конструтор с параметрами
    public Habitat(int tmCar, int tmBike, int probCar, int probBike, MyFrame frame)
    {
        this.timeCar = tmCar;
        this.timeBike = tmBike;
        this.probabilityCar = probCar;
        this.probabilityBike = probBike;
        this.myframe = frame;
    }

    // Создание рандомной координаты
//    private Point generatePoint()
//    {
//        int x = (int) (Math.random() * (SIZEWINDOW - 99));
//        int y = (int) (Math.random() * (SIZEWINDOW - 99));
//        return new Point(x, y);
//    }

    // Рандом вероятности, чтобы выяснить отображать или нет объект Car
    boolean isOrdinaryCarBorn(int tmCar, int probCar, int time)
    {
        int probability = (int)(Math.random() * 100 + 1);
        return probability <= probCar && time % tmCar == 0;
    }

    // Рандом вероятности, чтобы выяснить отображать или нет объект Bike
    boolean isOrdinaryBikeBorn(int tmBike, int probBike, int time)
    {
        int probability = (int)(Math.random() * 100 + 1);
        return probability <= probBike && time % tmBike == 0;
    }

    // Обновление по таймеру
    public void update(int time)
    {
        AbstractFactory factory;
        controller.passTime(time);

        if(isOrdinaryCarBorn(timeCar, probabilityCar, time))
        {
            factory = new AbstractFactoryCar();
            Transport newTransport = factory.transportBorn((int) (Math.random() * (SIZEWINDOW - 99)),
                    (int) (Math.random() * (SIZEWINDOW - 99)),
                    pathToCar);
            transportList.add(newTransport);
            controller.toPaint(transportList);
        }

        if(isOrdinaryBikeBorn(timeBike, probabilityBike, time))
        {
            factory = new AbstractFactoryCar();
            Transport newTransport = factory.transportBorn((int) (Math.random() * (SIZEWINDOW - 99)),
                    (int) (Math.random() * (SIZEWINDOW - 99)),
                    pathToBike);
            transportList.add(newTransport);
            controller.toPaint(transportList);
        }
    }

    // Начало симуляции
    public void startBorn()
    {
        bornProcessOn = true;
        timer.schedule(bornProcess, 0, 1000);
    }

    // Остановка симуляции
    public void stopBorn()
    {
        timer.cancel();
        timer.purge();
        timer = new Timer();
        bornProcess = new BornProcess(this);
        transportList = new ArrayList<>();
        bornProcessOn = false;
    }

    public void refreshTransports()
    {
        Car.numberOfCars = 0;
        Bike.numberOfBikes = 0;
        Transport.countAllTransports = 0;
    }

    // Метод общения с контроллером
    public void configureController(Controller controller)
    {
        this.controller = controller;
    }

    public boolean isBornProcessOn()
    {
        return bornProcessOn;
    }

}
