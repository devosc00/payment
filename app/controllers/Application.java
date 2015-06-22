package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import models.AbstractFactory;
import models.InterfaceForAB;
import models.OrderHelper;
import models.SendJsonOrder;
import play.Play;
import play.data.DynamicForm;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.index;
import views.html.output;

public class Application extends Controller {


//   static List<Product> productList = Arrays.asList(new Product("product1", "1000", "2"), new Product("product2", "200", "2"));

    public static Result index() {
//        Form<Order> orderForm = Form.form(Order.class).fill(new Order("127.0.0.1", "4143", "description", "1", "PLN", productList , "continue/url", "notify/url"));
        return ok(index.render());
    }

    public static Result sendJsonOrder() {
        OrderHelper orderHelper = new OrderHelper();
        JsonNode order = orderHelper.getJsonNodeTest();
        SendJsonOrder sendJsonOrder = new SendJsonOrder();
        sendJsonOrder.post(orderHelper.getHeaderSignature(), order);
        return redirect("/");
    }

    public static Result sendForm() {
        DynamicForm dynamicForm = Form.form().bindFromRequest();
        OrderHelper orderHelper = new OrderHelper();
        String formSignature = orderHelper.getFormSignature(dynamicForm.data());
        return //ok(index.render());

                ok(output.render(formSignature,
                        dynamicForm.data().get("totalAmount"),
                        dynamicForm.data().get("products[0].name"),
                        dynamicForm.data().get("products[0].unitPrice"),
                        dynamicForm.data().get("products[0].quantity")));
    }


    public static Result sendDataToFormBuilder() {
        JsonNode json = request().body().asJson();
        JsonNode materialArray = json.get("form");
        OrderHelper orderHelper = new OrderHelper();
        for (JsonNode material : materialArray) {
            orderHelper.storePartialPrices(material.get(1).asText(), (material.get(2).asText()));
        }
        String totalPrice = orderHelper.getTotalPrice();
        String formBody = orderHelper.buildFormBody(materialArray, totalPrice);
        String signature = orderHelper.getFormSignature(orderHelper.bulildDataForSignature(materialArray));
        ObjectNode result = play.libs.Json.newObject();
        result.put("form", formBody);
        result.put("signature", signature);
        return ok(result);
    }

    public static Result startFactory() {
        InterfaceForAB Abstract = AbstractFactory.createObject(false);
        System.out.println("startFactory:  " + Abstract.build());
        return ok(Abstract.build());
    }

    public static Result testOrderResponse () {
        OrderHelper orderHelper = new OrderHelper();
        System.out.println(Play.application().configuration().getString("payu.notifyUrl"));
        System.out.println(orderHelper.getOrderStatus(orderHelper.getJsonNodeResponse()));
        System.out.println(orderHelper.getHashFromSignature(orderHelper.getNotificationSignature()));
        return orderHelper.checkSignatureHash(orderHelper.notifySignature, orderHelper.getJsonNodeResponse()) ? ok("true") : ok("false");
    }


}

