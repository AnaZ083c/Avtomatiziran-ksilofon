#include <Wire.h>
#include <Xylophone.h>

#include <SoftwareSerial.h> // delete this later

#define SBD_G 9
#define SBD_D 6

#define SBL_G 5
#define SBL_D 3

#define RX 10
#define TX 11
#define BAUD 9600

int leftStartPos = 111;
int leftBeatPos = 117;
int rightStartPos = 106;
int rightBeatPos = 112;
float d = 0.0005;

int startPos = max(leftStartPos, rightStartPos);
int beatPos = max(leftBeatPos, rightBeatPos);

char note = 0;
bool servosBeat[2] = {false, false};

int servos[4] = {SBL_G, SBL_D, SBD_G, SBD_D};
// SoftwareSerial bt(RX, TX);
int positions[4] = {leftStartPos, leftBeatPos, rightStartPos, rightBeatPos};

Xylo xylo;

void setup() {
  // put your setup code here, to run once:
  // pinMode(RX, INPUT);
  // pinMode(TX, OUTPUT);

  xylo.setup(servos, positions);
  
  Wire.begin();
  // bt.begin(BAUD);
  Serial.begin(9600);
}

size_t len = 2;
void loop() {    
  Wire.beginTransmission(8);
  if (Serial.available() > 0) {
    note = Serial.read();
    // Serial.write(note);
    xylo.gotoNote(note, true);
    
    Serial.print(note);
    Serial.print(":");
    Serial.println(millis());
    
    Wire.write(note);
    Wire.endTransmission();
    
    note = 0;
    leftRight = 0;
    notes = "";
  } 
}
