package com.mparang.azlib;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class AZList {

  ArrayList<AZData> list = null;
  //private HashMap map_attribute = null;
  public AttributeData Attribute = null;

  private String _name = "", _value = "";

  public AZList() {
    list = new ArrayList<AZData>();
    //map_attribute = new HashMap();
    Attribute = new AttributeData();
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
  synchronized public String putAttribute(int pIdx, String pValue) {
    map_attribute.put("" + pIdx, pValue);
    return pValue;
  }
  synchronized public String removeAttribute(int pIdx) {
    return (String)map_attribute.remove("" + pIdx);
  }
  synchronized public int clearAttribute() {
    int rtnValue = map_attribute.size();
    map_attribute.clear();
    return rtnValue;
  }
  */

  public String getName() { return this._name; }

  public void setName(String name) { this._name = name; }

  public String getValue() { return this._value; }

  public void setValue(String value) { this._value = value; }

  public boolean add(AZData data) {
    return list.add(data);
  }

  public boolean remove(AZData data) {
    return list.remove(data);
  }

  public AZData remove(int index) {
    return list.remove(index);
  }

  public void clear() {
    list.clear();
  }

  public int size() {
    return list.size();
  }

  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder();
    for (int cnti=0; cnti<list.size(); cnti++) {
      AZData data = list.get(cnti);
      builder.append((cnti > 0 ? ", " : "") + "{" + data.toString() + "}");
    }
    return builder.toString();
  }

  public String toJsonString() {
    StringBuilder builder = new StringBuilder();
    for (int cnti=0; cnti<list.size(); cnti++) {
      AZData data = list.get(cnti);
      builder.append((cnti > 0 ? ", " : "") + data.toJsonString());
    }
    return "[" + builder.toString() + "]";
  }

  public String toXmlString() {
    StringBuilder builder = new StringBuilder();
    for (int cnti = 0; cnti < size(); cnti++) {
      AZData data = list.get(cnti);
      builder.append(data.toXmlString());
    }
    return builder.toString();
  }

  public AZData get(int index) { return getData(index); }

  public AZData getData(int index) { return list.get(index); }

  public <T> T[] convert(Class<T> entity) {
    //System.out.println("convert #1");
    T[] rtn_value = (T[]) Array.newInstance(entity, size());
    //System.out.println("convert #2");
    for (int cnti=0; cnti<size(); cnti++) {
      //System.out.println("convert #3 : " + get(cnti));
      try {
        rtn_value[cnti] = get(cnti).convert(entity);
        //System.out.println("convert #4");
      }
      catch (Exception ex) {
        //System.out.println("convert #4 ex : " + ex.toString());
      }
    }
    return rtn_value;
  }

  private class KeyValue {
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
  public class AttributeData {
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
