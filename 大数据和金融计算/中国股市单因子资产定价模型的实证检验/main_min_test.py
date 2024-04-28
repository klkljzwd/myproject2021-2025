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
plt.rcParams['figure.figsize']=(7.2, 6.6)

data_scale = [1,5,10,30,60,120,240]
data_scale = [1]
color = ['b', 'm', 'g', 'c', 'r', 'k', 'y' ]

data_file = './dataset/SSEC_min.mat'
data_min = DataMinDiff(data_file=data_file)
data_min.getData()
ep = EmpiricalProbability()
def r_picture():
    for i,scale in enumerate(data_scale):
        #获取收益率
        r_data = data_min.calLogR(scale)
        
        #---------------
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
        
        #计算峰度和偏度
        k = stats.kurtosis(data_daily)+3
        s = stats.skew(data_daily)


        print(str(scale)+"日度数据的偏度为\n",s)
        print(str(scale)+"日度数据的峰度为\n",k)
        JB_sta = (s**2) / (6/len(data_daily)) + ((k-3)**2) / (24/len(data_daily))

        p_value = 1 - stats.chi2.cdf(JB_sta, df=2)
        print(str(scale)+"日度数据的JB统计量为\n",JB_sta)
        print(str(scale)+"日度数据的JB统计量的p值为\n",p_value)
        

        mid,p =ep.getP(data_daily,31)
        plt.plot(mid,p,'o-r',lw=2, ms=7, mfc='k',label=r'$r_{day}$'+'  EMP PDF')
        plt.legend(loc=0,prop={'size':10})
        #计算经验概率
        mid,p = ep.getP(r_data,101)
        #----------------
        
        #拟合正态分布日度----------
        r_mean,r_std =stats.norm.fit(data_daily)
        x_fit_min = np.linspace(-0.1, 0.1, 300)
        y = stats.norm.pdf(x_fit_min,loc = r_mean,scale = r_std)
        plt.plot(x_fit_min,y,lw=2,label = r"$r_{day}$"+"  Norm Fits")
           
        #拟合正态分布分钟
        r_mean,r_std =stats.norm.fit(r_data)
        x_fit_min = np.linspace(-0.1, 0.1, 300)
        y = stats.norm.pdf(x_fit_min,loc = r_mean,scale = r_std)
        plt.plot(x_fit_min,y,lw=2, label = r"$r_{min}$"+"  Norm Fits")
        plt.xlim([-0.1,0.1])
        plt.ylim([10e-5,10e3])
        
        
        
        
        #画图
        plt.plot(mid,p,'s-'+color[i],lw=2, ms=7, mfc='k',label=r'$r_{min}$'+'  EMP PDF')
        plt.xlabel(r'$r$', fontsize = 26)
        plt.ylabel(r'$p(r)$', fontsize = 26)
        plt.text(-0.075, 0.035, 'daily', fontsize=20)
        plt.text(-0.045, 0.00025, 'minute', fontsize=20)
        plt.legend(loc='upper right', fontsize=10)
        
    plt.savefig('data1.png')
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
        plt.semilogy(mid,p,'s-'+color[i],label=str(scale)+'min')
        
        plt.semilogy(x_fit_min,y)
        plt.xlim([-0.1,0.1])
        plt.ylim([10e-5,10e3])
        plt.xlabel(r'$r$', fontsize = 26)
        plt.ylabel(r'$p(r)$', fontsize = 26)
        plt.legend(loc = 0)
        plt.savefig('./img/'+'norm-'+str(scale)+'min.png')
        
        plt.show()

def var():
    for i,scale in enumerate(data_scale):
        #获取收益率
        r_data = data_min.calLogR(scale)
        mid,p = ep.getP(r_data,101)
        linear = LinearRegression()
        
        ind = mid>0
        x_mid = mid[ind]
        y_p = p[ind]

        X = np.expand_dims(np.log(x_mid),axis=1)
        Y = np.expand_dims(np.log(y_p),axis=1)

        result = linear.fit(X,Y)

        y = linear.predict(X)

        
        plt.plot(X,y)
        
        plt.plot(np.log(x_mid),np.log(y_p),'s-'+color[i],label=str(scale)+'min')

        plt.xlabel(r'$r$', fontsize = 26)
        plt.ylabel(r'$p(r)$', fontsize = 26)
        plt.legend(loc=0)
        plt.savefig('./img/'+str(scale)+'min'+'-var.png')

        plt.show()
r_picture()
#r_norm()
#var()