from heapq import merge
import pandas as pd
import numpy as np
import matplotlib.pyplot as plt
import statsmodels.api as sm
from StockDeal import StockDeal
from Test import TestMethods

data = pd.read_excel('./data/月数据.xls',usecols=[0,2,3,4])
data.columns = ['code','date','r','rf']
data.dropna(inplace=True)
codes = np.unique(data['code'])
df_list = []
for code in codes:
    df = data.loc[data['code']==code]
    df_list.append(df)
for df in df_list:
    df['date'] = pd.to_datetime(df['date'])
    df.sort_values(by='date',inplace=True)
    df.dropna(inplace=True)

    
#获取月上证指数数据
ssec = pd.read_excel('./data/SSEC月.xls',usecols=[2,3,4])
ssec = ssec.loc[ssec['月末交易日标识_Monflg']==1]
ssec.columns = ['date','close','flag']

ssec['return'] = np.log(ssec['close'])-np.log(ssec['close'].shift(1))
ssec.dropna(inplace=True)
ssec['date']=pd.to_datetime(ssec['date'])

#内连接
codes = ['948','2094','2194','600094','600594','600694','600794','600894']
merge_result = ssec[['date','return']]
cols = ['date','return']
for i,df in enumerate(df_list):
    merge_result = pd.merge(left=merge_result[cols],right=df[['date','r']],on='date')
    cols.append(codes[i])
    merge_result.columns = cols
merge_result.columns = ['date','indR','948R','2094R','2194R','600094R','600594R','600694R','600794R','600894R']
merge_result = pd.merge(left=merge_result,right=df_list[0][['date','rf']],on='date')
columns = merge_result.columns
for col in columns[1:-1]:
    merge_result[col] = merge_result[col]-merge_result['rf']
print(merge_result)
test = TestMethods()
test.waldTest(merge_result)
