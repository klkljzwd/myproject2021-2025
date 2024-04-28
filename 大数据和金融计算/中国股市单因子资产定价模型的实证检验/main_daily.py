from cProfile import label
from regex import P
from DataMinute import DataMinDiff
from EmpiricalProbability import EmpiricalProbability
import numpy as np
import pandas as pd
import matplotlib.pyplot as plt
from scipy import stats
from sklearn.linear_model import LinearRegression


plt.rcParams.update({"font.family": "STIXGeneral",
                      "font.size": 20,
                      "mathtext.fontset": "cm"})
plt.rcParams['figure.figsize']=(6.8, 5.6)


#日数据所在文件路径
data_file='dataset/SSEC_day.csv'
#用以获取数据和计算收益率的类
data_tool = DataMinDiff(data_file)
#计算经验概率的类
ep = EmpiricalProbability()
#读取日度数据
data = pd.read_csv(data_file,encoding='gbk')
data = data['OT']
data = np.array(data)
#将数据赋值给datamin.data_min
data_tool.data_min = data

data_daily = data_tool.calLogR(1)

mid,p =ep.getP(data_daily,31)
plt.semilogy(mid,p,'o-r')
plt.show()