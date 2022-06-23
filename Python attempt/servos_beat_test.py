import sys
import time

from telemetrix import telemetrix

"""
Attach a pin to a servo and move it about.
"""

# some globals
SERVO_1_PIN = 3 # bottom left
SERVO_2_PIN = 5 # top left
SERVO_3_PIN = 6 # bottom right
SERVO_4_PIN = 9 # top right

WH_BEAT_DEG = 125

# Create a Telemetrix instance.
# board = telemetrix.Telemetrix(arduino_instance_id=1)
board2 = telemetrix.Telemetrix(arduino_instance_id=2)
# board.set_pin_mode_servo(SERVO_1_PIN, 544, 2400)
# board.set_pin_mode_servo(SERVO_2_PIN, 544, 2400)
# board.set_pin_mode_servo(SERVO_3_PIN, 544, 2400)
# board.set_pin_mode_servo(SERVO_4_PIN, 544, 2400)

board2.set_pin_mode_servo(SERVO_1_PIN, 544, 2400)
board2.set_pin_mode_servo(SERVO_2_PIN, 544, 2400)
board2.set_pin_mode_servo(SERVO_3_PIN, 544, 2400)
board2.set_pin_mode_servo(SERVO_4_PIN, 544, 2400)
time.sleep(5)

# # board.servo_write(SERVO_PIN, 80)
# time.sleep(.2)
# # board.servo_write(SERVO_PIN, -80)

def sweep_servo(servo_pin, min_deg, max_deg, sweep_speed):
    for pos in range(min_deg, max_deg + 1):
        print(pos)
        # board.servo_write(servo_pin, pos)
        time.sleep(sweep_speed)
    for pos in range(max_deg, min_deg-1, -1):
        print(pos)
        # board.servo_write(servo_pin, pos)
        time.sleep(sweep_speed)

def sweep_2_servo(servo1, servo2, min, max, dif, sweep_speed, _board):
    for pos in range(min, max + 1):
        _board.servo_write(servo1, pos)
        _board.servo_write(servo2, pos - dif)
        time.sleep(sweep_speed)
    for pos in range(max, min - 1, -1):
        _board.servo_write(servo1, pos)
        _board.servo_write(servo2, pos - dif)
        time.sleep(sweep_speed)

def move_servo(servo_pin, dest, move_speed: float):
    for pos in range(dest+1):
        print(pos)
        # board.servo_write(servo_pin, pos)
        time.sleep(move_speed)

def beat_servo(servo_pin, servo_beat_pin, max_deg):
    for pos in range(max_deg + 1):
        # board.servo_write(servo_beat_pin, 90)
        print(pos)
        # board.servo_write(servo_pin, pos)
        # board.servo_write(servo_beat_pin, WH_BEAT_DEG)
        time.sleep(0.015)

    for pos in range(max_deg, -1, -1):
        # board.servo_write(servo_beat_pin, 90)
        print(pos)
        # board.servo_write(servo_pin, pos)
        # board.servo_write(servo_beat_pin, WH_BEAT_DEG)
        time.sleep(0.015)



# board.servo_write(SERVO_2_PIN, 124)

# def beat(servo_pin, servo_pos, max_deg, speed):
#     if servo_pos < max_deg:
#         servo_pos += speed
#     if servo_pos > max_deg:
#         servo_pos -= speed
#     if (abs(servo_pos - max_deg) < speed:
#         break
#     board.servo_write(servo_pin, servo_pos)
#     time.sleep(0.2)
#
#     # for pos in range(max_deg, 90 - speed, -speed):
#     #     board.servo_write(servo_pin, pos)
#     # board.servo_write(servo_pin, 90)
#
#
# beat(SERVO_2_PIN, 120, 50)

# servo_pos = 115
# max_deg = 120
# speed = 20

""""""""""""""""""""""""""

left_start_pos = 111
left_beat_pos = 116
right_start_pos = 106
right_beat_pos = 111
speed = 0.0005

left_start_pos2 = 95
left_beat_pos2 = 100
right_start_pos2 = 100
right_beat_pos2 = 105

start_pos = max(left_start_pos, right_start_pos)
beat_pos = max(left_beat_pos, right_beat_pos)

start_pos2 = max(left_start_pos2, right_start_pos2)
beat_pos2 = max(left_beat_pos2, right_beat_pos2)


move_speed = 0.0015

# board.servo_write(SERVO_2_PIN, left_start_pos)
# board.servo_write(SERVO_4_PIN, right_start_pos)
board2.servo_write(SERVO_2_PIN, left_start_pos2)
board2.servo_write(SERVO_3_PIN, 80)
board2.servo_write(SERVO_4_PIN, right_start_pos2)

time.sleep(5)

while True:
    try:
        # board.servo_write(SERVO_2_PIN, 115)
        # time.sleep(0.5)
        # board.servo_write(SERVO_2_PIN, 1)
        # time.sleep(0.1)

        # for pos in range(40, 181):
        #     move_servo(SERVO_1_PIN, pos, move_speed)
        #     time.sleep(0.2)
        #     sweep_servo(SERVO_2_PIN, start_pos, beat_pos, speed)
        #     time.sleep(0.2)
        #
        # sweep_servo(SERVO_2_PIN, left_start_pos, left_beat_pos, speed)
        # sweep_servo(SERVO_4_PIN, right_start_pos, right_beat_pos, speed)

        # sweep_2_servo(SERVO_2_PIN, SERVO_4_PIN, start_pos, beat_pos, 5, speed, board)
        sweep_2_servo(SERVO_2_PIN, SERVO_4_PIN, start_pos2, beat_pos2, 5, speed, board2)

        # time.sleep(0.2)

        # sweep_servo(SERVO_1_PIN, 0, 180, 0.1)

    except KeyboardInterrupt:
        break

# board.shutdown()
board2.shutdown()
sys.exit()
