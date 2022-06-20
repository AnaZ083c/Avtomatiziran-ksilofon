#include <Wire.h>
#include <SoftwareSerial.h>
#include <Servo.h>

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
int rightBeatPos = 113;
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

Servo servos[4];
SoftwareSerial bt(RX, TX);

void gotoNote(char note = 0, bool play = false);
void beat2(int servo1, int servo2, int min_, int max_, float servoSpeed = 0.2, bool twoServo = false);
void beat1(int servo, int min_, int max_, float servoSpeed = 0.2);

void setup() {
  // put your setup code here, to run once:
  pinMode(RX, INPUT);
  pinMode(TX, OUTPUT);

  servos[0].attach(SBL_G);
  servos[1].attach(SBL_D);
  servos[2].attach(SBD_G);
  servos[3].attach(SBD_D);

  servos[0].write(111);
  servos[2].write(106);
  
  Wire.begin();
  bt.begin(BAUD);
  Serial.begin(9600);
}


void loop() {  
  if (bt.available() > 0) {
    note = bt.read();
    Serial.write(note);
    
    gotoNote(note, true);
    note = 0;

    Wire.beginTransmission(8);
    Wire.write(note);
    Wire.endTransmission();

  }
}

/**************************** MY FUNCTIONS ****************************/
void gotoNote(char note = 0, bool play = false) {
  int servoHead;
  bool twoNotes = false;
  
  switch(note) {
    // SBL
    case 'a':
      servos[1].write(130);
      servoHead = 0;
      servosBeat[0] = true;
      break;
    case 'b':
      servos[1].write(115);
      servoHead = 0;
      servosBeat[0] = true;
      break;
    case 'c':
      servos[1].write(105);
      servoHead = 0;
      servosBeat[0] = true;
      break;
    case 'd':
      servos[1].write(90);
      servoHead = 0;
      servosBeat[0] = true;
      break;
    case 'e':
      servos[1].write(80);
      servoHead = 0;
      servosBeat[0] = true;
      break;
    case 'f':
      servos[1].write(70);
      servoHead = 0;
      servosBeat[0] = true;
      break;

    // SBD
    case 'g':
      servos[3].write(107);
      servoHead = 2;
      servosBeat[1] = true;
      break;    
    case 'h':
      servos[3].write(95);
      servoHead = 2;
      servosBeat[1] = true;
      break;
    case 'i':
      servos[3].write(85);
      servoHead = 2;
      servosBeat[1] = true;
      break;
    case 'j':
      servos[3].write(74);
      servoHead = 2;
      servosBeat[1] = true;
      break;
    case 'k':
      servos[3].write(62);
      servoHead = 2;
      servosBeat[1] = true;
      break;
    case 'l':
      servos[3].write(50);
      servoHead = 2;
      servosBeat[1] = true;
      break;
    default: 
      play = false;
      break;
  }

  int min_ = (servoHead == 0)? leftStartPos : rightStartPos;
  int max_ = (servoHead == 0)? leftBeatPos : rightBeatPos;

  //int beat = 0;
  if (play) {
    delay(600);
    // TODO
    
    // beat(0, 2, min_, max_, 20, servosBeat[0] && servosBeat[1]);
    beat1(servoHead, min_, max_, 5);
  }
}

void beat2(int servo1, int servo2, int min_, int max_, float servoSpeed = 0.2, bool twoServo = false) {
  for (int pos = min_; pos < max_ + 1; pos++) {
    servos[servo1].write(pos);
    if (twoServo)
      servos[servo2].write(pos - 5);
    delay(servoSpeed);
  }

  for (int pos = max_; pos > min_ - 1; pos--) {
    servos[servo1].write(pos);
    if (twoServo)
      servos[servo2].write(pos - 5);
    delay(servoSpeed);
  }
}

void beat1(int servo, int min_, int max_, float servoSpeed = 0.2) {
  for (int pos = min_; pos < max_ + 1; pos++) {
    servos[servo].write(pos);
    delay(servoSpeed);
  }
  

  for (int pos = max_; pos > min_ - 1; pos--) {
    servos[servo].write(pos);
    delay(servoSpeed);
  }
}



/* NON-WORKING CODE */
/**
 * int noteIndex = note - 97;
 * Serial.println();
 * Serial.println(noteIndex);
 *
 * if (note < 'g') {
 *   Serial.println(notes[noteIndex]);
 *   //servos[1].write(notes[noteIndex]);
 *   servoHead = 0;
 *   servosBeat[0] = true;
 * }
 * else if (note >= 'g' && note <= 'l') {
 *   servos[3].write(notes[noteIndex]);
 *   servoHead = 2;
 *   servosBeat[1] = true;
 * }
 * else
 *   play = false;
 */
