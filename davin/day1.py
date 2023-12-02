
#%%
import os
import numpy as np
import pandas as pd


#############################################################
# Problem 1
#############################################################
#%%

def first_integer(string, reverse = False):
    for i in range(len(string)):
        if reverse:
            char = string[-(i+1)]
        else:
            char = string[i]
        try:
            output = int(char)
            break
        except:
            continue
    return output

df = pd.read_csv('input.txt', sep = '\t', header = None)
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

def first_number_singledigit(string, search_number, reverse = False):
    # returns the index of when search_number first appears. Returns None if not found.
    search_len = len(search_number)
    
    if reverse:
        for i in range(len(string)-search_len, -1, -1):
            substr = string[i:i+search_len]
            if substr == search_number:
                return i
    else:
        for i in range(len(string)+1-search_len):
            substr = string[i:i+search_len]
            if substr == search_number:
                return i
    return None

def first_number(string, digit_list, digit_mapping, reverse = False):
    # wrapper that applies first_number_singledigit over a list of digits
    digit_index = []
    for digit in digit_list:
        digit_index.append(first_number_singledigit(string, digit, reverse = reverse))

    if reverse:
        optimal_idx = digit_index.index(max([d for d in digit_index if d is not None])) # get index of digit that appears first
    else:
        optimal_idx = digit_index.index(min([d for d in digit_index if d is not None])) # get index of digit that appears first
    return digit_mapping[optimal_idx] # convert index to integer from digit_mapping


df['firstnum'] = df.loc[:,0].apply(first_number, digit_list = digit_list, digit_mapping = digit_mapping, reverse = False)
df['lastnum'] = df.loc[:,0].apply(first_number, digit_list = digit_list, digit_mapping = digit_mapping, reverse = True)
df['code_p2'] = (df['firstnum'].astype('str') + df['lastnum'].astype('str')).astype('int')
answer2 = df['code_p2'].sum()
print('Answer 2: %s' % str(answer2))


# %%
display(df)

# %%
