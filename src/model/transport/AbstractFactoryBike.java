package model.transport;

public class AbstractFactoryBike implements AbstractFactory
{
    @Override
    public Transport transportBorn(int X, int Y, String path, int birthTime, int deathTime)
    {
        return new Bike(X, Y, path, birthTime, deathTime);
    }
}
