#include <Wire.h>
#include <Xylophone.h>

#define SCD_G 9
#define SCD_D 6

#define SCL_G 5
#define SCL_D 3

int leftStartPos = 98;
int leftBeatPos = 103;
int rightStartPos = 95;
int rightBeatPos = 100;
float d = 0.0005;

int startPos = max(leftStartPos, rightStartPos);
int beatPos = max(leftBeatPos, rightBeatPos);

char note = 0;
bool servosBeat[2] = {false, false};

int servos[4] = {SCL_G, SCL_D, SCD_G, SCD_D};
int positions[4] = {leftStartPos, leftBeatPos, rightStartPos, rightBeatPos};

Xylo xylo;
String notes = "";

void setup() {
    xylo.setup(servos, positions);
    xylo.servos[1].write(85);
    xylo.servos[3].write(80);
    
    Wire.begin(8);
    Wire.onReceive(receiveEvent);
    Serial.begin(9600);
}

void loop() {
  //delay(100);
  if (note) {
    for (int i = 0; i < notes.length(); i++)
      xylo.gotoNote(notes.charAt(i), true, true);
    note = 0;
    notes = "";
  }
}


void receiveEvent(int howMany) {
  while(Wire.available()) {
    note = Wire.read();
    notes += note;
  }
}
