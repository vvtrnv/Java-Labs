package controller;

// Данный класс служит для общения между пакетами model и view

import model.TransportStorage;
import model.transport.Bike;
import model.transport.Car;
import model.transport.Transport;
import model.transport.habitat.Habitat;
import model.transport.moveAI.BikeAI;
import model.transport.moveAI.CarAI;
import view.ControlPanel;
import view.MyField;
import view.MyFrame;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.TreeMap;

public class Controller
{
    private MyField field;
    private Habitat habitat;
    private MyFrame frame;
    private ControlPanel controlPanel;

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

        this.bikeAI = new BikeAI();
        bikeAI.start();
    }

    public void toPaint(ArrayList<Transport> transports) { field.paintTransport(transports); }

    public void startBornProcess()
    {
        habitat.startBorn();
        frame.setStartButtonProcessInMenu();
    }

    public void stopBornProcess()
    {
        habitat.stopBorn();
        frame.setStopButtonProcessInMenu();
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

    public void switchTimeRadioGroupStateOff() {
        controlPanel.switchTimeRadioGroupStateOff();
    }

    public void switchTimeRadioGroupStateOn() {
        controlPanel.switchTimeRadioGroupStateOn();
    }

    public boolean isInfoDialogEnabled() {
        return controlPanel.isInfoDialogEnabled();
    }

    public boolean showInfoDialog() { return frame.showFinishDialog();}

    public void refreshField() {
        field.refreshField();
    }

    public void setStartButtonState() {
        controlPanel.setStartButtonEnabled();
    }

    public void setStopButtonState() {
        controlPanel.setStopButtonEnabled();
    }

    public void switchInfoRadioButtonState() {
        controlPanel.switchInfoRadioGroupState();
    }

    public void switchDialogRadioButtonState() {
        frame.switchDialogRadioButtonState();
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

    public void changeCarPriority(int priority)
    {
        carAI.setAIPriority(priority);
    }

    public void changeBikePriority(int priority)
    {
        bikeAI.setAIPriority(priority);
    }

    public void setN1(int N1) {
        habitat.setN1(N1);
        controlPanel.setN1(N1);
        frame.setN1(N1);
    }

    public void setN2(int N2) {
        habitat.setN2(N2);
        controlPanel.setN2(N2);
        frame.setN2(N2);
    }

    public void setP1(int P1) {
        habitat.setP1(P1);
        controlPanel.setP1(P1);
        frame.setP1(P1);
    }

    public void setP2(int P2) {
        habitat.setP2(P2);
        controlPanel.setP2(P2);
        frame.setP2(P2);
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
