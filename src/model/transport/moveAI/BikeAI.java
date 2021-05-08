package model.transport.moveAI;

import model.TransportStorage;
import model.transport.Bike;

import model.transport.Transport;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class BikeAI extends BaseAI
{
    public BikeAI(){}

    public synchronized void run()
    {
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                update();
            }
        }, 0, 10);
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
