import numpy as np
import pandas as pd
import torch
import torch.nn as nn
from sklearn.preprocessing import MinMaxScaler
from sklearn.metrics import mean_absolute_error as mae
from sklearn.metrics import mean_absolute_percentage_error as mape
from sklearn.metrics import mean_squared_error as mse
import math
from Xgb import XgbForecast
from Model import DeepLearnModel
from Dataload import TimeSeriesDataLoader
from Draw import PltDraw
from Eval import Eval
from Conf import Conf
conf = Conf()
def cal_error(x,y,str):
    print(str,"  ","MAE: ",mae(x,y))
    print(str,"  ","RMSE: ",math.sqrt(mse(x,y)))
    print(str,"  ","MAPE: ",mape(x,y)*100,"%")
    
dataload = TimeSeriesDataLoader(conf.csv,conf.window_len,conf.target,conf.radio,conf.batch_size)
data = dataload.read_csv()
label = data
#lstm训练数据
train_data_tensor,train_label_tensor,eval_data_tensor,eval_label_tensor,test_data_tensor,test_label_tensor,train_loader,mm_x,mm_y = dataload.lstm_data(data,label)
#xgboost训练数据
train_data,train_label,test_data,test_label,eval_data,eval_label = dataload.xgb_data(data,label)
#训练设备
device=torch.device("cuda:0" if torch.cuda.is_available() else "cpu")

#训练模型
net = DeepLearnModel(input_size=1,output_size=1,hidden_size=4,window_len=conf.window_len,num_layer=3,is_bid=conf.isBid)
net = net.to(device)
net=torch.load(conf.model)
net.eval()

model_eval = Eval()
weight,b = model_eval.get_w()

draw = PltDraw()
xgb = XgbForecast()
xgb_predict = xgb.test_forecast(train_data,train_label,test_data)
xgb_predict = np.expand_dims(xgb_predict,axis=1)

cal_error(xgb_predict,test_label,"xgboost")

gru_predict = net(test_data_tensor.to(device))
gru_predict = gru_predict.to(torch.device("cpu"))
gru_predict = mm_y.inverse_transform(gru_predict.detach().numpy())

cal_error(gru_predict,test_label,"CNN-GRU")

predict = np.concatenate((xgb_predict,gru_predict),axis=1)

final_predict = np.matmul(predict,weight)+b

draw.draw(final_predict,test_label)

cal_error(final_predict,test_label,"linear_regression_weight")

