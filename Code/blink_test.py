import sys
import time

from telemetrix import telemetrix

DIGITAL_PIN = 13

board_1 = telemetrix.Telemetrix(arduino_instance_id=1)
board_2 = telemetrix.Telemetrix(arduino_instance_id=2)

board_1.set_pin_mode_digital_output(DIGITAL_PIN)

for blink in range(5):
    try:
        print('1')
        board_1.digital_write(DIGITAL_PIN, 1)
        time.sleep(1)
        print('0')
        board_1.digital_write(DIGITAL_PIN, 0)
        time.sleep(1)
    except KeyboardInterrupt:
        board_1.shutdown()
        board_2.shutdown()
        sys.exit()

# if __name__ == '__main__':
#     # TODO
#     pass
