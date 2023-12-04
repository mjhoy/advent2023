
#%%
import os
import numpy as np
import pandas as pd

from utils import *


#############################################################
# Problem 1
#############################################################
#%%

df = pd.read_csv('Files/input_day1.txt', sep = '\t', header = None)
df['firstdigit'] = df.loc[:,0].apply(first_integer, reverse = False)
df['lastdigit'] = df.loc[:,0].apply(first_integer, reverse = True)
df['code'] = (df['firstdigit'].astype('str') + df['lastdigit'].astype('str')).astype('int')
answer1 = df['code'].sum()
print('Answer 1: %s' % str(answer1))

#############################################################
# Problem 2
#############################################################
#%%

digit_list = [
    'one',
    'two',
    'three',
    'four',
    'five',
    'six',
    'seven',
    'eight',
    'nine',
]
digit_list += map(str, np.arange(10).tolist())
digit_mapping = np.arange(1,10).tolist() + np.arange(10).tolist() # map digit_list to integer

df['firstnum'] = df.loc[:,0].apply(first_number, digit_list = digit_list, digit_mapping = digit_mapping, reverse = False)
df['lastnum'] = df.loc[:,0].apply(first_number, digit_list = digit_list, digit_mapping = digit_mapping, reverse = True)
df['code_p2'] = (df['firstnum'].astype('str') + df['lastnum'].astype('str')).astype('int')
answer2 = df['code_p2'].sum()
print('Answer 2: %s' % str(answer2))


# %%
display(df)

# %%
