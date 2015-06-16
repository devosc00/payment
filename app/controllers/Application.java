package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import models.OrderHelper;
import models.SendJsonOrder;
import models.Toolkit;
import play.api.libs.json.Json;
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

    public static Result sendForm () {
        DynamicForm dynamicForm = Form.form().bindFromRequest();
        OrderHelper orderHelper = new OrderHelper();
        String formSignature = orderHelper.getFormSignature(dynamicForm.data());
        System.out.println(dynamicForm.data());
        String productName = dynamicForm.data().get("products[0].name");
        return //ok(index.render());

                ok(output.render(formSignature,
                dynamicForm.data().get("totalAmount"),
                productName,
                dynamicForm.data().get("products[0].unitPrice"),
                dynamicForm.data().get("products[0].quantity")));
    }


    public static Result sendDataToFormBuilder () {
        JsonNode json = request().body().asJson();
        JsonNode materialArray = json.get("form");
        OrderHelper orderHelper = new OrderHelper();
        for (JsonNode material: materialArray) {
//            System.out.println(material.get(0).asText());
            orderHelper.storePartialPrices(material.get(1).asText(),(material.get(2).asText()));
        }
        String totalPrice = orderHelper.getTotalPrice();
        orderHelper.buildFormBody(materialArray, totalPrice);
        System.out.println(materialArray);
        return ok(totalPrice);
    }

}

