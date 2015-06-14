package models;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;
import play.Logger;

import java.util.*;

/**
 * Created by rmasal on 2015-06-09.
 */
public class OrderHelper {
    static String testPosId = "145227";
    static String testSecondKey = "13a980d4f851f3d9a1cfc792fb1f5e50";
    static String posId = "192231";
    static String secondKey = "5cfc60882859e248b9d81b026a0ce1ee";

    public static class Order {
       public String url;
       public JsonNode node;

        public Order(String url, JsonNode node) {
            this.url = url;
            this.node = node;
        }
    }

    public static class OrderString {
         public String url;
         public String order;
        public String header;

        public OrderString(String url, String order, String header) {
            this.url = url;
            this.order = order;
            this.header = header;
        }
    }

    public List<String> sortValuesByItsName(Map form) {
        List<String> valuesList = new ArrayList<>();
        SortedSet<String> keys = new TreeSet(form.keySet());
        for (String key : keys) {
            String value = form.get(key).toString();
            valuesList.add(value);
        }
        return valuesList;
    }

    public String getFormSignature(Map formularz) {
        StringBuilder result = new StringBuilder();
        StringBuilder content = new StringBuilder();
        List<String> sortedValues = sortValuesByItsName(formularz);
        for (String value : sortedValues) {
            content.append(value);
        }
        content.append(secondKey);
        result.append("signature=" + digestMD5(content.toString()) + ";");
        result.append("algorithm=" + "MD5;");
        result.append("sender=" + posId);
        return result.toString();
    }

    public String digestMD5(String parameters) {
        return DigestUtils.md5Hex(parameters);

    }

    public String getHeaderSignature() {
        String content = (new StringBuffer(posId).append(":").append(secondKey)).toString();
        byte[] bytes = content.getBytes();
        String authVal = "Basic " + new String(new Base64().encode(bytes));
        return authVal;
    }

    public JsonNode getJsonNodeTest() {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode testNode = null;
        try {
            testNode = objectMapper.readTree("{\"notifyUrl\": \"http://shop.com\", " +
                    "\"customerIp\": \"123.123.123.123\"," +
                    "\"merchantPosId\":  \"" + posId + "\"," +
                    "\"description\": \"Your order description\"," +
                    "\"currencyCode\": \"PLN\"," +
                    "\"totalAmount\": \"1000\"," +
                    "\"products\": [" +
                    "{" +
                    "\"name\": \"Product 1\"," +
                    "\"unitPrice\": \"400\"," +
                    "\"quantity\": \"1\"" +
                    "}," +
                    "{" +
                    "\"name\": \"Product 2\"," +
                    "\"unitPrice\": \"600\"," +
                    "\"quantity\": \"1\"" +
                    "}" +
                    "]" +
                    "}");
        } catch (Exception e) {
            Logger.error(e.getMessage());
        }
        return testNode;
    }

//    public String getOrderString() {
//        return "{\"notifyUrl\": \"https://your.eshop.com/notify\", " +
//                "\" customerIp\": \"127.0.0.1\"," +
//                "\" merchantPosId\": \"192194\"," +
//                "\" description\": \"Your order description\"," +
//                "\" currencyCode\": \"PLN\"," +
//                "\" totalAmount\": \"1000\"," +
//                "\" products\": [" +
//                "{" +
//                "\" name\": \"Product 1\"," +
//                "\" unitPrice\": \"400\"," +
//                "\" quantity\": \"1\"" +
//                "}," +
//                "{" +
//                "\" name\": \"Product 2\"," +
//                "\" unitPrice\": \"600\"," +
//                "\" quantity\": \"1\"" +
//                "}" +
//                "]" +
//                "}";
//    }

}

