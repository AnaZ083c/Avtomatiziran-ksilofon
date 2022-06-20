# Avtomatiziran ksilofon
 Diplomska naloga
 
 Stari Arduino: COM4, ID 1 <br>
 Novi Arduino: COM7, ID 2 <br>
 <br>
 arduino_instance_id ... ARDUINO_ID <br>
 
## TODO - na novo z Arduino IDE
- [x] Usposobi Arduino IDE
- [x] Preizkusi I2C vezavo med dvema Arduinoma
  - [x] Testiranje 
    - [x] Zraven poveži še Bluetooth modul HC-05 in ga preizkusi tako, da prek telefona prižgeš LED diode, ki so priklopljene na Arduino
    - [x] Preizkusi hkratnost prižiganja in ugašanja LED diod
    - [x] LED diode sedaj zamenjaj s 4-mi motorji iz robota ter popravi program tako, da boš lahko iz telefona kontrolirala 2 robotski roki na Master Arduinu.
  - [x] Pravi program za kontroliranje Avtomatiziranega ksilofona prek Bluetooth modula
    - [x] Knjižnica Xylophone; da ni potrebe po večkratnem kopiranju kode
    - [x] Program na Master Arduinu
    - [x] Program na Slave Arduinu 
- [ ] Naredi Android aplikacijo, preko katere uporabnik pošilja podatke na Bluetooth, iz katerega Master Arduino podatke prebere in določene pošlje na Slave Arduino.
  - [x] Usposobi Android JS
  - [x] Usposobi Node.js
    - [x] Inštaliraj nvm
  - [x] Inštaliraj Android JS
  - [ ] Aplikacija za kontroliranje Avtomatiziranega ksilofona prek Bluetooth modula

## Napredki
- 20/06/2022 https://youtu.be/CKBQpshl-Xo

## Ugotovitve
1. Stabilne in delujoče knjižnice za Bluetooth (PyBluez) za Python 3.7 ali novejše ni; Python knjižnica za Arduino deluje le za Python verzije 3.7 ali novejše, stabilna verzija je le 0.22, kar pa deluje za Python 3.3 ali starejše.
2. Programirati Arduino v Python-u s knjižnico Firmata se ne da, saj se ob izklopu Arduina iz računalnika Python program v celoti izbriše, ostane le gonilnik Firmata.
3. Vezje moram narediti še enkrat
4. Uporabila bom Arduino IDE za programiranje Arduinov in Javascript za izdelavo aplikacije za Android, saj mi bo to omogočilo pošiljanje sporočil prek Bluetooth modula
5. Uporabila bom vezavo I2C Bus, kjer je Arduino na katerega je veza Bluetooth Master, drugi Arduino in Bluetooth modul pa sta Slave.
 
## Viri in povezave
1. Knjižnica Telemetrix za programiranje Arduina v programskem jeziku Python: https://mryslab.github.io/telemetrix/
2. GitHub repozitorij primerov uporabe knjižnice Telemetrix: https://github.com/MrYsLab/telemetrix/tree/master/examples
3. Knjižnica Kivy za Android GUI aplikacijo: https://kivy.org/doc/stable/gettingstarted/installation.html
4. PCB izdelava: https://jlcpcb.com/ (pošiljajo tudi v Slovenijo)
5. Načrtovanje PCB in načrta: https://easyeda.com/editor#id=280289e818d9433c9aece03fcf519e8a|6830f085d17f4884a8d563e2a4c2cf8f
6. Inštalacija Node.js: https://docs.microsoft.com/en-us/windows/dev-environment/javascript/nodejs-on-wsl
7. Inštalacija Android JS: https://android-js.github.io/
