import numpy as np
import pandas as pd
import torch
from sklearn.preprocessing import MinMaxScaler
from torch.utils.data.dataset import Dataset
from torch.utils.data import DataLoader 
import torch.utils.data as Data
from Conf import Conf
conf = Conf()
class TimeSeriesDataLoader:
    def __init__(self,csv_name,window_len,target_name,split_radio,batch_size) -> None:
        self.csv_name = csv_name
        self.window_len = window_len
        self.target_name = target_name
        self.split_radio = split_radio
        self.batch_size = batch_size
    def read_csv(self):
        csv = pd.read_csv(self.csv_name,encoding=conf.encoding)
        data = csv[self.target_name]
        
        data = data.dropna()
        if(conf.is_reversed):
            data=data[::-1]
        data = np.array(data)
        data = np.expand_dims(data,axis=1)
        return data
    
    def split_data(self,data,label):
        x=[]
        y=[]
        for i in range(len(data)-self.window_len-1):
            _x=data[i:(i+self.window_len),:]
            _y=label[i+self.window_len,-1]
            x.append(_x)
            y.append(_y)
        x=np.array(x)
        y=np.array(y)
        y=np.expand_dims(y,axis=1)
        train_len = int(self.split_radio[0] * len(x))
        eval_len  = int(self.split_radio[1] * len(x))+train_len
        
        train_data=x[:train_len,:,:]
        train_label=y[:train_len,:]
    
        eval_data=x[train_len:eval_len,:,:]
        eval_label=y[train_len:eval_len,:]
        test_data=x[eval_len:,:,:]
        test_label=y[eval_len:,:]
        return train_data,train_label,eval_data,eval_label,test_data,test_label
    
    def lstm_data(self,data,label):
        mm_x = MinMaxScaler()
        mm_y = MinMaxScaler()
        data = mm_x.fit_transform(data)
        label = mm_y.fit_transform(label)
        train_data,train_label,eval_data,eval_label,test_data,test_label = self.split_data(data,label)
        #转为tensor形式 创建pytorch DataLoader
        train_data_tensor = torch.tensor(train_data,dtype=torch.float32)
        train_label_tensor = torch.tensor(train_label,dtype=torch.float32)
        test_data_tensor = torch.tensor(test_data,dtype=torch.float32)
        test_label_tensor = torch.tensor(test_label,dtype=torch.float32)
        eval_data_tensor = torch.tensor(eval_data,dtype=torch.float32)
        eval_label_tensor = torch.tensor(eval_label,dtype=torch.float32)

        train_loader = Data.TensorDataset(train_data_tensor,train_label_tensor)
        train_loader = DataLoader(dataset=train_loader,batch_size=self.batch_size,shuffle=False,drop_last=True)
        return train_data_tensor,train_label_tensor,eval_data_tensor,eval_label_tensor,test_data_tensor,test_label_tensor,train_loader,mm_x,mm_y
    
    def xgb_data(self,data,label):
        train_data,train_label,eval_data,eval_label,test_data,test_label = self.split_data(data,label)
        train_data = train_data.reshape(len(train_data),-1)
        test_data = test_data.reshape(len(test_data),-1)
        eval_data = eval_data.reshape(len(eval_data),-1)
        return train_data,train_label,test_data,test_label,eval_data,eval_label
    

