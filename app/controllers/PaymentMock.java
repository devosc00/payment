package controllers;

import play.libs.F;
import play.mvc.Controller;
import play.mvc.Result;
import scala.Function;

/**
 * Created by rmasal on 2015-06-22.
 */
public class PaymentMock extends Controller {

    public static Result payuPayment() throws Exception {
        String response = request().body().asJson().asText();
        String uri = request().uri();
        System.out.println(response);
        return ok("redirect from Payu" + response + "uri: " + uri);
    }


    public static Result notification() throws Exception {
        String header = request().getHeader("Open-PayuSignature");
        String body = request().body().asJson().asText();
        System.out.println("header: " + header);
        System.out.println("body: " + body);
        return ok(header + body );
    }
}
