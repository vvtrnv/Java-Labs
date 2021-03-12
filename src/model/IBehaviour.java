package model;

// Интерфейс программы
public interface IBehaviour
{
    // Координаты
    int getX();
    int getY();

    void setX(int X);
    void setY(int Y);

    // Для дальнейшей работы
    void move(int X, int Y);

}
