__author__ = 'Zirx'
from bs4 import BeautifulSoup
with open(r'./data/xstop.aspx.htm') as info :
    soup = BeautifulSoup(info, from_encoding='GB2312')
    #print(soup.prettify())
    i = soup.find(attrs={'id':'_div'})
    #print(i)
    s = i.find_all('span')
    for x in s:
        print(x.string)


