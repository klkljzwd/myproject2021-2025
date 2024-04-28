from xml.dom import minidom
import numpy as np
import pandas as pd
import scipy.io as sio
from scipy import stats
import matplotlib.pyplot as plt
from sympy import re
from DataMinute import DataMinDiff


class EmpiricalProbability:
    def __init__(self) -> None:
        pass
    def getP(self,r_data,bin_num):
        bin = np.linspace(np.min(r_data),np.max(r_data),bin_num)
        #记录每个区间的频次
        f = np.zeros(len(bin)-1)
        mid = np.zeros(len(bin)-1)
        for r in r_data:
            for i in range(len(bin)-1):
                mid[i] = (bin[i]+bin[i+1])/2
                if r>=bin[i] and r<=bin[i+1]:
                    f[i] = f[i] + 1
        #计算经验概率
        p = f/np.diff(bin)/len(r_data)
        #数据清洗
        ind = p>0
        p = p[ind]
        f = f[ind]
        mid = mid[ind]
        return mid,p
    
    