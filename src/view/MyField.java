package view;

import controller.Controller;
import model.transport.Bike;
import model.transport.Car;
import model.transport.Transport;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class MyField extends JPanel
{
    ArrayList<Transport> transports = new ArrayList<>();
    Controller controller;
    static private Image background_img;
    // Вызов конструктора JPanel
    public MyField() {
        super();
        //setBackground(Color.decode("#F0FFF0"));
    }

    static
    {
        try {
            background_img = ImageIO.read(new File("src/resources/background.png"));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

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
        g.drawImage(background_img, 0, 0, null);


        for(int i = 0; i < transports.size(); i++)
        {
            Transport transport = transports.get(i);

            if(transport instanceof Car)
                g.drawImage(transport.getImage(), transport.getX(), transport.getY(), null);
            else
                g.drawImage(transport.getImage(), transport.getX(), transport.getY(), null);
        }
    }

    public void refreshField() {
        transports = new ArrayList<>();
        repaint();
    }
}
