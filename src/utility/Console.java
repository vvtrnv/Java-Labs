package utility;

import controller.Controller;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Console extends JDialog
{
    private JTextArea textArea = new JTextArea();       // отображение текста команд
    private JTextField textField = new JTextField();    // поле ввода команд
    private String text =  "Здесь можно уменьшить кол-во мотоциклов\n" +
                           "Введите параметр N, где N - процент уменьшения.\n";
    private Controller controller;  // Контроллер

    public Console(JFrame owner, Controller controller)
    {
        super(owner, "Console", true);
        this.controller = controller;

        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setSize(500, 500);
        setLocationRelativeTo(null);
        setResizable(false);

        textArea.setEditable(false);
        textArea.setLineWrap(true);
        textArea.setText(text);

        setLayout(new FlowLayout(FlowLayout.CENTER));
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(490, 420));
        add(scrollPane);

        textField.setPreferredSize(new Dimension(495, 25));
        add(textField);
    }

    public void showConsole()
    {
        textField.setFocusable(true);
        textField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if(e.getKeyCode() == KeyEvent.VK_ENTER)
                {
                    text += textField.getText() + "\n";
                    textArea.setText(text);

                    if(textField.getText().contains("/reduce"))
                    {
                        String string = textField.getText();
                        Pattern pattern = Pattern.compile("\\d+");
                        Matcher matcher = pattern.matcher(string);

                        while(matcher.find())
                            controller.reduceBikeAmount(Integer.parseInt(matcher.group()));
                    }
                    else
                    {
                        text += "Неизвестная команда.\n" +
                                "Введите параметр N, где N - процент уменьшения.\n";
                    }
                    textField.setText(null);
                    textArea.setText(text);
                }
            }
        });
        setVisible(true);
    }
}
