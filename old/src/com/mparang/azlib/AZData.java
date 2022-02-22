package com.mparang.azlib;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;

public class AZData {

  //private boolean isSynchronous = false;

  //private ConcurrentHashMap map_sync = null;
  private HashMap map_async = null;
  //private HashMap map_attribute = null;
  public AttributeData Attribute = null;
  //private HashMap map_index = null;
  private ArrayList<KeyLink> indexer = null;

  private String _name = "", _value = "";

  /**
   * 기본 생성자
   * 작성일 : 2014-11-13
   */
  public AZData() {
    //map_sync = new ConcurrentHashMap();
    map_async = new HashMap();
    //map_index = new HashMap();
    //map_attribute = new HashMap();
    Attribute = new AttributeData();
    indexer = new ArrayList<KeyLink>();
  }

    /*
    synchronized public String[] getAttributeNames() {
      String[] rtn_value = new String[map_attribute.size()];
      Object[] arrs = map_attribute.keySet().toArray();
      //Dictionary<string, object>.Enumerator arrs = map_attribute.GetEnumerator();
      for (int cnti = 0; cnti < arrs.length; cnti++) {
          rtn_value[cnti] = arrs[cnti].toString();
      }
      return rtn_value;
    }
    synchronized public Object getAttribute(String pName) {
      return map_attribute.get(pName);
    }
    synchronized public Object putAttribute(String pName, Object pValue) {
      map_attribute.put(pName, pValue);
      return pValue;
    }
    synchronized public String removeAttribute(String pName) {
      return (String)map_attribute.remove(pName);
    }
    synchronized public int clearAttribute() {
      int rtnValue = map_attribute.size();
      map_attribute.clear();
      return rtnValue;
    }
    */

  synchronized public AZData add(AZData value) {
    for (int cnti = 0; cnti < value.size(); cnti++) {
      add(value.getKey(cnti), value.get(cnti));
    }
    return this;
  }

  /**
   * key값과 쌍을 이루는 value를 추가
   * @param pKey
   * @param pValue
   * @return
   * 작성일 : 2014-11-18
   */
  synchronized public AZData add(String pKey, Object pValue) {
    if (map_async.get(pKey) != null) {
      // 동일 키값이 이미 존재하는 경우
      String linkString = AZString.getRandom(20);
      map_async.put(linkString, pValue);
      indexer.add(new KeyLink(pKey, linkString));
    }
    else {
      // 동일 키값이 존재하지 않는 경우 -> put
      map_async.put(pKey, pValue);
      indexer.add(new KeyLink(pKey, pKey));
    }
    return this;
  }

  public AZData merge(AZData value, boolean overwrite) {
    for (int cnti = 0; cnti < value.size(); cnti++) {
      if (hasKey(value.getKey(cnti))) {
        if (overwrite) set(value.getKey(cnti), value.get(cnti));
      }
      else {
        add(value.getKey(cnti), value.get(cnti));
      }
    }
    return this;
  }

  public AZData merge(AZData value) {
    return merge(value, false);
  }

  public String[] getAllKeys() {
    ArrayList<String> rtnVal = new ArrayList<String>();
  }

  public String getName() { return this._name; }

  public void setName(String name) { this._name = name; }

  public String getValue() { return this._value; }

  public void setValue(String value) { this._value = value; }

  /**
   *
   * @param pEntity
   * @param <T>
   * @return
   * Created in 2015-07-26, leeyonghun
   */
  public <T> T convert(Class<T> pEntity) {
    T rtn_value = null;
    try {
      rtn_value = (T)pEntity.newInstance();
    }
    catch (Exception ex) {
      //System.out.println("convert ex - " + ex.toString());
    }
    return convert(rtn_value);
  }
  /**
   *
   * @param pEntity
   * @param <T>
   * @return
   * Created in 2015-07-26, leeyonghun
   */
  private <T> T convert(T pEntity) {
    for (int cnti = 0; cnti < size(); cnti++) {
      String key = getKey(cnti);
      Field field = null;
      try {
        field = pEntity.getClass().getField(key);
      } catch (Exception ex) {
        //ex.printStackTrace();
      }

      if (field != null) {
        // 동일한 key값이 존재하는 경우
        try {
          if (field.getType() == int.class) {
            field.setInt(pEntity, getInt(key));
          } else if (field.getType() == float.class) {
            field.setFloat(pEntity, getFloat(key));
          } else if (field.getType() == float.class) {
            field.set(pEntity, getString(key));
          } else {
            field.set(pEntity, get(key));
          }
        } catch (Exception ex) {
          //ex.printStackTrace();
        }
      } else {
        Method method = null;
        Class<?> type = null;
        try {
          String methodName = "set" + key.substring(0, 1).toUpperCase() + key.substring(1);
          Method[] methods = pEntity.getClass().getDeclaredMethods();
          for (int cntk=0; cntk<methods.length; cntk++) {

            if (methods[cntk].getName().equals(methodName)) {
              if (methods[cntk].getParameterTypes().length != 1) {
                continue;
              }
              type = methods[cntk].getParameterTypes()[0];
              method = pEntity.getClass().getDeclaredMethod(methodName, type);
              break;
            }
          }
        } catch (Exception ex) {
          //ex.printStackTrace();
        }

        if (method != null) {
          try {
            if (type == int.class) {
              method.invoke(pEntity, getInt(key));
            }
            else if (type == float.class) {
              method.invoke(pEntity, getFloat(key));
            }
            else if (type == String.class) {
              method.invoke(pEntity, getString(key));
            }
            else {
              method.invoke(pEntity, get(key));
            }
          } catch (Exception ex) {
            ex.printStackTrace();
          }
        }
      }
    }
    return pEntity;
  }

  public Object get(int pIndex) { return map_async.get(indexer.get(pIndex).getLink()); }

  public Object get(String pKey) { return map_async.get(pKey); }

  public AZList getList(int pIndex) { return (AZList)get(pIndex); }

  public AZList getList(String pKey) { return (AZList)get(pKey); }

  public AZData getData(int pIndex) { return (AZData)get(pIndex); }

  public AZData getData(String pKey) { return (AZData)get(pKey); }

  public String getString(int pIndex) { return AZString.init(get(pIndex)).string(); }

  public String getString(String pKey) { return AZString.init(get(pKey)).string(); }

  public int getInt(int pIndex) { return AZString.toInt(getString(pIndex), 0); }

  public int getInt(int pIndex, int pDefaultValue) { return AZString.toInt(getString(pIndex), pDefaultValue); }

  public int getInt(String pKey) { return AZString.toInt(getString(pKey), 0); }

  public int getInt(String pKey, int pDefaultValue) { return AZString.toInt(getString(pKey), pDefaultValue); }

  public float getFloat(int pIndex) { return AZString.toFloat(getString(pIndex), 0); }

  public float getFloat(int pIndex, float pDefaultValue) { return AZString.toFloat(getString(pIndex), pDefaultValue); }

  public float getFloat(String pKey) { return AZString.toFloat(getString(pKey), 0); }

  public float getFloat(String pKey, float pDefaultValue) { return AZString.toFloat(getString(pKey), pDefaultValue); }

  public String getKey(int pIndex) { return indexer.get(pIndex).getKey(); }

  public String getLink(int pIndex) { return indexer.get(pIndex).getLink(); }

  synchronized public boolean set(String pKey, Object pValue) {
    boolean rtnValue = false;
    if (map_async.get(pKey) != null) {
      // 동일 키값이 이미 존재하는 경우
      map_async.put(pKey, pValue);
      rtnValue = true;
    }
    return rtnValue;
  }

  synchronized public boolean set(int pIndex, Object pValue) {
    boolean rtnValue = false;
    if (map_async.get(indexer.get(pIndex).getLink()) != null) {
      // 동일 키값이 이미 존재하는 경우
      map_async.put(indexer.get(pIndex).getLink(), pValue);
      rtnValue = true;
    }
    return rtnValue;
  }

  synchronized public boolean remove(String pKey) {
    boolean rtnValue = false;
    map_async.remove(pKey);
    for (int cnti=0; cnti<indexer.size(); cnti++) {
      if (indexer.get(cnti).getKey().equals(pKey)) {
        indexer.remove(cnti);
        rtnValue = true;
        break;
      }
    }
    return rtnValue;
  }

  synchronized public boolean remove(int pIndex) {
    boolean rtnValue = false;
    String keyString = indexer.get(pIndex).getKey();
    rtnValue = map_async.remove(keyString) == null ? false : true;
    for (int cnti=0; cnti<indexer.size(); cnti++) {
      if (indexer.get(cnti).getKey().equals(keyString)) {
        indexer.remove(cnti);
        rtnValue = rtnValue == true ? true : false;
        break;
      }
    }
    return rtnValue;
  }

  public boolean hasKey(String key) {
    return map_async.containsKey(key);
  }

  synchronized public int indexOf(String pKey) {
    int rtnValue = -1;
    if (map_async.containsKey(pKey)) {
      for (int cnti=0; cnti<indexer.size(); cnti++) {
        if (indexer.get(cnti).getKey().equals(pKey)) {
          rtnValue = cnti;
          break;
        }
      }
    }
    return rtnValue;
  }

  public int size() {
    return map_async.size();
  }

  public void clear() {
    map_async.clear();
    indexer.clear();
  }

  public AZData[] toArray() {
    AZData[] rtnValue = new AZData[size()];
    for (int cnti=0; cnti<size(); cnti++) {
      AZData dmyData = new AZData();
      dmyData.add(getKey(cnti), getString(cnti));
      rtnValue[cnti] = dmyData;
      dmyData = null;
    }
    return rtnValue;
  }

  public String[] toStringArray() {
    String[] rtnValue = new String[size()];
    for (int cnti=0; cnti<size(); cnti++) {
      rtnValue[cnti] = getString(cnti);
    }
    return rtnValue;
  }

  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder();
    for (int cnti=0; cnti<indexer.size(); cnti++) {
      if (get(cnti) instanceof AZData) {
        builder.append((cnti > 0 ? ", " : "") + "\"" + getKey(cnti) + "\"" +
            ":" + "{" + ((AZData)get(cnti)).toString() + "}");
      }
      else if (get(cnti) instanceof AZList) {
        builder.append((cnti > 0 ? ", " : "") + "\"" + getKey(cnti) + "\"" +
            ":" + "[" + ((AZList)get(cnti)).toString() + "]");
      }
      else {
        String valueString = getString(cnti);
        builder.append((cnti > 0 ? ", " : "") + "\"" + getKey(cnti) + "\"" +
            ":" + "\"" + valueString + "\"");
      }
    }
    return builder.toString();
  }

  public String toJsonString() {
    StringBuilder builder = new StringBuilder();
    for (int cnti=0; cnti<indexer.size(); cnti++) {
      if (get(cnti) instanceof AZData) {
        builder.append((cnti > 0 ? ", " : "") + "\"" + AZString.toJSONSafeEncoding(getKey(cnti)) + "\"" +
            ":" + ((AZData)get(cnti)).toJsonString());
      }
      else if (get(cnti) instanceof AZList) {
        builder.append((cnti > 0 ? ", " : "") + "\"" + AZString.toJSONSafeEncoding(getKey(cnti)) + "\"" +
            ":" + ((AZList)get(cnti)).toJsonString());
      }
      else {
        String valueString = getString(cnti);
        builder.append((cnti > 0 ? ", " : "") + "\"" + AZString.toJSONSafeEncoding(getKey(cnti)) + "\"" +
            ":" + "\"" + AZString.toJSONSafeEncoding(valueString) + "\"");
      }
    }
    return "{" + builder.toString() + "}";
  }

  public String toXmlString() {
    StringBuilder builder = new StringBuilder();
    builder.append("<" + getName());
    //String[] attribute_names = getAttributeNames();
    for (int cnti = 0; cnti < Attribute.size(); cnti++) {
      builder.append(" " + Attribute.getKey(cnti) + "=\"" + Attribute.get(cnti) + "\"");
    }
    builder.append(">");

    for (int cnti = 0; cnti < indexer.size(); cnti++) {
      if (get(cnti) instanceof AZData) {
        builder.append(((AZData)get(cnti)).toXmlString());
      }
      else if (get(cnti) instanceof AZList) {
        builder.append(((AZList)get(cnti)).toXmlString());
      }
    }
    builder.append(getValue() + "</" + getName() + ">");
    return builder.toString();
  }

  private class KeyLink {
    private String key, link;
    // 기본생성자
    public KeyLink() {
      this.key = "";
      this.link = "";
    }

    public KeyLink(String pKey, String pLink) {
      this.key = pKey;
      this.link = pLink;
    }

    public String getKey() { return this.key; }
    public String getLink() { return this.link; }
    @Override
    public String toString() { return getKey() + ":" + getLink(); }
  }

  private static class KeyValue {
    private String key;
    private Object value;
    // 기본 생성자
    public KeyValue() {
      this.key = "";
      this.value = "";
    }

    public KeyValue(String key, Object value) {
      this.key = key;
      this.value = value;
    }

    public String getKey() { return this.key; }

    public Object getValue() { return this.value; }
    public void setValue(Object value) { this.value = value; }
    @Override
    public String toString() { return getKey() + ":" + getValue(); }
  }

  /**
   * 속성값(attribute)에 대한 자료 저장용 클래스
   * 작성일 : 2015-05-22 이용훈
   */
  public static class AttributeData {
    private ArrayList<KeyValue> attribute_list;

    public AttributeData() {
      this.attribute_list = new ArrayList<KeyValue>();
    }

    public Object add(String key, Object value) {
      this.attribute_list.add(new KeyValue(key, value));
      return value;
    }

    public Object insertAt(int index, String key, Object value) {
      Object rtn_value = null;
      if (index > -1 && index < size()) {
        this.attribute_list.add(index, new KeyValue(key, value));
        rtn_value = value;
      }
      return rtn_value;
    }

    public Object remove(String key) {
      Object rtn_value = null;
      for (int cnti=0; cnti<this.attribute_list.size(); cnti++) {
        if (this.attribute_list.get(cnti).getKey().equals(key)) {
          rtn_value = this.attribute_list.get(cnti).getValue();
          this.attribute_list.remove(cnti);
          break;
        }
      }
      return rtn_value;
    }

    public Object remove(int index) {
      Object rtn_value = null;
      if (index > -1 && index < size()) {
        rtn_value = get(index);
        this.attribute_list.remove(index);
      }
      return rtn_value;
    }

    public Object removeAt(int index) {
      return remove(index);
    }

    public void clear() {
      this.attribute_list.clear();
    }

    public int indexOf(String key) {
      int rtn_value = -1;
      for (int cnti=0; cnti<this.attribute_list.size(); cnti++) {
        if (this.attribute_list.get(cnti).getKey().equals(key)) {
          rtn_value = cnti;
          break;
        }
      }
      return rtn_value;
    }

    public Object get(String key) {
      Object rtn_value = null;
      for (int cnti=0; cnti<this.attribute_list.size(); cnti++) {
        if (this.attribute_list.get(cnti).getKey().equals(key)) {
          rtn_value = this.attribute_list.get(cnti).getValue();
          break;
        }
      }
      return rtn_value;
    }

    public Object get(int index) {
      Object rtn_value = null;
      if (index > -1 && index < size()) {
        rtn_value = this.attribute_list.get(index).getValue();
      }
      return rtn_value;
    }

    public Object set(String key, Object value) {
      Object rtn_value = null;
      for (int cnti=0; cnti<this.attribute_list.size(); cnti++) {
        if (this.attribute_list.get(cnti).getKey().equals(key)) {
          rtn_value = this.attribute_list.get(cnti).getValue();
          this.attribute_list.get(cnti).setValue(value);
          break;
        }
      }
      return rtn_value;
    }

    public int size() {
      return this.attribute_list.size();
    }

    public String getKey(int index) {
      return this.attribute_list.get(index).getKey();
    }

    public String[] getKeys() {
      String[] rtn_value = new String[this.attribute_list.size()];
      for (int cnti=0; cnti<this.attribute_list.size(); cnti++) {
        rtn_value[cnti] = this.attribute_list.get(cnti).getKey();
      }
      return rtn_value;
    }

    /**
     *
     * @param key
     * @return
     * Created in 2015-07-03, leeyonghun
     */
    public boolean hasKey(String key) {
      boolean rtn_value = false;
      for (int cnti=0; cnti<this.attribute_list.size(); cnti++) {
        if (this.attribute_list.get(cnti).getKey().equals(key)) {
          rtn_value = true;
          break;
        }
      }
      return rtn_value;
    }

    @Override
    public String toString() {
      StringBuilder rtn_value = new StringBuilder();
      for (int cnti=0; cnti<this.attribute_list.size(); cnti++) {
        rtn_value.append((cnti > 0 ? ", " : "") + "\"" + AZString.toJSONSafeEncoding(getKey(cnti)) + "\"" +
            ":" + "\"" + (get(cnti) == null ? "" : AZString.toJSONSafeEncoding(get(cnti).toString())) + "\"");
      }
      return rtn_value.toString();
    }

    public String toJsonString() {
      return "{" + toString() + "}";
    }
  }
}
