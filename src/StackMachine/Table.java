package StackMachine;

import java.util.HashMap;

public class Table {
    private static HashMap<String, Integer> values = new HashMap<>();

    public static void assign(String name, Integer value) {
        if (!values.containsKey(name)) {
            values.put(name, value);
        } else {
            values.replace(name, value);
        }
    }

    public static Integer get(String name) throws MachineException{
        if (values.containsKey(name)) {
            return values.get(name);
        } else {
            throw new MachineException("No variable initialized with name: " + name);
        }
    }
}
