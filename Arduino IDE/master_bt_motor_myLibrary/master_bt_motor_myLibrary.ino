#include <Xylophone.h>

#include <Wire.h>
#include <SoftwareSerial.h>

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
int notes[12] = {
  130, 
  115, 
  105, 
  90, 
  80, 
  70, 
  107, 
  95, 
  85, 
  74, 
  62, 
  50
};
bool servosBeat[2] = {false, false};

int servos[4] = {SBL_G, SBL_D, SBD_G, SBD_D};
SoftwareSerial bt(RX, TX);
int positions[4] = {leftStartPos, leftBeatPos, rightStartPos, rightBeatPos};

Xylo xylo;

void setup() {
  // put your setup code here, to run once:
  pinMode(RX, INPUT);
  pinMode(TX, OUTPUT);

  xylo.setup(servos, positions);
  
  Wire.begin();
  bt.begin(BAUD);
  Serial.begin(9600);
}


void loop() {  
  Wire.beginTransmission(8);
  if (bt.available() > 0) {
    note = bt.read();
    Serial.write(note);

    xylo.gotoNote(note, true);
    Wire.write(note);
    Wire.endTransmission();
    
    /*if (note < 'm') {
      xylo.gotoNote(note, true);
      //note = 0;
    }
    else {
      // send the note to slave Arduino
      Wire.write(note);
      Wire.endTransmission();
    }*/
    note = 0;
  }
}
