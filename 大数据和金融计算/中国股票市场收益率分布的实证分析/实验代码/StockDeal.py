import re
import pandas as pd
import numpy as np
import matplotlib.pyplot as plt
import statsmodels.api as sm

class StockDeal:
    def __init__(self):
        pass
    def getData(self):
        
        df_list = []
        df1 = pd.read_excel("./data/2001_2010.xls",usecols=[0,2,3,4])
        df2 = pd.read_excel("./data/2011_2015.xls",usecols=[0,2,3,4])
        df3 = pd.read_excel("./data/2016_2020.xls",usecols=[0,2,3,4])
        df4 = pd.read_excel("./data/2021_2022.xls",usecols=[0,2,3,4])
        df_list.append(df1)
        df_list.append(df2)
        df_list.append(df3)
        df_list.append(df4)
        columns = ['stk_code','date','stk_close','stk_rf']
        for df in df_list:
            df.columns = columns
            df.sort_values(by='stk_code')
        return df_list
    
    def sliceStock(self,df_list):
        codes = np.unique(df_list[0]['stk_code'].values)
        stock_dict = {}
        for code in codes:
            stock_dict[code] = []
        
        for df in df_list:
            #np.unique(df)的主要作用是找出一个一维数组（或序列）中所有不重复的唯一元素
            for code in codes:
                stock_part = df.loc[df['stk_code']==code]
                stock_dict[code].append(stock_part)
        result = []
        for code in codes:
            stock_list = stock_dict[code]
            stock1 = pd.DataFrame(stock_list[0])
            for i in range(1,len(stock_list)):
                stock1 = pd.concat((stock1,pd.DataFrame(stock_list[i])),axis=0)
            result.append(stock1)
        return result
    #计算收益率
    def getReturn(self,df_list):
        result = []
        for df in df_list:
            df['return'] = np.log(df['stk_close'])-np.log(df['stk_close'].shift(periods=1))
            df.dropna(inplace=True)
            df = df.loc[(df['return']>=-0.1) & (df['return']<=0.1)]
            result.append(df)
        return result