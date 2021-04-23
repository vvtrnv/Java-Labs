package model;

import model.transport.Bike;
import model.transport.Car;
import model.transport.Transport;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.TreeMap;


public class TransportStorage
{
    private static TransportStorage instance;

    // Коллекция для хранения объектов
    private ArrayList<Transport> transportsList;
    // Коллекция для хранения уникальных идентификаторов
    private HashSet<String> aliveTransport;
    // Коллекция для хранения времени рождения объектов
    private TreeMap<String, Integer> transportBornTime;


    private TransportStorage()
    {
        this.transportsList = new ArrayList<>();
        this.aliveTransport = new HashSet<>();
        this.transportBornTime = new TreeMap<>();
    }

    public static TransportStorage getInstance() {
        if (instance == null) {
            instance = new TransportStorage();
        }
        return instance;
    }

    public void reset()
    {
        instance.transportsList = new ArrayList<>();
        instance.aliveTransport = new HashSet<>();
        instance.transportBornTime = new TreeMap<>();
    }

    public void removeTransport(int gameSec)
    {
        for(int i = 0; i < transportsList.size(); i++)
        {
            Transport transport = transportsList.get(i);
            if(gameSec == transport.getDeathTime())
            {
                if(transport instanceof Car)
                    Car.numberOfCars--;
                else
                    Bike.numberOfBikes--;

                Transport.countAllTransports--;
                transportsList.remove(transport);
                aliveTransport.remove(transport.getUuid());
                transportBornTime.remove(transport.getUuid());
            }
        }
    }

    // Геттеры
    public ArrayList<Transport> getTransportsList() {
        return transportsList;
    }

    public HashSet<String> getAliveTransport() {
        return aliveTransport;
    }

    public TreeMap<String, Integer> getTransportBornTime() {
        return transportBornTime;
    }
}
