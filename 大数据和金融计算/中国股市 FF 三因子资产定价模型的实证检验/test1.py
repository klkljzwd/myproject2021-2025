import numpy as np
import pandas as pd
import statsmodels.api as sm
from scipy.stats import f
from GRStest import grstest

data_factors = pd.read_excel("数据/三因子.xls",usecols=[2,6,7,8])
data_factors.columns = ['date','mkt','smb','hml']
data_factors['date'] = pd.to_datetime(data_factors['date'])



data_index = pd.read_excel("数据\指数周收益率.xls",usecols=[1,2,4])
data_index.columns = ['idxname','date','return']
data_index['date'] = pd.to_datetime(data_index['date'])



idxname = np.unique(data_index['idxname'].values)
datas = []

for i in range(len(idxname)):
    data = data_index.loc[data_index['idxname']==idxname[i]]
    datas.append(data)


data_rf = pd.read_excel("数据\指数周收益率.xls",usecols=[2,5])
data_rf.dropna(inplace=True)
data_rf.columns = ['date','return']
data_rf['date'] = pd.to_datetime(data_rf['date'])


data_matrix = data_rf[['return','date']]

cols = ['return','date']

for i in range(len(datas)):
    data_matrix = pd.merge(left=data_matrix,right=datas[i][['date','return']],on = 'date',how='inner')
    
    cols.append(idxname[i])
    data_matrix.columns = cols
    

data_matrix.dropna(inplace=True)
data_matrix = pd.merge(left=data_matrix,right=data_factors,on='date')

x = data_matrix.loc[:,'mkt':]
x = sm.add_constant(x)
y = data_matrix['上证信息'] - data_matrix['return']
model = sm.OLS(y,x)
fitresult = model.fit()

ym = data_matrix[['上证信息',    '上证公用',    '上证医药',    '上证可选',    '上证工业',      '上证材料',    '上证消费']]
xm = x
grstest(xm,ym)

