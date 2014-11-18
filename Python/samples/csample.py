__author__ = 'Zirx'

from http.cookiejar import CookieJar
from urllib.request import urlopen,Request,HTTPCookieProcessor,build_opener,install_opener
from urllib.parse import urlencode
from urllib.error import URLError
from bs4 import BeautifulSoup
import getpass


cj = CookieJar()
opener = build_opener(HTTPCookieProcessor(cj))

try:
    r = opener.open("http://192.168.1.136", timeout = 8)
    rv = r.read().decode('utf-8')
    print(rv)
except URLError as e:
    s = str(e.reason)
    if s == "timed out":
        print('ok')
