import numpy as np
import torch

import matplotlib.pyplot as plt
from sklearn.preprocessing import MinMaxScaler
from sklearn.metrics import mean_absolute_error as mae
from sklearn.metrics import mean_absolute_percentage_error as mape
from sklearn.metrics import mean_squared_error as mse
from sklearn.linear_model import LinearRegression
from Dataload import TimeSeriesDataLoader
from Xgb import XgbForecast
from Model import DeepLearnModel

from Conf import Conf
conf = Conf()
#读取数据

class Eval:
    def __init__(self) -> None:
        self.dataload = TimeSeriesDataLoader(conf.csv,conf.window_len,conf.target,conf.radio,conf.batch_size)
        self.data = self.dataload.read_csv()
        self.label = self.data
        self.window_len=conf.window_len
    def xgb_eval(self):
        train_data,train_label,test_data,test_label,eval_data,eval_label = self.dataload.xgb_data(self.data,self.label)
        xgb = XgbForecast()
        eval_pre = xgb.eval_forecast(train_data,train_label,eval_data)
        eval_pre = np.expand_dims(eval_pre,axis=1)
        return eval_pre,eval_label
    def lstm_eval(self):
        train_data_tensor,train_label_tensor,eval_data_tensor,eval_label_tensor,test_data_tensor,test_label_tensor,train_loader,mm_x,mm_y = self.dataload.lstm_data(self.data,self.label)
        device=torch.device("cuda:0" if torch.cuda.is_available() else "cpu")
        net = DeepLearnModel(input_size=1,output_size=1,hidden_size=4,window_len=self.window_len,num_layer=3,is_bid=True)
        net = net.to(device)
        net=torch.load(conf.model)
        net.eval()

        eval_predict = net(eval_data_tensor.to(device))
        eval_predict = eval_predict.to(torch.device("cpu"))
        eval_predict = mm_y.inverse_transform(eval_predict.detach().numpy())
        eval_label =eval_label_tensor.detach().numpy()
        eval_label = mm_y.inverse_transform(eval_label)

        return eval_predict
    def get_w(self):
        x1,Y = self.xgb_eval()
        x2 = self.lstm_eval()
        X = np.concatenate((x1,x2),axis=1)
        linear = LinearRegression()
        linear.fit(X,Y)
        return linear.coef_[0],linear.intercept_




