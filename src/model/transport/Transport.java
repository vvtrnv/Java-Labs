package model.transport;

import model.IBehaviour;

public abstract class Transport implements IBehaviour
{
    private int x;
    private int y;
    public static int countAllTransports = 0;
    private String pathToImg;

    // Конструктор
    Transport(int X, int Y, String path)
    {
        this.x = X;
        this.y = Y;
        this.pathToImg = path;
    }

    public String getPathToImg() { return pathToImg; }

    // Переопределение функций из интерфейса
    @Override
    public int getX() { return this.x; }
    @Override
    public int getY() { return this.y;}

    @Override
    public void setX(int X) { this.x = X; }
    @Override
    public void setY(int Y) { this.y = Y ; }

    @Override
    public void move(int X, int Y) { }
}
