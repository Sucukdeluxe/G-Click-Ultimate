package misc;

import gearth.extensions.ExtensionBase;

public class Functions {

    public static void sleep(int ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static ExtensionBase.MessageListener guard(ExtensionBase.MessageListener inner) {
        return m -> {
            try {
                inner.act(m);
            } catch (Throwable t) {
                System.err.println("[G-Click Ultimate] intercept guard swallowed " + t);
            }
        };
    }

}
