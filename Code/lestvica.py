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
board = telemetrix.Telemetrix(arduino_instance_id=1)
board2 = telemetrix.Telemetrix(arduino_instance_id=2)
board.set_pin_mode_servo(SERVO_1_PIN, 544, 2400)
board.set_pin_mode_servo(SERVO_2_PIN, 544, 2400)
board.set_pin_mode_servo(SERVO_3_PIN, 544, 2400)
board.set_pin_mode_servo(SERVO_4_PIN, 544, 2400)

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
        board.servo_write(servo_pin, pos)
        time.sleep(sweep_speed)
    for pos in range(max_deg, min_deg-1, -1):
        print(pos)
        board.servo_write(servo_pin, pos)
        time.sleep(sweep_speed)

def sweep_2_servo(servo1, servo2, min, max, dif, sweep_speed, _board, two_servo: bool):
    for pos in range(min, max + 1):
        _board.servo_write(servo1, pos)
        if two_servo:
            _board.servo_write(servo2, pos - dif)
        time.sleep(sweep_speed)
    for pos in range(max, min - 1, -1):
        _board.servo_write(servo1, pos)
        if two_servo:
            _board.servo_write(servo2, pos - dif)
        time.sleep(sweep_speed)

def move_servo(servo_pin, dest, move_speed: float, _board = None):
    for pos in range(dest+1):
        print(pos)
        if _board is not None:
            _board.servo_write(servo_pin, pos)
        time.sleep(move_speed)

def beat_servo(servo_pin, servo_beat_pin, max_deg):
    for pos in range(max_deg + 1):
        board.servo_write(servo_beat_pin, 90)
        print(pos)
        board.servo_write(servo_pin, pos)
        board.servo_write(servo_beat_pin, WH_BEAT_DEG)
        time.sleep(0.015)

    for pos in range(max_deg, -1, -1):
        board.servo_write(servo_beat_pin, 90)
        print(pos)
        board.servo_write(servo_pin, pos)
        board.servo_write(servo_beat_pin, WH_BEAT_DEG)
        time.sleep(0.015)


def goto_note(_board, servo, servo2, note: int, move_speed = 0.0015, beat: bool = False, _min = 111, _max = 116):
    if note == 1:
        _board.servo_write(servo, 130)
    elif note == 2:
        _board.servo_write(servo, 115)
    elif note == 3:
        _board.servo_write(servo, 105)
    elif note == 4:
        _board.servo_write(servo, 90)
    elif note == 5:
        _board.servo_write(servo, 80)
    elif note == 6:
        _board.servo_write(servo, 70)

    elif note == 7:
        _board.servo_write(servo, 107)
    elif note == 8:
        _board.servo_write(servo, 95)
    elif note == 9:
        _board.servo_write(servo, 85)
    elif note == 10:
        _board.servo_write(servo, 74)
    elif note == 11:
        _board.servo_write(servo, 62)
    elif note == 12:
        _board.servo_write(servo, 50)

    # sharps
    elif note == 13:
        _board.servo_write(servo, 40)
    elif note == 14:
        _board.servo_write(servo, 50)
    elif note == 15:
        _board.servo_write(servo, 72)
    elif note == 16:
        _board.servo_write(servo, 85)

    elif note == 17:
        _board.servo_write(servo, 50)
    elif note == 18:
        _board.servo_write(servo, 70)
    elif note == 19:
        _board.servo_write(servo, 80)
    elif note == 20:
        _board.servo_write(servo, 102)


    if beat:
        time.sleep(.2)
        sweep_2_servo(servo2, None, _min, _max, 5, 0.0002, _board, False)

""""""""""""""""""""""""""

left_start_pos = 111
left_beat_pos = 115
right_start_pos = 106
right_beat_pos = 110
speed = 0.0005

left_start_pos2 = 98
left_beat_pos2 = 103
right_start_pos2 = 95
right_beat_pos2 = 100

start_pos = max(left_start_pos, right_start_pos)
beat_pos = max(left_beat_pos, right_beat_pos)

start_pos2 = max(left_start_pos2, right_start_pos2)
beat_pos2 = max(left_beat_pos2, right_beat_pos2)


move_speed = 0.0015

board.servo_write(SERVO_2_PIN, left_start_pos)
board.servo_write(SERVO_4_PIN, right_start_pos)
board2.servo_write(SERVO_2_PIN, left_start_pos2)
board2.servo_write(SERVO_1_PIN, 85)
board2.servo_write(SERVO_4_PIN, 95)
board2.servo_write(SERVO_3_PIN, 80)
time.sleep(.2)

time.sleep(5)

for i in range(1, 7):
    goto_note(board, SERVO_1_PIN, SERVO_2_PIN, i, 0.0015, True, left_start_pos, left_beat_pos)
    time.sleep(.2)

for i in range(7, 13):
    goto_note(board, SERVO_3_PIN, SERVO_4_PIN, i, 0.0015, True, right_start_pos, right_beat_pos)
    time.sleep(.2)

for i in range(13, 17):
    goto_note(board2, SERVO_1_PIN, SERVO_2_PIN, i, 0.0015, True, left_start_pos2, left_beat_pos2)
    time.sleep(.2)

for i in range(17, 21):
    goto_note(board2, SERVO_3_PIN, SERVO_4_PIN, i, 0.0015, True, right_start_pos2, right_beat_pos2)
    time.sleep(.2)

# goto_note(board2, SERVO_3_PIN, SERVO_4_PIN, 20) # , 0.0015, True)

# while True:
#     try:
#         pass
#         # sweep_2_servo(SERVO_2_PIN, SERVO_4_PIN, start_pos, beat_pos, 5, speed, board)
#         # sweep_2_servo(SERVO_2_PIN, SERVO_4_PIN, start_pos2, beat_pos2, 5, speed, board2)
#         # for i in range(1, 6):
#         #     goto_note(board, SERVO_1_PIN, i)
#         #     time.sleep(1)
#
#
#
#     except KeyboardInterrupt:
#         break

board.shutdown()
board2.shutdown()
sys.exit()
