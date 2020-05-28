package nl.ictm2a4.javagame.event;

public abstract class EventExecutor {

    /**
     * Execute a method in the listener associated with the Event
     * @param listener Listener class to execute the method in
     * @param event Event to execute
     */
    public abstract void execute(Listener listener, Event event);
}
