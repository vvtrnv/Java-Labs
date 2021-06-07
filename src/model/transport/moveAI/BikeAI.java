package model.transport.moveAI;

import controller.Controller;
import model.TransportStorage;
import model.transport.Bike;

import model.transport.Transport;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class BikeAI extends BaseAI
{
    private Controller controller;
    public BikeAI(Controller controller)
    {
        this.controller = controller;
    }

    public synchronized void run()
    {
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                TransportStorage storage = TransportStorage.getInstance();
                controller.toPaint(storage.getTransportsList());
                update();
            }
        }, 0, 15);
    }

    void update()
    {
        if(!isActive)
        {
            return;
        }

        ArrayList<Transport> transportArrayList = TransportStorage.getInstance().getTransportsList();

        for(int i = 0 ; i < transportArrayList.size(); i++)
        {
            if(transportArrayList.get(i) instanceof Bike)
            {
                Bike bike = (Bike) transportArrayList.get(i);
                //int bikeY = bike.getY();
                int speed = 1;

                bike.move(speed);
            }
        }
    }



}
