#include <Wire.h>

#define LED1 4
#define LED2 7

void setup() {
    pinMode(LED1, OUTPUT);
    pinMode(LED2, OUTPUT);
    
    Wire.begin(8);
    Wire.onReceive(receiveEvent);
    Serial.begin(9600);
}

void loop() {
  // delay(100);
}


void receiveEvent(int howMany) {
  char ledOn = Wire.read();

  switch(ledOn) {
    case '4': digitalWrite(LED1, LOW); break;
    case '5': digitalWrite(LED1, HIGH); break;
    case '6': digitalWrite(LED2, LOW); break;
    case '7': digitalWrite(LED2, HIGH); break;
    default: break;
  }
}
