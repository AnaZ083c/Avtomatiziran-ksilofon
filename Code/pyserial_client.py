import serial

ser = serial.Serial("COM8", 9600, timeout=1)

def retrieveData():
    ser.write(b'1')
    data = ser.readline().decode('ascii')
    return data

while True:
    inp = input("Retrieve data?")
    if inp == '1':
        print(retrieveData())
    else:
        ser.write(b'0')