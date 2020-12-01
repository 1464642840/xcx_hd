package com.hxh.basic.project.utils.string;



import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.node.TextNode;
import javafx.scene.Node;
import jdk.nashorn.internal.parser.Lexer;
import jdk.nashorn.internal.runtime.ParserException;
import org.apache.commons.lang3.StringUtils;
import org.wltea.analyzer.core.IKSegmenter;
import org.wltea.analyzer.core.Lexeme;

import java.io.IOException;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.baomidou.mybatisplus.generator.config.ConstVal.UTF8;

/**
 * 字符串的帮助类，提供静态方法，不可以实例化。
 */
public class StrUtils {
    /**
     * 禁止实例化
     */
    private StrUtils() {
    }

    public static byte[] addBytes(byte[] data1, byte[] data2) {
        byte[] data3 = new byte[data1.length + data2.length];
        System.arraycopy(data1, 0, data3, 0, data1.length);
        System.arraycopy(data2, 0, data3, data1.length, data2.length);
        return data3;

    }
    public static String transformName(String originName){
        if(originName != null && originName.length() > 0 ){
            return originName.substring(0,1).toLowerCase() + originName.substring(1) ;
        }
        return originName;
    }

    /**
     * 处理url
     * <p>
     * url为null返回null，url为空串或以http://或https://开头，则加上http://，其他情况返回原参数。
     *
     * @param url
     * @return
     */
    public static String handelUrl(String url) {
        if (url == null) {
            return null;
        }
        url = url.trim();
        if (url.equals("") || url.startsWith("http://") || url.startsWith("https://")) {
            return url;
        } else {
            return "http://" + url.trim();
        }
    }

    /**
     * 将数组分隔
     *
     * @param string
     */
    public static String join(Object[] strs, String string) {
        try {
            StringBuilder sb = new StringBuilder();
            for (Object object : strs) {
                sb.append(object + string);
            }
            return sb.substring(0, sb.length() - string.length());

        } catch (Exception e) {
        }
        return null;
    }

    public static String join(Object[] strs, String string, int start, int end) {
        try {
            end = end>strs.length?strs.length:end;
            start = start>strs.length?strs.length:start;
            StringBuilder sb = new StringBuilder();
            for (int i=start;i<end;i++) {
                sb.append(strs[i] + string);
            }
            return sb.substring(0, sb.length() - string.length());

        } catch (Exception e) {
        }
        return null;
    }


    /***
     * 去除字符串中的所有符号
     * */

    public static String clearAllSymbol(String source) {
        if (isNull(source)) {
            return "";
        }
        String regEx = "[\n`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。， 、？\r\t]";

        //可以在中括号内加上任何想要替换的字符，实际上是一个正则表达式

        String aa = "";//这里是将特殊字符换为aa字符串,""代表直接去掉

        Pattern p = Pattern.compile(regEx);

        Matcher m = p.matcher(source);//这里把想要替换的字符串传进来

        String newString = m.replaceAll(aa).trim();
        return newString;

    }


    /**
     * 分割并且去除空格
     *
     * @param str  待分割字符串
     * @param sep  分割符
     * @param sep2 第二个分隔符
     * @return 如果str为空，则返回null。
     */
    public static String[] splitAndTrim(String str, String sep, String sep2) {
        if (StringUtils.isBlank(str)) {
            return null;
        }
        if (!StringUtils.isBlank(sep2)) {
            str = StringUtils.replace(str, sep2, sep);
        }
        String[] arr = StringUtils.split(str, sep);
        // trim
        for (int i = 0, len = arr.length; i < len; i++) {
            arr[i] = arr[i].trim();
        }
        return arr;
    }

    /**
     * 文本转html
     *
     * @param txt
     * @return
     */
    public static String txt2htm(String txt) {
        if (StringUtils.isBlank(txt)) {
            return txt;
        }
        StringBuilder sb = new StringBuilder((int) (txt.length() * 1.2));
        char c;
        boolean doub = false;
        for (int i = 0; i < txt.length(); i++) {
            c = txt.charAt(i);
            if (c == ' ') {
                if (doub) {
                    sb.append(' ');
                    doub = false;
                } else {
                    sb.append("&nbsp;");
                    doub = true;
                }
            } else {
                doub = false;
                switch (c) {
                    case '&':
                        sb.append("&amp;");
                        break;
                    case '<':
                        sb.append("&lt;");
                        break;
                    case '>':
                        sb.append("&gt;");
                        break;
                    case '"':
                        sb.append("&quot;");
                        break;
                    case '\n':
                        sb.append("<br/>");
                        break;
                    default:
                        sb.append(c);
                        break;
                }
            }
        }
        return sb.toString();
    }



    public static List<String> getVideoSrc(String htmlCode) {
        List<String> imageSrcList = new ArrayList<String>();
        String regular = "<video(.*?)src=\"(.*?)\"(.*?)</video>";
        String video_pre = "<video(.*?)src=\"";
        String video_sub = "\"(.*?)</video>";
        Pattern p = Pattern.compile(regular, Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(htmlCode);
        String src = null;
        while (m.find()) {
            src = m.group();
            src = src.replaceAll(video_pre, "").replaceAll(video_sub, "").trim();
            imageSrcList.add(src);
        }
        return imageSrcList;
    }

    /**
     * 剪切文本。如果进行了剪切，则在文本后加上"..."
     *
     * @param s   剪切对象。
     * @param len 编码小于256的作为一个字符，大于256的作为两个字符。
     * @return
     */
    public static String textCut(String s, int len, String append) {
        if (s == null) {
            return null;
        }
        int slen = s.length();
        if (slen <= len) {
            return s;
        }
        // 最大计数（如果全是英文）
        int maxCount = len * 2;
        int count = 0;
        int i = 0;
        for (; count < maxCount && i < slen; i++) {
            if (s.codePointAt(i) < 256) {
                count++;
            } else {
                count += 2;
            }
        }
        if (i < slen) {
            if (count > maxCount) {
                i--;
            }
            if (!StringUtils.isBlank(append)) {
                if (s.codePointAt(i - 1) < 256) {
                    i -= 2;
                } else {
                    i--;
                }
                return s.substring(0, i) + append;
            } else {
                return s.substring(0, i);
            }
        } else {
            return s;
        }
    }




    /**
     * @param keyword 源词汇
     * @param smart   是否智能分词
     * @return 分词词组(, 拼接)
     */
    public static String getKeywords(String keyword, boolean smart) {
        StringReader reader = new StringReader(keyword);
        IKSegmenter iks = new IKSegmenter(reader, smart);
        StringBuilder buffer = new StringBuilder();
        try {
            Lexeme lexeme;
            while ((lexeme = iks.next()) != null) {
                buffer.append(lexeme.getLexemeText()).append(',');
            }
        } catch (IOException e) {
        }
        // 去除最后一个,
        if (buffer.length() > 0) {
            buffer.setLength(buffer.length() - 1);
        }
        return buffer.toString();
    }

    /**
     * p换行
     *
     * @param inputString
     * @return
     */
    public static String removeHtmlTagP(String inputString) {
        if (inputString == null)
            return null;
        String htmlStr = inputString; // 含html标签的字符串
        String textStr = "";
        Pattern p_script;
        Matcher m_script;
        Pattern p_style;
        Matcher m_style;
        Pattern p_html;
        Matcher m_html;
        try {
            // 定义script的正则表达式{或<script[^>]*?>[\\s\\S]*?<\\/script>
            String regEx_script = "<[\\s]*?script[^>]*?>[\\s\\S]*?<[\\s]*?\\/[\\s]*?script[\\s]*?>";
            // 定义style的正则表达式{或<style[^>]*?>[\\s\\S]*?<\\/style>
            String regEx_style = "<[\\s]*?style[^>]*?>[\\s\\S]*?<[\\s]*?\\/[\\s]*?style[\\s]*?>";
            String regEx_html = "<[^>]+>"; // 定义HTML标签的正则表达式
            p_script = Pattern.compile(regEx_script, Pattern.CASE_INSENSITIVE);
            m_script = p_script.matcher(htmlStr);
            htmlStr = m_script.replaceAll(""); // 过滤script标签
            p_style = Pattern.compile(regEx_style, Pattern.CASE_INSENSITIVE);
            m_style = p_style.matcher(htmlStr);
            htmlStr = m_style.replaceAll(""); // 过滤style标签
            htmlStr.replace("</p>", "\n");
            p_html = Pattern.compile(regEx_html, Pattern.CASE_INSENSITIVE);
            m_html = p_html.matcher(htmlStr);
            htmlStr = m_html.replaceAll(""); // 过滤html标签
            textStr = htmlStr;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return textStr;// 返回文本字符串
    }

    public static String removeHtmlTag(String inputString) {
        if (inputString == null)
            return null;
        String htmlStr = inputString; // 含html标签的字符串
        String textStr = "";
        Pattern p_script;
        Matcher m_script;
        Pattern p_style;
        Matcher m_style;
        Pattern p_html;
        Matcher m_html;
        try {
            // 定义script的正则表达式{或<script[^>]*?>[\\s\\S]*?<\\/script>
            String regEx_script = "<[\\s]*?script[^>]*?>[\\s\\S]*?<[\\s]*?\\/[\\s]*?script[\\s]*?>";
            // 定义style的正则表达式{或<style[^>]*?>[\\s\\S]*?<\\/style>
            String regEx_style = "<[\\s]*?style[^>]*?>[\\s\\S]*?<[\\s]*?\\/[\\s]*?style[\\s]*?>";
            String regEx_html = "<[^>]+>"; // 定义HTML标签的正则表达式
            p_script = Pattern.compile(regEx_script, Pattern.CASE_INSENSITIVE);
            m_script = p_script.matcher(htmlStr);
            htmlStr = m_script.replaceAll(""); // 过滤script标签
            p_style = Pattern.compile(regEx_style, Pattern.CASE_INSENSITIVE);
            m_style = p_style.matcher(htmlStr);
            htmlStr = m_style.replaceAll(""); // 过滤style标签
            p_html = Pattern.compile(regEx_html, Pattern.CASE_INSENSITIVE);
            m_html = p_html.matcher(htmlStr);
            htmlStr = m_html.replaceAll(""); // 过滤html标签
            textStr = htmlStr;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return textStr;// 返回文本字符串
    }

    /**
     * 检查字符串中是否包含被搜索的字符串。被搜索的字符串可以使用通配符'*'。
     *
     * @param str
     * @param search
     * @return
     */
    public static boolean contains(String str, String search) {
        if (StringUtils.isBlank(str) || StringUtils.isBlank(search)) {
            return false;
        }
        String reg = StringUtils.replace(search, "*", ".*");
        Pattern p = Pattern.compile(reg);
        return p.matcher(str).matches();
    }

    public static boolean containsKeyString(String str) {
        if (StringUtils.isBlank(str)) {
            return false;
        }
        if (str.contains("'") || str.contains("\"") || str.contains("\r") || str.contains("\n") || str.contains("\t")
                || str.contains("\b") || str.contains("\f")) {
            return true;
        }
        return false;
    }

    public static String addCharForString(String str, int strLength, char c, int position) {
        int strLen = str.length();
        if (strLen < strLength) {
            while (strLen < strLength) {
                StringBuffer sb = new StringBuffer();
                if (position == 1) {
                    // 右補充字符c
                    sb.append(c).append(str);
                } else {
                    // 左補充字符c
                    sb.append(str).append(c);
                }
                str = sb.toString();
                strLen = str.length();
            }
        }
        return str;
    }

    // 将""和'转义
    public static String replaceKeyString(String str) {
        if (containsKeyString(str)) {
            return str.replace("'", "\\'").replace("\"", "\\\"").replace("\r", "\\r").replace("\n", "\\n")
                    .replace("\t", "\\t").replace("\b", "\\b").replace("\f", "\\f");
        } else {
            return str;
        }
    }

    // 单引号转化成双引号
    public static String replaceString(String str) {
        if (containsKeyString(str)) {
            return str.replace("'", "\"").replace("\"", "\\\"").replace("\r", "\\r").replace("\n", "\\n")
                    .replace("\t", "\\t").replace("\b", "\\b").replace("\f", "\\f");
        } else {
            return str;
        }
    }

    public static String getSuffix(String str) {
        int splitIndex = str.lastIndexOf(".");
        return str.substring(splitIndex + 1);
    }

    /**
     * 数字转字母
     *
     * @param num
     * @return
     */
    public static String numberToLetter(int num) {//数字转字母 1-26 ： A-Z
        if (num <= 0) {
            return null;
        }
        String letter = "";
        num--;
        do {
            if (letter.length() > 0) {
                num--;
            }
            letter = ((char) (num % 26 + (int) 'A')) + letter;
            num = (int) ((num - num % 26) / 26);
        } while (num > 0);

        return letter;
    }

    /**
     * 字母转数字
     *
     * @param letter
     * @return
     */
    public static Integer letterToNumber(String letter) {//字母转数字  A-Z ：1-26
        int length = letter.length();
        int num = 0;
        int number = 0;
        for (int i = 0; i < length; i++) {
            char ch = letter.charAt(length - i - 1);
            num = (int) (ch - 'A' + 1);
            num *= Math.pow(26, i);
            number += num;
        }
        return number;
    }

    /**
     * 补齐不足长度
     *
     * @param length 长度
     * @param number 数字
     * @return
     */
    public static String lpad(int length, Long number) {
        String f = "%0" + length + "d";
        return String.format(f, number);
    }

    /**
     * 保留两位小数（四舍五入）
     *
     * @param value
     * @return
     */
    public static Double retainTwoDecimal(double value) {
        long l1 = Math.round(value * 100); // 四舍五入
        double ret = l1 / 100.0; // 注意:使用 100.0 而不是 100
        return ret;
    }

    /**
     * 将容易引起xss漏洞的半角字符直接替换成全角字符
     *
     * @param s
     * @return
     */
    public static String xssEncode(String s) {
        if (s == null || s.equals("")) {
            return s;
        }
        // < > ' " \ / # &
        s = s.replaceAll("<", "&lt;").replaceAll(">", "&gt;");
        s = s.replaceAll("\\(", "&#40;").replaceAll("\\)", "&#41;");
        s = s.replaceAll("'", "&#39;");
        s = s.replaceAll("eval\\((.*)\\)", "");
        s = s.replaceAll("[\\\"\\\'][\\s]*javascript:(.*)[\\\"\\\']", "\"\"");
        s = s.replaceAll("script", "");
        s = s.replaceAll("#", "＃");
        s = s.replaceAll("%", "％");
        try {
            s = URLDecoder.decode(s, UTF8);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return s;
    }

    public static boolean isNull(String str) {

        if (str == null) {
            return true;
        }
        if ("".equals(str.trim()) || str.trim().length() == 0 || "null".equals(str)) {
            return true;
        }
        return false;
    }

    public static String getPhone(String phone) {
        phone = phone.trim();
        if (phone.length() == 11 && phone.startsWith("1")) {
            return phone;
        }
        if (phone.contains("/")) {
            String[] split = phone.split("/");
            for (String p : split) {
                if (p.length() == 11 && p.startsWith("1")) {
                    return p;
                }
            }

        }
        return "";
    }

    public static boolean isNumber(String str) {
        if (str == null) {
            return false;
        }
        try {
            Double.parseDouble(str);
        } catch (NumberFormatException e) {
            return false;
        }

        return true;
    }


    public static String jsonToParam(HashMap<String,Object> object) {
        StringBuilder sb = new StringBuilder();
        try {
            Iterator<String> keys = object.keySet().iterator();
            while (keys.hasNext()) {
                String key = keys.next() + "";
                sb.append(key + "=" + object.get(key));
                if (keys.hasNext()) {
                    sb.append("&");
                }
            }

            return sb.toString();
        } catch (Exception e) {
            return "";
        }

    }

    public static Set<String> stringTOSet(String source, String separator, String left, String right) {
        HashSet<String> set = new HashSet<>();
        if (isNull(source)) {
            return set;
        }
        for (String s : source.split(separator)) {
            set.add(left + s + right);

        }
        return set;
    }


    public static String mapToString(Map<String, String[]> map) {
        try {
            Set<String> keySet = map.keySet();
            //将set集合转换为数组
            String[] keyArray = keySet.toArray(new String[keySet.size()]);
            //给数组排序(升序)
            Arrays.sort(keyArray);
            //因为String拼接效率会很低的，所以转用StringBuilder
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < keyArray.length; i++) {
                // 参数值为空，则不参与签名 这个方法trim()是去空格
                if ((String.valueOf(map.get(keyArray[i]))).trim().length() > 0) {
                    sb.append(keyArray[i]).append(":").append(String.valueOf(Arrays.toString(map.get(keyArray[i]))).trim());
                }
                if (i != keyArray.length - 1) {
                    sb.append(",");
                }
            }
            return sb.toString();
        } catch (Exception e) {
            System.out.printf(map.toString());
            e.printStackTrace();
            return "";
        }
    }


    public static String clearHTML(String url) {

        return url;
    }
}
