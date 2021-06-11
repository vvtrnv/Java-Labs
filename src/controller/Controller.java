package controller;

// Данный класс служит для общения между пакетами model и view

import model.TransportStorage;
import model.transport.Bike;
import model.transport.Car;
import model.transport.Transport;
import model.transport.habitat.Habitat;
import model.transport.moveAI.BikeAI;
import model.transport.moveAI.CarAI;
import utility.Configuration;
import utility.DataBaseController;
import utility.Music;
import utility.Serialization;
import view.ControlPanel;
import view.MyField;
import view.MyFrame;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.TreeMap;

public class Controller
{
    private MyField field;
    private Habitat habitat;
    private MyFrame frame;
    private ControlPanel controlPanel;
    private Serialization serialization;

    private CarAI carAI;
    private BikeAI bikeAI;

    // Конструктор класса
    public Controller(MyField myField, Habitat habitat, MyFrame myframe, ControlPanel controlPanel)
    {
        this.field = myField;
        this.habitat = habitat;
        this.frame = myframe;
        this.controlPanel = controlPanel;

        this.carAI = new CarAI();
        carAI.start();

        this.bikeAI = new BikeAI(this);
        bikeAI.start();

        serialization = new Serialization();
    }

    public void toPaint(ArrayList<Transport> transports) { field.paintTransport(transports); }

    public void startBornProcess()
    {
        habitat.startBorn();
    }

    public void stopBornProcess()
    {
        habitat.stopBorn();
    }

    public void pauseBornProcess() {
        habitat.pauseBorn();
    }
    public void resumeBornProcess() {
        habitat.resumeBorn();
    }

    public boolean isBornProcessOn() { return habitat.isBornProcessOn(); }

    public int getCarsAmount() { return Car.numberOfCars; }
    public int getBikesAmount() { return Bike.numberOfBikes; }
    public int getAllTransportsAmount() { return Transport.countAllTransports; }

    public void refreshTransports() { habitat.refreshTransports(); }

    public void passTime(int time) { frame.updateTime(time); }


    public void switchTimeRadioGroupState() {
        controlPanel.switchTimeRadioGroupState();
    }

    public void turnTimeLabelOn() {
        frame.turnTimeLabelOn();
    }

    public void turnTimeLabelOff() {
        frame.turnTimeLabelOff();
    }

    // Включение и остановка потока передвижения для Car
    public void turnCarAIOn()
    {
        if(!carAI.isAIActive())
        {
            try
            {
                carAI.startAI();
                carAI.wait();
            }
            catch (Exception eInterrupted)
            {
                System.out.println("Error in Controller.turnCarAIOn");
            }
        }
    }

    public void turnCarAIOff()
    {
        if(carAI.isAIActive())
            carAI.stopAI();
    }

    // Включение и остановка потока передвижения для Bike
    public void turnBikeAIOn()
    {
        if(!bikeAI.isAIActive())
        {
            try
            {
                bikeAI.startAI();
                bikeAI.wait();
            }
            catch (Exception eInterrupted)
            {
                System.out.println("Error in Controller.turnBikeAIOn");
            }
        }
    }

    public void turnBikeAIOff()
    {
        if(bikeAI.isAIActive())
            bikeAI.stopAI();
    }

    public boolean showInfoDialog() { return frame.showFinishDialog();}

    public void refreshField() {
        field.refreshField();
    }

    public void showTransportsList()
    {
        JPanel panel = new JPanel(new GridLayout(1,1));
        JTextArea area = new JTextArea(6, 25);
        area.setEditable(false);
        HashSet<String> aliveTransports = TransportStorage.getInstance().getAliveTransport();
        TreeMap<String, Integer> transportBornTime = TransportStorage.getInstance().getTransportBornTime();
        for(String uuid : aliveTransports)
        {
            int bornTime = transportBornTime.get(uuid);
            area.append("ID: " + uuid + " Born time: " + bornTime + "\n" );
        }
        panel.add(area);
        JOptionPane.showMessageDialog(null, new JScrollPane(panel) , "Transports", JOptionPane.INFORMATION_MESSAGE);
    }

    public void setStopButtonState() {
        controlPanel.setStopButtonEnabled();
    }

    public void setStartButtonState() {
        controlPanel.setStartButtonEnabled();
    }

    public void refreshTransportPopulation() {
        habitat.refreshTransports();
    }

    public boolean isInfoDialogEnabled() {
        return controlPanel.isInfoDialogEnabled();
    }

    public void changeCarPriority(int priority)
    {
        carAI.setAIPriority(priority);
    }

    public void changeBikePriority(int priority)
    {
        bikeAI.setAIPriority(priority);
    }

    public void reduceBikeAmount(int percentage)
    {
        TransportStorage.getInstance().reduceBikeAmount(percentage);
    }

    public ArrayList<Integer> loadConfig()
    {
        return Configuration.loadConfig();
    }
    public void saveConfig()
    {
        Configuration.saveConfig();
    }

    public void serializeSimulation()
    {
        serialization.serialize();
    }

    public void deserializeSimulation()
    {
        serialization.deserialize();
    }

    public void saveConfigDataBase()
    {
        try {
            DataBaseController.saveConfigToDataBase();
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

    public void loadConfigDataBase()
    {
        try
        {
            ArrayList<Integer> params = DataBaseController.loadConfigFromDataBase();
            setN1(params.get(0));
            setP1(params.get(1));
            setN2(params.get(2));
            setP2(params.get(3));
            setD1(params.get(4));
            setD2(params.get(5));
        }
        catch(SQLException e)
        {
            e.printStackTrace();
        }
    }

    public void saveRoadDataBase()
    {
        try
        {
            DataBaseController.saveTransportsToBD();
        }
        catch(SQLException e)
        {
            e.printStackTrace();
        }
    }

    public void loadRoadDataBase()
    {
        try
        {
            DataBaseController.loadTransportsFromBD();
        }
        catch(SQLException e)
        {
            e.printStackTrace();
        }
    }

    public void setN1(int N1) {
        habitat.setN1(N1);
        controlPanel.setN1(N1);
    }

    public void setN2(int N2) {
        habitat.setN2(N2);
        controlPanel.setN2(N2);
    }

    public void setP1(int P1) {
        habitat.setP1(P1);
        controlPanel.setP1(P1);
    }

    public void setP2(int P2) {
        habitat.setP2(P2);
        controlPanel.setP2(P2);
    }

    public void setD1(int D1) {
        habitat.setD1(D1);
        controlPanel.setD1(D1);
    }
    public void setD2(int D2) {
        habitat.setD2(D2);
        controlPanel.setD2(D2);
    }

}
