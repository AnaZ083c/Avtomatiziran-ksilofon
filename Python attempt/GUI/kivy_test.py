import random
import kivy
from kivy.uix.boxlayout import BoxLayout
from kivy.app import App

kivy.require('2.1.0')

class MyRoot(BoxLayout):
    def __init__(self):
        super(MyRoot, self).__init__()

    def generate_random(self):
        self.random_num.text = str(random.randint(0, 1000))


class Hello(App):
    def build(self):
        return MyRoot()


if __name__ == '__main__':
    Hello().run()
