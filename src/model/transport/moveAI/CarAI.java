package model.transport.moveAI;

import model.TransportStorage;
import model.transport.Car;
import model.transport.Transport;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class CarAI extends BaseAI
{
    private int routeX = 1;

    public CarAI(){}

    public synchronized void run()
    {
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                update();
            }
        }, 0, 100);
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
            if(transportArrayList.get(i) instanceof Car)
            {
                Car car = (Car) transportArrayList.get(i);
                //int carX = car.getX();
                int speed = 1;

                car.move(speed);
            }
        }
    }

}
