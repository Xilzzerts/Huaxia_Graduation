__author__ = 'Zirx'
from bs4 import BeautifulSoup
import xml.dom.minidom
with open(r'./data/xstop.aspx.htm') as info:
    soup = BeautifulSoup(info, from_encoding='GB2312')
    #print(soup.prettify())
    i = soup.find(attrs={'id': '_div'})
    #print(i)
    s = i.find_all('span')
    ainfo = []
    for x in s:
        print(x.string)
        ainfo.append(x.string.strip())

    doc = xml.dom.minidom.Document()

    mainnode = doc.createElement("stuinfo")

    namenode = doc.createElement("name")
    namenode.appendChild(doc.createTextNode(ainfo[0]))

    majornode = doc.createElement("major")
    majornode.appendChild(doc.createTextNode(ainfo[1]))

    classnode = doc.createElement("class")
    classnode.appendChild(doc.createTextNode(ainfo[2]))

    numnode = doc.createElement("num")
    numnode.appendChild(doc.createTextNode(ainfo[3]))

    mainnode.appendChild(namenode)
    mainnode.appendChild(majornode)
    mainnode.appendChild(classnode)
    mainnode.appendChild(numnode)

    doc.appendChild(mainnode)
    print(doc.toprettyxml(encoding="UTF-8").decode("UTF-8"))