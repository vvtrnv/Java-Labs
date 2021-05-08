package model.transport.moveAI;


import java.util.Timer;

public abstract class BaseAI extends Thread
{
    protected Timer timer;
    protected boolean isActive = true;

    public synchronized void startAI() throws InterruptedException
    {
        isActive = true;
    }

    public synchronized void stopAI()
    {
        isActive = false;
        notify(); // освобождение монитора потока
    }

    public boolean isAIActive() {
        return isActive;
    }

    public void setAIPriority(int priority)
    {
        setPriority(priority);
    }

    public int getAIPriority()
    {
        return getPriority();
    }
}
