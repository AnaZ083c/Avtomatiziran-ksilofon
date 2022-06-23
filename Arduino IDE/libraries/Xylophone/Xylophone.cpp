#include "Xylophone.h"


/*float Xylo::calculateDelay(char startNote, char endNote, float noteDelay) {
    int startInd = this->notSharps.indexOf(startNote);
    int endInd = this->notSharps.indexOf(endNote);

    int maxInd = max(startInd, endInd);
    int minInd = min(startInd, endInd);

    return (float)(maxInd - minInd) * noteDelay;
}*/

void Xylo::gotoNote(char note = 0, bool play = false, bool sharps = false, float noteDelay = 650) {
  int servoHead;
  bool twoNotes = false;
  //float calculatedDelay = this->calculateDelay()


  bool playTmp = play;

  if (!sharps) {
    play = playTmp || play;

    switch(note) {
        // SBL
        case 'a':
            this->servos[1].write(130);
            servoHead = 0;
            this->servosBeat[0] = true;
            break;
        case 'b':
            this->servos[1].write(115);
            servoHead = 0;
            this->servosBeat[0] = true;
            break;
        case 'c':
            this->servos[1].write(105);
            servoHead = 0;
            this->servosBeat[0] = true;
            break;
        case 'd':
            this->servos[1].write(90);
            servoHead = 0;
            this->servosBeat[0] = true;
            break;
        case 'e':
            this->servos[1].write(80);
            servoHead = 0;
            this->servosBeat[0] = true;
            break;
        case 'f':
            this->servos[1].write(70);
            servoHead = 0;
            this->servosBeat[0] = true;
            break;

        // SBD
        case 'g':
            this->servos[3].write(107);
            servoHead = 2;
            this->servosBeat[1] = true;
            break;    
        case 'h':
            this->servos[3].write(95);
            servoHead = 2;
            this->servosBeat[1] = true;
            break;
        case 'i':
            this->servos[3].write(85);
            servoHead = 2;
            this->servosBeat[1] = true;
            break;
        case 'j':
            this->servos[3].write(74);
            servoHead = 2;
            this->servosBeat[1] = true;
            break;
        case 'k':
            this->servos[3].write(62);
            servoHead = 2;
            this->servosBeat[1] = true;
            break;
        case 'l':
            this->servos[3].write(50);
            servoHead = 2;
            this->servosBeat[1] = true;
            break;

        default: 
            play = false;
            this->servosBeat[0] = false;
            this->servosBeat[1] = false;
            break;
    }
 }
 // SHARPS
 else {
    play = playTmp || play;

    switch(note) {
        //SCL
        case 'm':
            this->servos[1].write(40);
            servoHead = 0;
            this->servosBeat[0] = true;
            break;
        case 'n':
            this->servos[1].write(50);
            servoHead = 0;
            this->servosBeat[0] = true;
            break;
        case 'o':
            this->servos[1].write(72);
            servoHead = 0;
            this->servosBeat[0] = true;
            break;
        case 'p':
            this->servos[1].write(85);
            servoHead = 0;
            this->servosBeat[0] = true;
            break;

        // SCD
        case 'r':
            this->servos[3].write(50);
            servoHead = 2;
            this->servosBeat[1] = true;
            break;
        case 's':
            this->servos[3].write(70);
            servoHead = 2;
            this->servosBeat[1] = true;
            break;
        case 't':
            this->servos[3].write(80);
            servoHead = 2;
            this->servosBeat[1] = true;
            break;
        case 'u':
            this->servos[3].write(102);
            servoHead = 2;
            this->servosBeat[1] = true;
            break;
        
        default: 
            play = false;
            this->servosBeat[0] = false;
            this->servosBeat[1] = false;
            break;
    }
  }

  int min_ = (servoHead == 0)? this->leftStartPos : this->rightStartPos;
  int max_ = (servoHead == 0)? this->leftBeatPos : this->rightBeatPos;

  //int beat = 0;

  if (play) {
    //delay(noteDelay);
    // TODO

    // beat(0, 2, min_, max_, 20, servosBeat[0] && servosBeat[1]);
    this->beat1(servoHead, min_, max_, 5);
  }
}

void Xylo::beat2(int servo1, int servo2, int min_, int max_, float servoSpeed = 0.2, bool twoServo = false) {
  for (int pos = min_; pos < max_ + 1; pos++) {
    this->servos[servo1].write(pos);
    if (twoServo)
      this->servos[servo2].write(pos - 5);
    delay(servoSpeed);
  }

  for (int pos = max_; pos > min_ - 1; pos--) {
    this->servos[servo1].write(pos);
    if (twoServo)
      this->servos[servo2].write(pos - 5);
    delay(servoSpeed);
  }
}

void Xylo::beat1(int servo, int min_, int max_, float servoSpeed = 0.2) {
  for (int pos = min_; pos < max_ + 1; pos++) {
    this->servos[servo].write(pos);
    delay(servoSpeed);
  }
  

  for (int pos = max_; pos > min_ - 1; pos--) {
    this->servos[servo].write(pos);
    delay(servoSpeed);
  }
}