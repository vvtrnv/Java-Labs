package view;

import controller.Controller;
import model.transport.Bike;
import model.transport.Car;
import model.transport.Transport;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class MyField extends JPanel
{
    ArrayList<Transport> transports = new ArrayList<>();
    Controller controller;

    // Вызов конструктора JPanel
    public MyField() { super(); }

    // Метод перерисовки
    public void paintTransport(ArrayList<Transport> trnsprts)
    {
        this.transports = trnsprts;
        repaint();
    }

    public void configureController(Controller controller)
    {
        this.controller = controller;
    }

    @Override
    public void paint(Graphics g)
    {
        super.paint(g);

        for(int i = 0; i < transports.size(); i++)
        {
            Transport transport = transports.get(i);

            if(transport instanceof Car)
                g.drawImage(Car.image, transport.getX(), transport.getY(), null);
            else
                g.drawImage(Bike.image, transport.getX(), transport.getY(), null);
        }
    }
}
