import numpy as np
import pandas as pd
from sklearn.preprocessing import MinMaxScaler
from sklearn.metrics import mean_absolute_error as mae
from sklearn.metrics import mean_absolute_percentage_error as mape
from sklearn.metrics import mean_squared_error as mse
from xgboost import XGBRegressor
from lightgbm import LGBMRegressor
import matplotlib.pyplot as plt
import math
from Dataload import TimeSeriesDataLoader
from Draw import PltDraw
class XgbForecast:
    def forecast(self,train_data,train_label):
        xgb = XGBRegressor(booster="gbtree",max_deepth=60,learning_rate=0.01,reg_alpha=0.01,n_estimators=500,gamma=0.1,min_child_weight=1)
        #xgb = LGBMRegressor(learning_rate=0.1,n_estimators=100)
        xgb.fit(train_data,train_label)
        return xgb
    def eval_forecast(self,train_data,train_label,eval_data):
        xgb = self.forecast(train_data,train_label)
        pre = xgb.predict(eval_data)
        return pre
    def test_forecast(self,train_data,train_label,test_data):
        xgb = self.forecast(train_data,train_label)
        pre = xgb.predict(test_data)
        return pre
