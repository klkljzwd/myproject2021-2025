import matplotlib.pyplot as plt
from sklearn.metrics import mean_absolute_error as mae
from sklearn.metrics import mean_absolute_percentage_error as mape
from sklearn.metrics import mean_squared_error as mse
import numpy as np

class PltDraw:
    def draw(self,predict,real):
        index = np.arange(len(predict))
        plt.plot(index,predict,label="predict")
        plt.plot(index,real,label="real")
        plt.legend()
        plt.show()