__author__ = 'Zirx'

from bs4 import BeautifulSoup
#with open('pp.htm', 'r+', encoding = 'GB2312') as infile:
#    soup = BeautifulSoup(infile, from_encoding = 'GB2312');
#    #print(soup.prettify())
#
#    print(soup.title.string)
#
#    s = soup.find(attrs={"name":"__VIEWSTATE"})
#    print(s['value'])


with open('./data/xscj.aspx.htm', 'r+') as infile:
    soup = BeautifulSoup(infile, from_encoding = 'GB2312');
    #print(soup.prettify())

    print(soup.title.string)

    s = soup.find(attrs={"id":"DataGrid1"})
    re = s.prettify()
    rs = BeautifulSoup(re)
    tab = rs.find_all('tr')

    table = []
    item = []


    for row in tab:
        for col in row.find_all('td'):
            item.append(col.string.strip()) #第循环行第一列
        table.append(item)
        item = []

        for x in table[1:-1]:
            print(x)