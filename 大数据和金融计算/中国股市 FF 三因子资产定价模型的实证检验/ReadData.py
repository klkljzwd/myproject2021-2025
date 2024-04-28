import numpy as np
import pandas as pd
import pickle
import statsmodels.api as sm
root_name = "数据\RESSET_MRESSTK_"
#第一个数据
stock = pd.read_excel(root_name+"1.xls")
for i in range(2,11):
    file_name = root_name+str(i)+".xls"
    stock = pd.concat((stock,pd.read_excel(file_name)),axis=0)
stock.columns = ['code','date','price','amount','lamount','return','rf','pe']
#处理日期
stock['date'] = pd.to_datetime(stock['date'])
stock['ym'] = stock['date'].dt.strftime("%Y%m")
stock = stock[['code','ym','price','lamount','return','rf','pe']]
stock.dropna(inplace=True)
#市值
stock['size'] = np.log(stock['price'] * stock['lamount'])

#盈利价格比
stock['ep'] = 1 / stock['pe']
#超额收益
stock['R'] = stock['return'] - stock['rf']

stock = stock[['code','ym','size','ep','R']]
stock = stock.sort_values(by='code')
#拆分个股
stock_list = []
codes = np.unique(stock['code'])
codes = list(codes)
for code in codes:
    stk = stock.loc[stock['code']==code]
    stk = stk.sort_values(by='ym')
    stock_list.append(stk)
#计算平均市值
size_list = {}
for i in range(len(stock_list)):
    code = np.unique(stock_list[i]['code']).item()
    size_list[code] = np.mean(stock_list[i]['size'])
#排序
size_list = sorted(size_list.items(),key=lambda x:x[1],reverse=True)
#去除壳公司
codes = []
for i in range(int(len(size_list)*0.7)):
    codes.append(size_list[i][0])
used_stocks = []
for stock in stock_list:
    if np.unique(stock['code']).item() in codes:
        used_stocks.append(stock)
print(len(used_stocks))

f = open("stock_list.pkl",'wb')
pickle.dump(used_stocks,f)
f.close()

root_name = "数据\个股beta"
#第一个数据
b = pd.read_excel(root_name+"1.xls")
for i in range(2,11):
    file_name = root_name+str(i)+".xls"
    b = pd.concat((b,pd.read_excel(file_name)),axis=0)
b.columns = ['code','date','beta']
#处理日期
b['date'] = pd.to_datetime(b['date'])
b['ym'] = b['date'].dt.strftime("%Y%m")
b['ym'] = pd.to_datetime(b['ym'],format="%Y%m")
b= b[['code','ym','beta']]
b.dropna(inplace=True)
b = b.sort_values(by='code')
b_list = []
codes = np.unique(b['code'])
codes = list(codes)
for code in codes:
    stk = b.loc[b['code']==code]
    stk.sort_values(by='ym')
    stk = stk.loc[(stk['ym']>=pd.to_datetime('200101',format=("%Y%m"))) & (stk['ym']<=pd.to_datetime('202201',format=("%Y%m")))]
    stk['ym'] = stk['ym'].dt.strftime("%Y%m")
    b_list.append(stk)

f = open("b_list.pkl",'wb')
pickle.dump(b_list,f)
f.close()
with open('stock_list.pkl','rb') as f:
    stock_list = pickle.load(f)
with open("b_list.pkl",'rb') as f:
    b_list = pickle.load(f)
    
final_stocks = []

#组合股票
for i in range(len(stock_list)):
    cods = np.unique(stock_list[i]['code'])
    s = stock_list[i].loc[stock_list[i]['code']==cods[0]]
    b = b_list[i].loc[b_list[i]['code']==cods[0]]

    stock = pd.merge(left=stock_list[i],right=b_list[i][['ym','beta']],on='ym',how='inner')
    
    r_np = np.array(stock['R'])
    r_year = []
    for i in range(len(r_np)-12):
        r_year.append(np.mean(r_np[i:i+12]))
    stock['R'] = pd.Series(r_year)
    
    # stock['R'] = stock['R'].shift(1)
    stock.dropna(inplace=True)
    final_stocks.append(stock)
    print(stock)

    
    



f = open("final_list.pkl",'wb')
pickle.dump(final_stocks,f)
f.close()