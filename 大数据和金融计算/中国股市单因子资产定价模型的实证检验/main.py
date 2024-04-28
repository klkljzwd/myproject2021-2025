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
plt.grid(True)
data_scale = [1,5,10,30,60,120,240]
color = ['r', 'm', 'g', 'c', 'b', 'k', 'y' ]

data_file = './dataset/SSEC_min.mat'
data_min = DataMinDiff(data_file=data_file)
data_min.getData()
ep = EmpiricalProbability()
def r_picture():
    for i,scale in enumerate(data_scale):
        #获取收益率
        r_data = data_min.calLogR(scale)
        #计算经验概率
        mid,p = ep.getP(r_data,101)
        #画图
        plt.semilogy(mid,p,'s-'+color[i],label=str(scale)+'min r')
        plt.legend(loc=0)
    plt.savefig('data.png')
    plt.show()
def r_norm():
    for i,scale in enumerate(data_scale):
        #获取收益率
        r_data = data_min.calLogR(scale)
        
        
        #拟合正态分布
        r_mean,r_std = stats.norm.fit(r_data)
        x_fit_min = np.linspace(-0.1, 0.1, 300)
        y = stats.norm.pdf(x_fit_min,loc = r_mean,scale = r_std)
        mid,p = ep.getP(r_data,101)

        
        #计算峰度和偏度
        k = stats.kurtosis(r_data)+3
        s = stats.skew(r_data)

        print(str(scale)+"分钟数据的数量为\n",len(r_data))
        print(str(scale)+"分钟数据的偏度为\n",s)
        print(str(scale)+"分钟数据的峰度为\n",k)
        JB_sta = (s**2) / (6/len(r_data)) + ((k-3)**2) / (24/len(r_data))

        p_value = 1 - stats.chi2.cdf(JB_sta, df=2)
        print(str(scale)+"分钟数据的JB统计量为\n",JB_sta)
        print(str(scale)+"分钟数据的JB统计量的p值为\n",p_value)
        
        
        plt.semilogy(mid,p,'s-'+color[i],label=str(scale)+'min r data')
        
        plt.semilogy(x_fit_min,y,label=str(scale)+'min r norm')
        plt.xlim([-0.1,0.1])
        plt.ylim([10e-5,10e3])
        plt.legend(frameon=False,loc="upper right",fontsize='small') 
        plt.savefig('./img/'+'norm-'+str(scale)+'min.png')
        plt.show()

def var():
    for i,scale in enumerate(data_scale):
        #获取收益率
        r_data = data_min.calLogR(scale)
        mid,p = ep.getP(r_data,101)
        linear = LinearRegression()
        #筛选正尾数据
        ind = mid>0
        x_mid = mid[ind]
        y_p = p[ind]
        #线性回归
        X = np.expand_dims(np.log(x_mid),axis=1)
        Y = np.expand_dims(np.log(y_p),axis=1)
        result = linear.fit(X,Y)
        y = linear.predict(X)
        intercept = result.intercept_
        k = result.coef_

        text = "y="+"%.3f"%(k[0,0])+'x'+"%.3f"%(intercept[0])
        
        plt.plot(X,y)
        plt.plot(np.log(x_mid),np.log(y_p),'s-'+color[i],label=str(scale)+'min')
        plt.text(-5,-1,text, fontsize=12)
        plt.legend(loc="upper right",fontsize='small')
        plt.savefig('./img/'+str(scale)+'min'+'正尾.svg')
        plt.show()
def var_fu():
    for i, scale in enumerate(data_scale):
        # 获取收益率
        r_data = data_min.calLogR(scale)
        mid, p = ep.getP(r_data, 101)
        linear = LinearRegression()
        #获取负半部分尾部
        ind = mid <= -(0.01)
        #获取正半部分尾部
        #ind = mid >= (0.01)

        x_mid = mid[ind]
        y_p = p[ind]
        x_mid = np.abs(x_mid)
        y_p = np.abs(y_p)

        #线性回归
        X = np.expand_dims(np.log(x_mid), axis=1)
        Y = np.expand_dims(np.log(y_p), axis=1)
        result = linear.fit(X, Y)
        y = linear.predict(X)
        intercept = result.intercept_
        k = result.coef_
        print(k[0,0])
        #还原回去，用loglog作图
        X = np.exp(X)
        y = np.exp(y)

        plt.loglog(X, y)

        plt.loglog(x_mid, y_p, 's-' + color[i], label=str(scale) + 'min')

        plt.legend(loc="upper right", fontsize='small')


        plt.savefig('./img/' + str(scale) + 'min' + '负尾.svg')

        plt.show()
def tail_show():
    for i, scale in enumerate(data_scale):
        # 获取收益率
        r_data = data_min.calLogR(scale)
        mid, p = ep.getP(r_data, 101)
        linear = LinearRegression()
        ind = mid > 0
        x_mid = mid[ind]
        y_p = p[ind]
        x_mid = np.abs(x_mid)
        y_p = np.abs(y_p)
        plt.loglog(x_mid, y_p, '.-' + color[i], label=str(scale) + 'min')
        plt.legend(frameon=False,loc="lower left", fontsize='xx-small')

    plt.savefig('./img/' + '-正尾集合.svg')
    plt.show()

#r_picture()
#r_norm()
#var()
var_fu()
#tail_show()