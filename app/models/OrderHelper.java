package models;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;
import play.Logger;
import play.data.DynamicForm;
import play.data.Form;
import play.mvc.BodyParser;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by rmasal on 2015-06-09.
 */
public class OrderHelper {
//    static String testPosId = "145227";
//    static String testSecondKey = "13a980d4f851f3d9a1cfc792fb1f5e50";
    static String posId = "192231";
    static String secondKey = "5cfc60882859e248b9d81b026a0ce1ee";
    public List<Integer> pricesList = new ArrayList<>();

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


    public void storePartialPrices(String amount, String price) {
        int iprice = Integer.parseInt(price);
        iprice = iprice * 100;
        int iamount = Integer.parseInt(amount);
        int sum = iprice * iamount;
        pricesList.add(sum);
    }

    public String getTotalPrice () {
        Integer price = 0;
        for (int prices : pricesList) {
            price += prices;
        }
            price.toString();
        return price.toString();
    }

    public String buildFormBody (JsonNode materials, String totalPrice){
        StringBuilder formBody = new StringBuilder();
        formBody.append("<input ").append("type=\"hidden\"").append(" name=\"customerIp\"")   .append(" value=\"").append("123.123.123.123\">");
        formBody.append("<input ").append("type=\"hidden\"").append(" name=\"merchantPosId\"").append(" value=\"").append(posId).append("\"").append(">");
        formBody.append("<input ").append("type=\"hidden\"").append(" name=\"description\"")  .append(" value=\"").append("description\">");
        formBody.append("<input ").append("type=\"hidden\"").append(" name=\"totalAmount\"")  .append(" value=\"").append(totalPrice).append("\"").append(">");
        formBody.append("<input ").append("type=\"hidden\"").append(" name=\"currencyCode\"") .append(" value=\"").append("PLN\">");
        formBody.append("<input ").append("type=\"hidden\"").append(" name=\"notifyUrl\"")    .append(" value=\"").append("http://shop.com/notify\">");
        formBody.append("<input ").append("type=\"hidden\"").append(" name=\"continueUrl\"")  .append(" value=\"").append("http://google.com\">");
        int index = 0;
        for (JsonNode material: materials) {
            formBody.append("<input ").append("type=\"hidden\"").append(" name=").append("\"products[").append(index + "]").append(".name\"")
                    .append(" value=").append("\"").append(material.get(0).asText()).append("\"").append(">");
            formBody.append("<input ").append("type=\"hidden\"").append(" name=").append("\"products[").append(index + "]").append(".unitPrice\"")
                    .append(" value=").append("\"").append(getZloty(material.get(1).asText())).append("\"").append(">");
            formBody.append("<input ").append("type=\"hidden\"").append(" name=").append("\"products[").append(index + "]").append(".quantity\"")
                    .append(" value=").append("\"").append(material.get(2).asText()).append("\"").append(">");
            index ++;
        }
//        System.out.println(formBody.toString());
//        String form = formBody.toString();
//        BodyParser.MultipartFormData prepareToSort = Form.form().bindFromRequest(form);
//        Pattern pattern = Pattern.compile("(\\w+)=\"*((?<=\")[^\"]+(?=\")|([^\\s]+))\"*");
//
//        Matcher matcher = pattern.matcher(form);
//
//        Map<String, String> map;
//        while(matcher.find()){
//            StringBuilder stringBuilder = new StringBuilder();
//            stringBuilder.append(matcher.group(1)).append("=").append(matcher.group(2));
//            String str = stringBuilder.toString().replaceAll("\\btype=hidden\\b", "");
//            str = str.replaceAll("\\btype=hidden\\b", "");
//            System.out.println(str);
//        }
//        System.out.println(macher.toString());
        return formBody.toString();
    }

    public String getZloty (String price) {
        Integer iprice = Integer.parseInt(price);
        iprice = iprice * 100;
        return iprice.toString();
    }

    public Map<String, String> bulildDataForSignature (JsonNode jsonNode) {
        Map <String, String> mapData = new HashMap<>();
        mapData.put("customerIp", "123.123.123.123");
        mapData.put("merchantPosId", posId);
        mapData.put("description", "description");
        mapData.put("totalAmount", getTotalPrice());
        mapData.put("currencyCode", "PLN");
        mapData.put("notifyUrl", "http://shop.com/notify");
        mapData.put("continueUrl", "http://google.com");
        int index = 0;
        for (JsonNode node: jsonNode) {
            mapData.put("products[" + index + "].name", node.get(0).asText());
            mapData.put("products[" + index + "].unitPrice", getZloty(node.get(1).asText()));
            mapData.put("products[" + index + "].quantity", node.get(2).asText());
        index ++;
        }
        return mapData;
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

