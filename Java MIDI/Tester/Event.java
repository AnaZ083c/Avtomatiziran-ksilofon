package midiparse;

import java.util.ArrayList;

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

    public static void decodeMeta(byte[] meta) {
        ArrayList<Byte> decoded = new ArrayList<>();
        for (int i = 0; i < 16; i++) {
            if (metaEventCodes[i].length <= meta.length) {
                for (int j = 0; j < metaEventCodes[i].length; j++)
                    if (meta[j] == metaEventCodes[i][j])
                        decoded.add(metaEventCodes[i][j]);
            }
        }
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer("");
        
        // print delta time
        sb.append(this.deltaTime.toString());

        // print event code
        for (int i = 0; i < this.eventCode.size(); i++)
            sb.append(String.format("%x ", this.eventCode.get(i)));

        // print params
        for (int i = 0; i < this.eventParams.size(); i++)
            sb.append(String.format("%x ", this.eventParams.get(i)));

        return sb.toString();
    }  
}


/*public class MetaEvent extends Event {
    public MetaEvent(DeltaTime deltaTime, ArrayList<Byte> eventCode, ArrayList<Byte> eventParams) {
        super(deltaTime, eventCode, eventParams);
    }
}*/
