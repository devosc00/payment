package models;

/**
 * Created by rmasal on 2015-06-18.
 */
public class A implements InterfaceForAB{
    public String build () {
        System.out.println ("get A object ");
        return "A";
    }

}
