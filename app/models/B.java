package models;

/**
 * Created by rmasal on 2015-06-18.
 */
public class B implements InterfaceForAB {
    public String build () {
        PayByKir payByKir = PayByKir.getInstance();
        System.out.println("get B object");
        return "B: " + payByKir.payByKir;

    }

}
