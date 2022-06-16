#include <Wire.h>
#include <SoftwareSerial.h>

#define LED1 4
#define LED2 7

#define RX 10
#define TX 11
#define BAUD 9600

char ledOn = 0;
SoftwareSerial bt(RX, TX);

void setup() {
  // put your setup code here, to run once:
  pinMode(LED1, OUTPUT);
  pinMode(LED2, OUTPUT);
  pinMode(RX, INPUT);
  pinMode(TX, OUTPUT);
  
  Wire.begin();
  bt.begin(BAUD);
  Serial.begin(9600);
}

void loop() {
  // put your main code here, to run repeatedly:
  Wire.beginTransmission(8);
  if (bt.available() > 0) {
    ledOn = bt.read();
    Serial.write(ledOn);

    switch(ledOn) {
      case '0': digitalWrite(LED1, LOW); break;
      case '1': digitalWrite(LED1, HIGH); break;
      case '2': digitalWrite(LED2, LOW); break;
      case '3': digitalWrite(LED2, HIGH); break;
      default: break;
    }

    Wire.write(ledOn);
    Wire.endTransmission();
  }
  // delay(100);
}
