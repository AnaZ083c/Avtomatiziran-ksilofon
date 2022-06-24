#ifndef XYLOPHONE_H
#define XYLOPHONE_H

#include <Arduino.h>
#include <Servo.h>

class Xylo {
    public:
        Servo servos[4];
        bool servosBeat[2] = {false, false};

        int leftStartPos;
        int leftBeatPos;
        int rightStartPos;
        int rightBeatPos;

        int startPos;
        int beatPos;

        // inits
        void servosInit(int servos[4], int positions[4]) {
            this->servos[0].attach(servos[0]);
            this->servos[1].attach(servos[1]);
            this->servos[2].attach(servos[2]);
            this->servos[3].attach(servos[3]);

            this->leftStartPos = positions[0];
            this->leftBeatPos = positions[1];
            this->rightStartPos = positions[2];
            this->rightBeatPos = positions[3];

            //this->gotoNote('d');
            //this->gotoNote('h');

            this->startPos = max(leftStartPos, rightStartPos);
            this->beatPos = max(leftBeatPos, rightBeatPos);

            this->servos[0].write(this->leftStartPos);
            this->servos[2].write(this->rightStartPos);
        }

        // constructor
        Xylo() {
            this->servosBeat[0] = false;
            this->servosBeat[1] = false;
        }

        void setup(int servos[4], bool servosBeat[2], int positions[4]) {
            this->servosInit(servos, positions);
            
            this->servosBeat[0] = servosBeat[0];
            this->servosBeat[1] = servosBeat[1];
        }

        void setup(int servos[4], int positions[4]) {
            this->servosInit(servos, positions);
        }


        // functions
        void gotoNote(char note = 0, bool play = false, bool sharps = false, float noteDelay = 200);
        void beat2(int servo1, int servo2, int min_, int max_, float servoSpeed = 0.2, bool twoServo = false);
        void beat1(int servo, int min_, int max_, float servoSpeed = 0.2);

    
    private:
        bool moveToPos(Servo servo, int pos, int step);

        /*int notSharpNotes[12] = {
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
        };*/

        String notSharps = "abcdefghijkl";
        String sharps = "mnoprstu";

        // functions
        float calculateDelay(char startNote, char endNote, float noteDelay);
        int calculateSteps(Servo servo, int newPos);
};


#endif