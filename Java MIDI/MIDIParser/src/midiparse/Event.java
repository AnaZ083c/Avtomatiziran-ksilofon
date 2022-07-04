package midiparse;

import java.lang.reflect.Array;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.sound.midi.MetaEventListener;

public class Event {
    private DeltaTime deltaTime;
    private ArrayList<Byte> eventCode;
    private ArrayList<Byte> eventParams;

    // Event Codes
    public static byte[][] metaEventCodes = new byte[][]{
            {(byte)0xFF, (byte)0x00, (byte)0x02},
            {(byte)0xFF, (byte)0x01},
            {(byte)0xFF, (byte)0x02},
            {(byte)0xFF, (byte)0x03},
            {(byte)0xFF, (byte)0x04},
            {(byte)0xFF, (byte)0x05},
            {(byte)0xFF, (byte)0x06},
            {(byte)0xFF, (byte)0x07},
            {(byte)0xFF, (byte)0x20, (byte)0x01},
            {(byte)0xFF, (byte)0x21, (byte)0x01},
            {(byte)0xFF, (byte)0x2F, (byte)0x00},
            {(byte)0xFF, (byte)0x51, (byte)0x03},
            {(byte)0xFF, (byte)0x54, (byte)0x05},
            {(byte)0xFF, (byte)0x58, (byte)0x04},
            {(byte)0xFF, (byte)0x59, (byte)0x02},
            {(byte)0xFF, (byte)0x7F},
    };

    public static Map<String, Integer> metaEventsNumOfArgs = Stream.of(new Object[][] {
            { "ff 0 2", 0 },
            { "ff 1"  , 2 },
            { "ff 2"  , 2 },
            { "ff 3"  , 2 },
            { "ff 4"  , 2 },
            { "ff 5"  , 2 },
            { "ff 6"  , 2 },
            { "ff 7"  , 2 },
            { "ff 20 1"  , 1 },
            { "ff 21 1"  , 1 },
            { "ff 2f 0"  , 0 },
            { "ff 51 3"  , 1 },
            { "ff 54 5"  , 5 },
            { "ff 58 4"  , 4 },
            { "ff 59 2"  , 2 },
            { "ff 7f"  , 2 },
    }).collect(Collectors.toMap(data -> (String) data[0], data -> (Integer) data[1]));

    public Event(DeltaTime deltaTime, ArrayList<Byte> eventCode, ArrayList<Byte> eventParams) {
        this.deltaTime = deltaTime;
        this.eventCode = new ArrayList<>(eventCode);
        this.eventParams = new ArrayList<>(eventParams);
    }

    public Event(DeltaTime deltaTime, byte eventCode, ArrayList<Byte> eventParams) {
        this.deltaTime = deltaTime;

        this.eventCode = new ArrayList<>();
        this.eventCode.add(eventCode);

        this.eventParams = new ArrayList<>(eventParams);
    }

    public Event(DeltaTime deltaTime, ArrayList<Byte> eventCode) {
        this.deltaTime = deltaTime;
        this.eventCode = new ArrayList<>(eventCode);
        this.eventParams = null;
    }

    public DeltaTime getDeltaTime() {
        return deltaTime;
    }

    public ArrayList<Byte> getEventCode() {
        return eventCode;
    }

    public ArrayList<Byte> getEventParams() {
        return eventParams;
    }

    public void setDeltaTime(DeltaTime deltaTime) {
        this.deltaTime = deltaTime;
    }

    public void setEventCode(ArrayList<Byte> eventCode) {
        this.eventCode = eventCode;
    }

    public void setEventParams(ArrayList<Byte> eventParams) {
        this.eventParams = eventParams;
    }

    public boolean isMetaEvent() {
        if (eventCode.get(0) == (byte)0xFF) {
            for (int i = 0; i < 15; i++) {
                if (metaEventCodes[i].length >= eventCode.size()) {
                    for (int j = 0; j < metaEventCodes[i].length; j++) {
                        if (eventCode.get(j).byteValue() != metaEventCodes[i][j])
                            return false;
                    }
                }
            }
        }

        return true;
    }

    public static String decodeMeta(byte[] meta) {
        StringBuffer decoded = new StringBuffer("ff ");
        StringBuffer sb = new StringBuffer("");

        for (byte b : meta) {
            sb.append(String.format("%x ", b));
        }

        decoded.append(String.format("%x", meta[1]));

        if (metaEventsNumOfArgs.containsKey(decoded.toString()))
            return decoded.toString();

        decoded.append(" ");

        for (String s : metaEventsNumOfArgs.keySet()) {
            String[] tmp = s.split(" ");
            String[] tmp2 = sb.toString().split(" ");

            for (int i = 2; i < tmp.length; i++) {
                int decodedLen = decoded.toString().split(" ").length;
                if (tmp[i].equals(tmp2[i]) && decodedLen < 3)
                    decoded.append(String.format("%s ", tmp[i]));
            }
        }

        decoded.deleteCharAt(decoded.length()-1);

        return decoded.toString();
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer("");

        // print delta time
        sb.append(this.deltaTime.toString());

        // print event code
        for (byte b : eventCode)
            sb.append(String.format("%x ", b));

        // print params
        for (byte b : eventParams)
            sb.append(String.format("%x ", b));

        return sb.toString();
    }

    public static String eventArrayListToString(ArrayList<Event> events) {
        StringBuffer sb = new StringBuffer("");

        for (Event e : events) {
            sb.append(e.toString());
            sb.append("\n");
        }

        return sb.toString();
    }
}


/*public class MetaEvent extends Event {
    public MetaEvent(DeltaTime deltaTime, ArrayList<Byte> eventCode, ArrayList<Byte> eventParams) {
        super(deltaTime, eventCode, eventParams);
    }
}*/
