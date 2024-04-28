import pandas as pd
import numpy as np
from util import sort_portfolio

root_name = '数据/RESSET_MRESSTK'
data = pd.read_excel(root_name+'_1'+".xls")
for i in range(2,11):
    file_name = root_name+"_"+str(i)+".xls"
    data_1 = pd.read_excel(file_name)
    data = pd.concat((data,data_1),axis=0)
data = data.loc[:,:'市盈率_PE']

data.columns = ['stk','date','close','fshare','tshare','monret','monrf','pe']
data['date'] = pd.to_datetime(data['date'])
data['yearmonth'] = data['date'].dt.strftime("%Y%m")
data['stksize'] = data['close']*data['fshare']#流通市值
data['stkep'] = 1/data['pe']#市盈率倒数
data['monexcret'] = data['monret']-data['monrf'] #超额收益
data.dropna(inplace=True,subset=['stksize','stkep'])
data = data[['stk','monexcret','stksize','stkep','yearmonth']]

#计算有多少个月份，每年六月调仓
uym = np.unique(data['yearmonth'])


meanret = []
lcname = []
for i in range(5, 234, 12):
    if len(uym[i:i+13]) == 13:
        lcname.append(str(uym[i]))
        sp = sort_portfolio(data, uym[i:i+13], 5)
        spmreturn = sp.sequence_sort_mreturn()
        if len(meanret) == 0:
            meanret = spmreturn['mret']
        else:
            meanret = pd.concat([meanret, spmreturn['mret']], axis=1)
meanret.columns = lcname
print(meanret)
meanret['meanreturn'] = meanret.apply(lambda x: x.mean(), axis=1)
a = meanret['meanreturn'].values.reshape((5,5))
print('{:>10s} {:>10s}, {:>10s}, {:>10s}, {:>10s}, {:>10s}'.format('', 'EP1', 'EP2', 'EP3', 'EP4', 'EP5'))
for i in range(5):
    print('{:>10s} {:10.5f}, {:10.5f}, {:10.5f}, {:10.5f}, {:10.5f}'.format('SIZE'+str(i+1), 
                                                                            a[i, 0], 
                                                                            a[i, 1], 
                                                                            a[i, 2], 
                                                                            a[i, 3], 
                                                                            a[i, 4]))
    


    