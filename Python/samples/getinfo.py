__author__ = 'Zirx'

from http.cookiejar import CookieJar
from urllib.request import urlopen,Request,HTTPCookieProcessor,build_opener,install_opener
from urllib.parse import urlencode
from urllib.error import URLError
from bs4 import BeautifulSoup
import getpass


#num = input('NUMBER: ')
#pwd = getpass.getpass('PASSWORD: ')
num = '10212810105'
pwd = ''
url = 'http://210.42.141.4/'

cj = CookieJar()
opener = build_opener(HTTPCookieProcessor(cj))

r = opener.open(url)
rv = r.read().decode('GB2312')
#print(rv)
#检查系统警告

"""
if system waring
the output of rv is:
<HTML>
    <HEAD>
        <title>系统警告！</title>
        <link href="css.css" type="text/css" rel="stylesheet"></link>
    </HEAD>
    <body>
        <form id="Form1" method="post" runat="server">
            <div class="main" align="center">
                <div id="Div1" style="width:350px;margin-top:50px;	border: 1px solid #ccc;">
                    <div id="Div2">
                        <p>&nbsp;</p>
                        <font color="red">
                            <p class="t14">系统警告！</p>
                        </font>
                        <p>&nbsp;</p>
                        <p align="center">很抱歉系统发现错误!请到服务器上面查看详细信息！</p>
                        <p>&nbsp;</p>
                    </div>
                </div>
            </div>
        </form>
    </body>
</HTML>
"""



#GET VIEWSTATE
soup = BeautifulSoup(rv, from_encoding = 'GB2312')
s = soup.find(attrs={"name":"__VIEWSTATE"})
m = s['value']
print(m)
#m = re.findall(r'<input type="hidden" name="__VIEWSTATE" value="(.+?)" />', rv)

data = urlencode({'tbYHM': num,
                  'tbPSW': pwd,
                  'ddlSF': '学生'.encode('GB2312'),
                  '__VIEWSTATE': m,
                  'imgDL.x': '0',
                  'imgDL.y': '0'})
data = data.encode('GB2312')
request = Request(url + 'default3.aspx')
rs = opener.open(request, data)


#print(rs.info())
res = rs.read().decode('GB2312')
#print(res)

#if1 正确 if2 密码错误 if3 系统警告 else未知错误
if(res.find('密码不正确') != -1):
    print('密码不正确')
    opener.close()
else:
    stuinfo = opener.open("http://210.42.141.4/xstop.aspx")
    si = BeautifulSoup(stuinfo)
    #print(si.prettify())
    #sname = si.find(attrs = {'id' :'_div'})
    #print()

    rss = opener.open('http://210.42.141.4/xscj.aspx?xh=' + num)
    #print(rss.read().decode('GB2312'))
    rsss = rss.read()
    soup = BeautifulSoup(rsss, from_encoding = 'GB2312');
    ooo = soup.prettify()
    #print(soup.prettify())
    #print(soup.original_encoding)
    #print(soup.title.string)

    # soup = BeautifulSoup(ooo, from_encoding = 'GB2312');
    # s = soup.find(attrs={"id":"DataGrid1"})
    # re = s.prettify()
    # rs = BeautifulSoup(re)
    # tab = rs.find_all('tr')
    #
    # table = []
    # item = []
    #
    #
    # for row in tab:
    #     for col in row.find_all('td'):
    #         item.append(col.string.strip()) #第循环行第一列
    #     table.append(item)
    #     item = []
    #
    # for x in table[1:-1]:
    #     print(x)
    #


    opener.close()