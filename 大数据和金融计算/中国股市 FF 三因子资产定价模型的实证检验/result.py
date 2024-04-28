import numpy as np
import pandas as pd
import statsmodels.api as sm
from scipy.stats import norm,t


def calT(avg_y,y):
    T = len(y)
    sigma = 0.0
    for i in range(len(y)):
        mse = (y[i]-avg_y)**2
        sigma+=mse
    si = np.sqrt(1/(T-1)*sigma)
    t = avg_y*np.sqrt(T)/si
    return t


params = pd.read_hdf('params.h5',key='df')
avg_const = np.mean(params['const'])
avg_size = np.mean(params['size'])
avg_ep = np.mean(params['ep'])
avg_beta = np.mean(params['beta'])

tc = calT(avg_const,np.array(params['const']))
print("const")
print("统计量",tc)
print("系数",avg_const)
print("P值",1-norm.cdf(abs(tc),0,1))

tc = calT(avg_size,np.array(params['size']))
print("size")
print("统计量",tc)
print("系数",avg_size)
print("P值",1-norm.cdf(abs(tc),0,1))

tc = calT(avg_ep,np.array(params['ep']))
print("ep")
print("统计量",tc)
print("系数",avg_ep)
print("P值",1-norm.cdf(abs(tc),0,1))

tc = calT(avg_beta,np.array(params['beta']))
print("beta")
print("统计量",tc)
print("系数",avg_beta)
print("P值",1-norm.cdf(abs(tc),0,1))