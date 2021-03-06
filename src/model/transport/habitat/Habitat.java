package model.transport.habitat;

import bornProcess.BornProcess;
import controller.Controller;
import model.TransportStorage;
import model.transport.*;
import view.MyFrame;

import java.util.ArrayList;
import java.util.Timer;

public class Habitat
{
    /*
    * СВОЙСТВА ОБЪЕКТА
    * */
    private int N1;

    private int N2;
    private int P1;
    private int P2;

    // ЛР3. время жизни объекта
    private int D1;
    private int D2;

    // Размер окна
    final public int SIZEWINDOW = 800;

    // Путь к файлам
    final private String pathToCar = "src/resources/car.png";
    final private String pathToBike = "src/resources/bike.png";

    // Список для хранения объектов и таймер

    private Timer timer = new Timer();
    private int time = 0;

    // Состояние процесса(On/Off)
    private boolean bornProcessOn = false;

    private Controller controller;
    MyFrame myframe;
    BornProcess bornProcess = new BornProcess(this);

    /*
    * МЕТОДЫ КЛАССА
    * */

    // Конструтор с параметрами
    public Habitat(int tmCar, int tmBike, int probCar, int probBike, MyFrame frame, int D1, int D2)
    {
        this.N1 = tmCar;
        this.N2 = tmBike;
        this.P1 = probCar;
        this.P2 = probBike;

        this.D1 = D1;
        this.D2 = D2;

        this.myframe = frame;
    }

    // Рандом вероятности, чтобы выяснить отображать или нет объект Car
    boolean isCarBorn(int tmCar, int probCar, int time)
    {
        int probability = (int)(Math.random() * 100 + 1);
        return probability <= probCar && time % tmCar == 0;
    }

    // Рандом вероятности, чтобы выяснить отображать или нет объект Bike
    boolean isBikeBorn(int tmBike, int probBike, int time)
    {
        int probability = (int)(Math.random() * 100 + 1);
        return probability <= probBike && time % tmBike == 0;
    }

    // Обновление по таймеру
    public void update(int time)
    {
        AbstractFactory factory;
        TransportStorage storage = TransportStorage.getInstance();
        controller.passTime(time);
        this.time = time;

        if(isCarBorn(N1, P1, time))
        {
            factory = new AbstractFactoryCar();
            Transport newTransport = factory.transportBorn((int) (Math.random() * (SIZEWINDOW - 99)),
                    (int) (Math.random() * (SIZEWINDOW - 99)),
                    pathToCar , time, time + D1);
            storage.getTransportsList().add(newTransport);
            storage.getAliveTransport().add(newTransport.getUuid());
            storage.getTransportBornTime().put(newTransport.getUuid(), newTransport.getBirthTime());

        }

        if(isBikeBorn(N2, P2, time))
        {
            factory = new AbstractFactoryBike();
            Transport newTransport = factory.transportBorn((int) (Math.random() * (SIZEWINDOW - 99)),
                    (int) (Math.random() * (SIZEWINDOW - 99)),
                    pathToBike, time, time + D2);
            storage.getTransportsList().add(newTransport);
            storage.getAliveTransport().add(newTransport.getUuid());
            storage.getTransportBornTime().put(newTransport.getUuid(), newTransport.getBirthTime());

        }


        storage.removeTransport(time);
        controller.toPaint(storage.getTransportsList());
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
        TransportStorage.getInstance().reset();
        bornProcessOn = false;
    }

    // Пауза
    public void pauseBorn()
    {
        timer.cancel();
        timer.purge();
    }

    // Возобновить
    public void resumeBorn()
    {
        timer = new Timer();
        bornProcess = new BornProcess(this, time);
        timer.schedule(bornProcess, 0, 1000);

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

    public void setN1(int N1) {
        this.N1 = N1;
    }

    public void setN2(int N2) {
        this.N2 = N2;
    }

    public void setP1(int P1) {
        this.P1 = P1;
    }

    public void setP2(int P2) {
        this.P2 = P2;
    }

    public void setD1(int D1) {
        this.D1 = D1;
    }

    public void setD2(int D2) {
        this.D2 = D2;
    }
}
