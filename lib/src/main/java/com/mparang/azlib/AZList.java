package com.mparang.azlib;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class AZList implements Iterable<AZData> {
    private final Lock lock = new ReentrantLock();
    protected ArrayList<AZData> _list;
    protected AZList.Attribute _attribute;

    public enum Seek {
        FIRST, LAST
    }

    public AZList() {
        _list = new ArrayList<AZData>();
    }

    public AZList add(AZData data) {
        _list.add(data);
        return this;
    }

    public AZList remove(AZData data) {
        _list.remove(data);
        return this;
    }

    public AZList remove(int index) {
        _list.remove(index);
        return this;
    }

    public AZList clear() {
        _list.clear();
        return this;
    }

    public AZData get(int index) {
        return _list.get(index);
    }

    public <T> T seek(Object... keys) {
        Object rtnVal = null;
        Object[] subKeys = null;

        Object selectedKey = null;
        for (int cnti = 0; cnti < keys.length; cnti++) {
            if (selectedKey == null) {
                selectedKey = keys[0];
            }
            else {
                if (subKeys == null) subKeys = new Object[keys.length - 1];
                subKeys[cnti - 1] = keys[cnti];
            }
        }
        //
        if (selectedKey instanceof AZList.Seek) {
            switch ((AZList.Seek)selectedKey) {
                case FIRST:
                    selectedKey = 0;
                    break;
                case LAST:
                    selectedKey = size() - 1;
                    if ((int)selectedKey < 0) selectedKey = null;
                    break;
            }
        }
        //
        if (selectedKey instanceof Integer && ((int)selectedKey) > -1 && ((int)selectedKey) < size()) {
            rtnVal = get((int)selectedKey);
        }
        else {
            rtnVal = null;
        }
        //
        if (rtnVal != null && subKeys != null && subKeys.length > 0) {
            if (rtnVal instanceof AZData) {
                rtnVal = ((AZData)rtnVal).seek(subKeys);
            }
            else if (rtnVal instanceof AZList) {
                rtnVal = ((AZList)rtnVal).seek(subKeys);
            }
        }
        //
        return (T)rtnVal;
    }

    public AZData push(AZData data) {
        _list.add(data);
        return data;
    }

    public AZData pop() {
        AZData rtnVal = null;
        if (size() > 0) {
            lock.lock();
            int idx = _list.size() - 1;
            rtnVal = _list.get(idx);
            _list.remove(idx);
            lock.unlock();
        }
        return rtnVal;
    }

    public AZData shift() {
        AZData rtnVal = null;
        if (size() > 0) {
            lock.lock();
            rtnVal = _list.get(0);
            _list.remove(0);
            lock.unlock();
        }
        return rtnVal;
    }

    public AZData unshift(AZData data) {
        _list.add(0, data);
        return data;
    }

    public int size() {
        return _list.size();
    }

    private String getJsonSafeString(String src) {
        return src
            .replaceAll("\"", "\\\\\"")
            .replaceAll("\b", "\\\\\b")
            .replaceAll("\f", "\\\\\f")
            .replaceAll("\n", "\\\\\n")
            .replaceAll("\r", "\\\\\r")
            .replaceAll("\t", "\\\\\t")
            .replaceAll("\\\\", "\\\\\\\\")
            .replaceAll("/", "\\\\/");
    }

    private String getJsonElementString(String key, Object value) {
        StringBuilder rtnVal = new StringBuilder();
        //
        if (
            value instanceof Short || value instanceof Integer || value instanceof Long ||
                value instanceof Float || value instanceof Double
        ) {
            rtnVal.append("\"" + key + "\":" + value);
        }
        else if (value instanceof Character) {
            switch ((char)value) {
                case '"':
                    rtnVal.append("\"" + key+ "\":\"" + "\\\"" + "\"");
                    break;
                case '/':
                    rtnVal.append("\"" + key + "\":\"" + "\\/" + "\"");
                    break;
                default:
                    rtnVal.append("\"" + key + "\":\"" + value + "\"");
                    break;
            }
        }
        else if (value instanceof String) {
            rtnVal.append("\"" + key + "\":\"" + getJsonSafeString((String)value) + "\"");
        }
        else if (value instanceof Boolean) {
            rtnVal.append("\"" + key + "\":" + value);
        }
        else if (value instanceof AZData || value instanceof AZList) {
            rtnVal.append("\"" + key + "\":" + value);
        }
        else if (value == null) {
            rtnVal.append("\"" + key + "\":" + value);
        }
        else {
            rtnVal.append("\"" + key + "\":\"" + value + "\"");
        }
        //
        return rtnVal.toString();
    }

    @Override
    public String toString() {
        StringBuilder rtnVal = new StringBuilder();
        rtnVal.append("[");
        boolean appened = false;
        for (AZData row: _list) {
            if (appened) rtnVal.append(",");
            rtnVal.append(row.toString());
            appened = true;
        }
        rtnVal.append("]");
        return rtnVal.toString();
    }

    @Override
    public Iterator iterator() {
        return new AZList.DataIterator();
    }

    class DataIterator implements Iterator {
        int current = 0;

        @Override
        public boolean hasNext() {
            return current < (_list.size() - 1);
        }

        @Override
        public Object next() {
            return current < (_list.size() - 1) ? get(current++) : null;
        }
    }

    public AZList.Attribute attribute() {
        return _attribute;
    }

    public class Attribute {
        private HashMap<String, Object> _attrMap;
        public Attribute() {
            _attrMap = new HashMap<>();
        }

        public AZList.Attribute add(String key, Object value) {
            if (!_attrMap.containsKey(key)) _attrMap.put(key, value);
            return this;
        }

        public AZList.Attribute set(String key, Object value) {
            if (_attrMap.containsKey(key)) {
                _attrMap.replace(key, value);
            }
            else {
                _attrMap.put(key, value);
            }
            return this;
        }

        public AZList.Attribute remove(String key) {
            _attrMap.remove(key);
            return this;
        }

        public boolean hasKey(String key) {
            return _attrMap.containsKey(key);
        }

        public <T> T get(String key, T defaultValue) {
            if (!hasKey(key)) return defaultValue;
            return (T)_attrMap.get(key);
        }

        public <T> T get(String key) {
            return get(key, null);
        }

        public Object get(int index) {
            Object[] keys = _attrMap.keySet().toArray();
            return _attrMap.get(keys[index]);
        }

        public String[] getAllKeys() {
            return _attrMap.keySet().toArray(new String[0]);
        }

        @Override
        public String toString() {
            StringBuilder rtnVal = new StringBuilder();
            //
            rtnVal.append("{");
            //
            Object[] keys = this.getAllKeys();
            for (int cnti = 0; cnti < keys.length; cnti++) {
                String key = (String)keys[cnti];
                Object value = _attrMap.get(key);
                //
                if (cnti > 0) rtnVal.append(",");
                //
                rtnVal.append(getJsonElementString(key, value));
            }
            //
            rtnVal.append("}");
            //
            return rtnVal.toString();
        }
    }
}
