#include "Xylophone.h"


float Xylo::calculateDelay(char startNote, char endNote, float noteDelay) {
    int startInd = this->notSharps.indexOf(startNote);
    int endInd = this->notSharps.indexOf(endNote);

    int maxInd = max(startInd, endInd);
    int minInd = min(startInd, endInd);

    return (float)(maxInd - minInd) * noteDelay;
}

int Xylo::calculateSteps(Servo servo, int newPos) {
    int start = servo.read();
    int steps = 0;

    // s = v * t
    // s ... pot
    // v ... hitrost
    // t ... čas
    int d = max(start, newPos) - min(start, newPos); // delta
    
    return (int) d/2;
}

bool Xylo::moveToPos(Servo servo, int pos, int step) {
    int beg = servo.read();

    if (beg > pos)
        for (int i = beg-1; i > pos; i-=step)
            servo.write(i);
    else
        for (int i = beg-1; i < pos; i+=step)
            servo.write(i);
    
    return servo.read() == pos;
}

void Xylo::gotoNote(char note = 0, bool play = false, bool sharps = false, float noteDelay = 200) {
  int servoHead;
  bool twoNotes = false;
  //float calculatedDelay = this->calculateDelay()
  int endPos;
  int startPos;

  bool playTmp = play;
  bool hasFinishedMoving = false;

  int delta = 0;

  if (!sharps) {
    play = playTmp || play;

    switch(note) {
        // SBL
        case 'a':
            startPos = servos[1].read();
            this->servos[1].write(130);
            // hasFinishedMoving = this->moveToPos(this->servos[1], 130, 60);
            endPos = 130;
            servoHead = 0;
            this->servosBeat[0] = true;
            break;
        case 'b':
            startPos = servos[1].read();
            this->servos[1].write(115);
            // hasFinishedMoving = this->moveToPos(this->servos[1], 115, 60);
            endPos = 115;
            servoHead = 0;
            this->servosBeat[0] = true;
            break;
        case 'c':
            startPos = servos[1].read();
            this->servos[1].write(105);
            endPos = 105;
            servoHead = 0;
            this->servosBeat[0] = true;
            break;
        case 'd':
            startPos = servos[1].read();
            this->servos[1].write(90);
            endPos = 90;
            servoHead = 0;
            this->servosBeat[0] = true;
            break;
        case 'e':
            startPos = servos[1].read();
            this->servos[1].write(80);
            endPos = 80;
            servoHead = 0;
            this->servosBeat[0] = true;
            break;
        case 'f':
            startPos = servos[1].read();
            this->servos[1].write(70);
            //hasFinishedMoving = this->moveToPos(this->servos[1], 70, 4);
            endPos = 70;
            servoHead = 0;
            this->servosBeat[0] = true;
            break;

        // SBD
        case 'g':
            startPos = servos[3].read();
            this->servos[3].write(107);
            endPos = 107;
            servoHead = 2;
            this->servosBeat[1] = true;
            break;    
        case 'h':
            startPos = servos[3].read();
            this->servos[3].write(95);
            endPos = 95;
            servoHead = 2;
            this->servosBeat[1] = true;
            break;
        case 'i':
            startPos = servos[3].read();
            this->servos[3].write(85);
            endPos = 85;
            servoHead = 2;
            this->servosBeat[1] = true;
            break;
        case 'j':
            startPos = servos[3].read();
            this->servos[3].write(74);
            endPos = 74;
            servoHead = 2;
            this->servosBeat[1] = true;
            break;
        case 'k':
            startPos = servos[3].read();
            this->servos[3].write(62);
            endPos = 62;
            servoHead = 2;
            this->servosBeat[1] = true;
            break;
        case 'l':
            startPos = servos[3].read();
            this->servos[3].write(50);
            endPos = 50;
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
            startPos = servos[1].read();
            this->servos[1].write(40);
            endPos = 40;
            servoHead = 0;
            this->servosBeat[0] = true;
            break;
        case 'n':
            startPos = servos[1].read();
            this->servos[1].write(50);
            endPos = 50;
            servoHead = 0;
            this->servosBeat[0] = true;
            break;
        case 'o':
            startPos = servos[1].read();
            this->servos[1].write(72);
            endPos = 72;
            servoHead = 0;
            this->servosBeat[0] = true;
            break;
        case 'p':
            startPos = servos[1].read();
            this->servos[1].write(85);
            endPos = 85;
            servoHead = 0;
            this->servosBeat[0] = true;
            break;

        // SCD
        case 'r':
            startPos = servos[3].read();
            this->servos[3].write(50);
            endPos = 50;
            servoHead = 2;
            this->servosBeat[1] = true;
            break;
        case 's':
            startPos = servos[3].read();
            this->servos[3].write(70);
            endPos = 70;
            servoHead = 2;
            this->servosBeat[1] = true;
            break;
        case 't':
            startPos = servos[3].read();
            this->servos[3].write(80);
            endPos = 80;
            servoHead = 2;
            this->servosBeat[1] = true;
            break;
        case 'u':
            startPos = servos[3].read();
            this->servos[3].write(102);
            endPos = 102;
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

  if (note == 'q') {
    this->playbackMode = true;
    delay(100);  // remove if necesary
  }
  else if (note == 'y')
    this->playbackMode = false;

  if (note == 'D') {
    this->doubleNote = true;
  } else if (note == 'N') {
    this->doubleNote = false;
  }

  int min_ = (servoHead == 0)? this->leftStartPos : this->rightStartPos;
  int max_ = (servoHead == 0)? this->leftBeatPos : this->rightBeatPos;

  //int beat = 0;

  if (play) {
    if (this->servos[servoHead+1].read() == endPos) {
        if ((startPos != endPos) || this->playbackMode)
            delay(noteDelay);
        
        /*if (this->servosBeat[0] && this->servosBeat[1]) {
            this->beat2(0, 2, min_, max_, 5, true);
            this->servosBeat[0] = false;
            this->servosBeat[1] = false;
        }*/

        /* if (this->doubleNote)
            this->beat2(0, 2, min_, max_, 5, true);
        else
            this->beat1(servoHead, min_, max_, 5);*/
        
        beat3(servoHead, min_, max_);
        
        // TODO

        // beat2(0, 2, min_, max_, 5, count >= 2);
        
    }
  }
}


void Xylo::receiveByte(char leftRight, char note = 0, bool play = false, bool sharps = false, float noteDelay = 200) {
    int servoHead;
    bool twoNotes = false;
    //float calculatedDelay = this->calculateDelay()
    int endPos;
    int startPos;

    bool playTmp = play;
    bool hasFinishedMoving = false;

    int delta = 0;

    if (!sharps) {
        play = playTmp || play;

        // SBL
        switch(leftRight) {
            case 'L':
                this->left = true;
                switch(note) {
                    case 'a':
                        startPos = servos[1].read();
                        this->servos[1].write(130);
                        // hasFinishedMoving = this->moveToPos(this->servos[1], 130, 60);
                        endPos = 130;
                        servoHead = 0;
                        this->servosBeat[0] = true;
                        break;
                    case 'b':
                        startPos = servos[1].read();
                        this->servos[1].write(115);
                        // hasFinishedMoving = this->moveToPos(this->servos[1], 115, 60);
                        endPos = 115;
                        servoHead = 0;
                        this->servosBeat[0] = true;
                        break;
                    case 'c':
                        startPos = servos[1].read();
                        this->servos[1].write(105);
                        endPos = 105;
                        servoHead = 0;
                        this->servosBeat[0] = true;
                        break;
                    case 'd':
                        startPos = servos[1].read();
                        this->servos[1].write(90);
                        endPos = 90;
                        servoHead = 0;
                        this->servosBeat[0] = true;
                        break;
                    case 'e':
                        startPos = servos[1].read();
                        this->servos[1].write(80);
                        endPos = 80;
                        servoHead = 0;
                        this->servosBeat[0] = true;
                        break;
                    case 'f':
                        startPos = servos[1].read();
                        this->servos[1].write(70);
                        //hasFinishedMoving = this->moveToPos(this->servos[1], 70, 4);
                        endPos = 70;
                        servoHead = 0;
                        this->servosBeat[0] = true;
                        break;
                    default:
                        play = false;
                        this->servosBeat[0] = false;
                        this->servosBeat[1] = false;
                        break;
                }
                break;

            // SBD
            case 'R':
                this->right = true;
                switch (note) {
                    case 'g':
                        startPos = servos[3].read();
                        this->servos[3].write(107);
                        endPos = 107;
                        servoHead = 2;
                        this->servosBeat[1] = true;
                        break;    
                    case 'h':
                        startPos = servos[3].read();
                        this->servos[3].write(95);
                        endPos = 95;
                        servoHead = 2;
                        this->servosBeat[1] = true;
                        break;
                    case 'i':
                        startPos = servos[3].read();
                        this->servos[3].write(85);
                        endPos = 85;
                        servoHead = 2;
                        this->servosBeat[1] = true;
                        break;
                    case 'j':
                        startPos = servos[3].read();
                        this->servos[3].write(74);
                        endPos = 74;
                        servoHead = 2;
                        this->servosBeat[1] = true;
                        break;
                    case 'k':
                        startPos = servos[3].read();
                        this->servos[3].write(62);
                        endPos = 62;
                        servoHead = 2;
                        this->servosBeat[1] = true;
                        break;
                    case 'l':
                        startPos = servos[3].read();
                        this->servos[3].write(50);
                        endPos = 50;
                        servoHead = 2;
                        this->servosBeat[1] = true;
                        break;

                    default: 
                        play = false;
                        this->servosBeat[0] = false;
                        this->servosBeat[1] = false;
                        break;
                }
                break;
            default: 
                this->left = false;
                this->right = false;
                break;
        }
    }
    // SHARPS
    else {
        play = playTmp || play;

        switch(note) {
            //SCL
            case 'm':
                startPos = servos[1].read();
                this->servos[1].write(40);
                endPos = 40;
                servoHead = 0;
                this->servosBeat[0] = true;
                break;
            case 'n':
                startPos = servos[1].read();
                this->servos[1].write(50);
                endPos = 50;
                servoHead = 0;
                this->servosBeat[0] = true;
                break;
            case 'o':
                startPos = servos[1].read();
                this->servos[1].write(72);
                endPos = 72;
                servoHead = 0;
                this->servosBeat[0] = true;
                break;
            case 'p':
                startPos = servos[1].read();
                this->servos[1].write(85);
                endPos = 85;
                servoHead = 0;
                this->servosBeat[0] = true;
                break;

            // SCD
            case 'r':
                startPos = servos[3].read();
                this->servos[3].write(50);
                endPos = 50;
                servoHead = 2;
                this->servosBeat[1] = true;
                break;
            case 's':
                startPos = servos[3].read();
                this->servos[3].write(70);
                endPos = 70;
                servoHead = 2;
                this->servosBeat[1] = true;
                break;
            case 't':
                startPos = servos[3].read();
                this->servos[3].write(80);
                endPos = 80;
                servoHead = 2;
                this->servosBeat[1] = true;
                break;
            case 'u':
                startPos = servos[3].read();
                this->servos[3].write(102);
                endPos = 102;
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

    if (note == 'q') {
        this->playbackMode = true;
        delay(100);  // remove if necesary
    }
    else if (note == 'y')
        this->playbackMode = false;

    int min_ = (servoHead == 0)? this->leftStartPos : this->rightStartPos;
    int max_ = (servoHead == 0)? this->leftBeatPos : this->rightBeatPos;

    //int beat = 0;

    if (play) {
        if (this->servos[servoHead+1].read() == endPos) {
            if ((startPos != endPos) || this->playbackMode)
                delay(noteDelay);
            
            /*if (this->servosBeat[0] && this->servosBeat[1]) {
                this->beat2(0, 2, min_, max_, 5, true);
                this->servosBeat[0] = false;
                this->servosBeat[1] = false;
            }*/

            if (this->left && this->right) {
                this->beat2(0, 2, min_, max_, 5, true);
                this->left = false;
                this->right = false;
            }
            else
                this->beat1(servoHead, min_, max_, 5);
            
            
            // TODO

            // beat2(0, 2, min_, max_, 5, count >= 2);
            
        }
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

void Xylo::beat3(int servo, int min_, int max_) {
    this->servos[servo].write(max_);
    delay(20);
    this->servos[servo].write(min_);
} 