__author__ = 'Xu'

import os
import re

lists = []
all = []
files = []
for roots, dirs, files in os.walk(r'L:\info'):
    print(files)

for file in files:
    name = ['姓名', file.split('.')[0]]
    lists.append(name)
    with open(roots + '\\' + file, 'r') as f:
        for line in f:
            if line.strip():
                a = re.split(r'[：, :, ]', line)
                if a[1].strip():
                    temp = a[1].strip()
                else:
                    temp = '无'
                dicts = [a[0].strip(), temp]
                lists.append(dicts)
    all.append(dict(lists))
    lists = []




with open(r'L:\output.txt', 'w') as out:
    for item in all:
        print(item)
        s = item.get('姓名') + ' ' + item.get('就业单位') + ' ' + item.get('联系方式') + ' ' + item.get(
                '家庭联系方式') + ' ' + item.get('清考科目') + ' ' + item.get('公选课学分') + ' ' + item.get(
                '最高学分绩点') + ' ' + item.get('英语四级最高分') + '\n'
        out.write(s)

"""
nnXX.txt
就业单位：1111
联系方式：2222
家庭联系方式：3333
清考科目：4444
公选课学分：5555
最高学分绩点：6666
英语四级最高分：7777
"""