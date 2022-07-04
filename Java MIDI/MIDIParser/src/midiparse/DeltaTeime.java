package midiparse;

import java.util.Set;
import java.util.ArrayList;
import java.util.Arrays;

class DeltaTime {

    // MAX number of Bytes is 4 Bytes
    // VLV ... Variable Length Values (a number with a variable width)
    private ArrayList<Byte> vlvBytes;
    private boolean isLastVLV = false;

    public DeltaTime() {
        this.vlvBytes = new ArrayList<>();
    }

    public DeltaTime(ArrayList<Byte> bytes) {
        this.vlvBytes = new ArrayList<>();
        this.vlvBytes.addAll(bytes);
    }

    public void setVlvBytes(ArrayList<Byte> vlvBytes) {
        this.vlvBytes = new ArrayList<>();
        this.vlvBytes.addAll(vlvBytes);
    }

    public ArrayList<Byte> getVlvBytes() {
        return vlvBytes;
    }

    public static boolean isVLV(byte b) {
        return (b >= (byte) 0x80);
    }

    /**
     * addToVLV(byte b)
     * @param b byte to be added
     * @return TRUE if byte was added and FALSE if byte was not added
     */
    public boolean addToVLV(byte b) {
        this.isLastVLV = isVLV(b);

        if (!this.isLastVLV) {
            vlvBytes.add(b);
            return true;
        } else if (this.isLastVLV && this.vlvBytes.isEmpty()) {

        }
        return false;
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer("");

        for (int i = 0; i < this.vlvBytes.size(); i++) {
            sb.append(String.format("%x ", this.vlvBytes.get(i)));
        }

        return sb.toString();
    }
}