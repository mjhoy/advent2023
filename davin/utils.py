
#############################################################
# IO
#############################################################

def loadtxt(path, strip = True):
    # returns list of strings, each element is a line
    file = open(path, 'r')
    content = file.readlines()
    file.close()

    # remove newline
    if strip:
        for i,line in enumerate(content):
            content[i] = line.strip()

    return content





#############################################################
# Day 1
#############################################################
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

#############################################################
# Day 1
#############################################################