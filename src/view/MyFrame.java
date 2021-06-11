package view;

import controller.Controller;
import model.transport.habitat.Habitat;
import utility.Music;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.util.ArrayList;

// Окно JFrame
public class MyFrame extends JFrame implements KeyListener
{
    final private int N1 = 5;
    final private int N2 = 5;
    final private int P1 = 100;
    final private int P2 = 100;
    final private int controlPanelSize = 500;
    int time;

    // Время жизни объекта
    private int D1 = 5;
    private int D2 = 10;

    Habitat habitat;
    Controller controller;
    MyField myField;
    ControlPanel controlPanel;
    JLabel timeLabel;

    public MyFrame()
    {
        habitat = new Habitat(1, 2, 100, 100, this, D1, D2);
        myField = new MyField();
        controlPanel = new ControlPanel(N1, N2, P1, P2, D1, D2, this);

        // Привяжем контроллер
        controller = new Controller(myField, habitat, this, controlPanel);
        habitat.configureController(controller);
        myField.configureController(controller);
        controlPanel.configureController(controller);

        // Задаём параметры окна
        setTitle("Transports");
        setPreferredSize(new Dimension(habitat.SIZEWINDOW + controlPanelSize,
                habitat.SIZEWINDOW));
        setLayout(new BorderLayout());
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setFocusable(true);
        requestFocusInWindow();

        addWindowListener(new WindowListener() {
            @Override
            public void windowOpened(WindowEvent windowEvent) {
                ArrayList<Integer> parameters = controller.loadConfig();
                controller.setN1(parameters.get(0));
                controller.setP1(parameters.get(1));
                controller.setN2(parameters.get(2));
                controller.setP2(parameters.get(3));
                controller.setD1(parameters.get(4));
                controller.setD2(parameters.get(5));
            }

            @Override
            public void windowClosing(WindowEvent windowEvent) {
                controller.saveConfig();
                System.exit(0);
            }

            @Override
            public void windowClosed(WindowEvent windowEvent) {

            }

            @Override
            public void windowIconified(WindowEvent windowEvent) {

            }

            @Override
            public void windowDeiconified(WindowEvent windowEvent) {

            }

            @Override
            public void windowActivated(WindowEvent windowEvent) {

            }

            @Override
            public void windowDeactivated(WindowEvent windowEvent) {

            }
        });
        addKeyListener(this);
        setFocusable(true);
        requestFocusInWindow();

        // Задаём параметры поля
        myField.setPreferredSize(new Dimension(habitat.SIZEWINDOW, habitat.SIZEWINDOW));
        add(myField);

        // Добавляем control Panel
        add(controlPanel, BorderLayout.EAST);

        // Добавляем Mouse Listener
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                requestFocusInWindow();
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                requestFocusInWindow();
            }
        });

//        // Добавляем клавиши для запуска в поле myField
//        // Start, кнопка B
//        AbstractAction start = new StartPr();
//        KeyStroke keyStrokeStart = KeyStroke.getKeyStroke("B");
//        InputMap inputMap = myField.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
//        inputMap.put(keyStrokeStart, "startPr");
//        ActionMap actionMap = myField.getActionMap();
//        actionMap.put("startPr", start);
//
//        // Stop, кнопка E
//        AbstractAction stop = new StopPr();
//        KeyStroke keyStrokeStop = KeyStroke.getKeyStroke("E");
//        InputMap inputMapStop = myField.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
//        inputMap.put(keyStrokeStop, "stopPr");
//        ActionMap actionMapStop = myField.getActionMap();
//        actionMap.put("stopPr", stop);
//
//        // Показ текста, кнопка T
//        AbstractAction visibleTime = new VisibleTime();
//        KeyStroke keyStrokeVisible = KeyStroke.getKeyStroke("T");
//        InputMap inputMapVisible = myField.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
//        inputMap.put(keyStrokeVisible, "visibleTime");
//        ActionMap actionMapVisible = myField.getActionMap();
//        actionMap.put("visibleTime", visibleTime);


        // Задаём параметры JLabel для поля времени
        timeLabel = new JLabel("",SwingConstants.CENTER);
        add(timeLabel,BorderLayout.SOUTH);

        try{
            Music.playSound("src/resources/backgroundMusic.wav");
        }catch (LineUnavailableException | IOException |UnsupportedAudioFileException e)
        {
            e.printStackTrace();
        }

        // Расположение окна, его видимость и т.д
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
        setResizable(true);
    }

//    class StartPr extends AbstractAction
//    {
//        @Override
//        public void actionPerformed(ActionEvent e)
//        {
//            if (!controller.isBornProcessOn())
//            {
//                myField = new MyField();
//                myField.configureController(controller);
//                controller.startBornProcess();
//            }
//        }
//    }
//
//    class StopPr extends AbstractAction
//    {
//        @Override
//        public void actionPerformed(ActionEvent e)
//        {
//            if (controller.isBornProcessOn())
//            {
//                controller.stopBornProcess();
//                showFinishDialog();
//                controller.refreshTransports();
//            }
//        }
//    }

    @Override
    public void keyTyped(KeyEvent keyEvent) {
        switch (keyEvent.getKeyCode()) {
            case KeyEvent.VK_B:
                if (!controller.isBornProcessOn()) {
                    myField = new MyField();
                    myField.configureController(controller);
                    controller.startBornProcess();
                    controller.setStopButtonState();
                }
                break;
            case KeyEvent.VK_E:
                if (controller.isBornProcessOn()) {
                    if (controller.isInfoDialogEnabled()) {
                        controller.pauseBornProcess();
                        if (showFinishDialog()) {
                            controller.stopBornProcess();
                            controller.setStartButtonState();
                            controller.refreshTransportPopulation();
                        } else {
                            controller.resumeBornProcess();
                        }
                    }
                    else {
                        controller.setStartButtonState();
                        controller.stopBornProcess();
                        controller.refreshTransportPopulation();
                    }
                }
                break;
            case KeyEvent.VK_T:
                timeLabel.setVisible(!timeLabel.isVisible());
                controller.switchTimeRadioGroupState();
                break;
        }
    }

    class VisibleTime extends AbstractAction
    {
        @Override
        public void actionPerformed(ActionEvent e)
        {
            timeLabel.setVisible(!timeLabel.isVisible());
        }
    }

    // Обновить таймер на экране
    public void updateTime(int time)
    {
        this.time = time;
        timeLabel.setText(time / 60 + " minutes " + time % 60 + " seconds");
    }


    // Вывод в диалоговом окне результат работы.
    public boolean showFinishDialog()
    {
        JPanel panel = new JPanel(new GridLayout(1, 1));
        JTextArea area = new JTextArea();
        area.setEditable(false);

        area.setText("Born process is finished. Results:\n" +
                "Cars: " + controller.getCarsAmount() + "\n" +
                "Bikes: " + controller.getBikesAmount() + "\n" +
                "All transport: " + controller.getAllTransportsAmount() + "\n" +
                "Passed time: " + time / 60 + " minutes " + time % 60 + " seconds");

        panel.add(area);

        int res = JOptionPane.showConfirmDialog(null, panel, "Simulation state",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (res == JOptionPane.OK_OPTION) {
            controller.refreshField();
            return true;
        } else {
            return false;
        }

    }

    public void turnTimeLabelOn() {
        timeLabel.setVisible(true);
    }

    public void turnTimeLabelOff() {
        timeLabel.setVisible(false);

    }

    @Override
    public void keyPressed(KeyEvent keyEvent) { }

    @Override
    public void keyReleased(KeyEvent keyEvent) { }
}
