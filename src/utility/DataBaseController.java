package utility;

import model.TransportStorage;
import model.transport.Bike;
import model.transport.Car;
import model.transport.Transport;
import model.transport.habitat.Habitat;

import java.security.PublicKey;
import java.sql.*;
import java.util.ArrayList;

public class DataBaseController
{
    private static final String DB_USERNAME = "postgres";
    private static final String DB_PASSWORD = "swiftiks735";
    private static final String DB_URL = "jdbc:postgresql://localhost:5432/carsAndMotos";

    public static void saveConfigToDataBase() throws SQLException
    {
        Connection connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
        Statement statement = connection.createStatement();
        String sqlClear = "truncate Config;";
        statement.executeUpdate(sqlClear);

        String sql = "insert into Config (N1, P1, N2, P2, D1, D2) VALUES (?,?,?,?,?,?)";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, Habitat.getN1());
        preparedStatement.setInt(2, Habitat.getP1());
        preparedStatement.setInt(3, Habitat.getN2());
        preparedStatement.setInt(4, Habitat.getP2());
        preparedStatement.setInt(5, Habitat.getD1());
        preparedStatement.setInt(6, Habitat.getD2());

        preparedStatement.executeUpdate();
        connection.close();
    }

    // Скачать конфигурацию из базы данных
    public static ArrayList<Integer> loadConfigFromDataBase() throws SQLException
    {
        ArrayList<Integer> params = new ArrayList<>();
        Connection connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
        Statement statement = connection.createStatement();

        String sql = "select * from Config;";
        ResultSet resultSet = statement.executeQuery(sql);
        while(resultSet.next())
        {
            params.add(resultSet.getInt("N1"));
            params.add(resultSet.getInt("P1"));
            params.add(resultSet.getInt("N2"));
            params.add(resultSet.getInt("P2"));
            params.add(resultSet.getInt("D1"));
            params.add(resultSet.getInt("D2"));
        }
        connection.close();
        return params;
    }

    // Сохранить транспорты в базу данных
    public static void saveTransportsToBD() throws SQLException
    {
        Connection connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
        Statement statement = connection.createStatement();

        String sqlClear = "truncate Road;";
        statement.executeUpdate(sqlClear);

        ArrayList<Transport> transportArrayList = TransportStorage.getInstance().getTransportsList();
        PreparedStatement preparedStatement;
        String sql;

        for(int i = 0; i < transportArrayList.size(); i++)
        {
            sql = "insert into Road (name, birthTime, deathTime, uuid, x, y, route) " +
                    "values (?,?,?,?,?,?,?);"; //запрос
            preparedStatement = connection.prepareStatement(sql);

            if(transportArrayList.get(i) instanceof Car)
            {
                preparedStatement.setString(1, "Car");
            }
            else
            {
                preparedStatement.setString(1, "Bike");
            }
            preparedStatement.setInt(2, transportArrayList.get(i).getBirthTime());
            preparedStatement.setInt(3, transportArrayList.get(i).getDeathTime());
            preparedStatement.setString(4, transportArrayList.get(i).getUuid());
            preparedStatement.setInt(5, transportArrayList.get(i).getX());
            preparedStatement.setInt(6, transportArrayList.get(i).getY());
            preparedStatement.setInt(7, transportArrayList.get(i).getRoute());
            preparedStatement.executeUpdate();
        }
        connection.close();
    }

    public static void loadTransportsFromBD() throws SQLException
    {
        Connection connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
        Statement statement = connection.createStatement();

        ArrayList<Transport> transportArrayList = new ArrayList<>();

        String sql = "select * from Road;";
        ResultSet resultSet = statement.executeQuery(sql);
        while(resultSet.next())
        {
            //Car car;
            int _birthTime =  resultSet.getInt("birthTime");
            int _deathTime = resultSet.getInt("deathTime");
            String _UUID = resultSet.getString("uuid");
            int _x = resultSet.getInt("x");
            int _y = resultSet.getInt("y");
            int _route = resultSet.getInt("route");

            if("Car".equals(resultSet.getString("name")))
            {
                Car carAdd = new Car(_x, _y, "path", _birthTime, _deathTime);
                carAdd.setX(_x);
                carAdd.setY(_y);
                carAdd.setRoute(_route);
                carAdd.setIMG();
                transportArrayList.add(carAdd);
            }
            else
            {
                Bike bikeAdd = new Bike(_x, _y, "path", _birthTime, _deathTime);
                bikeAdd.setX(_x);
                bikeAdd.setY(_y);
                bikeAdd.setRoute(_route);
                bikeAdd.setIMG();
                transportArrayList.add(bikeAdd);
            }
            TransportStorage.getInstance().reset(); // обнуляем счётчики
            TransportStorage.getInstance().setAllTransports(transportArrayList);
            ArrayList<Transport> transportsList = TransportStorage.getInstance().getTransportsList();
            if(!transportsList.isEmpty())
            {
                for(int i = 0; i < transportsList.size(); i++)
                {
                    Transport transport = transportsList.get(i);
                    int deathTime = transport.getDeathTime() - transport.getBirthTime();
                    transport.setBirthTime(Habitat.getGameTime());
                    transport.setDeathTime(transport.getBirthTime() + deathTime);
                }
            }
        }
        connection.close();
    }
}


