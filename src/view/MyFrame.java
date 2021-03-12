package view;

import controller.Controller;
import model.transport.habitat.Habitat;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.security.Key;

// Окно JFrame
public class MyFrame extends JFrame implements KeyListener
{
    JLabel timeLabel;
    int time;

    Habitat habitat;
    Controller controller;
    MyField myField;

    public MyFrame()
    {
        habitat = new Habitat(1, 2, 100, 50, this);
        myField = new MyField();

        // Привяжем контроллер
        controller = new Controller(myField, habitat, this);
        habitat.configureController(controller);
        myField.configureController(controller);

        // Задаём параметры окна
        setTitle("Transports");
        setPreferredSize(new Dimension(habitat.SIZEWINDOW, habitat.SIZEWINDOW));
        setLayout(new BorderLayout());
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        // Задаём параметры поля
        myField.setPreferredSize(new Dimension(habitat.SIZEWINDOW, habitat.SIZEWINDOW));
        add(myField);
        addKeyListener(this);

        // Задаём параметры JLabel для поля времени
        timeLabel = new JLabel("",SwingConstants.CENTER);
        add(timeLabel,BorderLayout.SOUTH);

        // Расположение окна, его видимость и т.д
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
        setResizable(true);
    }

    // Обновить таймер на экране
    public void updateTime(int time)
    {
        this.time = time;
        timeLabel.setText(time / 60 + " minutes " + time % 60 + " seconds");
    }

    // Привязка клавиш к старт/финиш/показ таймера
    @Override
    public void keyTyped(KeyEvent keyEvent)
    {
        switch (keyEvent.getKeyCode())
        {
            case KeyEvent.VK_B:
                if(!controller.isBornProcessOn())
                {
                    myField = new MyField();
                    myField.configureController(controller);
                    controller.startBornProcess();
                }
                break;

            case KeyEvent.VK_E:
                if(controller.isBornProcessOn())
                {
                    controller.stopBornProcess();
                    showFinishDialog();
                    controller.refreshTransports();
                }
                break;

            case KeyEvent.VK_T:
                timeLabel.setVisible(!timeLabel.isVisible());
                break;
        }
    }

    public void configureController(Controller controller)
    {
        this.controller = controller;
    }

    // Вывод в диалоговом окне результат работы.
    private void showFinishDialog()
    {
        JDialog dialog = new JDialog(this, "Процесс закончен", true);
        JPanel panel = new JPanel(new GridLayout(5,1));

        JLabel messageLabel = createLabel("Процесс закончен. Результаты: ",
                SwingConstants.CENTER,
                new Font("Serif", Font.BOLD, 16),
                Color.BLACK);

        JLabel carsLabel = createLabel("Ordinary: " + controller.getCarsAmount(),
                SwingConstants.CENTER,
                new Font("Courier New", Font.ITALIC, 16),
                Color.RED);

        JLabel bikesLabel = createLabel("Albinos: " + controller.getBikesAmount(),
                SwingConstants.CENTER,
                new Font("Times New Roman", Font.BOLD, 16),
                Color.MAGENTA);

        JLabel allTransportsCount = createLabel("All rabbits: " + controller.getAllTransportsAmount(),
                SwingConstants.CENTER,
                new Font("Times New Roman", Font.ITALIC, 16),
                Color.ORANGE);

        JLabel timeLabel = createLabel("Passed time: " + time / 60 + " minutes " + time % 60 + " seconds",
                SwingConstants.CENTER,
                new Font("Arial", Font.PLAIN, 16),
                Color.BLUE);

        panel.add(messageLabel);
        panel.add(carsLabel);
        panel.add(bikesLabel);
        panel.add(allTransportsCount);
        panel.add(timeLabel);
        dialog.add(panel);

        dialog.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        dialog.setPreferredSize(new Dimension(300,300));
        dialog.setResizable(false);
        dialog.pack();
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    // Создание параметров для JLabel такие как текст, шрифт, цвет, позиция
    private JLabel createLabel(String text, int fontPosition, Font font, Color fontColor)
    {
        JLabel label = new JLabel(text, fontPosition);
        label.setFont(font);
        label.setForeground(fontColor);

        return label;
    }

    @Override
    public void keyPressed(KeyEvent keyEvent) { }
    @Override
    public void keyReleased(KeyEvent keyEvent) { }
}
