#include<SoftwareSerial.h>

#define RX 10
#define TX 11
#define BAUD 9600

SoftwareSerial bt(10, 11);

void setup() {
  // put your setup code here, to run once:
  pinMode(RX, INPUT);
  pinMode(TX, OUTPUT);
  
  bt.begin(BAUD);
  Serial.begin(9600);
}

void loop() {
  // put your main code here, to run repeatedly:
  
  if (bt.available() > 0)
    Serial.write(bt.read());

  delay(100);
}
