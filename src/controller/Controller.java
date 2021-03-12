package controller;

// Данный класс служит для общения между пакетами model и view

import model.transport.Bike;
import model.transport.Car;
import model.transport.Transport;
import model.transport.habitat.Habitat;
import view.MyField;
import view.MyFrame;

import java.util.ArrayList;

public class Controller
{
    private MyField field;
    private Habitat habitat;
    private MyFrame frame;

    // Конструктор класса
    public Controller(MyField myField, Habitat habitat, MyFrame myframe)
    {
        this.field = myField;
        this.habitat = habitat;
        this.frame = myframe;
    }

    public void toPaint(ArrayList<Transport> transports) { field.paintTransport(transports); }
    public void startBornProcess() { habitat.startBorn();}
    public void stopBornProcess() { habitat.stopBorn(); }

    public boolean isBornProcessOn() { return habitat.isBornProcessOn(); }

    public int getCarsAmount() { return Car.numberOfCars; }
    public int getBikesAmount() { return Bike.numberOfBikes; }
    public int getAllTransportsAmount() { return Transport.countAllTransports; }

    public void refreshTransports() { habitat.refreshTransports(); }

    public void passTime(int time) { frame.updateTime(time); }
}
