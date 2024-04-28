import numpy as np
import pandas as pd
import pickle
import statsmodels.api as sm


with open('final_list.pkl','rb') as f:
    stock_list = pickle.load(f)
ym_nums = np.unique(stock_list[0]['ym'])
print(stock_list[0])


params = pd.DataFrame({"const":[],"size":[],"ep":[],"beta":[]})
for ym in ym_nums:
    #组合截面数据
    sym = stock_list[0].loc[stock_list[0]['ym']==ym]
    for i in range(1,len(stock_list)):
        s = stock_list[i].loc[stock_list[i]['ym']==ym]
       
        if s.empty:
            continue 
        sym = pd.concat((sym,s),axis=0)
    #回归
    y = sym['R']
    
    x = sym[['size','ep','beta']]
    # x = sym[['size','ep']]
    # x = sym[['size','beta']]
    # x = sym[['ep','beta']]
    # x = sym[['size']]
    # x = sym[['ep']]
    # x = sym[['beta']]
    
    x = sm.add_constant(x)
    model = sm.OLS(y,x)
    fit_model = model.fit()
    param = fit_model.params
    p = pd.DataFrame({"const":[param['const']],
                      "size":[param['size']],
                      "ep":[param['ep']],
                      "beta":[param['beta']]})
    # p = pd.DataFrame({"const":[param['const']],
    #                   "ep":[param['ep']],
    #                   "beta":[param['beta']]})
    # p = pd.DataFrame({"const":[param['const']],
    #                   "size":[param['size']],
    #                   "beta":[param['beta']]})
    # p = pd.DataFrame({"const":[param['const']],
    #                   "size":[param['size']],
    #                   "ep":[param['ep']],
    #                   })
    # p = pd.DataFrame({"const":[param['const']],
    #                   "size":[param['size']],
    #                   })
    # p = pd.DataFrame({"const":[param['const']],
    #                   "ep":[param['ep']],
    #                   })
    # p = pd.DataFrame({"const":[param['const']],
    #                   "beta":[param['beta']]
    #                   })

    params = pd.concat((params,p),axis=0)
    

params.to_hdf("params.h5",key='df',mode='w')
