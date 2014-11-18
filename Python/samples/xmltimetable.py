__author__ = 'Zirx'


from bs4 import BeautifulSoup
import re
import xml.dom.minidom
with open(r'./data/xsgrkb.aspx.htm', 'r') as infile:
    soup = BeautifulSoup(infile, from_encoding='GB2312')
    table = soup.find(attrs={'id':'table6'})

    table = str(table).replace('<br>', '\n')
    table = str(table).replace('</br>', '')
    table = re.sub(r'<td.*?>第.+?节</td>', '', table)
    #print(table)
    soup = BeautifulSoup(table)
    total = []
    item = []
    for row in soup.find_all('tr')[2:-8]:
        for col in row.find_all('td'):
            p = col.string.strip()
            item.append(p)
        total.append(item)
        item = []
    tables = []
    tables.append(total[0])
    tables.append(total[2])
    tables.append(total[5])
    tables.append(total[7])

    monday = []
    tuesday = []
    webnesday = []
    thursday =[]
    friday = []

    for i in tables:
        monday.append(i[0])
        tuesday.append(i[1])
        webnesday.append(i[2])
        thursday.append(i[3])
        friday.append(i[4])
    res = []
    res.append(monday)
    res.append(tuesday)
    res.append(webnesday)
    res.append(thursday)
    res.append(friday)

    #for r in res:
        #print(r)

    mainnode = xml.dom.minidom.Document()

    mondaynode = mainnode.createElement("monday")
    tuesdaynode = mainnode.createElement("tuesday")
    webnesdaynode = mainnode.createElement("wednesday")
    thursdaynode = mainnode.createElement("thursday")
    fridaynode = mainnode.createElement("friday")

    #it's some difficult to split a class string to detail information
    #so parse it in Android end


    #print(table)
    #pattern = re.compile(r'<td align="Center" rowspan="2">(.+?)<br>(.+?)<br>(.+?)<br>(.+?)')
    #for row in table.find_all('tr')[2:-8]:
    #    for col in row.find_all('td')[:-2]:
    #        print(col)
    #    print()
    #item = table.find_all('tr')[2:-8]
    #print(item[0])#line1
    #m = re.match(pattern, str(item[0]))
    #if m:
    #    print(m.group(0))

    #print(item[2])#line2
    #print(item[5])#line3
    #print(item[7])#line4
    #pattern = re.compile(r'<td align="Center".+?>(.+?)<br>(.+?)<br>(.+?)<br>(.+?)</br></br></br></td>')
    #
    #s = table.find_all(attrs={'align':'Center'})
    #for x in s[14:21]:
    #
    #    m = re.match(pattern, str(x))
    #    if m:
    #        print(m.group(0))

    #print()
    #for x in s[24:31]:
    #    print(x)
    #print()
    #for x in s[42:49]:
    #    print(x)
    #print()
    #for x in s[53:60]:
    #    print(x)
