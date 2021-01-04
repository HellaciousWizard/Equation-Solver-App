import numpy as np
import cv2
import matplotlib.pyplot as plt
from PIL import Image
import io
import base64
import math

def isDigit(s):
    f = True
    for i in s:
        if(not(i.isdigit()) and i != "-" and i != "."):
            f = False
            break
    return f

def isZero(s):
    s1 = ""
    for i in s:
        if(i.isdigit() or i == "-" or i == "."):
            s1 += i
        else:
            break
    if (eval(s1) == 0.0):
        return True
    else:
        return False

def Graph_Img(s,x,y,count):
    fig = plt.figure(figsize = (8,8))
    x_flag = True
    y_flag = True
    if(x != "" and y != ""):
        st = s.split(",")
        n = ""
        l = []
        for i in st:
            t = i[:-2].split("+")
            s1 = ""
            for j in range(len(t)):
                if(isZero(t[j])):
                    continue
                if(j != 0 and j != len(t)-1):
                    if(t[j][0]=="-"):
                        s1 += t[j]
                    else:
                        s1 += "+" + t[j]
                elif(j == len(t)-1):
                    n = t[j]
                else:
                    s1 += t[j]
            print(n)
            if (isDigit(n)):
                s1 += "=" + str(-eval(n))
            else:
                s1 += n + "=0"
            l.append(s1)
        X = x.split("|")
        Y = y.split("|")
        c = 0
        m1 = -math.inf
        m2 = -math.inf
        m3 = math.inf
        m4 = math.inf

        while(True):
            if(c == count):
                break

            x_data = []
            y_data = []
            x_data = list(map(float, X[c].split(",")))
            y_data = list(map(float, Y[c].split(",")))

            for i in x_data:
                if(i==0):
                    x_flag = False
                else:
                    x_flag = True
                    break

            for i in y_data:
                if(i==0):
                    y_flag = False
                else:
                    y_flag = True
                    break


            if(max(x_data)>m1):
                m1 = max(x_data)
            if(max(y_data)>m2):
                m2 = max(y_data)
            if(min(x_data)<m3):
                m3 = min(x_data)
            if(min(y_data)<m4):
                m4 = min(y_data)

            print(x_data)
            plt.plot(x_data, y_data,label=l[c])
            plt.legend(bbox_to_anchor=(0.98, 0.98),fontsize='x-large', loc=1, borderaxespad=0.0)

            if(c != count):
                c += 1

        plt.xlim(m3, m1)
        plt.ylim(m4, m2)
    plt.grid(linestyle = '--')
    plt.xlabel('<--- X - Axis ---> ', size='xx-large')
    plt.ylabel('<--- Y - Axis ---> ', size='xx-large')
    if(x != "" and y != ""):
        if(y_flag):
            plt.axhline(y = 0, color='k')
        if(x_flag):
            plt.axvline(x = 0, color='k')
    else:
        plt.xlim(-5, 5)
        plt.ylim(-5, 5)
        if(s=="1x=0"):
            plt.axvline(x = 0, color='b',label="x=0")
            plt.legend(bbox_to_anchor=(0.98, 0.98),fontsize='x-large', loc=1, borderaxespad=0.0)
            plt.axhline(y = 0, color='k')
        elif(s=="1x=0,1y=0" or s=="1y=0,1x=0"):
            plt.axvline(x = 0, color='b',label="x=0")
            plt.axhline(y = 0, color='r',label="y=0")
            plt.legend(bbox_to_anchor=(0.98, 0.98),fontsize='x-large', loc=1, borderaxespad=0.0)
        elif(s=="1y=0"):
            plt.axhline(y = 0, color='r',label="y=0")
            plt.legend(bbox_to_anchor=(0.98, 0.98),fontsize='x-large', loc=1, borderaxespad=0.0)
            plt.axvline(x = 0, color='k')


    fig.canvas.draw() #to convert data to numpy array

    img =  np.fromstring(fig.canvas.tostring_rgb(),dtype=np.uint8,sep='')
    img = img.reshape(fig.canvas.get_width_height()[::-1]+(3,))  #Reshape data
    img = cv2.cvtColor(img,cv2.COLOR_RGB2BGR)

    #now converted to cv2 image
    # will convert PIL image and byte string

    pil_im = Image.fromarray(img)
    buff = io.BytesIO()
    pil_im.save(buff,format="PNG")
    img_str = base64.b64encode(buff.getvalue())

    return ""+str(img_str,'utf-8')