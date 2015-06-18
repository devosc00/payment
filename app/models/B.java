package models;

/**
 * Created by rmasal on 2015-06-18.
 */
public class B implements InterfaceForAB {
    public String build () {
        System.out.println("get B object");
        return "B";
    }

}
