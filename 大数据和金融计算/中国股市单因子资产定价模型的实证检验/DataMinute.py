import numpy as np
import pandas as pd
import scipy.io as sio
from scipy import stats
import matplotlib.pyplot as plt
from sympy import re
#不同尺度收益率的数据
class DataMinDiff:
    def __init__(self,data_file) -> None:
        self.data_file = data_file
        self.data_min = None
    def getData(self):
        #从文件中读取数据
        mat_file = sio.loadmat(self.data_file)
        data_min = mat_file['p'][:,0]
        self.data_min = data_min

    def calLogR(self,minute):
        #获取数据
        data_min = self.data_min
        #根据指定的时间间隔获取数据
        data = data_min[::minute]
        data = data[data>0]
        #计算指数收益率
        r_data = np.log(data[1:])-np.log(data[:-1])
    
        #清洗数据
        r_data = r_data[~np.isnan(r_data)]
        r_data = r_data[(r_data >= -0.1) & (r_data <= 0.1)]
        return r_data


if __name__== "__main__":
    dataMinDiff = DataMinDiff("./dataset/SSEC_min.mat")
    r_data = dataMinDiff.calLogR(1)
    
    print(r_data.shape)
    plt.figure(figsize=(30,20))
    plt.plot(r_data)
    plt.grid(True)
    plt.show()