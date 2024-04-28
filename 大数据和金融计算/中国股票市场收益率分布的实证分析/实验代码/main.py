import pandas as pd
import numpy as np
import matplotlib.pyplot as plt
import statsmodels.api as sm
from StockDeal import StockDeal
from Test import TestMethods

stockDeal = StockDeal()
#获取股票df的列表
df_list = stockDeal.getData()
#print(df_list[0])
#获取每个股票拼接后的df
result = stockDeal.sliceStock(df_list)

#数据清洗
for df in result:
    df.dropna(inplace=True)
    df['date']=pd.to_datetime(df['date'])
    df.sort_values(by='date',inplace=True)

result = stockDeal.getReturn(result)

#读取指数数据
index = pd.read_excel('./data/SSEC.xls')
index = index[['交易日期_TrdDt','收盘价(元/点)_ClPr']]
index.columns = ['date','ind_close']
index.dropna(inplace=True)
#计算指数收益率
index['return'] = np.log(index['ind_close'])-np.log(index['ind_close'].shift(1))
index.dropna(inplace=True)
index = index.loc[(index['return']>=-0.1) & (index['return']<=0.1)]
index['date'] = pd.to_datetime(index['date'])

#内连接矩阵
merage_result = index
# merage_result = pd.merge(left=merage_result[['date','return']],right=result[0][['date','return']],on='date')
# merage_result.columns = ['date','ind_return','948_return']
# merage_result = pd.merge(left=merage_result[['date','ind_return','948_return']],right=result[0][['date','return']],on='date')
# merage_result.columns = ['date','ind_return','948_return','2094_return']
# merage_result = pd.merge(left=merage_result[['date','ind_return','948_return','2094_return']],right=result[0][['date','return']],on='date')
# merage_result.columns = ['date','ind_return','948_return','2094_return','2194_return']

cols = ['date','return']
codes = ['948','2094','2194','600094','600594','600694','600794','600894']
for i,df in enumerate(result):
    merage_result = pd.merge(left=merage_result[cols],right=df[['date','return']],on='date')
    cols.append(codes[i])
    merage_result.columns = cols
    print(merage_result)
merage_result.columns = ['date','indR','948R','2094R','2194R','600094R','600594R','600694R','600794R','600894R']
merage_result = pd.merge(left=merage_result,right=result[0][['date','stk_rf']],on='date')
print(merage_result)
#r - rf
columns = merage_result.columns
for col in columns[1:-1]:
    merage_result[col] = merage_result[col]-merage_result['stk_rf']

#线性回归

for col in columns[1:-1]:
    x = sm.add_constant(merage_result['indR'])
    model = sm.OLS(merage_result[col],x)
    result = model.fit()
    print(result.summary())

# test = TestMethods()
# test.waldTest(merage_result)
