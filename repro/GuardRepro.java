import gearth.extensions.ExtensionBase;
import misc.Functions;

public class GuardRepro {
    public static void main(String[] args) {
        ExtensionBase.MessageListener throwing = m -> {
            throw new RuntimeException("boom");
        };
        ExtensionBase.MessageListener guarded = Functions.guard(throwing);
        try {
            guarded.act(null);
            System.out.println("guard: NO PROPAGATION");
            System.out.println("RESULT: PASS");
        } catch (Throwable t) {
            System.out.println("guard: PROPAGATED " + t);
            System.out.println("RESULT: FAIL");
            System.exit(1);
        }
    }
}
