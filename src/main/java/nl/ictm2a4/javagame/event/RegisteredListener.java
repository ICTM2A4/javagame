package nl.ictm2a4.javagame.event;

public class RegisteredListener {

    private Listener listener;
    private EventExecutor executor;

    /**
     * Create a RegisteredListener
     * @param listener The listener to save in the registeredListener
     * @param executor the eventExecutor to save in the registeredListener
     */
    public RegisteredListener(Listener listener, EventExecutor executor) {
        this.listener = listener;
        this.executor = executor;
    }

    /**
     * Execute a event and pass it to the listeners
     * @param event
     */
    public void execute(Event event) {
        executor.execute(listener, event);
    }
}
