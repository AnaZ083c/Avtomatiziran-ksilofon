import random
import kivy
from kivy.uix.boxlayout import BoxLayout
from kivy.app import App

kivy.require('2.1.0')

class RootApp(BoxLayout):
    def __init__(self):
        super(RootApp, self).__init__()

    def generate_random(self):
        self.random_num.text = str(random.randint(0, 1000))


class Robophone(App):
    def build(self):
        return RootApp()


if __name__ == '__main__':
    Robophone().run()
