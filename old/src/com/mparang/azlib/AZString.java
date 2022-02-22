package com.mparang.azlib;

import java.util.ArrayList;
import java.util.Random;

public class AZString {
  private final Object value;

    public static final int RANDOM_ALPHABET_ONLY = -101;
    public static final int RANDOM_NUMBER_ONLY = -102;
    public static final int RANDOM_ALPHABET_NUMBER = -103;

    //public static final String FORMAT_STRING_LENGTH_NOT_EQUAL = "";
    /**
     * 기본생성자
     * 작성일 : 2014-10-02 이용훈
     */
    public AZString(Object string) {
      this.value = string;
    }

    public static AZString init(Object string) {
      return new AZString(string);
    }

    public String string() {
      return string("");
    }

    public String string(String defaultValue) {
        String rtnValue = "";
        if (this.value == null) {
          rtnValue = defaultValue;
        }
        else {
          if (this.value instanceof Integer) {
            try {
              rtnValue = "" + this.value;
            } catch (Exception ex) {
              rtnValue = defaultValue;
            }
          }
          else {
            try {
              rtnValue = this.value.toString();
            } catch (Exception ex) {
              rtnValue = defaultValue;
            }
          }
        }
        return rtnValue;
    }

    @Override
    public String toString() {
      return string();
    }

    public int toInt() {
      return toInt(0);
    }

    public int toInt(int defaultValue) {
      int rtnValue = 0;
      if (this.value == null) {
        rtnValue = defaultValue;
      }
      else {
        try {
          rtnValue = Integer.parseInt(this.value.toString());
        }
        catch (Exception ex) {
          rtnValue = defaultValue;
        }
      }
      return rtnValue;
    }

    public static int toInt(String value, int defaultValue) {
      int rtnValue = 0;
      try {
        rtnValue = Integer.parseInt(value);
      }
      catch (Exception ex) {
        rtnValue = defaultValue;
      }
      return rtnValue;
    }

    public static int toInt(String value) {
      return AZString.toInt(value, 0);
    }

    public float toFloat() {
      return toFloat(0f);
    }

    public float toFloat(float defaultValue) {
      float rtnValue = 0f;
      if (this.value == null) {
        rtnValue = defaultValue;
      }
      else {
        try {
          rtnValue = Float.parseFloat(this.value.toString());
        }
        catch (Exception ex) {
          rtnValue = defaultValue;
        }
      }
      return rtnValue;
    }

    public static float toFloat(String value, float defaultValue) {
      float rtnValue = 0f;
      try {
        rtnValue = Float.parseFloat(value);
      }
      catch (Exception ex) {
        rtnValue = defaultValue;
      }
      return rtnValue;
    }

    public static float toFloat(String value) {
        return AZString.toFloat(value, 0f);
    }

    public long toLong() {
      return toLong(0);
    }

    public long toLong(long defaultValue) {
      long rtnValue = 0;
      if (this.value == null) {
        rtnValue = defaultValue;
      }
      else {
        try {
          rtnValue = Long.parseLong(this.value.toString());
        }
        catch (Exception ex) {
          rtnValue = defaultValue;
        }
      }
      return rtnValue;
    }

    public static long toLong(String value, long defaultValue) {
      long rtnValue = 0;
      try {
        rtnValue = Long.parseLong(value);
      }
      catch (Exception ex) {
        rtnValue = defaultValue;
      }
      return rtnValue;
    }

    public static long toLong(String value) {
      return AZString.toLong(value, 0);
    }

    public static String getRandom() {
      int randomLength = 6;
      Random random = new Random();
      int rndValue = random.nextInt(12);
      if (rndValue >= 3) {
        rndValue = randomLength;
      }
      return AZString.getRandom(rndValue, AZString.RANDOM_ALPHABET_NUMBER, true);
    }

    public static String getRandom(int length) {
      return AZString.getRandom(length, AZString.RANDOM_ALPHABET_NUMBER, true);
    }

    public static String getRandom(int length, String source) {
      StringBuffer rtnValue = new StringBuffer();
      Random random = new Random();

      if (source.length() < 1) {
        source = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
      }

      for (int cnti=0; cnti<length; cnti++) {
        rtnValue.append(source.charAt(random.nextInt(source.length())));
      }

      return rtnValue.toString();
    }

    public static String getRandom(int length, int randomType, boolean caseSensitive) {
      String sourceString = "";
      switch (randomType) {
        case AZString.RANDOM_NUMBER_ONLY:
          sourceString = "1234567890";
            break;
        case AZString.RANDOM_ALPHABET_NUMBER:
          sourceString = "1234567890";
        case AZString.RANDOM_ALPHABET_ONLY:
          sourceString += "abcdefghijklmnopqrstuvwxyz";
          if (caseSensitive) {
            sourceString += "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
          }
          break;
      }
      return AZString.getRandom(length, sourceString);
    }

    public static String getRepeat(String string, int length) {
      StringBuilder rtnValue = new StringBuilder();
      for (int cnti=0; cnti<length; cnti++) {
        rtnValue.append(string);
      }
      return rtnValue.toString();
    }

    public String getRepeat(int length) {
      StringBuilder rtnValue = new StringBuilder();
      for (int cnti=0; cnti<length; cnti++) {
        rtnValue.append(this.value);
      }
      return rtnValue.toString();
    }

    /**
     *
     * @param length
     * @return
     * 작성일 : 2014-11-23 이용훈
     */
    public String[] toStringArray(int length) {
      String[] rtnValue = new String[(int)Math.ceil(this.string().length() / length)];

      String src = this.string();
      int idx = 0;
      try {
        while (true) {
          if (src.length() > length) {
            rtnValue[idx] = src.substring(0, length);
            src = src.substring(length);
            idx++;
          } else {
            rtnValue[idx] = src;
            break;
          }
        }
      }
      catch (Exception ex) {
        ex.printStackTrace();
        rtnValue = null;
      }
      finally {
        src = null;
      }

      return rtnValue;
    }

    /**
     *
     * @param source
     * @param length
     * @return
     * 작성일 : 2014-11-23 이용훈
     */
    public static String[] toStringArray(String source, int length) {
      return new AZString(source).toStringArray(length);
    }

    /**
     *
     * @return
     * 작성일 : 2014-11-23 이용훈
     *
     */
    public String toXSSSafeEncoding() {
      String rtnValue;

      rtnValue = this.string();
      rtnValue = rtnValue.replaceAll("<s", "&lt;&#115;");
      rtnValue = rtnValue.replaceAll("<S", "&lt;&#83;");
      rtnValue = rtnValue.replaceAll("<i", "&lt;&#105;");
      rtnValue = rtnValue.replaceAll("<I", "&lt;&#73;");

      return rtnValue;
    }

    /**
     *
     * @param source
     * @return
     * 작성일 : 2014-11-23 이용훈
     */
    public static String toXSSSafeEncoding(String source) {
      return new AZString(source).toXSSSafeEncoding();
    }

    /**
     *
     * @return
     * 작성일 : 2014-11-23 이용훈
     */
    public String toSQLSafeEncoding() {
      String rtnValue;

      rtnValue = this.string();
      rtnValue = rtnValue.replaceAll("&nbsp;", "&nbsp");
      rtnValue = rtnValue.replaceAll("#", "&#35;");      // # 위치 확인!
      rtnValue = rtnValue.replaceAll(";", "&#59;");
      rtnValue = rtnValue.replaceAll("'", "&#39;");
      rtnValue = rtnValue.replaceAll("--", "&#45;&#45;");
      rtnValue = rtnValue.replaceAll("\\\\", "&#92;");
      rtnValue = rtnValue.replaceAll("[*]", "&#42;");
      rtnValue = rtnValue.replaceAll("&nbsp", "&nbsp;");

      return rtnValue;
    }

    /**
     *
     * @param source
     * @return
     * 작성일 : 2014-11-23 이용훈
     */
    public static String toSQLSafeEncoding(String source) {
      return new AZString(source).toSQLSafeEncoding();
    }

    /**
     *
     * @return
     * 작성일 : 2014-11-23 이용훈
     */
    public String toSQLSafeDecoding() {
      String rtnValue;

      rtnValue = this.string();
      rtnValue = rtnValue.replaceAll("&nbsp;", "&nbsp");
      rtnValue = rtnValue.replaceAll("&#42;", "*");
      rtnValue = rtnValue.replaceAll("&#92;", "\\");
      rtnValue = rtnValue.replaceAll("&#45;&#45;", "--");
      rtnValue = rtnValue.replaceAll("&#39;", "'");
      rtnValue = rtnValue.replaceAll("&#35;", "#");
      rtnValue = rtnValue.replaceAll("&#59;", ";");
      rtnValue = rtnValue.replaceAll("&#35;", "#");
      rtnValue = rtnValue.replaceAll("&nbsp", "&nbsp;");

      return rtnValue;
    }

    /**
     *
     * @param source
     * @return
     * 작성일 : 2014-11-23 이용훈
     */
    public static String toSQLSafeDecoding(String source) {
      return new AZString(source).toSQLSafeDecoding();
    }

    /**
     *
     * @return
     * 작성일 : 2014-11-23 이용훈
     */
    public String toHTMLSafeEncoding() {
      String rtnValue;

      rtnValue = this.string();
      rtnValue = rtnValue.replaceAll("&", "&amp;");
      rtnValue = rtnValue.replaceAll("<", "&lt;");
      rtnValue = rtnValue.replaceAll(">", "&gt;");
      rtnValue = rtnValue.replaceAll("\"", "&quot;");

      return rtnValue;
    }

    /**
     *
     * @param source
     * @return
     * 작성일 : 2014-11-23 이용훈
     */
    public static String toHTMLSafeEncoding(String source) {
      return new AZString(source).toHTMLSafeEncoding();
    }

    /**
     *
     * @return
     * 작성일 : 2014-11-23 이용훈
     */
    public String toHTMLSafeDecoding() {
      String rtnValue;

      rtnValue = this.string();
      rtnValue = rtnValue.replaceAll("&lt;", "<");
      rtnValue = rtnValue.replaceAll("&gt;", ">");
      rtnValue = rtnValue.replaceAll("&quot;", "\"");
      rtnValue = rtnValue.replaceAll("&amp;", "&");

      return rtnValue;
    }

    /**
     *
     * @param source
     * @return
     * 작성일 : 2014-11-23 이용훈
     */
    public static String toHTMLSafeDecoding(String source) {
      return new AZString(source).toHTMLSafeDecoding();
    }

    public String toJSONSafeEncoding() {
      String rtnValue;

      rtnValue = this.string();
      rtnValue = rtnValue.replace("\\", "\\\\");
      rtnValue = rtnValue.replace("\"", "\\\"");
      rtnValue = rtnValue.replace("\b", "\\b");
      rtnValue = rtnValue.replace("\f", "\\f");
      rtnValue = rtnValue.replace("\n", "\\n");
      rtnValue = rtnValue.replace("\r", "\\r");
      rtnValue = rtnValue.replace("\t", "\\t");

      return rtnValue;
    }
    public static String toJSONSafeEncoding(String source) {
        return new AZString(source).toJSONSafeEncoding();
    }

    public String toJSONSafeDecoding() {
      String rtnValue;

      rtnValue = this.string();
      rtnValue = rtnValue.replace("\\t", "\t");
      rtnValue = rtnValue.replace("\\r", "\r");
      rtnValue = rtnValue.replace("\\n", "\n");
      rtnValue = rtnValue.replace("\\f", "\f");
      rtnValue = rtnValue.replace("\\b", "\b");
      rtnValue = rtnValue.replace("\\\"", "\"");
      rtnValue = rtnValue.replace("\\\\", "\\");

      return rtnValue;
    }
    public static String toJSONSafeDecoding(String source) {
      return new AZString(source).toJSONSafeDecoding();
    }

    /**
     *
     * @param character
     * @return
     * 작성일 : 2014-11-23 이용훈
     */
    public int getContinuousCharacterCount(char character) {
      int rtnValue = 0;

      if (this.string().length() > 0) {
        if (this.string().charAt(0) == character) {
          String dmyString;
          int cnti;
          for (cnti=0; cnti<this.string().length(); cnti++) {
            dmyString = this.string().substring(cnti, cnti + 1);

            if (!dmyString.equals("" + character)) {
              break;
            }
          }
          rtnValue = cnti;
        }
      }

      return rtnValue;
    }

    /**
     *
     * @param source
     * @param character
     * @return
     * 작성일 : 2014-11-23 이용훈
     */
    public static int getContinuousCharacterCount(String source, char character) {
      return new AZString(source).getContinuousCharacterCount(character);
    }

    /**
     *
     * @param inFormat
     * @param outFormat
     * @return
     * 작성일 : 2014-11-23 이용훈
     */
    public String toFormat(String inFormat, String outFormat) {
      String rtnValue = outFormat;

      try {
        String divString, compString, dmyString;
        int divIndex, compIndex;

        for (int cnti = 0; cnti < rtnValue.length(); cnti++) {
          compString = outFormat.substring(cnti);
          dmyString = compString.substring(0, 1);

          compIndex = getContinuousCharacterCount(compString, dmyString.charAt(0));
          divString = compString.substring(0, compIndex);

          if ((divIndex = inFormat.indexOf(divString)) > -1) {
            rtnValue = rtnValue.replaceAll(divString, this.string().substring(divIndex, divString.length() + divIndex));
          }
          else {
            continue;
          }
          cnti += compIndex - 1;
        }
      }
      catch (Exception ex) {
        ex.printStackTrace();
        rtnValue = null;
      }

      return rtnValue;
    }

    public static class XML {
        private final String xml;

        public XML(String xml) { this.xml = xml; }

        public static AZString.XML init(String xml) { return new AZString.XML(xml); }

        private static String replaceInnerDQ(String string, char replaceChr) {
            return replaceInner(string, "\"", "\"", replaceChr);
        }

        private static String replaceInner(String string, String startString, String endString, char replaceChr) {
          StringBuilder builder = new StringBuilder();

          int dqStartIdx = -1, dqEndIdx = -1, idx = 0, prevIdx = 0;
          int startCharExistAfterDQStart = 0, idxAfterDQStart = -1;   // appended
          boolean inDQ = false;
          while (true) {
            idx = inDQ ? string.indexOf(endString, idx) : string.indexOf(startString, idx);
            if (idx > -1) {
              if (idx == 0 || idx > 0 && string.charAt(idx - 1) != '\\') {
                if (inDQ) {

                  // appended
                  idxAfterDQStart = string.indexOf(startString, startCharExistAfterDQStart == 0 ? (dqStartIdx + 1) : idxAfterDQStart + 1);
                  if (idxAfterDQStart > -1 && idxAfterDQStart < idx && string.charAt(idxAfterDQStart - 1) != '\\') {
                    startCharExistAfterDQStart = 1;
                    idx += 1;
                    continue;
                  }
                  // appended

                  dqEndIdx = idx;
                  inDQ = false;

                  if (dqEndIdx > -1) {
                    builder.append(AZString.getRepeat("" + replaceChr, dqEndIdx - dqStartIdx - 1));
                  }
                }
                else {
                  dqStartIdx = idx;
                  inDQ = true;
                  builder.append(string.substring(prevIdx, idx) + startString);
                }
              }
              prevIdx = idx;
              idx++;
            }
            else {
              builder.append(string.substring(dqEndIdx < 0 ? 0 : dqEndIdx));
              break;
            }
          }
          return builder.toString();
        }

        public AZData getData() { return toAZData(); }
        public AZData toAZData() { return AZString.XML.toAZData(this.xml); }
        public static AZData getData(String xml) { return toAZData(xml); }
        public static AZData toAZData(String xml) {
          AZData rtn_value = new AZData();
          xml = xml.trim();
          if (xml.length() < 3) {
            return rtn_value;
          }
          if (xml.toLowerCase().startsWith("<?xml")) {
            int index_xml = xml.indexOf("?>");
            if (index_xml < 0 || index_xml >= xml.length()) {
              return rtn_value;
            }
            else {
              xml = xml.substring(index_xml + 2);
            }
          }
          if (xml.startsWith("<") && xml.endsWith(">")) {
            xml = xml.substring(1, xml.length()).trim();
            String rmDQStr = replaceInnerDQ(xml, '_');
            //Console.WriteLine("test : " + rmDQStr);

            int idx_closer = rmDQStr.indexOf(">");
            String tag_name = "";
            if (rmDQStr.indexOf(" ") > idx_closer) {
              tag_name = xml.substring(0, idx_closer - 1).trim();
            }
            else {
              tag_name = xml.substring(0, rmDQStr.indexOf(" ")).trim();
            }
            //Console.WriteLine("tag_name : " + tag_name);
            //
            rtn_value.setName(tag_name);

            int idx_ender = rmDQStr.indexOf("</" + tag_name + ">");

            String tag_inner_string = xml.substring(xml.indexOf(" "), idx_closer).trim();
            //Console.WriteLine("tag_inner_string : " + tag_inner_string);

            String tag_inner_string_rpDQ = replaceInnerDQ(tag_inner_string, '_');
            //Console.WriteLine("tag_inner_string_rpDQ : " + tag_inner_string_rpDQ);

            int pre_idx = -1, start_idx = 0;
            while (true) {
              if ((start_idx = tag_inner_string_rpDQ.indexOf(" ", ++pre_idx)) > -1) {
                //Console.WriteLine("tag_inner_string_rpDQ.index : " + pre_idx + " / " + start_idx);
                String attribute = tag_inner_string.substring(pre_idx, start_idx);
                //Console.WriteLine("tag_inner_string_rpDQ.attribute : " + attribute);

                String attribute_name = attribute.substring(0, attribute.indexOf("=")).trim();
                String attribute_value = attribute.substring(attribute.indexOf("=") + 1).trim();
                if (attribute_value.length() > 1) {
                  if (attribute_value.startsWith("\"") && attribute_value.endsWith("\"")) {
                    attribute_value = attribute_value.substring(1, attribute_value.length() - 1);
                  }
                }
                //
                //Console.WriteLine("tag_inner_string_rpDQ.attributes : " + attribute_name + ":" + attribute_value);
                rtn_value.Attribute.add(attribute_name, attribute_value);
                pre_idx = start_idx;
              }
              else if ((start_idx = tag_inner_string_rpDQ.indexOf(" ", ++pre_idx)) < 0 && tag_inner_string_rpDQ.substring(pre_idx + 1).length() > 2) {
                String attribute = tag_inner_string.substring(pre_idx - 1); ;

                String attribute_name = attribute.substring(0, attribute.indexOf("=")).trim();
                String attribute_value = attribute.substring(attribute.indexOf("=") + 1).trim();
                if (attribute_value.length() > 1) {
                  if (attribute_value.startsWith("\"") && attribute_value.endsWith("\"")) {
                    attribute_value = attribute_value.substring(1, attribute_value.length() - 1);
                  }
                }
                //
                //Console.WriteLine("tag_inner_string_rpDQ.attributes.2 : " + attribute_name + ":" + attribute_value);
                rtn_value.Attribute.add(attribute_name, attribute_value);
                break;
              }
              else {
                break;
              }
            }

            String tag_lower_string = xml.substring(idx_closer + 1, idx_ender).trim();
            //Console.WriteLine("tag_lower_string : " + tag_lower_string);

            while (tag_lower_string.trim().length() > 0) {

              tag_lower_string = tag_lower_string.trim();

              if (tag_lower_string.startsWith("<") && !tag_lower_string.startsWith("<!")) {
                //Console.WriteLine("if_1 tag_lower_string : " + tag_lower_string);

                String inner_rmDQStr = replaceInnerDQ(tag_lower_string, '_');
                int inner_idx_closer = inner_rmDQStr.indexOf(">");
                String inner_tag_name = "";
                if (rmDQStr.indexOf(" ") > idx_closer) {
                  inner_tag_name = tag_lower_string.substring(1, idx_closer).trim();
                }
                else {
                  inner_tag_name = tag_lower_string.substring(1, inner_rmDQStr.indexOf(" ")).trim();
                }
                //Console.WriteLine("inner_tag_name : " + inner_tag_name);


                int inner_idx_ender = inner_rmDQStr.indexOf("</" + inner_tag_name + ">");
                String inner_data_string = "";
                if (inner_idx_ender < 0) {
                  inner_data_string = tag_lower_string;
                  tag_lower_string = "";
                }
                else {
                  inner_data_string = tag_lower_string.substring(0, inner_idx_ender + ("</" + inner_tag_name + ">").length());
                  tag_lower_string = tag_lower_string.substring(inner_data_string.length());
                }

                //Console.WriteLine("inner_data_string : " + inner_data_string);
                //Console.WriteLine("tag_lower_string : " + tag_lower_string);

                //
                if (!rtn_value.hasKey(inner_tag_name)) {
                  AZList child_list = new AZList();
                  child_list.add(toAZData(inner_data_string));
                  rtn_value.add(inner_tag_name, child_list);
                  child_list = null;
                }
                else {
                  AZList child_list = rtn_value.getList(inner_tag_name);
                  child_list.add(toAZData(inner_data_string));
                  rtn_value.set(inner_tag_name, child_list);
                  child_list = null;
                }
              }
              else {
                //Console.WriteLine("if_2 tag_lower_string : " + tag_lower_string);

                String inner_rmDQStr = replaceInnerDQ(tag_lower_string, '_');
                //Console.WriteLine("inner_rmDQStr : " + inner_rmDQStr);
                String inner_rmCDataStr = replaceInner(inner_rmDQStr, "<![CDATA[", "]]>", '_');
                //Console.WriteLine("inner_rmCDataStr : " + inner_rmCDataStr);

                int inner_end_index = inner_rmCDataStr.indexOf("<");
                if (inner_end_index < 0) {
                  rtn_value.setValue(tag_lower_string);
                  tag_lower_string = "";
                }
                else {
                  rtn_value.setValue(tag_lower_string.substring(0, inner_end_index));
                  tag_lower_string = tag_lower_string.substring(inner_end_index);
                }
                //System.out.println("value string : " + rtn_value.getName() + " / " + rtn_value.getValue());
              }
            }

            /*
            if (tag_lower_string.startsWith("<")) {
                String inner_tag_name = tag_lower_string.substring(1, tag_lower_string.indexOf(" "));
                rtn_value.add(inner_tag_name, toAZData(tag_lower_string));
            }
            else {
                rtn_value.setValue(tag_lower_string);
            }
            */
          }

          return rtn_value;
        }
    }

    /**
     * JSON 관련된 문자열 처리에 대한 메소드들의 묶음 클래스
     * 작성일 : 2014-11-23 이용훈
     */
    public static class JSON {
        private final String json;

        public JSON(String json) { this.json = json; }

        public static AZString.JSON init(String json) { return new AZString.JSON(json); }

        private static String removeInnerDQ(String string) {
            return removeInner(string, '"', '"');
        }

        private static String removeInner(String string, char pStartChr, char pEndChr) {
            StringBuilder builder = new StringBuilder();

            int dqStartIdx = -1, dqEndIdx = -1, idx = 0, prevIdx = 0;
            boolean inDQ = false;
            while (true) {
                idx = inDQ ? string.indexOf(pEndChr, idx) : string.indexOf(pStartChr, idx);
                if (idx > -1) {
                    if (idx == 0 || idx > 0 && string.charAt(idx - 1) != '\\') {
                        if (inDQ) {
                            dqEndIdx = idx;
                            inDQ = false;

                            if (dqEndIdx > -1) {
                                builder.append(AZString.getRepeat(" ", dqEndIdx - dqStartIdx - 1));
                            }
                        }
                        else {
                            dqStartIdx = idx;
                            inDQ = true;
                            builder.append(string.substring(prevIdx, idx) + pStartChr);
                        }
                    }
                    prevIdx = idx;
                    idx++;
                }
                else {
                    builder.append(string.substring(dqEndIdx < 0 ? 0 : dqEndIdx));
                    break;
                }
            }
            return builder.toString();
        }

        public AZList getList() { return toAZList(); }
        public AZList toAZList() { return AZString.JSON.toAZList(this.json); }

        public static AZList getList(String json) {
          return AZString.JSON.toAZList(json);
        }
        public static AZList toAZList(String json) {
          AZList rtnValue = new AZList();
          json = json.trim();
          if (json.charAt(0) == '[' && json.charAt(json.length() - 1) == ']') {
            json = json.substring(1, json.length() - 1).trim();
            String rmDQStr = removeInnerDQ(json);
            String rmMStr = removeInner(rmDQStr, '[', ']');
            String rmStr = removeInner(rmMStr, '{', '}');

            int idx = 0, preIdx = 0;
            while (true) {
              idx = rmStr.indexOf(",", idx);
              if (idx > -1 || (idx == -1 && preIdx > 0)) {
                String dataStr = "";
                if (idx > -1) {
                  dataStr = json.substring(preIdx, idx);
                }
                else if (idx == -1) {
                  dataStr = json.substring(preIdx + 1);
                }
                dataStr = dataStr.trim();
                if (dataStr.charAt(0) == ',') dataStr = dataStr.substring(1).trim();
                if (dataStr.charAt(dataStr.length() - 1) == ',') dataStr = dataStr.substring(0, dataStr.length()).trim();

                if (dataStr.charAt(0) == '{') rtnValue.add(getData(dataStr));

                if (idx == -1) break;

                preIdx = idx;
                idx++;
              }
              else {
                if (preIdx < 1 && idx < 0) {
                  String dataStr = rmStr.trim();
                  if (dataStr.length() > 1) {
                    if (dataStr.charAt(0) == '{') {
                      dataStr = json.substring(0);
                      rtnValue.add(getData(dataStr));
                    }
                  }
                }
                break;
              }
            }
          }
          return rtnValue;
        }


        public AZData getData() { return toAZData(); }
        public AZData toAZData() { return AZString.JSON.toAZData(this.json); }

        public static AZData getData(String json) {
            return AZString.JSON.toAZData(json);
        }
        public static AZData toAZData(String json) {
          AZData rtnValue = new AZData();
          json = json.trim();
          if (json.length() < 3) {
            return rtnValue;
          }
          if (json.charAt(0) == '{' && json.charAt(json.length() - 1) == '}') {
            json = json.substring(1, json.length() - 1).trim();
            String rmDQStr = removeInnerDQ(json);
            String rmMStr = removeInner(rmDQStr, '[', ']');
            String rmStr = removeInner(rmMStr, '{', '}');

            int idx = 0, preIdx = 0;
            while (true) {
              idx = rmStr.indexOf(",", idx);
              if (idx == -1 && preIdx == 0) {
                String dataStr = "";
                dataStr = json;
                dataStr = dataStr.trim();

                int key_value_idx = dataStr.indexOf(":");
                String key = dataStr.substring(0, key_value_idx).trim();
                String valueString = dataStr.substring(key_value_idx + 1).trim();

                if (key.charAt(0) == '"' || key.charAt(0) == '\'') key = key.substring(1, key.length() - 1);

                Object value = null;
                if (valueString.charAt(0) == '{') {
                  value = (AZData)getData(valueString);
                }
                else if (valueString.charAt(0) == '[') {
                  value = (AZList)getList(valueString);
                }
                else if (valueString.charAt(0) == '"' || valueString.charAt(0) == '\'') {
                  value = (String)valueString.substring(1, valueString.length() - 1);
                }
                else {
                  value = valueString;
                }

                rtnValue.add(key, value);

                break;
              }
              else if (idx > -1 || (idx == -1 && preIdx > 0)) {
                String dataStr = "";
                if (idx > -1) {
                  dataStr = json.substring(preIdx, idx);
                }
                else if (idx == -1) {
                  dataStr = json.substring(preIdx + 1);
                }
                dataStr = dataStr.trim();
                if (dataStr.charAt(0) == ',') dataStr = dataStr.substring(1).trim();
                if (dataStr.charAt(dataStr.length() - 1) == ',') dataStr = dataStr.substring(0, dataStr.length()).trim();

                int key_value_idx = dataStr.indexOf(":");
                String key = dataStr.substring(0, key_value_idx).trim();
                String valueString = dataStr.substring(key_value_idx + 1).trim();

                if (key.charAt(0) == '"' || key.charAt(0) == '\'') key = key.substring(1, key.length() - 1);

                Object value = null;
                if (valueString.charAt(0) == '{') {
                  value = (AZData)getData(valueString);
                }
                else if (valueString.charAt(0) == '[') {
                  value = (AZList)getList(valueString);
                }
                else if (valueString.charAt(0) == '"' || valueString.charAt(0) == '\'') {
                  value = (String)valueString.substring(1, valueString.length() - 1);
                }
                else {
                  value = valueString;
                }

                rtnValue.add(key, value);

                if (idx == -1) break;

                preIdx = idx;
                idx++;
              }
              else {
                break;
              }
            }
          }
          return rtnValue;
        }
    }

    public static class HTML {
        private final String html;

        public static class TAG {
          private final String tag;
          public TAG(String tag) {
            this.tag = " " + tag.trim() + " ";
          }

          public String getTag() {
            return this.tag;
          }

          public String getProperty(String property) {
            String rtnValue = null;

            int propLength = property.length();

            int idx = this.tag.toLowerCase().indexOf(" " + property.toLowerCase() + "=");
            int closeIdx = idx;

            if (idx > -1) {
              if (this.tag.charAt(idx + propLength + 2) == '"') {
                // tag가 "로 감싸져있는경우
                closeIdx = this.tag.indexOf("\"", idx + propLength + 2 + 1);
                rtnValue = this.tag.substring(idx + propLength + 3, closeIdx);
              }
              else {
                // tag가 "로 감싸지지 않은 경우
                closeIdx = this.tag.indexOf(" ", idx + propLength + 2);
                rtnValue = this.tag.substring(idx + propLength + 2, closeIdx);
              }
            }

            return rtnValue;
          }

        }

        public HTML(String html) {
          this.html = html;
        }

        public static AZString.HTML init(String html) { return new AZString.HTML(html); }

        public String removeTag() {
          return AZString.HTML.removeTag(this.html);
        }

        /**
         *
         * @param html
         * @return
         */
        public static String removeTag(String html) {
          String rtnValue = "";
          int openTagIdx, closeTagIdx;

          try {
            openTagIdx = html.indexOf("<");
            closeTagIdx = html.indexOf(">");

            if (closeTagIdx < openTagIdx) {
              html = html.substring(openTagIdx);
            }

            while ((openTagIdx = html.indexOf("<")) >= 0) {
              closeTagIdx = html.indexOf(">", openTagIdx);

              if (closeTagIdx >= 0) {
                html = html.replace(html.substring(openTagIdx, closeTagIdx + 1), "");
              }
              else {
                html = html.substring(0, openTagIdx);
              }
            }
            rtnValue = html;
          }
          catch (Exception ex) {
            ex.printStackTrace();
          }
          return rtnValue;
        }

        /**
         *
         * @param tag
         * @return
         */
        public AZString.HTML.TAG[] getTags(String tag) {
          int idx = 0;
          int count = 0;
          int htmlLength = this.html.length();
          tag = "<" + tag;

          ArrayList array = new ArrayList();

          while ((idx = this.html.toLowerCase().indexOf(tag.toLowerCase(), idx)) > -1) {
            if (idx + 1 > htmlLength) {
              break;
            }
            int closeIdx = this.html.indexOf(">", idx + 1);

            if (idx + 1 + tag.length() >= closeIdx) {
              idx += (1 + tag.length());
              continue;
            }

            String tagString = this.html.substring(idx + 1 + tag.length(), closeIdx);
            if (tagString.endsWith("/")) {
              tagString = tagString.substring(0, tagString.length() - 1);
            }
            tagString = tagString.trim();

            array.add(new AZString.HTML.TAG(tagString));

            count++;
            idx += tagString.length();
          }

          AZString.HTML.TAG[] rtnValue = new AZString.HTML.TAG[array.size()];
          for (int cnti=0; cnti<array.size(); cnti++) {
            rtnValue[cnti] = (AZString.HTML.TAG)array.get(cnti);
          }

          array = null;

          return rtnValue;
        }
    }
}
