import sys
import time

from telemetrix import telemetrix

"""
Attach a pin to a servo and move it about.
"""

# some globals
SERVO_1_PIN = 3
SERVO_2_PIN = 6

# Create a Telemetrix instance.
board = telemetrix.Telemetrix(arduino_instance_id=1)
board.set_pin_mode_servo(SERVO_1_PIN, 544, 2400)
board.set_pin_mode_servo(SERVO_2_PIN, 544, 2400)
time.sleep(5)

# board.servo_write(SERVO_PIN, 80)
# time.sleep(.2)
# board.servo_write(SERVO_PIN, -80)

def move_servo(servo_pin):
    for pos in range(181):
        print(pos)
        board.servo_write(servo_pin, pos)
        time.sleep(0.015)
    for pos in range(180, -1, -1):
        print(pos)
        board.servo_write(servo_pin, pos)
        time.sleep(0.015)


while True:
    try:
        move_servo(SERVO_1_PIN)
        move_servo(SERVO_2_PIN)
    except KeyboardInterrupt:
        break

board.shutdown()
sys.exit()
