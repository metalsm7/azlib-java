package com.mparang.azlib;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class AZData implements Iterable<AZData.KeyLink> {
    private final String RANDOM_SRC = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
    private final Lock lock = new ReentrantLock();
    protected HashMap<String, Object> _map;
    protected ArrayList<KeyLink> _indexer;
    protected Attribute _attribute;

    public AZData() {
        _map = new HashMap<>();
        _indexer = new ArrayList<>();
        //
        _attribute = new Attribute();
    }

    public AZData add(String key, Object value) {
        String linkKey = key;
        if (hasKey(linkKey)) {
            linkKey += ("_" + createRandom(20));
        }
        //
        lock.lock();
        //
        _map.put(linkKey, value);
        _indexer.add(new KeyLink(key, linkKey));
        //
        lock.unlock();
        //
        return this;
    }

    public AZData add(AZData data) {
        for (int cnti = 0; cnti < data._indexer.size(); cnti++) {
            KeyLink keyLink = data._indexer.get(cnti);
            //
            add(keyLink.getKey(), data._map.get(keyLink.getLink()));
        }
        //
        return this;
    }

    public AZData set(String key, Object value) {
        if (hasKey(key)) {
            //
            lock.lock();
            //
            _map.replace(key, value);
            //
            lock.unlock();
        }
        else {
            add(key, value);
        }
        //
        return this;
    }

    public AZData merge(AZData data, boolean overwrite) {
        for (int cnti = 0; cnti < data._indexer.size(); cnti++) {
            String key = data.getKey(cnti);
            Object value = data.get(cnti);
            if (hasKey(key)) {
                if (overwrite) set(key, value);
            }
            else {
                add(key, value);
            }
        }
        //
        return this;
    }

    public <T> T get(int index, T defaultValue) {
        try {
            if (_map.containsKey(_indexer.get(index).getLink())) {
                return (T) _map.get(_indexer.get(index).getLink());
            }
        }
        catch (Exception _ex) {}
        return defaultValue;
    }

    public <T> T get(int index) {
        return get(index, null);
    }

    public <T> T get(String key, T defaultValue) {
        try {
            if (_map.containsKey(key)) {
                return (T) _map.get(key);
            }
        }
        catch (Exception _ex) {}
        return defaultValue;
    }

    public <T> T get(String key) {
        return get(key, null);
    }

    public Object[] getAll(String key) {
        ArrayList rtnVal = new ArrayList();
        for (int cnti = 0; cnti < _indexer.size(); cnti++) {
            KeyLink link = _indexer.get(cnti);
            if (link.getKey() == key) {
                rtnVal.add(_map.get(link.getLink()));
            }
        }
        return rtnVal.toArray();
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

        if (selectedKey instanceof String && hasKey((String)selectedKey)) {
            rtnVal = get((String)selectedKey);
        }
        else if (selectedKey instanceof Integer && ((int)selectedKey) > -1 && ((int)selectedKey) < size()) {
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

    public AZData remove(int index) {
        if (index > -1 && index < _indexer.size()) {
            lock.lock();
            KeyLink link = _indexer.remove(index);
            if (link != null) {
                _map.remove(link.getLink());
            }
            lock.unlock();
        }
        //
        return this;
    }

    public AZData remove(String key) {
        for (int cnti = 0; cnti < _indexer.size(); cnti++) {
            KeyLink link = _indexer.get(cnti);
            if (link.getKey() == key) {
                remove(cnti);
                break;
            }
        }
        //
        return this;
    }

    public AZData removeAll(String key) {
        for (int cnti = 0; cnti < _indexer.size(); cnti++) {
            KeyLink link = _indexer.get(cnti);
            if (link.getKey() == key) {
                remove(cnti);
            }
        }
        //
        return this;
    }

    public String getKey(int index) {
        return _indexer.get(index).getKey();
    }

    public String getLink(int index) {
        return _indexer.get(index).getLink();
    }

    public AZData merge(AZData data) {
        return merge(data, false);
    }

    public boolean hasKey(String key) {
        return _map.containsKey(key);
    }

    public int indexOf(String key) {
        int rtnVal = -1;
        if (hasKey(key)) {
            for (int cnti = 0; cnti < _indexer.size(); cnti++) {
                if (_indexer.get(cnti).getKey().equals(key)) {
                    rtnVal = cnti;
                    break;
                }
            }
        }
        return rtnVal;
    }

    public String[] getAllKeys() {
        String[] rtnVal = new String[_indexer.size()];
        for (int cnti = 0; cnti < _indexer.size(); cnti++) {
            rtnVal[cnti] = _indexer.get(cnti).getKey();
        }
        return rtnVal;
    }

    public int size() {
        return _map.size();
    }

    public AZData clear() {
        lock.lock();
        _indexer.clear();
        _map.clear();
        lock.unlock();
        //
        return this;
    }

//    public static AZData parse(String json) {
//
//    }

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
        //
        rtnVal.append("{");
        //
        Object[] links = _indexer.toArray();
        for (int cnti = 0; cnti < links.length; cnti++) {
            KeyLink link = (KeyLink)links[cnti];
            Object value = _map.get(link.getLink());
            //
            if (cnti > 0) rtnVal.append(",");
            //
            rtnVal.append(getJsonElementString(link.getKey(), value));
        }
        //
        rtnVal.append("}");
        //
        return rtnVal.toString();
    }

    private String createRandom(int length) {
        StringBuilder rtnVal = new StringBuilder();
        Random random = new Random();

        for (int cnti=0; cnti<length; cnti++) {
            rtnVal.append(RANDOM_SRC.charAt(random.nextInt(RANDOM_SRC.length())));
        }

        return rtnVal.toString();
    }

    public Attribute attribute() {
        return _attribute;
    }

    @Override
    public Iterator iterator() {
        return new DataIterator();
    }

    class DataIterator implements Iterator {
        int current = 0;

        @Override
        public boolean hasNext() {
            return current < (_indexer.size() - 1);
        }

        @Override
        public Object next() {
            return current < (_indexer.size() - 1) ? get(current++) : null;
        }
    }

    class KeyLink {
        @Getter(AccessLevel.PUBLIC) @Setter
        protected String key, link;

        public KeyLink(String key, String link) {
            setKey(key);
            setLink(link);
        }
    }

    class KeyValue {
        @Getter(AccessLevel.PUBLIC) @Setter
        protected String key;
        @Getter(AccessLevel.PUBLIC) @Setter(AccessLevel.PUBLIC)
        protected Object value;

        public KeyValue(String key, Object value) {
            setKey(key);
            setValue(value);
        }
    }

    public class Attribute {
        private HashMap<String, Object> _attrMap;
        public Attribute() {
            _attrMap = new HashMap<>();
        }

        public Attribute add(String key, Object value) {
            if (!_attrMap.containsKey(key)) _attrMap.put(key, value);
            return this;
        }

        public Attribute set(String key, Object value) {
            if (_attrMap.containsKey(key)) {
                _attrMap.replace(key, value);
            }
            else {
                _attrMap.put(key, value);
            }
            return this;
        }

        public Attribute remove(String key) {
            _attrMap.remove(key);
            return this;
        }

        public boolean hasKey(String key) {
            return _attrMap.containsKey(key);
        }

        public Object get(String  key) {
            if (!hasKey(key)) return null;
            return _attrMap.get(key);
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