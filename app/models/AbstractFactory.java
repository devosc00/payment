package models;

/**
 * Created by rmasal on 2015-06-18.
 */
public abstract class AbstractFactory {

    public static InterfaceForAB createObject (boolean bool)
    {
        System.out.println("Abstract Factory");
        if (bool)
    {
        return new A();
    } else return new B();
}
}
