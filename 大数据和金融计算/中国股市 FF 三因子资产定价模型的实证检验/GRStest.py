import numpy as np
import pandas as pd
from scipy.stats import f

#ym是所有资产矩阵，即因变量
#xm是自变量
def grstest(xm,ym):
    T = len(ym)#时间序列长度
    N = 10#资产数Y
    K = 3#因子数3
    #xm是(306,4)包含截距项的自变量矩阵，ym是因变量矩阵为(306,7)，表示7个行业
    #xmT*xm = (4,4)为相关系数矩阵
    #xmT*ym = (4,306) * (306,7) = (4,7)可以理解为xm和ym的相关性
    xmTxm = np.dot(np.transpose(xm),xm)
    xmTym = np.dot(np.transpose(xm),ym)
    #AB_hat = (4,4) * (4,7) = (4,7) 是线性回归估计的系数矩阵
    AB_hat = np.dot(np.linalg.inv(xmTxm),xmTym)
    #ALPHA是系数矩阵中的截距项
    ALPHA = AB_hat[0]

    #残差
    RESD = ym-np.dot(xm,AB_hat)
    #残差相关系数
    COV = np.dot(np.transpose(RESD),RESD)/T
    invCOV = np.linalg.inv(COV)
    #fs是剩下的三个变量
    fs = xm.loc[:,['mkt','smb','hml']]
    fs  =np.array(fs)
    #均值
    muhat = np.mean(fs,axis=0).reshape((3,1))
    fs = fs - np.mean(fs,axis=0)
    omeghat = np.dot(np.transpose(fs),fs)/T
    invOMG = np.linalg.inv(omeghat)
    xxx = np.dot(np.dot(np.transpose(muhat),invOMG),muhat)

    yyy = np.dot(np.dot(ALPHA,invCOV),np.transpose(ALPHA))


    GRS = (T-N-K)/N*(1+xxx[0][0])*yyy
    print(GRS)
    pvalue = 1-f.cdf(GRS,N,T-N-K)

    print(pvalue)