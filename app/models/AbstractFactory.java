package models;

import play.Play;

/**
 * Created by rmasal on 2015-06-18.
 */
public abstract class AbstractFactory {

    public static InterfaceForAB createObject (boolean bool)
    {
        if (bool)
    {
        return new A();
    } else return new B();
}
}
