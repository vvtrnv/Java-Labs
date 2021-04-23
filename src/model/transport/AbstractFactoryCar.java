package model.transport;

public class AbstractFactoryCar implements AbstractFactory
{
    @Override
    public Transport transportBorn(int X, int Y, String path, int birthTime, int deathTime)
    {
        return new Car(X, Y, path, birthTime, deathTime);
    }
}
