from sys import argv

scriptname,  filename_1,  filename_2,  filename_3, filename_4, filename_5  = argv

SIZE = 251
HOW_MANY = 5

column_1 = range(SIZE)
column_2 = [0.0] * SIZE
column_3 = [0.0] * SIZE
column_4 = [0.0] * SIZE
column_5 = [0.0] * SIZE

def load_data(filename):
    
    f_in_1 = open(filename)
    index = 0

    for line in f_in_1.readlines():
        values = line.split()
    
        column_2[index] = column_2[index] + float(values[1])
        column_3[index] = column_3[index] + float(values[2])
        column_4[index] = column_4[index] + float(values[3])
        column_5[index] = column_5[index] + float(values[4])
        
        index = index + 1
    
    
def save(filename):
    
    for i in range(0, SIZE):
            
        column_2[i] = column_2[i] / HOW_MANY            
        column_3[i] = column_3[i] / HOW_MANY
        column_4[i] = column_4[i] / HOW_MANY            
        column_5[i] = column_5[i] / HOW_MANY
        
    
    fout = open(filename, 'w')
    for i in range(0, SIZE):
            
        line = str(column_1[i])  + " " + str(column_2[i] )+ " " + str(column_3[i]) + " " + str(column_4[i]) + " " + str(column_5[i])  + "\n"
        fout.write(line)
            
    fout.close()   
    
    
load_data(filename_1)
load_data(filename_2)
load_data(filename_3)
load_data(filename_4)
load_data(filename_5)

save('merged')

    
