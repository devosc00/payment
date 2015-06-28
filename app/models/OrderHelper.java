package models;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;
import play.Logger;
import play.Play;
import org.kocakosm.pitaya.security.*;

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
    public String notifySignature = "c33a38d89fb60f873c039fcec3a14743";
    public String transId = "PGlkX2NsaWVudD43ODExMDk1NDIzPC9pZF9jbGllbnQ+PGlkX3RyYW5zPjAwMDAwMDAzMTM8L2lkX3RyYW5zPjxkYXRlX3ZhbGlkPjI1LTA2LTIwMTUgMTI6MzQ6MDQ8L2RhdGVfdmFsaWQ+PGFtb3VudD40Miw3MDwvYW1vdW50PjxjdXJyZW5jeT5QTE48L2N1cnJlbmN5PjxlbWFpbD48L2VtYWlsPjxhY2NvdW50Pjk0OTA0MzEwNTQ2NTE5ODExNDAyMjUwNzA0PC9hY2NvdW50PjxhY2NuYW1lPlN5c3RoZXJtLUluZm8gU0tMRVAgVGVzdG93eV5OTV42MC0xODleWlBeUG96bmFuXkNJXnVsLiBabG90b3dza2EgMjdeU1ReUG9sc2thXkNUXjwvYWNjbmFtZT48YmFja3BhZ2U+aHR0cDovLzc3LjY1LjE4LjM0OjgwODIvcGxhdG5vc2Mvc3VrY2VzPC9iYWNrcGFnZT48YmFja3BhZ2VyZWplY3Q+aHR0cDovLzc3LjY1LjE4LjM0OjgwODIvcGxhdG5vc2MvcG9yYXprYTwvYmFja3BhZ2VyZWplY3Q+PGhhc2g+Mjg1NzE3YTY0ZGQ0OGUwNTFhNDBjNWRkMWZjOTE1NjkxNzIzY2QxZDwvaGFzaD4=";

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
        result.append("signature=" + DigestUtils.md5Hex(content.toString()) + ";");
        result.append("algorithm=" + "MD5;");
        result.append("sender=" + posId);
        return result.toString();
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


    public String getNotificationWithKey(JsonNode notification) {
        String jsonAsString = notification.toString();
        jsonAsString = jsonAsString + secondKey;
        System.out.println(jsonAsString);
        return jsonAsString;
    }

    public String getCountedSignature(String header, JsonNode notificationBody) {
        String hashFunction = header.toLowerCase();
        if (hashFunction.contains("md5")) {
            return DigestUtils.md5Hex(getNotificationWithKey(notificationBody));
        } else if (hashFunction.contains("sha-1")) {
            return DigestUtils.sha1Hex(getNotificationWithKey(notificationBody));
        } else if (hashFunction.contains("sha-256")) {
            return DigestUtils.sha256Hex(getNotificationWithKey(notificationBody));
        } else if (hashFunction.contains("sha-384")) {
            return DigestUtils.sha384Hex(getNotificationWithKey(notificationBody));
        } else if (hashFunction.contains("sha-512")) {
            return DigestUtils.sha512Hex(getNotificationWithKey(notificationBody));
        } else if (hashFunction.contains("sha3-256")) {
            return "Nie wspierana funkcja skrótu sha-3";
        } else {
            return "Nie znaleziono funkcji skrótu";
        }
    }

    public String getHashFromHeaderSignature (String signature) {
        Pattern pattern = Pattern.compile("signature=(.*?);");
        Matcher matcher = pattern.matcher(signature);
        if (matcher.find()) {
            System.out.println("incoming signature: " + matcher.group(1));
            return matcher.group(1);
        } else
        return "Nie znaleziono sygnatury.";
    }

    public String getNotificationSignature() {
        String openPayuSignature =
        "sender=checkout;signature=c33a38d89fb60f873c039fcec3a14743;algorithm=MD5;content=DOCUMENT";
        return openPayuSignature;
    }

    public boolean checkSignatureHash(String header, JsonNode json) {
        String countSignature = getCountedSignature(header, json);
        System.out.println("counted signature: " + countSignature);
        return countSignature.equals(getHashFromHeaderSignature(header));
    }

    public String getOrderStatus(JsonNode node) {
        JsonNode order = node.get("order");
        return order.get("status").asText();
    }

    public String buildFormBody (JsonNode materials, String totalPrice){
        StringBuilder formBody = new StringBuilder();
        formBody.append("<input ").append("type=\"hidden\"").append(" name=\"customerIp\"")   .append(" value=\"").append("123.123.123.123\">");
        formBody.append("<input ").append("type=\"hidden\"").append(" name=\"merchantPosId\"").append(" value=\"").append(posId).append("\"").append(">");
        formBody.append("<input ").append("type=\"hidden\"").append(" name=\"description\"")  .append(" value=\"").append("description\">");
        formBody.append("<input ").append("type=\"hidden\"").append(" name=\"totalAmount\"")  .append(" value=\"").append(totalPrice).append("\"").append(">");
        formBody.append("<input ").append("type=\"hidden\"").append(" name=\"currencyCode\"") .append(" value=\"").append("PLN\">");
        formBody.append("<input ").append("type=\"hidden\"").append(" name=\"notifyUrl\"")    .append(" value=\"").append(Play.application().configuration().getString("payu.notifyUrl")).append("\"").append(">");
        formBody.append("<input ").append("type=\"hidden\"").append(" name=\"continueUrl\"")  .append(" value=\"").append(Play.application().configuration().getString("payu.continueUrl")).append("\"").append(">");
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

    public boolean checkSHA3() {
        String hash = "";
        Digest keccakInstance = Digests.keccak256();
        System.out.println(keccakInstance);
        byte[] hashBytes = keccakInstance.digest("some message".getBytes());
        try {
          hash = Hex.encodeHexString(hashBytes);
            System.out.println(hash);
        } catch (Exception e) {
            e.getMessage();
        }
        return (hash.equals("9df8dba3720d00bd48ad744722021ef91b035e273bccfb78660ca8df9574b086"));
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
        mapData.put("extOrderId", transId);
        mapData.put("description", "description");
        mapData.put("totalAmount", getTotalPrice());
        mapData.put("currencyCode", "PLN");
        mapData.put("notifyUrl", Play.application().configuration().getString("payu.notifyUrl"));
        mapData.put("continueUrl", Play.application().configuration().getString("payu.continueUrl"));
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

    public JsonNode getJsonNodeResponse () {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode testNode = null;
        try {
            testNode = objectMapper.readTree("{" +
                    "   \"order\":{" +
                    "      \"orderId\":\"LDLW5N7MF4140324GUEST000P01\"," +
                    "      \"extOrderId\":\"Id zamówienia w Twoim sklepie\"," +
                    "      \"orderCreateDate\":\"2012-12-31T12:00:00\"," +
                    "      \"notifyUrl\":\"http://tempuri.org/notify\"," +
                    "      \"customerIp\":\"127.0.0.1\"," +
                    "      \"merchantPosId\":\"{Id punktu płatności (pos_id)}\"," +
                    "      \"description\":\"Twój opis zamówienia\"," +
                    "      \"currencyCode\":\"PLN\"," +
                    "      \"totalAmount\":\"200\"," +
                    "      \"buyer\":{" +
                    "         \"email\":\"john.doe@example.org\"," +
                    "         \"phone\":\"111111111\"," +
                    "         \"firstName\":\"John\"," +
                    "         \"lastName\":\"Doe\"" +
                    "      },\n" +
                    "      \"products\":[" +
                    "         {" +
                    "               \"name\":\"Product 1\"," +
                    "               \"unitPrice\":\"200\"," +
                    "               \"quantity\":\"1\"" +
                    "         }" +
                    "      ]," +
                    "      \"status\":\"COMPLETED\"" +
                    "   }" +
                    "} ");
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

