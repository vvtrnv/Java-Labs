package view;

import controller.Controller;
import model.transport.habitat.Habitat;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

// Окно JFrame
public class MyFrame extends JFrame
{
    final private int N1 = 3;
    final private int N2 = 5;
    final private int P1 = 50;
    final private int P2 = 100;
    final private int controlPanelSize = 600;
    int time;

    // Время жизни объекта
    private int D1 = 5;
    private int D2 = 10;

    Habitat habitat;
    Controller controller;
    MyField myField;
    ControlPanel controlPanel;

    JLabel timeLabel;
    JMenuBar menuBar;

    private JRadioButtonMenuItem timeOnRadioButton;
    private JRadioButtonMenuItem timeOffRadioButton;
    private JRadioButtonMenuItem showDialogRadioButton;

    private JTextField carGenPeriodTextField;
    private JTextField bikeGenPeriodTextField;

    private JMenuItem startItem;
    private JMenuItem stopItem;
    private JMenuItem quitItem;

    private ButtonGroup carButtonGroup;
    private ButtonGroup bikeButtonGroup;

    private JMenu carProbability;
    private JMenu bikeProbability;

    public MyFrame()
    {
        habitat = new Habitat(1, 2, 100, 100, this, D1, D2);
        myField = new MyField();
        controlPanel = new ControlPanel(N1, N2, P1, P2, D1, D2);

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

        configureMenuBar();

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

        // Добавляем клавиши для запуска в поле myField
        // Start, кнопка B
        AbstractAction start = new StartPr();
        KeyStroke keyStrokeStart = KeyStroke.getKeyStroke("B");
        InputMap inputMap = myField.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        inputMap.put(keyStrokeStart, "startPr");
        ActionMap actionMap = myField.getActionMap();
        actionMap.put("startPr", start);

        // Stop, кнопка E
        AbstractAction stop = new StopPr();
        KeyStroke keyStrokeStop = KeyStroke.getKeyStroke("E");
        InputMap inputMapStop = myField.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        inputMap.put(keyStrokeStop, "stopPr");
        ActionMap actionMapStop = myField.getActionMap();
        actionMap.put("stopPr", stop);

        // Показ текста, кнопка T
        AbstractAction visibleTime = new VisibleTime();
        KeyStroke keyStrokeVisible = KeyStroke.getKeyStroke("T");
        InputMap inputMapVisible = myField.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        inputMap.put(keyStrokeVisible, "visibleTime");
        ActionMap actionMapVisible = myField.getActionMap();
        actionMap.put("visibleTime", visibleTime);


        // Задаём параметры JLabel для поля времени
        timeLabel = new JLabel("",SwingConstants.CENTER);
        add(timeLabel,BorderLayout.SOUTH);

        // Расположение окна, его видимость и т.д
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
        setResizable(true);
    }



    class StartPr extends AbstractAction
    {
        @Override
        public void actionPerformed(ActionEvent e)
        {
            if (!controller.isBornProcessOn())
            {
                myField = new MyField();
                myField.configureController(controller);
                controller.startBornProcess();
            }
        }
    }

    class StopPr extends AbstractAction
    {
        @Override
        public void actionPerformed(ActionEvent e)
        {
            if (controller.isBornProcessOn())
            {
                controller.stopBornProcess();
                showFinishDialog();
                controller.refreshTransports();
            }
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


    public void configureController(Controller controller)
    {
        this.controller = controller;
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

    // Создание параметров для JLabel такие как текст, шрифт, цвет, позиция
    private JLabel createLabel(String text, int fontPosition, Font font, Color fontColor)
    {
        JLabel label = new JLabel(text, fontPosition);
        label.setFont(font);
        label.setForeground(fontColor);

        return label;
    }

    //-----------------ADD

    public void configureMenuBar()
    {
        menuBar = new JMenuBar();
        JMenu simControlMenu = new JMenu("Simulation Control");
        startItem = new JMenuItem("Start");
        stopItem = new JMenuItem("Stop");
        quitItem = new JMenuItem("Quit");

        simControlMenu.add(startItem);
        stopItem.setEnabled(false);
        simControlMenu.add(stopItem);
        simControlMenu.addSeparator();
        simControlMenu.add(quitItem);

        startItem.addActionListener(actionEvent->
        {
            if(!controller.isBornProcessOn())
            {
                startItem.setEnabled(false);
                stopItem.setEnabled(true);
                myField = new MyField();
                myField.configureController(controller);
                controller.startBornProcess();
                controller.setStopButtonState();
            }
        });

        stopItem.addActionListener(actionEvent->
        {
            if(controller.isInfoDialogEnabled())
            {
                controller.pauseBornProcess();
                if(showFinishDialog())
                {
                    controller.stopBornProcess();
                    controller.setStartButtonState();
                    controller.refreshTransports();
                }
                else
                {
                    controller.resumeBornProcess();
                }
            }
            else
            {
                controller.stopBornProcess();
            }
        });

        quitItem.addActionListener(actionEvent-> {System.exit(0);});
        menuBar.add(simControlMenu);

        JMenu infoMenu = new JMenu("Simulation info");
        timeOnRadioButton = new JRadioButtonMenuItem("Time label ON", true);
        timeOffRadioButton = new JRadioButtonMenuItem("Time label OFF");
        showDialogRadioButton = new JRadioButtonMenuItem("Show simulation state", true);

        timeOnRadioButton.addActionListener(actionEvent->
        {
            timeLabel.setVisible(true);
            timeOnRadioButton.setSelected(true);
            timeOffRadioButton.setSelected(false);
            controller.switchTimeRadioGroupStateOn();
        });

        timeOffRadioButton.addActionListener(actionEvent -> {
            timeLabel.setVisible(false);
            timeOnRadioButton.setSelected(false);
            timeOffRadioButton.setSelected(true);
            controller.switchTimeRadioGroupStateOff();
        });

        showDialogRadioButton.addActionListener(actionEvent -> {
            controller.switchInfoRadioButtonState();
        });

        ButtonGroup buttonGroup = new ButtonGroup();
        buttonGroup.add(timeOnRadioButton);
        buttonGroup.add(timeOffRadioButton);
        infoMenu.add(timeOnRadioButton);
        infoMenu.add(timeOffRadioButton);
        infoMenu.add(showDialogRadioButton);
        menuBar.add(infoMenu);

        JMenu transportMenu = new JMenu("Transport");
        JMenu carMenu = new JMenu("Cars");
        JMenu bikeMenu = new JMenu("Bikes");
        carProbability = new JMenu("Probability");
        bikeProbability = new JMenu("Probability");
        JMenu carTimeBornPeriod = new JMenu("Generation period");
        JMenu bikeTimeBornPeriod = new JMenu("Generation period");

        carGenPeriodTextField = new JTextField();
        carGenPeriodTextField.setText(String.valueOf(N1));
        carGenPeriodTextField.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                setFocusable(true);
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                setFocusable(true);
            }
        });

        carGenPeriodTextField.addActionListener(action->
        {
            if (!carGenPeriodTextField.getText().isEmpty()) {
                try {
                    controller.setN1(Integer.parseInt(carGenPeriodTextField.getText()));
                } catch (NumberFormatException a) {
                    carGenPeriodTextField.setText(String.valueOf(N1));
                    JOptionPane.showMessageDialog(null,
                            "You have set wrong parameters!",
                            "Warning: wrong parameters",
                            JOptionPane.WARNING_MESSAGE);
                }
            } else {
                carGenPeriodTextField.setText(String.valueOf(N1));
                JOptionPane.showMessageDialog(null,
                        "You have set wrong parameters!",
                        "Warning: wrong parameters",
                        JOptionPane.WARNING_MESSAGE);
            }
        });

        carTimeBornPeriod.add(carGenPeriodTextField);

        /////

        bikeGenPeriodTextField = new JTextField();
        bikeGenPeriodTextField.setText(String.valueOf(N2));
        bikeGenPeriodTextField.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                setFocusable(true);
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                setFocusable(true);
            }
        });

        bikeGenPeriodTextField.addActionListener(action->
        {
            if (!bikeGenPeriodTextField.getText().isEmpty()) {
                try {
                    controller.setN2(Integer.parseInt(bikeGenPeriodTextField.getText()));
                } catch (NumberFormatException a) {
                    bikeGenPeriodTextField.setText(String.valueOf(N2));
                    JOptionPane.showMessageDialog(null,
                            "You have set wrong parameters!",
                            "Warning: wrong parameters",
                            JOptionPane.WARNING_MESSAGE);
                }
            } else {
                bikeGenPeriodTextField.setText(String.valueOf(N2));
                JOptionPane.showMessageDialog(null,
                        "You have set wrong parameters!",
                        "Warning: wrong parameters",
                        JOptionPane.WARNING_MESSAGE);
            }
        });
        bikeTimeBornPeriod.add(bikeGenPeriodTextField);

        ///

        ButtonGroup carButtonGroup = new ButtonGroup();
        ButtonGroup bikeButtonGroup = new ButtonGroup();

        for (int i = 0; i <= 100; i += 10) {
            JCheckBoxMenuItem carBoxMenuItem = new JCheckBoxMenuItem(String.valueOf(i));
            JCheckBoxMenuItem bikeBoxMenuItem = new JCheckBoxMenuItem(String.valueOf(i));
            if (i == P1) {
                carBoxMenuItem.setSelected(true);
            }
            if (i == P2) {
                bikeBoxMenuItem.setSelected(true);
            }
            bikeBoxMenuItem.addActionListener(actionEvent -> {
                controller.setP2(Integer.parseInt(bikeBoxMenuItem.getText()));
            });
            carBoxMenuItem.addActionListener(actionEvent -> {
                controller.setP1(Integer.parseInt(carBoxMenuItem.getText()));
            });
            bikeButtonGroup.add(bikeBoxMenuItem);
            carButtonGroup.add(carBoxMenuItem);
            bikeProbability.add(bikeBoxMenuItem);
            carProbability.add(carBoxMenuItem);
        }

        carMenu.add(carProbability);
        carMenu.add(carTimeBornPeriod);
        bikeMenu.add(bikeProbability);
        bikeMenu.add(bikeTimeBornPeriod);
        transportMenu.add(carMenu);
        transportMenu.add(bikeMenu);
        menuBar.add(transportMenu);

        setJMenuBar(menuBar);
    }

    public void turnTimeLabelOn() {
        timeLabel.setVisible(true);
        timeOnRadioButton.setSelected(false);
        timeOffRadioButton.setSelected(true);
    }

    public void turnTimeLabelOff() {
        timeLabel.setVisible(false);
        timeOnRadioButton.setSelected(true);
        timeOffRadioButton.setSelected(false);
    }

    public void setStopButtonProcessInMenu() {
        stopItem.setEnabled(false);
        startItem.setEnabled(true);
    }

    public void setStartButtonProcessInMenu() {
        stopItem.setEnabled(true);
        startItem.setEnabled(false);
    }

    public void switchDialogRadioButtonState() {
        showDialogRadioButton.setSelected(!showDialogRadioButton.isSelected());
    }

    public void setN1(int N1) {
        carGenPeriodTextField.setText(String.valueOf(N1));
    }

    public void setN2(int N2) {
        bikeGenPeriodTextField.setText(String.valueOf(N2));
    }

    public void setP1(int P1)
    {
        for (int i = 0; i <= 100; i += 10) {
            JCheckBoxMenuItem carBoxMenuItem = new JCheckBoxMenuItem(String.valueOf(i));
            if (i == P1) {
                carBoxMenuItem.setSelected(true);
            } else {
                carBoxMenuItem.setSelected(false);
            }
            carBoxMenuItem.addActionListener(actionEvent -> {
                controller.setP1(Integer.parseInt(carBoxMenuItem.getText()));
            });
            carButtonGroup.add(carBoxMenuItem);
            carProbability.add(carBoxMenuItem);
        }
    }

    public void setP2(int P2)
    {
        for (int i = 0; i <= 100; i += 10) {
            JCheckBoxMenuItem bikeBoxMenuItem = new JCheckBoxMenuItem(String.valueOf(i));
            if (i == P2) {
                bikeBoxMenuItem.setSelected(true);
            } else {
                bikeBoxMenuItem.setSelected(false);
            }
            bikeBoxMenuItem.addActionListener(actionEvent -> {
                controller.setP2(Integer.parseInt(bikeBoxMenuItem.getText()));
            });
            bikeButtonGroup.add(bikeBoxMenuItem);
            bikeProbability.add(bikeBoxMenuItem);
        }
    }
}
