package gearth.extensions.parsers;

import gearth.protocol.HPacket;

import java.util.ArrayList;
import java.util.List;

public class HEntityUpdate {
    private int index;
    private boolean isController = false;

    private HPoint tile;
    private HPoint movingTo = null;

    private HSign sign = null;
    private HStance stance = null;
    private HAction action = null;
    private HDirection headFacing;
    private HDirection bodyFacing;

    private HEntityUpdate(HPacket packet) {
        index = packet.readInteger();
        int x = packet.readInteger();
        int y = packet.readInteger();
        double z;
        try {
            String rawZ = packet.readString();
            z = rawZ.isEmpty() ? 0.0 : Double.parseDouble(rawZ);
        } catch (NumberFormatException e) {
            z = 0.0;
        }
        tile = new HPoint(x, y, z);

        HDirection[] directions = HDirection.values();
        int head = packet.readInteger();
        headFacing = directions[((head % directions.length) + directions.length) % directions.length];
        int body = packet.readInteger();
        bodyFacing = directions[((body % directions.length) + directions.length) % directions.length];

        String action = packet.readString();
        String[] actionData = action.split("/");

        for (String actionInfo : actionData) {
            String[] actionValues = actionInfo.split(" ");

            if (actionValues.length < 2) continue;
            if (actionValues[0].isEmpty()) continue;

            try {
                switch (actionValues[0]) {
                    case "flatctrl":
                        isController = true;
                        this.action = HAction.None;
                        break;
                    case "mv":
                        String[] values = actionValues[1].split(",");
                        if (values.length >= 3)
                            movingTo = new HPoint(Integer.decode(values[0]), Integer.decode(values[1]),
                                    Double.parseDouble(values[2]));

                        this.action = HAction.Move;
                        break;
                    case "sit":
                        this.action = HAction.Sit;
                        stance = HStance.Sit;
                        break;
                    case "lay":
                        this.action = HAction.Lay;
                        stance = HStance.Lay;
                        break;
                    case "sign":
                        sign = HSign.values()[Integer.decode(actionValues[1])];
                        this.action = HAction.Sign;
                        break;
                }
            } catch (Exception ignored) {
            }
        }
    }


    public static HEntityUpdate[] parse(HPacket packet) {
        int count;
        try {
            count = packet.readInteger();
        } catch (Throwable t) {
            return new HEntityUpdate[0];
        }

        if (count < 0 || count > 4096) {
            return new HEntityUpdate[0];
        }

        List<HEntityUpdate> updates = new ArrayList<>(count);
        for (int i = 0; i < count; i++) {
            try {
                updates.add(new HEntityUpdate(packet));
            } catch (Throwable t) {
                break;
            }
        }

        return updates.toArray(new HEntityUpdate[0]);
    }

    public int getIndex() {
        return index;
    }

    public boolean isController() {
        return isController;
    }

    public HPoint getTile() {
        return tile;
    }

    public HPoint getMovingTo() {
        return movingTo;
    }

    public HSign getSign() {
        return sign;
    }

    public HStance getStance() {
        return stance;
    }

    public HAction getAction() {
        return action;
    }

    public HDirection getHeadFacing() {
        return headFacing;
    }

    public HDirection getBodyFacing() {
        return bodyFacing;
    }
}
