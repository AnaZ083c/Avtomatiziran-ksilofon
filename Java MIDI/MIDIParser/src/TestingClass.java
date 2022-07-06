public class TestingClass {
    public static final String FILEPATH = "testFiles\\twinkle.mid";

    public static void main(String[] args) {
        try {
            MidiHandler midiHandler = new MidiHandler(FILEPATH);

            midiHandler.tickTest(true);

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
