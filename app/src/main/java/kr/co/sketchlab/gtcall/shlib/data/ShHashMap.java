package kr.co.sketchlab.gtcall.shlib.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ShHashMap extends HashMap<String, Object> {
    public ShHashMap(Map<? extends String, ?> m) {
        super(m);
    }

    public ShHashMap(int initialCapacity, float loadFactor) {
        super(initialCapacity, loadFactor);
    }

    public ShHashMap(int initialCapacity) {
        super(initialCapacity);
    }

    public ShHashMap() {
    }

    public ShHashMap(HashMap<String, Object> h) {
        super(h);
    }

    public int getInt(String name) {
        try {
            return (Integer)get(name); // int type으로 시도
        } catch (Exception e) {
            try {
                return Integer.parseInt((String) get(name)); // string 으로 시도
            } catch (Exception ex) {
                return Integer.parseInt(Long.toString((Long)get(name))); // long 으로 시도
            }
        }
    }
    public long getLong(String name) {
        try {
            return (Long)get(name);
        } catch (Exception e) {
            try {
                return Long.parseLong((String) get(name));
            } catch (Exception ex) {
                return (Integer)get(name);
            }
        }

    }

    public ShHashMap getHashMap(String name) {
        return (ShHashMap)get(name);
    }
    public ArrayList<ShHashMap> getHashMapList(String name) {
        return (ArrayList<ShHashMap>) get(name);
    }

    public String getString(String name) {
        try {
            return (String)get(name);
        } catch (Exception e) {
            try {
                return Long.toString(getLong(name));
            } catch (Exception ex) {
                return Double.toString(getDouble(name));
            }
        }
    }
    public double getDouble(String name) {
        try {
            return (Double)get(name);
        } catch (Exception e) {
            try {
                return Double.parseDouble((String)get(name));
            } catch (Exception ex) {
                return (Float)get(name);
            }
        }
    }
    public float getFloat(String name) {
        try {
            return (Float)get(name);
        } catch (Exception e) {
            return Float.parseFloat((String)get(name));
        }
    }
}
