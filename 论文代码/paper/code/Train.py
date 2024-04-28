import numpy as np
import torch
import torch.nn as nn
import matplotlib.pyplot as plt

from Model import DeepLearnModel
from Dataload import TimeSeriesDataLoader
from Draw import PltDraw
from Conf import Conf
conf = Conf()
#读取数据
dataload = TimeSeriesDataLoader(conf.csv,conf.window_len,conf.target,conf.radio,conf.batch_size)
data = dataload.read_csv()
label = data
#lstm训练数据
train_data_tensor,train_label_tensor,eval_data_tensor,eval_label_tensor,test_data_tensor,test_label_tensor,train_loader,mm_x,mm_y = dataload.lstm_data(data,label)
#训练设备
device=torch.device("cuda:0" if torch.cuda.is_available() else "cpu")
#训练模型
net = DeepLearnModel(input_size=1,output_size=1,hidden_size=4,window_len=conf.window_len,num_layer=4,is_bid=conf.isBid)
net = net.to(device)

loss_func = nn.MSELoss()
optim = torch.optim.AdamW(lr=0.001,params=net.parameters())

iter=0
for epoch in range(100):
    for i,(train_x,train_y) in enumerate(train_loader):
        output = net(train_x.to(device))
        optim.zero_grad()
        loss = loss_func(output,train_y.to(device))
        loss.backward()
        optim.step()
        iter+=1
        if(iter % 100 == 0):
            print("iter: %d, mse_loss: %1.5f" % (iter, loss.item()))

torch.save(net,"./saved_model/model_parm.pkl")

train_predict=net(train_data_tensor.to(device))
train_predict=train_predict.to(torch.device("cpu"))
train_predict=mm_y.inverse_transform(train_predict.detach().numpy())
label_real_train=mm_y.inverse_transform(train_label_tensor.detach().numpy())

draw = PltDraw()
draw.draw(train_predict[conf.window_len+1:],label_real_train[conf.window_len+1:])