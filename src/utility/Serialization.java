package utility;

import model.TransportStorage;
import model.transport.Transport;
import model.transport.habitat.Habitat;

import java.io.*;
import java.util.ArrayList;
import java.util.Vector;

public class Serialization // Складирование данных в файл
{
    private File file = new File("serialized.dat");

    public Serialization(){}

    public void serialize()
    {
        try (
                FileOutputStream fileOutputStream = new FileOutputStream(file);  // объект для записи байтов в файл
                ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream) //отвечает за вывод объяекта в поток
        ) {
            ArrayList<Transport> transportsList = TransportStorage.getInstance().getTransportsList();

            objectOutputStream.writeObject(transportsList);
        } catch (FileNotFoundException eFileNotFound) {
            System.err.println("Error: file serialized.dat not found while serializing.");
        } catch (IOException eIO) {
            System.err.println("Error: IOException while serializing");
        } catch (Exception ex) {
            System.err.println("Error: something happened while serializing");
        }
    }

    public void deserialize() {
        try (
                FileInputStream fileInputStream = new FileInputStream(file);
                ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream)
        ) {
            TransportStorage.getInstance().reset(); //обнуляем счетчики

            ArrayList<Transport> a = (ArrayList<Transport>)objectInputStream.readObject();
            TransportStorage.getInstance().setAllTransports(a);
            ArrayList<Transport> transportList = TransportStorage.getInstance().getTransportsList();

            if (!transportList.isEmpty()) {
                for (int i = 0; i < transportList.size(); i++) {
                    Transport transport = transportList.get(i);
                    int deathTime = transport.getDeathTime() - transport.getBirthTime();
                    transport.setBirthTime(Habitat.getGameTime());
                    transport.setDeathTime(transport.getBirthTime() + deathTime);
                }
            }
        } catch (FileNotFoundException eFileNotFound) {
            System.err.println("Error: file serialized.dat not found while serializing.");
        } catch (IOException eIO) {
            System.err.println("Error: IOException while serializing");
            eIO.printStackTrace();
        } catch (Exception ex) {
            System.err.println("Error: something happened while serializing");
        }
    }
}