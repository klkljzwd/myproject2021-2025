import torch
import torch.nn as nn

class DeepLearnModel(nn.Module):
    def __init__(self,input_size,output_size,hidden_size,window_len,num_layer,is_bid=False) -> None:
        super(DeepLearnModel,self).__init__()

        self.input_size = input_size
        self.output_size = output_size
        self.hidden_size = hidden_size
        self.window_len = window_len
        self.num_layer = num_layer
        self.bid = is_bid
        self.is_bid = 2 if is_bid else 1

        self.conv1 = nn.Conv1d(in_channels=input_size,out_channels=hidden_size,kernel_size=3,padding=1,stride=1,dilation=1)
        self.conv2 = nn.Conv1d(in_channels=hidden_size,out_channels=hidden_size,kernel_size=3,padding=1,stride=1,dilation=1)
        #未使用
        self.attention = mutihead_attention(self.hidden_size*self.is_bid,window_len,4)
        self.gru = nn.GRU(hidden_size,hidden_size,num_layer,bidirectional=self.bid)
        self.out = nn.Linear(window_len*hidden_size*self.is_bid,output_size)


    def forward(self,x):
        x=x.transpose(1,2)
        x=self.conv1(x)
        x=self.conv2(x)
        x=x.transpose(2,1)

        x,_=self.gru(x)
        #x=self.attention(x,x,x)
        s,b,h = x.shape
        x=x.view(s,b*h)
        
        x=self.out(x)
        return x
#未使用
def attention(q,k,v,window_num,input_size):
    score=torch.matmul(q,k.permute(0,1,3,2))
    score=score/(4**0.5)
    score=torch.softmax(score,dim=-1)
    score=torch.matmul(score,v)
    score=score.permute(0,2,1,3).reshape(-1,window_num,input_size)
    return score

class mutihead_attention(nn.Module):
    def __init__(self,input_size,window_num,head_num):
        super(mutihead_attention,self).__init__()
        self.fc_q=nn.Linear(input_size,input_size,2)
        self.fc_k=nn.Linear(input_size,input_size,2)
        self.fc_v=nn.Linear(input_size,input_size,2)
        self.out_fc=nn.Linear(input_size,input_size)
        self.norm=nn.LayerNorm(normalized_shape=input_size,elementwise_affine=True)
        self.dropout=nn.Dropout(p=0.0)
        self.window_num=window_num
        self.head_num=head_num
        self.input_size=input_size
    
    def forward(self,q,k,v):
        b=q.shape[0]
        clone_q=q.clone()
        q=self.norm(q)
        k=self.norm(k)
        v=self.norm(v)
        k=self.fc_k(k)
        v=self.fc_v(v)
        q=self.fc_q(q)
        q=q.reshape(b,self.window_num,self.head_num,self.input_size//self.head_num).permute(0,2,1,3)
        k=k.reshape(b,self.window_num,self.head_num,self.input_size//self.head_num).permute(0,2,1,3)
        v=v.reshape(b,self.window_num,self.head_num,self.input_size//self.head_num).permute(0,2,1,3)

        score=attention(q,k,v,self.window_num,self.input_size)

        score=self.dropout(self.out_fc(score))

        score=clone_q+score#残差结构
        return score
    