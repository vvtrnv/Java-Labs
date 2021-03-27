package bornProcess;

// Данный класс предназначен для учёта времени

import model.transport.habitat.Habitat;

import java.util.TimerTask;

public class BornProcess extends TimerTask
{
    Habitat habitat;
    int sec;
    int gameSec;
    int min;

    public BornProcess(Habitat h)
    {
        this.habitat = h;
    }

    public BornProcess(Habitat h, int time) {
        this.habitat = h;
        this.gameSec = time;
    }

    @Override
    public void run()
    {
        sec++;
        gameSec++;
        if(sec % 60 == 0)
        {
            min ++;
            sec = 0;
        }
        habitat.update(gameSec);
    }
}
