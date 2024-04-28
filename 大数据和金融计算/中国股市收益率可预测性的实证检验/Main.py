import pandas as pd
import numpy as np
import pickle
from sklearn.linear_model import *
from sklearn.svm import SVR
from sklearn.tree import DecisionTreeRegressor
from sklearn.neighbors import KNeighborsRegressor
from Util import *
# with open("date.pkl","wb") as f:
#     pickle.dump(data_from_month['date'],f)
with open("data_daily.pkl","rb") as f:
    data_from_daily = pickle.load(f)
data_from_month = pd.read_excel("月数据.xls")
data_from_month.columns = ['code','name','date','close','trdsum','turn','return','rf','pe','eps','roe','income']
data_from_month['return'] = data_from_month['return'] - data_from_month['rf']
#beta数据
beta = pd.read_excel("月beta.xls")
beta.columns = ['code','name','date','beta']
final_data = pd.merge(left=data_from_month,right=data_from_daily,on='date',how='inner')
final_data = pd.merge(left=final_data,right=beta[['date','beta']],on='date',how='inner')
final_data.dropna(inplace=True)
final_data['trdsum'] = np.abs(final_data['return']/np.log(final_data['trdsum']))

final_data['returnl1'] = final_data['return'].shift(-1)
final_data.dropna(inplace=True)
model_name = ["Linear","Ridge","ESnet","SVR","DSTree","KNN","Lasso","MLP","GBRT"]
model_name = "DSTree"
if model_name=="Linear":
    model = LinearRegression()
elif model_name=="Ridge":
    model = RidgeCV(cv=10)
elif model_name=="ESnet":
    model = ElasticNet(
        alpha=1,              # 正则化强度，或 `l1_ratio` 控制L1/L2比例
        l1_ratio=0.8,           # L1正则化相对于L2正则化的权重
        fit_intercept=True,     # 是否包含截距项
        normalize=False,        # 是否对数据进行标准化
        max_iter=10000,         # 最大迭代次数
        tol=1e-5,               # 收敛阈值
        random_state=42,    
    )
elif model_name=="SVR":
    model = SVR()
elif model_name=="DSTree":
    model = DecisionTreeRegressor()
elif model_name=="KNN":
    model = KNeighborsRegressor()
elif model_name=="Lasso":
    model = LassoCV(cv=5)
train_data = final_data.loc[0:int(len(final_data)*0.5),'trdsum':'returnl1']
#多因子检验
predict_list = []
real_list = []
mean_list = []
volt_list = []
rf_list = []
factor_list = ['trdsum','turn','return','pe','eps','roe','income','bodong','piandu','gaodian','beta']
first = 0
end = 11
print(factor_list[first:end])
for i in range(int(len(final_data)*0.5)+1,int(len(final_data))-2):
    train_data = np.array(final_data.loc[0:i,factor_list[first:end]])
    train_label = np.array(final_data.loc[0:i,'returnl1'])
    train_label = np.expand_dims(train_label,axis=1)
    fit_model = model.fit(train_data,train_label)
    test_data = np.array(final_data.loc[i+1,factor_list[first:end]])
    test_data = test_data.reshape(1,len(test_data))
    #预值
    predict = model.predict(test_data).item()
    predict_list.append(predict)
    #真实值
    real = final_data.loc[i+1,'returnl1']
    real_list.append(real)
    mean = np.mean(final_data.loc[0:i+1,'return'])
    mean_list.append(mean)
    #volt
    if i<12:
        volt = np.sum(final_data.loc[0:i,'return'].values**2)
        volt_list.append(volt)
    else:
        volt = np.sum(final_data.loc[i-12:i,'return'].values**2)
        volt_list.append(volt)
    #无风险利率
    rf = final_data.loc[i+2,'rf']
    rf_list.append(rf)
predict_np = np.array(predict_list)
real_np = np.array(real_list)
mean_np = np.array(mean_list)
volt_np = np.array(volt_list)
rf_np = np.array(rf_list)
print(factor_list[first:end])
myfun_stat_gains(predict_np,mean_np,real_np)
myfun_econ_gains(predict,mean_np,real_np,rf_np,volt_np)
