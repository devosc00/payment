package models;

/**
 * Created by rmasal on 2015-06-19.
 */
public class PayByPayu extends A {
    private static PayByPayu payuPayment = null;
    String payByPayu = "payByPayu";

    public static PayByPayu getInstance () {
        if(payuPayment == null) {
            payuPayment = new PayByPayu();
        }
        return payuPayment;
    }
}
