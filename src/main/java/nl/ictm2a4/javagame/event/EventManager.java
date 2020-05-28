package nl.ictm2a4.javagame.event;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

public class EventManager {

    private static EventManager instance;
    private Map<Class<? extends Event>, Set<RegisteredListener>> listenerMap;

    public EventManager() {
        instance = this;
        listenerMap = new HashMap<>();
    }

    public void callEvent(Event event) {

        Set<RegisteredListener> listeners = listenerMap.get(event.getClass());
        if (listeners == null)
            return;

        for(RegisteredListener registeredListener : listeners) {
            registeredListener.execute(event);
        }
    }

    public void registerListener(Listener listener) {

        // alle methods laden uit de listener class
        Method[] publicMethods = listener.getClass().getMethods();
        Method[] privateMethods = listener.getClass().getDeclaredMethods();

        Set<Method> methods = new HashSet<>(publicMethods.length + privateMethods.length, 1.0f);

        Collections.addAll(methods, publicMethods);
        Collections.addAll(methods, privateMethods);

        // kijken of de methode een listener method is
        for (final Method method : methods) {
            final EventHandler eh = method.getAnnotation(EventHandler.class);
            if (eh == null) continue;

            // avoid event duplication
            if (method.isBridge() || method.isSynthetic())
                continue;

            final Class<?> checkClass;
            if (method.getParameterTypes().length != 1 || !Event.class.isAssignableFrom(checkClass = method.getParameterTypes()[0])) {
                // listener is niet geldig
                System.out.println(" attempted to register an invalid EventHandler method signature \"" + method.toGenericString() + "\" in " + listener.getClass());
                continue;
            }

            final Class<? extends Event> eventClass = checkClass.asSubclass(Event.class);
            method.setAccessible(true);
            Set<RegisteredListener> eventSet = listenerMap.get(eventClass);
            if(eventSet == null) {
                eventSet = new HashSet<>();
                listenerMap.put(eventClass, eventSet);
            }

            EventExecutor executor = new EventExecutor() {
                @Override
                public void execute(Listener listener, Event event) {
                    try {
                        if (!eventClass.isAssignableFrom(event.getClass())) {
                            return;
                        }
                        method.invoke(listener, event);
                    } catch (IllegalAccessException | InvocationTargetException e) {
                        e.printStackTrace();
                    }
                }
            };

            eventSet.add(new RegisteredListener(listener, executor));
        }

    }

    public static EventManager getInstance() {
        if (instance == null) {
            new EventManager();
        }
        return instance;
    }
}
