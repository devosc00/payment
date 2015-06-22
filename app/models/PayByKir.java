package models;

/**
 * Created by rmasal on 2015-06-19.
 */
public class PayByKir {
    private static PayByKir kirPayment = null;
    String payByKir = "payByKir";

    public static PayByKir getInstance () {
        if(kirPayment == null) {
            kirPayment = new PayByKir();
        }
        return kirPayment;
    }
}
