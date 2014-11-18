#!/usr/bin/env python3
__author__ = 'Zirx'
# -*- coding: utf-8 -*-  

from http.cookiejar import CookieJar
from urllib.parse import urlencode, unquote
from urllib.error import URLError
from urllib.request import HTTPCookieProcessor, build_opener, Request
from bs4 import BeautifulSoup
import re
import sys
import xml.dom.minidom

TELECON_ADDR = 'http://59.173.2.28/'
UNICOM_ADDR = 'http://210.42.141.4/'
DEFAULT_PAGE = 'default3.aspx'
MAIN_PAGE = 'xsmainfs.aspx'
GRADE_PAGE = 'xscj.aspx'
TIMETABLE_PAGE = 'xsgrkb.aspx'
INFO_PAGE = 'xstop.aspx'
TIMEOUT = 6


class Content:
    def __init__(self, num, password, serverno):
        self.num = num
        self.password = password
        self.warning = False
        self.wrong_password = False
        self.system_error = False
        self.timeout = False
        self.stuinfo_ok = False
        self.timetable_ok = False
        self.transcript_ok = False
        self.stuinfo = []
        self.timetable = []
        self.transcript = []
        self.cj = CookieJar()
        self.opener = build_opener(HTTPCookieProcessor(self.cj))

        if not self.num or not self.password:
            self.warning = True
            self.opener.close()
            return
        if serverno == 0:
            self.addr = TELECON_ADDR
        elif serverno == 1:
            self.addr = UNICOM_ADDR
        else:
            self.warning = True
            self.opener.close()
            return

        if self.check_password():
            self.get_stuinfo()
            self.get_timetable()
            self.get_transcript()
        else:
            #"INCORRECT PASSWORD ,set the flag and do it in generator"
            self.wrong_password = True
            self.opener.close()
            return

    def check_password(self):
        try:
            open_handler = self.opener.open(self.addr, timeout=TIMEOUT)
        except URLError as e:
            if str(e.reason) == "timed out":
                self.timeout = True
                self.opener.close()
                return

        recv = open_handler.read().decode('GB2312')
        #it's hard to test system error
        if recv.find("系统警告") != -1:
            self.system_error = True
            self.opener.close()
            return
        else:
            "get VIEWSTATE"
            soup = BeautifulSoup(recv, from_encoding='GB2312')
            viewstateblock = soup.find(attrs={"name": "__VIEWSTATE"})
            viewstate = viewstateblock['value']
            "post and get return info"
            data = urlencode({'tbYHM': self.num,
                              'tbPSW': self.password,
                              'ddlSF': '学生'.encode('GB2312'),
                              '__VIEWSTATE': viewstate,
                              'imgDL.x': '0',
                              'imgDL.y': '0'})
            data = data.encode('GB2312')
            request = Request(self.addr + DEFAULT_PAGE)
            try:
                recv_index = self.opener.open(request, data, timeout=TIMEOUT)
            except URLError as e:
                if str(e.reason) == "timed out":
                    self.timeout = True
                    self.opener.close()
                    return

            res = recv_index.read().decode('GB2312')

            if res.find("密码不正确") != -1:
                self.wrong_password = True
                self.opener.close()
                return False
            elif res.find("系统警告") != -1:
                self.system_error = True
                self.opener.close()
                return
            else:
                return True

    def get_stuinfo(self):
        try:
            info = self.opener.open(self.addr + INFO_PAGE, timeout=TIMEOUT)
        except URLError as e:
            if str(e.reason) == "timed out":
                self.timeout = True
                self.opener.close()
                return

        info_s = info.read().decode("GBK")
        if info_s.find("系统警告") != -1:
            self.system_error = True
            self.opener.close()
            return

        si = BeautifulSoup(info_s, from_encoding='GB2312')
        soup = BeautifulSoup(si.prettify(), from_encoding='GB2312')
        info_block = soup.find(attrs={'id': '_div'})
        temp = info_block.find_all('span')
        for x in temp:
            self.stuinfo.append(x.string.strip())
        if self.stuinfo:
            self.stuinfo_ok = True
            #"FINFISH"

    def get_timetable(self):
        try:
            timeable_info = self.opener.open(self.addr + TIMETABLE_PAGE + "?xh=" + self.num + "&type=xs",
                                             timeout=TIMEOUT)
        except URLError as e:
            if str(e.reason) == "timed out":
                self.timeout = True
                self.opener.close()
                return

        timeable_info_s = timeable_info.read().decode("GBK")
        if timeable_info_s.find("系统警告") != -1:
            self.system_error = True
            self.opener.close()
            return

        soup = BeautifulSoup(timeable_info_s, from_encoding='GB2312')
        table = soup.find(attrs={'id': 'table6'})

        table = str(table).replace('<br>', '\n')
        table = str(table).replace('<br/>', '\n')
        table = str(table).replace('</br>', '')
        table = re.sub(r'<td.*?>第.+?节</td>', '', table)

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
        wednesday = []
        thursday = []
        friday = []

        for i in tables:
            monday.append(i[0])
            tuesday.append(i[1])
            wednesday.append(i[2])
            thursday.append(i[3])
            friday.append(i[4])

        res = []
        res.append(monday)
        res.append(tuesday)
        res.append(wednesday)
        res.append(thursday)
        res.append(friday)
        self.timetable = res
        self.timetable_ok = True
        #some students has it's empty timetable
        #so this if is useless
        #if self.timetable[0][0]:
        #    self.timetable_ok = True

    def get_transcript(self):
        try:
            grade_info = self.opener.open(self.addr + GRADE_PAGE + "?xh=" + self.num, timeout=TIMEOUT)
        except URLError as e:
            if str(e.reason) == "timed out":
                self.timeout = True
                self.opener.close()
                return
            #
        #GBK???
        #
        grade_info_s = grade_info.read().decode("GBK")
        if grade_info_s.find("系统警告") != -1:
            self.system_error = True
            self.opener.close()
            return

        soup = BeautifulSoup(grade_info_s, from_encoding='GB2312');
        s = soup.find(attrs={"id": "DataGrid1"})
        rs = BeautifulSoup(s.prettify())
        tab = rs.find_all('tr')
        table = []
        item = []

        for row in tab:
            for col in row.find_all('td'):
                item.append(col.string.strip())
            table.append(item)
            item = []
            #the 1 line is table head
        self.transcript = table[1:]
        if self.transcript:
            self.transcript_ok = True


class XMLGenerator:
    def __init__(self, content):
        self.content = content
        self.mainnode = xml.dom.minidom.Document()
        self.node = self.mainnode.createElement("root")
        #Mininode is the root of XML Document
        #if wanna to add a node to root
        #need to pass root as argument to function
        self.add_server_status()
        self.add_status()
        if self.content.stuinfo_ok:
            self.add_sutinfo()
        if self.content.timetable_ok:
            self.add_timetable()
        if self.content.transcript_ok:
            self.add_transcript()
        self.mainnode.appendChild(self.node)

    #if server is full, the CGI server will generate this
    #information and transfer it to client
    #so this function in python is always return normal information
    def add_server_status(self):
        temp = self.mainnode.createElement("serverstatus")
        temp.setAttribute("value", "ok")
        self.node.appendChild(temp)

        #waring info is serious error ,interrupt
    def add_status(self):
        temp = self.mainnode.createElement("status")
        temp.setAttribute("syserror", str(self.content.system_error))
        temp.setAttribute("password", str(self.content.wrong_password))
        temp.setAttribute("stuinfo", str(self.content.stuinfo_ok))
        temp.setAttribute("timeable", str(self.content.timetable_ok))
        temp.setAttribute("transcript", str(self.content.transcript_ok))
        self.node.appendChild(temp)

    def add_sutinfo(self):
        temp = self.mainnode.createElement("stuinfo")
        temp.setAttribute("name", self.content.stuinfo[0])
        temp.setAttribute("major", self.content.stuinfo[1])
        temp.setAttribute("class", self.content.stuinfo[2])
        temp.setAttribute("id", self.content.stuinfo[3])
        self.node.appendChild(temp)


    def add_timetable(self):
        #every element in timetable is a sigle string
        #so there need parse again
        x = self.mainnode.createElement("timetable_root")
        for item in self.content.timetable:
            #just like ['土力学与基础工程I', '2节/周', '苏明会z[01-15]', '2-407']
            for subitem in item:
                temparray = subitem.split("\n")
                temp = self.mainnode.createElement("timetable")
                if temparray[0]:
                    temp.setAttribute("classname", temparray[0])
                    temp.setAttribute("times", temparray[1])
                    temp.setAttribute("teacher", temparray[2])
                    temp.setAttribute("classroom", temparray[3])
                    x.appendChild(temp)
                else:
                    temp.setAttribute("classname", "")
                    temp.setAttribute("times", "")
                    temp.setAttribute("teacher", "")
                    temp.setAttribute("classroom", "")
                    x.appendChild(temp)
        self.node.appendChild(x)


    def add_transcript(self):
        #example ['2011-2012', '1', '中国近现代史纲要', '必修课', '刘娟z', '考查', '89', '', '', '3.9', '2']
        x = self.mainnode.createElement("transcript_root")
        for item in self.content.transcript:
            temp = self.mainnode.createElement("transcript")
            temp.setAttribute("academic_year", item[0])
            temp.setAttribute("semester", item[1])
            temp.setAttribute("course_name", item[2])
            temp.setAttribute("course_type", item[3])
            temp.setAttribute("course_teacher", item[4])
            temp.setAttribute("assessment_methods", item[5])
            temp.setAttribute("total_mark", item[6])
            temp.setAttribute("makeup_mark", item[7])
            temp.setAttribute("rebuild_mark", item[8])
            temp.setAttribute("grade_point", item[9])
            temp.setAttribute("credit", item[10])
            x.appendChild(temp)
        self.node.appendChild(x)


    def add_data(self):
        return self.mainnode


if __name__ == "__main__":
    a = sys.argv[1]
    b = unquote(sys.argv[2])
    c = sys.argv[3]
    s = Content(a, b, int(c))
    #print(s.num)
    #print(s.password)
    #print(s.wrong_password)
    #print(s.addr)
    #print(s.stuinfo_ok)
    #print(s.stuinfo)
    #print(s.timetable_ok)
    #print(s.timetable)
    #print(s.transcript_ok)
    #print(s.transcript)
    #print('HERE:')
    #for x in s.transcript:
        #print(x)
    #with open("info.txt", "w", encoding="UTF-8") as tmp:
        #tmp.write(str(s.stuinfo))
        #tmp.write("\r\n")
        #tmp.write(str(s.timetable))
        #tmp.write("\r\n")
        #tmp.write(str(s.transcript))
    x = XMLGenerator(s)
    outbyte = x.mainnode.toprettyxml(encoding="UTF-8")
    out = str(outbyte, encoding = "UTF-8")

 
    print("HTTP/1.1 200 OK\r\nServer: Zirconi's\r\nContent-Type: application/xml; charset=UTF-8\r\nContent-length:%d\r\n" % len(outbyte))
    print(out)

    #with open(r"./final.xml", "wb") as q:
    #    q.write(x.mainnode.toprettyxml(encoding="UTF-8"))
