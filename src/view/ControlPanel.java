package view;

import controller.Controller;
import utility.Console;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Arrays;

public class ControlPanel extends JPanel
{
    // Панели
    private JPanel buttonsPanel;
    private JPanel timePanel;
    private JPanel carPanel;
    private JPanel bikePanel;
    private JPanel serializerPanel;

    // Кнопки
    private JButton startButton = new JButton("Start");
    private JButton stopButton = new JButton("Stop");
    private JButton aliveTransportButton = new JButton("Show transports list");
    private JButton reduceBike = new JButton("Reduce Bike");
    private JButton loadSimulation = new JButton("Load simulation");
    private JButton saveSimulation = new JButton("Save simulation");

    // Переключатели
    private JRadioButton timeOnRadioButton;
    private JRadioButton timeOffRadioButton;
    private JRadioButton dialogRadioButton;

    private JRadioButton carAIRadioButtonOn;
    private JRadioButton carAIRadioButtonOff;

    private JRadioButton bikeAIRadioButtonOn;
    private JRadioButton bikeAIRadioButtonOff;

    // Текстовые
    private JTextField carGenPeriodTextField;
    private JTextField bikeGenPeriodTextField;

    private JTextField carDeathTimeTextField;
    private JTextField bikeDeathTimeTextField;

    // ComboBox
    private JComboBox<Integer> carProbabilityComboBox;
    private JComboBox<Integer> bikeProbabilityComboBox;

    private JComboBox<Integer> carPriorityComboBox;
    private JComboBox<Integer> bikePriorityComboBox;

    // Массив вероятностей для ComboBox
    final private Integer[] probabilitiesArray =
            {
                    0, 10, 20, 30, 40, 50,
                    60, 70, 80, 90, 100
            };

    final private Integer[] prioritiesArray =
            {
                    1, 2, 3, 4, 5,
                    6, 7, 8, 9, 10
            };

    // Контроллер и окно
    Controller controller;
    private JFrame frame;

    /* Методы */

    ControlPanel(int N1, int N2, int P1, int P2, int D1, int D2, JFrame frame)
    {
        super();
        this.frame = frame;
        setLayout(new GridLayout(5, 1));
        setBorder(this, "CONTROL PANEL");

        configureButtonsPanel();
        configureTimePanel();
        configureCarPanel(N1,P1, D1);
        configureBikePanel(N2, P2, D2);
        reduceTransportsPanel();
    }

    private void reduceTransportsPanel()
    {
        serializerPanel = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        serializerPanel.setPreferredSize((new Dimension(200,100)));
        setBorder(serializerPanel, "Configuraion");

        c.gridx = 0; c.gridy = 0; c.ipadx = 80; c.ipady = 30;
        serializerPanel.add(reduceBike, c);
        reduceBike.addActionListener(action->
        {
            Console console = new Console(frame, controller);
            console.showConsole();
        });


        c.gridx = 1; c.gridy = 0; c.ipadx = 50;
        serializerPanel.add(loadSimulation, c);
        loadSimulation.addActionListener(actionEvent-> controller.deserializeSimulation());

        c.gridx = 1; c.gridy = 1; c.ipadx = 50;
        serializerPanel.add(saveSimulation, c);
        saveSimulation.addActionListener(actionEvent-> controller.serializeSimulation());

        add(serializerPanel);
    }

    // Установка названия панели
    private void setBorder(JPanel panel, String text)
    {
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder(
                       BorderFactory.createEtchedBorder(EtchedBorder.LOWERED),
                       text, TitledBorder.RIGHT, TitledBorder.TOP),
                BorderFactory.createEmptyBorder(5,5,5,5)));
    }

    // Панель с кнопками. Настройка
    private void configureButtonsPanel()
    {
        buttonsPanel = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();

        buttonsPanel.setPreferredSize((new Dimension(200,100)));
        //buttonsPanel.setBackground(Color.decode("E6E6FA"));

        startButton.setEnabled(true);
        stopButton.setEnabled(false);
        startButton.setFocusable(false);
        stopButton.setFocusable(false);
        aliveTransportButton.setFocusable(false);

        // Расположение кнопки Start
        c.gridx = 0; c.gridy = 0;
        c.ipadx = 50;
        buttonsPanel.add(startButton, c);

        // Расположение кнопки Stop
        c.gridx = 0; c.gridy = 1;
        c.ipadx = 50;
        buttonsPanel.add(stopButton, c);

        // Расположение кнопки Show Transports
        c.gridx = 0;
        c.gridy = 2;
        c.ipadx = 50;
        buttonsPanel.add(aliveTransportButton, c);

        setBorder(buttonsPanel, "SIMULATION CONTROL");

        buttonsPanel.setVisible(true);
        add(buttonsPanel);

        startButton.addActionListener(listener->
        {
            startButton.setEnabled(false);
            stopButton.setEnabled(true);
            controller.startBornProcess();
        });

        stopButton.addActionListener(listener->
        {
            startButton.setEnabled(true);
            stopButton.setEnabled(false);

            if(isInfoDialogEnabled())
            {
                controller.pauseBornProcess();
                if(!controller.showInfoDialog())
                {
                    startButton.setEnabled(false);
                    stopButton.setEnabled(true);
                    controller.resumeBornProcess();
                }
                else
                {
                    controller.stopBornProcess();
                    controller.refreshTransports();
                }
            }
            else
            {
                controller.stopBornProcess();
                controller.refreshTransports();
            }
        });

        aliveTransportButton.addActionListener(actionEvent->
        {
            controller.showTransportsList();
        });
    }

    // Панель с переключателем отоборажения времени выполнения симуляции
    private void configureTimePanel()
    {
        timePanel = new JPanel(new GridLayout(3,1));
        setBorder(timePanel, "SIMULATION INFO");

        // Включить отображение времени
        timeOnRadioButton = new JRadioButton("Time label ON", true);
        timeOnRadioButton.addActionListener(action->
        {
            controller.turnTimeLabelOn();
        });
        timeOnRadioButton.setFocusable(false);
        timeOnRadioButton.setFocusPainted(false);

        // Выключить отображение времени
        timeOffRadioButton = new JRadioButton("Time label OFF", false);
        timeOffRadioButton.addActionListener(action->
        {
            controller.turnTimeLabelOff();
        });
        timeOffRadioButton.setFocusable(false);
        timeOffRadioButton.setFocusPainted(false);

        // Отобразить информацию о симуляции
        dialogRadioButton = new JRadioButton("Show info", true);
        timeOffRadioButton.setFocusable(false);
        timeOffRadioButton.setFocusPainted(false);

        // Объеденяем кнопки отображения времени в группу
        ButtonGroup timeRadioButtonsGroup = new ButtonGroup();
        timeRadioButtonsGroup.add(timeOnRadioButton);
        timeRadioButtonsGroup.add(timeOffRadioButton);

        timePanel.add(timeOnRadioButton);
        timePanel.add(timeOffRadioButton);
        timePanel.add(dialogRadioButton);

        timePanel.setVisible(true);
        timePanel.setFocusable(false);
        add(timePanel);
    }

    // Панель настройки поведения объектов машин
    private void configureCarPanel(int N1, int P1, int D1)
    {
        carPanel = new JPanel(new GridBagLayout());
        setBorder(carPanel, "CAR");

        // Переключатель вероятностей появления машин
        GridBagConstraints c = new GridBagConstraints();
        carProbabilityComboBox = new JComboBox<>(probabilitiesArray);
        carProbabilityComboBox.setSelectedIndex(Arrays.asList(probabilitiesArray).indexOf(P1));
        carProbabilityComboBox.setFocusable(false);
        carProbabilityComboBox.addActionListener(actionEvent->
        {
            controller.setP1(carProbabilityComboBox.getItemAt(carProbabilityComboBox.getSelectedIndex()));
        });
        c.gridx = 1; c.gridy = 0;
        carPanel.add(carProbabilityComboBox, c);

        carPriorityComboBox = new JComboBox<>(prioritiesArray);
        carPriorityComboBox.setSelectedIndex(Arrays.asList(prioritiesArray).indexOf(5));
        carPriorityComboBox.setFocusable(false);
        carPriorityComboBox.addActionListener(actionEvent ->
        {
            controller.changeCarPriority(carPriorityComboBox.
                    getItemAt(carPriorityComboBox.getSelectedIndex()));
        });
        c.gridx = 1;
        c.gridy = 4;
        carPanel.add(carPriorityComboBox, c);

        // Текстовое поле со временем
        carGenPeriodTextField = new JTextField();
        carGenPeriodTextField.setText(String.valueOf(N1));
        c.gridx = 1; c.gridy = 1;
        c.ipadx = 75;

        // Поставить в фокус при нажатии на текстовое поле
        carGenPeriodTextField.addMouseListener(new MouseAdapter()
        {
            @Override
            public void mouseClicked(MouseEvent e)
            {
                setFocusable(true);
            }

            @Override
            public void mouseEntered(MouseEvent e)
            {
                setFocusable(true);
            }
        });

        // Обработка введеного времени появления
        carGenPeriodTextField.addActionListener(action->
        {
            if(!carGenPeriodTextField.getText().isEmpty())
            {
                try
                {
                    controller.setN1(Integer.parseInt(carGenPeriodTextField.getText()));
                }catch (NumberFormatException a)
                {
                    carGenPeriodTextField.setText(String.valueOf(N1));
                    JOptionPane.showMessageDialog(null,
                            "You have set wrong parameters :(",
                            "Warning: wrong parameters",
                            JOptionPane.WARNING_MESSAGE);
                }
            }
            else
            {
                carGenPeriodTextField.setText(String.valueOf(N1));
                JOptionPane.showMessageDialog(null,
                        "You have set wrong parameters :(",
                        "Warning: wrong parameters",
                        JOptionPane.WARNING_MESSAGE);
            }
        });
        carPanel.add(carGenPeriodTextField, c);

        // Текстовое поле с временем жизни
        carDeathTimeTextField = new JTextField();
        carDeathTimeTextField.setText(String.valueOf(D1));
        c.gridx = 1;
        c.gridy = 2;
        c.ipadx = 75;

        // Поставить в фокус при нажатии на текстовое поле
        carDeathTimeTextField.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                setFocusable(true);
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                setFocusable(true);
            }
        });

        // Обработка введенного времени жизни
        carDeathTimeTextField.addActionListener(act->
        {
            if(!carDeathTimeTextField.getText().isEmpty())
            {
                try
                {
                    controller.setD1(Integer.parseInt(carDeathTimeTextField.getText()));
                }
                catch(NumberFormatException b)
                {
                    carDeathTimeTextField.setText(String.valueOf(D1));
                    JOptionPane.showMessageDialog(null,
                            "You have set wrong parameters :(",
                            "Warning: wrong parameters",
                            JOptionPane.WARNING_MESSAGE);
                }
            }
        });
        carPanel.add(carDeathTimeTextField, c);

        // JRadioButton для включения AI Mode
        carAIRadioButtonOn = new JRadioButton("AI Mode ON", true);
        carAIRadioButtonOn.addActionListener(action->
        {
            carAIRadioButtonOn.setSelected(true);
            carAIRadioButtonOff.setSelected(false);
            controller.turnCarAIOn();
        });
        carAIRadioButtonOn.setFocusable(false);
        carAIRadioButtonOn.setFocusPainted(false);
        c.gridx = 0;
        c.gridy = 3;
        carPanel.add(carAIRadioButtonOn, c);

        // JRadioButton для выключения AI Mode
        carAIRadioButtonOff = new JRadioButton("AI Mode OFF", true);
        carAIRadioButtonOff.setSelected(false);
        carAIRadioButtonOff.addActionListener(action->
        {
            carAIRadioButtonOff.setSelected(true);
            carAIRadioButtonOn.setSelected(false);
            controller.turnCarAIOff();
        });
        carAIRadioButtonOff.setFocusable(false);
        carAIRadioButtonOff.setFocusPainted(false);
        c.gridx = 1;
        c.gridy = 3;
        carPanel.add(carAIRadioButtonOff, c);

        // Добавление JLabel
        JLabel probabilityLabel = new JLabel("Probability (%):");
        c.gridx = 0; c.gridy = 0;
        carPanel.add(probabilityLabel, c);

        JLabel periodLabel = new JLabel("Generation period (sec):");
        c.gridx = 0; c.gridy = 1;
        carPanel.add(periodLabel, c);

        JLabel deathLabel = new JLabel("Death time (sec): ");
        c.gridx = 0; c.gridy = 2;
        carPanel.add(deathLabel, c);

        JLabel priorityLabel = new JLabel("Priority: ");
        c.gridx = 0;
        c.gridy = 4;
        carPanel.add(priorityLabel, c);

        carPanel.setVisible(true);
        carPanel.setFocusable(false);

        add(carPanel);
    }

    private void configureBikePanel(int N2, int P2, int D2)
    {
        bikePanel = new JPanel(new GridBagLayout());
        setBorder(bikePanel, "BIKE");

        // Добавляем переключатель вероятностей появления мотоциклов
        GridBagConstraints c = new GridBagConstraints();
        bikeProbabilityComboBox = new JComboBox<>(probabilitiesArray);
        bikeProbabilityComboBox.setSelectedIndex(Arrays.asList(probabilitiesArray).indexOf(P2));
        bikeProbabilityComboBox.setFocusable(false);
        bikeProbabilityComboBox.addActionListener(actionEvent->
        {
            controller.setP2(bikeProbabilityComboBox.getItemAt(bikeProbabilityComboBox.getSelectedIndex()));
        });
        c.gridx = 1; c.gridy = 0;
        bikePanel.add(bikeProbabilityComboBox, c);

        // ComboBox для выбора приоритета
        bikePriorityComboBox = new JComboBox<>(prioritiesArray);
        bikePriorityComboBox.setSelectedIndex(Arrays.asList(prioritiesArray).indexOf(4));
        bikePriorityComboBox.setFocusable(false);
        bikePriorityComboBox.addActionListener(actionEvent ->
        {
            controller.changeBikePriority(bikePriorityComboBox.
                    getItemAt(bikePriorityComboBox.getSelectedIndex()));
        });
        c.gridx = 1;
        c.gridy = 4;
        bikePanel.add(bikePriorityComboBox, c);

        // Добавляем текстовое поле для ввода промежутка появления мотоциклов
        bikeGenPeriodTextField = new JTextField();
        bikeGenPeriodTextField.setText(String.valueOf(N2));
        c.gridx = 1;
        c.gridy = 1;
        c.ipadx = 75;

        bikeGenPeriodTextField.addMouseListener(new MouseAdapter()
        {
            @Override
            public void mouseClicked(MouseEvent e)
            {
                setFocusable(true);
            }

            @Override
            public void mouseEntered(MouseEvent e)
            {
                setFocusable(true);
            }
        });

        bikeGenPeriodTextField.addActionListener(action->
        {
            if(!bikeGenPeriodTextField.getText().isEmpty())
            {
                try
                {
                    controller.setN2(Integer.parseInt(bikeGenPeriodTextField.getText()));
                }catch (NumberFormatException a)
                {
                    bikeGenPeriodTextField.setText(String.valueOf(N2));
                    JOptionPane.showMessageDialog(null,
                            "You have set wrong parameters :(",
                            "Warning: wrong parameters",
                            JOptionPane.WARNING_MESSAGE);
                }
            }
            else
            {
                bikeGenPeriodTextField.setText(String.valueOf(N2));
                JOptionPane.showMessageDialog(null,
                        "You have set wrong parameters :(",
                        "Warning: wrong parameters",
                        JOptionPane.WARNING_MESSAGE);
            }
        });
        bikePanel.add(bikeGenPeriodTextField, c);

        // Текстовое поле со временем жизни
        bikeDeathTimeTextField = new JTextField();
        bikeDeathTimeTextField.setText(String.valueOf(D2));
        c.gridx = 1;
        c.gridy = 2;
        c.ipadx = 75;

        // Делаем фокус при нажатии на поле
        bikeDeathTimeTextField.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                setFocusable(true);
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                setFocusable(true);
            }
        });

        // Обработка введенных данных
        bikeDeathTimeTextField.addActionListener(act->
        {
            if(!bikeDeathTimeTextField.getText().isEmpty())
            {
                try
                {
                    controller.setD2(Integer.parseInt(bikeDeathTimeTextField.getText()));
                }
                catch (NumberFormatException e)
                {
                    bikeDeathTimeTextField.setText(String.valueOf(D2));
                    JOptionPane.showMessageDialog(null,
                            "You have set wrong parameters :(",
                            "Warning: wrong parameters",
                            JOptionPane.WARNING_MESSAGE);
                }
            }
        });
        bikePanel.add(bikeDeathTimeTextField, c);

        // JRadioButton для включения AI Mode
        bikeAIRadioButtonOn = new JRadioButton("AI Mode ON", true);
        bikeAIRadioButtonOn.addActionListener(action->
        {
            bikeAIRadioButtonOn.setSelected(true);
            bikeAIRadioButtonOff.setSelected(false);
            controller.turnBikeAIOn();
        });
        bikeAIRadioButtonOn.setFocusable(false);
        bikeAIRadioButtonOn.setFocusPainted(false);
        c.gridx = 0;
        c.gridy = 3;
        bikePanel.add(bikeAIRadioButtonOn, c);

        // JRadioButton для выключения AI Mode
        bikeAIRadioButtonOff = new JRadioButton("AI Mode OFF", true);
        bikeAIRadioButtonOff.setSelected(false);
        bikeAIRadioButtonOff.addActionListener(action->
        {
            bikeAIRadioButtonOff.setSelected(true);
            bikeAIRadioButtonOn.setSelected(false);
            controller.turnBikeAIOff();
        });
        bikeAIRadioButtonOff.setFocusable(false);
        bikeAIRadioButtonOn.setFocusPainted(false);
        c.gridx = 1;
        c.gridy = 3;
        bikePanel.add(bikeAIRadioButtonOff, c);

        // Добавление JLabel
        JLabel probabilityLabel = new JLabel("Probability (%): ");
        c.gridx = 0; c.gridy = 0;
        bikePanel.add(probabilityLabel, c);

        JLabel periodLabel = new JLabel("Generation period (sec): ");
        c.gridx = 0; c.gridy = 1;
        bikePanel.add(periodLabel, c);

        JLabel deathLabel = new JLabel("Death time (sec): ");
        c.gridx =0; c.gridy = 2;
        bikePanel.add(deathLabel, c);

        JLabel priorityLabel = new JLabel("Priority: ");
        c.gridx = 0;
        c.gridy = 4;
        bikePanel.add(priorityLabel, c);

        bikePanel.setVisible(true);
        bikePanel.setFocusable(false);

        add(bikePanel);
    }

    public void configureController(Controller controller)
    {
        this.controller = controller;
    }

    public void setStartButtonEnabled()
    {
        startButton.setEnabled(true);
        stopButton.setEnabled(false);
    }

    public void setStopButtonEnabled()
    {
        startButton.setEnabled(false);
        stopButton.setEnabled(true);
    }

    public void switchTimeRadioGroupState()
    {
        boolean previousOnState = timeOnRadioButton.isSelected();
        boolean previousOffState = timeOffRadioButton.isSelected();
        timeOnRadioButton.setSelected(!previousOnState);
        timeOffRadioButton.setSelected(!previousOffState);
    }


    public void switchTimeRadioGroupStateOff()
    {
        timeOnRadioButton.setSelected(false);
        timeOffRadioButton.setSelected(true);
    }

    public void switchTimeRadioGroupStateOn()
    {
        timeOnRadioButton.setSelected(true);
        timeOffRadioButton.setSelected(false);
    }

    public void switchInfoRadioGroupState()
    {
        dialogRadioButton.setSelected(!dialogRadioButton.isSelected());
    }

    public boolean isInfoDialogEnabled()
    {
        return dialogRadioButton.isSelected();
    }

    public void setN1(int N1)
    {
        carGenPeriodTextField.setText(String.valueOf(N1));
    }

    public void setN2(int N2)
    {
        bikeGenPeriodTextField.setText(String.valueOf(N2));
    }

    public void setP1(int P1)
    {
        carProbabilityComboBox.setSelectedIndex(Arrays.asList(probabilitiesArray).indexOf(P1));
    }

    public void setP2(int P2)
    {
        bikeProbabilityComboBox.setSelectedIndex(Arrays.asList(probabilitiesArray).indexOf(P2));
    }

    public void setD1(int D1) {
        carDeathTimeTextField.setText(String.valueOf(D1));
    }

    public void setD2(int D2) {
        bikeDeathTimeTextField.setText(String.valueOf(D2));
    }
}


