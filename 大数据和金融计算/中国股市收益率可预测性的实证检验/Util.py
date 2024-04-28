import numpy as np
import pandas as pd
from math import pi

import statsmodels.api as sm
import statsmodels.formula.api as smf
from sklearn.linear_model import *
from sklearn.tree import DecisionTreeRegressor
def myfun_stat_gains(rout,rmean,rreal):
    R2os = 1 - np.sum((rreal-rout)**2)/np.sum((rreal-rmean)**2)
    d = (rreal-rmean)**2 - ((rreal-rout)**2 - (rmean-rout)**2)
    x = sm.add_constant(np.arange(len(d))+1)
    model = sm.OLS(d,x)
    fit_model = model.fit()
    MFSEadj = fit_model.tvalues[0]
    p_valueMFSE = fit_model.pvalues[0]
    
    if R2os>0 and p_valueMFSE<=0.01:
        jud = "在1%的显著性水平下有样本外预测能力"
    elif R2os>0 and p_valueMFSE>0.01 and p_valueMFSE<=0.05:
        jud = "在5%的显著性水平下有样本外预测能力"
    elif R2os>0 and p_valueMFSE>0.05 and p_valueMFSE<=0.1:
        jud = "在10%的显著性水平下有样本外预测能力"
    else:
        jud = "无样本外预测能力"
    print('Stat gains: R2os = {:f}, MFSEadj = {:f}, MFSEpvalue = {:f}'.format(R2os, MFSEadj, p_valueMFSE))
    print('Inference: {:s}'.format(jud))
    return R2os, MFSEadj,p_valueMFSE
def myfun_econ_gains(rout,rmean,rreal,rfree,volt2,gmm=5):
    #根据预测的收益率矩阵构造投资组合
    omg_out = rout/volt2/gmm
    rp_out = rfree+omg_out*rreal
    Uout = np.mean(rp_out)-0.5*gmm*np.var(rp_out)
    omg_mean = rmean/volt2/gmm
    #根据过去平均的收益率矩阵构造投资组合
    rp_mean = rfree + omg_mean*rreal
    Umean = np.mean(rp_mean) - 0.5*gmm * np.var(rp_mean)
    DeltaU = Uout - Umean
    if DeltaU<10**-6:
        jud = "没有经济意义"
    else:
        jud = "有经济意义"
    print('Econ Gains: Delta U = {:f}, Upred = {:f}, Umean = {:f}'.format(DeltaU, Uout, Umean))
    print('Inference: {:s}'.format(jud))
    return Uout,Umean,DeltaU

def single_in_predict(factor_out,factor_out_l1,datafit):
    model = smf.ols(f'ExRet ~ '+factor_out_l1,data=datafit[['ExRet',factor_out_l1]])
    results = model.fit()
    rg_con = results.params['Intercept']
    rg_con_p = results.pvalues['Intercept']
    rg_DP = results.params[factor_out_l1]
    rg_DP_p = results.pvalues[factor_out_l1]
    if rg_DP_p <= 0.01:
        jud = '在1%的显著性水平下有样本内预测能力'
    elif (rg_DP_p > 0.01) & (rg_DP_p <= 0.05):
        jud = '在5%的显著性水平下有样本内预测能力'
    elif (rg_DP_p> 0.05) & (rg_DP_p <= 0.1):
        jud = '在10%的显著性水平下有样本内预测能力'
    else:
        jud = '无样本内预测能力'
    print('In-sample tests for one factor model with OLS:')
    print('Predictor: {:s}'.format(factor_out))
    print('Regressing Results: b = {:f}, k = {:f}'.format(rg_con, rg_DP))
    print('Regressing Pvalues: p = {:f}, p = {:f}'.format(rg_con_p,rg_DP_p))
    print('Inference: {:s}'.format(jud))
    
def single_out_predict(factor_out,factor_out_l1,datafit):
    n_in = np.sum(datafit['yyyymm'] <= 195612)
    n_out = np.sum(datafit['yyyymm'] > 195612)
    rout = np.zeros(n_out)
    rmean = np.zeros(n_out)
    rreal = np.zeros(n_out)
    rfree = np.zeros(n_out)
    volt2 = np.zeros(n_out)

    for i in range(n_out):
        model = smf.ols('ExRet ~ '+factor_out_l1, data=datafit[['ExRet', factor_out_l1]].iloc[:(n_in+i), :])
        results = model.fit()
        b = results.params['Intercept']
        k = results.params[factor_out_l1]
        f = datafit[factor_out].iloc[n_in+i-1]
        rout[i] = k*f+b
        rmean[i] = np.mean(datafit['ExRet'].iloc[:n_in+i].values)
        rreal[i] = datafit['ExRet'].iloc[n_in+i]
        rfree[i] = datafit['Rfree'].iloc[n_in+i]
        volt2[i] = np.sum(datafit['Ret'].iloc[(n_in+i-12):(n_in+i)].values**2)

    print('Out-of-Sample tests for one factor model with OLS:')
    print('Predictor: {:s}'.format(factor_out))
    R2os, MSFEadj, pvalue_MSFEadj = myfun_stat_gains(rout, rmean, rreal)
    Uout, Umean, DeltaU = myfun_econ_gains(rout, rmean, rreal, rfree, volt2, gmm=5)  

def muti_out_predict(factor_out,datafit):
    n_in = np.sum(datafit['yyyymm'] <= 195612)
    n_out = np.sum(datafit['yyyymm'] > 195612)
    rout = np.zeros(n_out)
    rmean = np.zeros(n_out)
    rreal = np.zeros(n_out)
    rfree = np.zeros(n_out)
    volt2 = np.zeros(n_out)

    for i in range(n_out):
        model = smf.ols('ExRet ~ DPL1 + EPL1 + VOLL1 + BILLL1 + BONDL1 + TERML1 +'
                        'CREDITL1 + PPIGL1 + IPGL1 + MA112L1 + MA312L1 + MOM6L1',
                        data=datafit[['ExRet', 'DPL1', 'EPL1', 'VOLL1', 'BILLL1', 
                                    'BONDL1', 'TERML1','CREDITL1', 'PPIGL1', 
                                    'IPGL1', 'MA112L1', 'MA312L1', 'MOM6L1']].iloc[:(n_in+i), :])
        results = model.fit()
        k = results.params.values
        f = datafit[['DP', 'EP', 'VOL', 'BILL', 'BOND', 'TERM', 'CREDIT', 'PPIG',
                    'IPG', 'MA112', 'MA312', 'MOM6']].iloc[n_in+i-1, :].values
        f = np.concatenate((np.array([1]), f))
        rout[i] = np.sum(k*f)
        rmean[i] = np.mean(datafit['ExRet'].iloc[:n_in+i].values)
        rreal[i] = datafit['ExRet'].iloc[n_in+i]
        rfree[i] = datafit['Rfree'].iloc[n_in+i]
        volt2[i] = np.sum(datafit['Ret'].iloc[(n_in+i-12):(n_in+i)].values**2)

    print('Out-of-sample tests for multi-factor model with OLS:')
    print('Predictor: {:s}'.format(factor_out))
    R2os, MSFEadj, pvalue_MSFEadj = myfun_stat_gains(rout, rmean, rreal)
    Uout, Umean, DeltaU = myfun_econ_gains(rout, rmean, rreal, rfree, volt2, gmm=5) 
    
def muti_out_predict_other(factor_out,datafit,model):
    n_in = np.sum(datafit['yyyymm'] <= 195612)
    n_out = np.sum(datafit['yyyymm'] > 195612)
    rout = np.zeros(n_out)
    rmean = np.zeros(n_out)
    rreal = np.zeros(n_out)
    rfree = np.zeros(n_out)
    volt2 = np.zeros(n_out)
    if model=="Lasso":
        reg = Lasso()
    elif model=="Ridge":
        reg = Ridge(fit_intercept=True)
    elif model=="ElasticNet":
        reg = ElasticNet()
    elif model=="Linear":
        reg = ARDRegression()

    for i in range(n_out):
        X = datafit[['DPL1', 'EPL1', 'VOLL1', 'BILLL1', 
                    'BONDL1', 'TERML1','CREDITL1', 'PPIGL1', 
                    'IPGL1', 'MA112L1', 'MA312L1', 'MOM6L1']].iloc[:(n_in+i), :].values

        y = datafit['ExRet'].iloc[:(n_in+i)].values
        reg.fit(X, y)
        k = np.concatenate((np.array([reg.intercept_]), reg.coef_))
        f = datafit[['DP', 'EP', 'VOL', 'BILL', 'BOND', 'TERM', 'CREDIT', 'PPIG',
                    'IPG', 'MA112', 'MA312', 'MOM6']].iloc[n_in+i-1, :].values
        f = np.concatenate((np.array([1]), f))
        rout[i] = np.sum(k*f)
        rmean[i] = np.mean(datafit['ExRet'].iloc[:n_in+i].values)
        rreal[i] = datafit['ExRet'].iloc[n_in+i]
        rfree[i] = datafit['Rfree'].iloc[n_in+i]
        volt2[i] = np.sum(datafit['Ret'].iloc[(n_in+i-12):(n_in+i)].values**2)

    print('Out-of-sample tests for multi-factor model with ML method:')
    print('Predictor: {:s}'.format(factor_out))
    R2os, MSFEadj, pvalue_MSFEadj = myfun_stat_gains(rout, rmean, rreal)
    Uout, Umean, DeltaU = myfun_econ_gains(rout, rmean, rreal, rfree, volt2, gmm=5)
    
