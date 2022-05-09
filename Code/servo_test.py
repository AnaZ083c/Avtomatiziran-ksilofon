import sys
import time

from telemetrix import telemetrix

"""
Attach a pin to a servo and move it about.
"""

# some globals
SERVO_PIN = 3

# Create a Telemetrix instance.
board = telemetrix.Telemetrix(arduino_instance_id=2)
board.set_pin_mode_servo(SERVO_PIN, 544, 2400)
time.sleep(5)

# board.servo_write(SERVO_PIN, 80)
# time.sleep(.2)
# board.servo_write(SERVO_PIN, -80)

while True:
    try:
        for pos in range(181):
            print(pos)
            board.servo_write(SERVO_PIN, pos)
            time.sleep(0.015)
        for pos in range(180, -1, -1):
            print(pos)
            board.servo_write(SERVO_PIN, pos)
            time.sleep(0.015)
    except KeyboardInterrupt:
        break

board.shutdown()
sys.exit()
