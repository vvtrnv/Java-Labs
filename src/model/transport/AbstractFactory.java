package model.transport;

import model.transport.Transport;

/*
* Фабричный метод — это порождающий паттерн проектирования,
* который определяет общий интерфейс для создания объектов в суперклассе,
* позволяя подклассам изменять тип создаваемых объектов.
*/
public interface AbstractFactory
{
    Transport transportBorn(int X, int Y, String path, int birthTime, int deathTime);
}
