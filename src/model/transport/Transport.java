package model.transport;

import model.IBehaviour;

import java.util.UUID;

public abstract class Transport implements IBehaviour
{
    private int x;
    private int y;
    public static int countAllTransports = 0;
    private String pathToImg;

    private String  uuid;
    private int birthTime;
    private int deathTime;

    // Конструктор
    Transport(int X, int Y, String path, int birthTime, int deathTime)
    {
        this.x = X;
        this.y = Y;
        this.pathToImg = path;

        this.birthTime = birthTime;
        this.deathTime = deathTime;
        this.uuid = UUID.randomUUID().toString();
    }

    public String getPathToImg() { return pathToImg; }

    public String getUuid() {
        return uuid;
    }

    public int getBirthTime() {
        return birthTime;
    }

    public int getDeathTime() {
        return deathTime;
    }

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
