#%%
import os
import numpy as np
import pandas as pd
import torch

from utils import *



#############################################################
# Problem 1
#############################################################
# %%%%%%%%%%%%%%%%%%%% Load Data

data = loadtxt('Files/input_day3.txt')
# data = loadtxt('Files/test_day3.txt') # Test

# %%%%%%%%%%%%%%%%%%%% Clean data and convert to matrix

# separate characters into list
data_split = data.copy()
for i,line in enumerate(data):
    tmp = [c for c in line]
    data_split[i] = tmp
df = pd.DataFrame(data_split)

# get list of symbols
special_char_list = np.unique(df.values).tolist()
digits = list(map(str,np.arange(10).tolist())) + ['.']
special_char_list = [char for char in special_char_list if char not in digits]

# map '.' to 10 and symbols to 11
df = df.replace('.', '10')
for char in special_char_list:
    df = df.replace(char, '11')



# %%%%%%%%%%%%%%%%%%%% Apply MaxPool to identify locations adjacent to symbols


# 140 x 140
data_mat = torch.from_numpy(df.values.astype('float')).unsqueeze(0)

mp = torch.nn.MaxPool2d(
    kernel_size = 3,
    stride = 1,
    padding = 1
)

# 140x140
location_mat = mp(data_mat).squeeze(0).int().numpy()
location_mat[location_mat == 10] = 0
location_mat[location_mat == 11] = 1 # location mat is a binary matrix indicating symbol-adjacent locations




# %%%%%%%%%%%%%%%%%%%% Search locations for integers

data_mat = df.values.astype('int')
number_list = []
for i in range(location_mat.shape[0]):
    k2 = 0 # initialize
    for j in range(location_mat.shape[1]):
        if j <= k2: continue # skip to next potential location if a number has already been discovered
        
        if location_mat[i,j] == 1: # check potential location
            if data_mat[i,j] >= 10: # not a number
                continue
            else:
                
                # left
                k1 = j
                while data_mat[i,k1] < 10:
                    k1-=1

                # right
                k2 = j
                while data_mat[i,k2] < 10 and k2 < data_mat.shape[1]-1:
                    k2+=1

                if (data_mat[i,k2] < 10) and (k2 == data_mat.shape[1]-1): k2 +=1 # last col of matrix

                number = list(map(str,data_mat[i,k1+1:k2].tolist()))
                number = int("".join(number))
                number_list.append(number)






# %%%%%%%%%%%%%%%%%%%% Answer
answer1 = np.array(number_list).sum()
print('Answer 1: %s' % str(answer1))

# %%
