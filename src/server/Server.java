package server;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.Random;

public class Server extends JFrame implements TCPConnectionListener
{
    public static void main(String[] args)
    {
        SwingUtilities.invokeLater(()-> new Server());
    }

    boolean flag;

    private int WIDTH = 400;
    private int HEIGHT = 300;

    private JTextArea jTextArea;
    private JButton on;
    private JButton off;

    private static final ArrayList<TCPConnection> connections = new ArrayList<>();
    private static final Random random = new Random();

    private Server()
    {
        super("Server");
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        this.setBounds(dim.width / 2 - WIDTH / 2, dim.height / 2 - HEIGHT / 2, WIDTH, HEIGHT);
        this.setLayout(new FlowLayout());
        this.setResizable(false);

        // Настройка текстового поля
        jTextArea = new JTextArea();
        jTextArea.setEditable(false);
        jTextArea.setFocusable(false);
        jTextArea.setBackground(Color.decode("#C0C0C0"));
        jTextArea.setForeground(Color.WHITE);
        jTextArea.setTabSize(3);

        JScrollPane jScrollPane = new JScrollPane(jTextArea);
        jScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        jScrollPane.setPreferredSize(new Dimension(370, 220));

        // Настройка кнопок
        on = new JButton("ON");
        on.setFocusable(false);
        on.setFocusPainted(false);

        off = new JButton("OFF");
        off.setFocusable(false);
        off.setFocusPainted(false);
        off.setEnabled(false);

        // Обработка нажатия
        on.addActionListener(e->new Thread(()->
        {
            flag = true;
            on.setEnabled(false);
            off.setEnabled(true);

            jTextArea.append("Запуск сервера...\n");
            try( ServerSocket serverSocket = new ServerSocket(TCPConnection.PORT) )
            {
                serverSocket.setSoTimeout(100000);

                while(flag)
                {
                    try
                    {
                        new TCPConnection(Server.this, serverSocket.accept());
                    }
                    catch(IOException exc)
                    {
                        jTextArea.append("TCPConnection exception: " + e + "\n");
                        flag = false;
                        on.setEnabled(false);
                        off.setEnabled(true);
                    }
                }
            }
            catch (IOException ex)
            {
                throw new RuntimeException(ex);
            }
            jTextArea.append("Сервер недоступен...\n");
        }).start());

        off.addActionListener(e->
        {
            flag = false;
            off.setEnabled(false);
            jTextArea.append("Сервер недоступен...");
        });

        // Добавляем в окно
        add(jScrollPane);
        add(on);
        add(off);

        setVisible(true);
    }

    @Override
    public void onConnectionReady(TCPConnection tcpConnection)
    {
        connections.add(tcpConnection);
        update();
        jTextArea.append("Клиент подключается: " + tcpConnection + "\n");
    }

    @Override
    public void onReceiveObject(TCPConnection tcpConnection, Object object)
    {
        if(object instanceof File)
        {
            if(connections.size() > 1)
            {
                int user = -1;
                do {
                    user = random.nextInt(connections.size());
                }while (connections.get(user).equals(tcpConnection));
                connections.get(user).sendObject(object);
            }
        }
    }

    @Override
    public void onDisconnect(TCPConnection tcpConnection)
    {
        connections.remove(tcpConnection);
        update();
        jTextArea.append("Клиент отключился: " + tcpConnection + "\n");
        jTextArea.append("Текущие подключения: " + connections.size() + "\n");
    }

    @Override
    public void onException(TCPConnection tcpConnection, Exception e)
    {
        jTextArea.append("TCPConnection exception: " + e + "\n");
    }

    private void update()
    {
        final ArrayList<String> cons = new ArrayList<>(connections.size());
        for(TCPConnection tCon : connections)
            cons.add(tCon.toString());

        sendToAllConnections(new TCPConnection.BoxUsers(cons));
    }

    private void sendToAllConnections(Object object)
    {
        for(TCPConnection tCon : connections)
            tCon.sendObject(object);
    }

}

