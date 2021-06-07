package model.transport;

import model.IBehaviour;

import java.awt.*;
import java.io.Serializable;
import java.util.UUID;

public abstract class Transport implements IBehaviour, Serializable
{
    private int x;
    private int y;
    public static int countAllTransports = 0;

    private String  uuid;
    private int birthTime;
    private int deathTime;

    private Image image;

    // Конструктор
    Transport(int X, int Y, Image img, int birthTime, int deathTime)
    {
        this.x = X;
        this.y = Y;
        this.image = img;
        this.birthTime = birthTime;
        this.deathTime = deathTime;
        this.uuid = UUID.randomUUID().toString();
    }

    public Image getImage() { return image; }

    public void setImage(Image img)
    {
        this.image = img;
    }

    public String getUuid() {
        return uuid;
    }

    public int getBirthTime() {
        return birthTime;
    }

    public int getDeathTime() {
        return deathTime;
    }

    public void setBirthTime(int birthTime)
    {
        this.birthTime = birthTime;
    }

    public void setDeathTime(int deathTime)
    {
        this.deathTime = deathTime;
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
