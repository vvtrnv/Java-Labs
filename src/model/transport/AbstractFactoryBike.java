package model.transport;

public class AbstractFactoryBike implements AbstractFactory
{
    @Override
    public Transport transportBorn(int X, int Y, String path)
    {
        return new Bike(X, Y, path);
    }
}
