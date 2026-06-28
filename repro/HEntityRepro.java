import gearth.protocol.HPacket;
import gearth.extensions.parsers.HEntity;

public class HEntityRepro {
    public static void main(String[] args) {
        HPacket p = craftOverflowUsers();
        try {
            HEntity[] users = HEntity.parse(p);
            System.out.println("HEntity.parse RETURNED " + users.length + " entities (NO THROW)");
            System.out.println("RESULT: PASS");
        } catch (Throwable t) {
            System.out.println("HEntity.parse THREW " + t);
            System.out.println("RESULT: FAIL");
            System.exit(1);
        }
    }

    static HPacket craftOverflowUsers() {
        HPacket p = new HPacket(0);
        p.appendInt(1);
        p.appendInt(123);
        p.appendString("name");
        p.appendString("motto");
        p.appendString("fig");
        p.appendInt(5);
        p.appendInt(2);
        p.appendInt(3);
        p.appendString("0.4             /mv 33,14,");
        return p;
    }
}
