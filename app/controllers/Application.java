package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import models.OrderHelper;
import models.SendJsonOrder;
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
        return ok(index.render("", "@routes.Application.sendForm()"));
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
//        System.out.println(formSignature);
        return ok(index.render(formSignature, "https://secure.payu.com/api/v2_1/orders"));
    }


}

