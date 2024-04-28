import numpy as np
import pandas as pd
import pickle
import datetime
from dateutil.relativedelta import relativedelta
with open("date.pkl","rb") as f:
    date = pickle.load(f)
#日度数据
daily_data1 = pd.read_excel("2001-2010.xls")
daily_data2 = pd.read_excel("2011-2015.xls")
daily_data3 = pd.read_excel("2016-2020.xls")
daily_data4 = pd.read_excel("2021-2023.xls")
daily_data = pd.concat((daily_data1,daily_data2,daily_data3,daily_data4),axis=0)
daily_data.columns = ['code','name','date','close']
daily_data.dropna(inplace=True)
daily_data['date'] = pd.to_datetime(daily_data['date'])
daily_data.sort_values(by="date",inplace=True)
daily_data['return'] = np.log(daily_data['close']) - np.log(daily_data['close'].shift(1))
daily_data.dropna(inplace=True)
date0  =pd.to_datetime("20010131")
#计算月波动率
df = pd.DataFrame()
month_list = []
bodong_list = []
piandu_list = []
for i in range(len(date)-1):
    date_s = date[i]
    date_b = date[i+1]
    data_month = daily_data.loc[(daily_data['date']>pd.to_datetime(date_s)) & (daily_data['date']<=pd.to_datetime(date_b))]
    #计算波动率
    r = data_month['return']
    r2 = r**2
    bodong_i = np.sum(r2)
    if bodong_i == 0.0:
        continue
    #计算月已实现偏度
    r3 = r**3
    r3 = np.sqrt(len(r3)) * np.sum(r3)
    RDSkew = r3/(np.sqrt(bodong_i ** 3))
    month_list.append(date_b)
    bodong_list.append(bodong_i)
    piandu_list.append(RDSkew)
df['date'] = month_list
df['bodong'] = bodong_list
df['piandu'] = piandu_list
df.dropna(inplace=True)
# 计算月股价高点
gaodian = []
date_list2 = []
dfg = pd.DataFrame()
for i in range(0,len(date)-4):
    date_s = date[i]
    date_m = date[i+3]
    date_b = date[i+4]
    daily_data_1_3 = daily_data[(daily_data['date']>pd.to_datetime(date_s)) & (daily_data['date']<=pd.to_datetime(date_m))] 
    daily_data_4 = daily_data[(daily_data['date']>pd.to_datetime(date_m)) & (daily_data['date']<=pd.to_datetime(date_b))]
    #计算前三个月股价最高点
    max_1_3 = np.max(daily_data_1_3['close'])
    #当月股价最高点
    max_4 = np.max(daily_data_4['close'])
    #计算比值
    result = max_4/max_1_3
    gaodian.append(result)
    date_list2.append(date_b)
dfg['date'] = date_list2
dfg['gaodian'] = gaodian
dfg.dropna(inplace=True)
final_df = pd.merge(left=df,right=dfg,on='date')
print(final_df)
with open("data_daily.pkl","wb") as f:
    pickle.dump(final_df,f)
