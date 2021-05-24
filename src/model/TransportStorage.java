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

    public void setAllTransports(ArrayList<Transport> transports)
    {
        transportsList.addAll(transports);
        Car.numberOfCars = 0;
        Bike.numberOfBikes = 0;
        Transport.countAllTransports = 0;

        for(int i = 0; i < transportsList.size(); i++)
        {
            Transport trnspt = transportsList.get(i);
            Transport.countAllTransports++;
            if(trnspt instanceof Car)
                Car.numberOfCars++;
            else
                Bike.numberOfBikes++;

            aliveTransport.add(trnspt.getUuid());
            transportBornTime.put(trnspt.getUuid(), trnspt.getBirthTime());
        }
    }

    public void reduceBikeAmount(int percentage)
    {
        int bikeToDie = (int) ((double) Bike.numberOfBikes / 100 * percentage);
        Bike.numberOfBikes--;

        while(bikeToDie != 0)
        {
            Bike bike = (Bike) transportsList.stream()
                    .filter(transport -> transport instanceof  Bike)
                    .findFirst()
                    .orElse(null);

            transportsList.remove(bike);
            if(bike != null)
            {
                aliveTransport.remove(bike.getUuid());
                transportBornTime.remove(bike.getUuid());
            }
            bikeToDie--;
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
