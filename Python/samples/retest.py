__author__ = 'Xu'
from bs4 import BeautifulSoup


str1 = "<td align=\"Center\" rowspan=\"2\" width=\"14%\"> 土力学与基础工程I<br/>2节/周<br/>苏明会z[01-15]<br/>2-407</td>"

str2 = str1.replace("<br/>", "\n")



sssss = BeautifulSoup(str2)

print(sssss.string.strip())