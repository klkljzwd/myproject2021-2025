import numpy as np
import pandas as pd
from math import pi

import statsmodels.api as sm
import statsmodels.formula.api as smf
from sklearn.linear_model import *
import matplotlib.pyplot as plt
from sklearn.svm import SVR
from sklearn.preprocessing import normalize

from Util import *
data = pd.read_excel("1EData_PredictorData2019.xlsx",sheet_name="Monthly")

data['DP'] = data['D12'].apply(np.log)-data['Index'].apply(np.log)
data['EP'] = data['E12'].apply(np.log)-data['Index'].apply(np.log)
data['VOL'] = data['CRSP_SPvw'].abs().rolling(window=12).mean()*np.sqrt(pi*6)
data['BILL'] = data['tbl'] - data['tbl'].rolling(window=12).mean()
data['BOND'] = data['lty'] - data['lty'].rolling(window=12).mean()
data['TERM'] = data['lty'] - data['tbl']
data['CREDIT'] = data['AAA'] - data['lty']
data['MA112'] = data['Index'] >= data['Index'].shift(periods=12)
data['MA312'] = data['Index'].rolling(window=3).mean() >= data['Index'].rolling(window=12).mean()
data['MOM6'] =  data['Index'] >= data['Index'].shift(periods=6)
data['ExRet'] = data['CRSP_SPvw'] - data['Rfree']
data[['MA112', 'MA312', 'MOM6']] = data[['MA112', 'MA312', 'MOM6']].astype(int)

data =  pd.concat((data[['yyyymm','CRSP_SPvw','Rfree','ExRet','DP','EP','VOL','BILL','BOND','TERM','CREDIT','PPIG','IPG','MA112','MA312','MOM6']]
                   ,data[['DP','EP','VOL','BILL','BOND','TERM','CREDIT','PPIG','IPG','MA112','MA312','MOM6']].shift(1)),axis=1)
data.columns = ['yyyymm', 'Ret', 'Rfree', 'ExRet',
                'DP', 'EP', 'VOL', 'BILL', 'BOND', 'TERM','CREDIT', 
                'PPIG', 'IPG', 'MA112', 'MA312', 'MOM6',
                'DPL1', 'EPL1', 'VOLL1', 'BILLL1', 'BONDL1', 'TERML1','CREDITL1', 
                'PPIGL1', 'IPGL1', 'MA112L1', 'MA312L1', 'MOM6L1']
data = data[data['yyyymm']>=192701]
data.reset_index(drop=True,inplace=True)
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
    omg_out = rout/volt2/gmm
    rp_out = rfree+omg_out*rreal
    Uout = np.mean(rp_out)-0.5*gmm*np.var(rp_out)
    omg_mean = rmean/volt2/gmm
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



factor_out = 'DP, EP, VOL, BILL, BOND, TERM, CREDIT, PPIG, IPG, MA112, MA312, MOM6'
datafit = data.copy(deep=True)
# muti_out_predict(factor_out,datafit)
factor_out = 'DP, EP, VOL, BILL, BOND, TERM, CREDIT, PPIG, IPG, MA112, MA312, MOM6'
datafit = data.copy(deep=True)
factor_out = 'DP, EP, VOL, BILL, BOND, TERM, CREDIT, PPIG, IPG, MA112, MA312, MOM6'
datafit = data.copy(deep=True)

n_in = np.sum(datafit['yyyymm'] <= 195612)
n_out = np.sum(datafit['yyyymm'] > 195612)
rout = np.zeros(n_out)
rmean = np.zeros(n_out)
rreal = np.zeros(n_out)
rfree = np.zeros(n_out)
volt2 = np.zeros(n_out)


reg = ElasticNetCV(random_state=0, cv=10, fit_intercept=True, 
                        normalize=True, precompute='auto', copy_X=True, 
                        n_jobs=-1, max_iter=10**9, tol=10**-6)
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


