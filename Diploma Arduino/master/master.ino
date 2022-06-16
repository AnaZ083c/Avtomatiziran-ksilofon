#include <Wire.h>
#include <SoftwareSerial.h>

#define LED1 4
#define LED2 7

#define RX 10
#define TX 11
#define BAUD 9600

char ledOn = 0;
SoftwareSerial bt(11, 12);

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
  if (bt.available()) {
     
    Serial.write(bt.read());

    if (Serial.available() > 0) {    
      ledOn = Serial.read();
      
      if (ledOn == '0')
        digitalWrite(LED1, LOW);
      else if (ledOn == '1')
        digitalWrite(LED1, HIGH);
      else if (ledOn == '2')
        digitalWrite(LED2, LOW);
      else if (ledOn == '3')
        digitalWrite(LED2, HIGH);
      
      Wire.write(ledOn);
      Wire.endTransmission();
    }
  }
}
