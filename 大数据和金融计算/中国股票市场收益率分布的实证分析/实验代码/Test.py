import pandas as pd
import numpy as np
from scipy.stats import chi2,f

class TestMethods:
    def __init__(self):
        pass
    def waldTest(self,df):
        ret_ind = df['indR']
        res_stocks = df.loc[:,'948R':'600894R']
        T = len(ret_ind)
        N = 8
        mu_market = np.mean(ret_ind)
        sigma_market = np.sum(ret_ind-mu_market**2)/T
        #无限制模型
        x = np.ones((T,2))
        x[:,1] = ret_ind
        y = res_stocks
        xTx = np.dot(np.transpose(x),x)
        xTy = np.dot(np.transpose(x),y)
        AB_hat = np.dot(np.linalg.inv(xTx),xTy)
        alpha = AB_hat[0]
        beta = AB_hat[1]
        resid = y-np.dot(x,AB_hat)
        cov = np.dot(np.transpose(resid),resid)/T
        invCov = np.linalg.inv(cov)
        #限制模型
        xr = np.ones((T,1))
        xr[:,0]=ret_ind
        yr = res_stocks
        xrTxr = np.dot(np.transpose(xr),xr)
        xrTyr = np.dot(np.transpose(xr),yr)
        ABr_hat = np.dot(np.linalg.inv(xrTxr),xrTyr)
        RESDr = yr-np.dot(xr,ABr_hat)
        COVr = np.dot(np.transpose(RESDr),RESDr)/T
        invCOVr = np.linalg.inv(COVr)
        #Wald检验
        trans_ALPHA = np.ones((len(alpha),1))
        trans_ALPHA[:,0]=alpha
        SWChi2 = T*(1/(1+mu_market**2/sigma_market))*np.dot(np.dot(alpha,invCov),trans_ALPHA)
        SWF= (T-N-1)/N*(1/(1+mu_market**2/sigma_market))*np.dot(np.dot(alpha,invCov),trans_ALPHA)
        pvalue_Wchi2 = 1 - chi2.cdf(SWChi2[0],N)
        pvalue_WF = 1-f.cdf(SWF[0],N,T-N-1)
        print("wald检验结果:{0:.3f} {1:.3f}".format(pvalue_Wchi2,pvalue_WF))
        #似然比检验
        SLRchi2 = T*(np.log(np.linalg.det(COVr))-np.log(np.linalg.det(cov)))
        p_SLR = 1 - chi2.cdf(SLRchi2,N)
        print("似然比检验结果:{0:.3f}".format(p_SLR))
        #拉格朗日乘子
        a = np.zeros((8,1))
        a[:,0] = np.sum(RESDr,axis=0)
        salpha = np.dot(invCOVr,a)
        b = np.dot(ret_ind,RESDr)
        sbeta = np.zeros((8,1))
        sbeta[:,0] = np.dot(invCOVr,b)
        score = np.concatenate((salpha,sbeta),axis=0)
        a = np.concatenate((invCOVr*T,invCOVr*np.sum(ret_ind)),axis=1)
        b = np.concatenate((invCOVr*np.sum(ret_ind),invCOVr*np.sum(ret_ind**2)),axis = 1)
        Minfo = np.concatenate((a,b),axis=0)
        SLMchi2 = np.dot(np.dot(np.transpose(score),np.linalg.inv(Minfo)),score)
        pvalue_SLMchi2 = 1 - chi2.cdf(SLMchi2[0][0],N)
        print("拉格朗日检验结果:{0:.3f}".format(pvalue_SLMchi2))
        
        
        