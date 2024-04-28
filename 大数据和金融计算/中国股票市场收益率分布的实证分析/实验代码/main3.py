import pandas as pd
import numpy as np

df_list = []
file_list = ['data\data3\RESSET_MRESSTK_20240314144150\RESSET_MRESSTK_1.xls',
             'data\data3\RESSET_MRESSTK_20240314144150\RESSET_MRESSTK_2.xls',
             'data\data3\RESSET_MRESSTK_20240314144402\RESSET_MRESSTK_1.xls',
             'data\data3\RESSET_MRESSTK_20240314144402\RESSET_MRESSTK_2.xls',
             'data\data3\RESSET_MRESSTK_20240314144532\RESSET_MRESSTK_1.xls',
             'data\data3\RESSET_MRESSTK_20240314144532\RESSET_MRESSTK_2.xls',
             'data\data3\RESSET_MRESSTK_20240314144555\RESSET_MRESSTK_1.xls']
for path in file_list:
    df1 = pd.read_excel(path,usecols=[0,2,3])
    df1.columns = ['code','date','close']
    df1['date']=pd.to_datetime(df1['date'])
    df1.dropna(inplace=True)
    df_list.append(df1)
    
code_list = []
stock_list = []
for df in df_list:
    codes = np.unique(df['code'])
    for code in codes:
        d = df.loc[df['code']==code]
        stock_list.append(d)

del_index = []
for i in range(len(stock_list)):
    stock_list[i] = stock_list[i].loc[(stock_list[i]['date']>='2002-01-01') &(stock_list[i]['date']<='2022-01-01')]
    stock_list[i].dropna(inplace=True)
    if len(stock_list[i]) != len(stock_list[0]):
        del_index.append(i)
for i in del_index:
    stock_list.pop(i)
#计算过去一个月的收益
# for stock in stock_list:
#     stock['return'] = np.log(stock['close'])-np.log(stock['close'].shift(1))
#     stock.dropna(inplace = True)

#重设索引
index = np.arange(0,len(stock_list[0]))
for stock in stock_list:
    stock['index'] = index
    stock.set_index('index',inplace=True)

#参数
last_m = 1   #表示过去月份
next_m = 1   #表示未来月份
    
totalQ = {0:0,1:0,2:0,3:0,4:0}
countQ = {0:0,1:0,2:0,3:0,4:0}
for month in range(last_m,len(stock_list[0])-next_m):#月份遍历
    qlist = [] #每一个月的收益率
    for stock in stock_list:
        #计算上一个月的收益
        r_last = np.log(stock.loc[month,'close'])-np.log(stock.loc[month-last_m,'close'])
        
        qlist.append(r_last)
    qlist = np.array(qlist)
   
    qlist = np.sort(qlist)
    #得到5个分位区间
    q = np.linspace(0,len(stock_list)-1,6).astype('int')
    #print(q)
    Q = {0:0,1:0,2:0,3:0,4:0}
    count = {0:0,1:0,2:0,3:0,4:0}
    
    for stock in stock_list:
        #计算上一个月的收益
        r_last = np.log(stock.loc[month,'close'])-np.log(stock.loc[month-last_m,'close'])
        for i in range(len(q)-1):
            if r_last>=qlist[q[i]] and r_last<=qlist[q[i+1]]:
                #计算下一个月的收益
                r_next = np.log(stock.loc[month+next_m,'close'])-np.log(stock.loc[month,'close'])
                #算入对应组别投资组合的累计收益率
                Q[i] = Q[i] + r_next
                count[i] = count[i]+1
   
    for i in range(5):
        
        totalQ[i] = totalQ[i]+Q[i]
        countQ[i] = countQ[i]+1


for i in range(5):
    totalQ[i] = totalQ[i]/countQ[i]
print(totalQ)
        
        

