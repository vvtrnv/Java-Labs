package model;

import model.transport.Transport;

import java.util.ArrayList;

public class TransportStorage
{
    private static TransportStorage instance;
    private ArrayList<Transport> transportsList;

    private TransportStorage()
    {
        this.transportsList = new ArrayList<>();
    }

    public static TransportStorage getInstance() {
        if (instance == null) {
            instance = new TransportStorage();
        }
        return instance;
    }

    public ArrayList<Transport> getRabbitsList() {
        return transportsList;
    }
}
