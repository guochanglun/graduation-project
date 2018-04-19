'''
使用parse_to_real_url.py中得到的真实url，获取所有的文章，并存入数据库
'''

import requests
import pymysql

# 分类
tags = [
    #1 社会
    # 'news_society',

    #2 娱乐
    'news_entertainment',

    #3 军事
    # 'news_military',

    #4 科技
    'news_tech',

    #5 体育
    'news_sports',

    #6 财经
    'news_finance',

    #7 国际
    'news_world',

    #8 历史
    'news_history',

    #10 养生
    'news_regimen'
]
for tag in tags:
    file = open('./url/'+tag+'.txt', 'r')
    lines = file.readlines()

    params = {
        '_signature':'9N7LWxAZrpE6tU2Jlfb1bPTey0'
    }
    headers = {
        'user-agent': 'my-app/0.0.1',
        'Cookie': 'tt_webid=6509739529191523843; UM_distinctid=160e4c8af9245-02b00769fccc93-3c604504-144000-160e4c8af9320f; csrftoken=86d45d56062ab75ab4dc55d6f39e1532; W2atIF=1; _ga=GA1.2.1077435743.1515667578; _gid=GA1.2.305319671.1515667578; _ba=BA0.2-20180111-51225-0LDGHqd0BsUXLPz50YYJ; __tasessionId=vek1n4x7q1515667581859; uuid="w:a9b6b3aa1279406c847243a187380354"',
        'Host':'m.toutiao.com',
        'Upgrade-Insecure-Requests':'1',
        'User-Agent':'Mozilla/5.0 (Linux; Android 6.0; Nexus 5 Build/MRA58N) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.132 Mobile Safari/537.36'
    }
    # 连接mysql
    connection = pymysql.connect(host='localhost',
                                 port=3306,
                                 user='root',
                                 password='gcl',
                                 db='toutiao',
                                 charset='utf8',
                                 cursorclass=pymysql.cursors.DictCursor)

    cursor = connection.cursor()

    for line in lines:
        infos = line.split(' ')
        if len(infos) != 2: continue
        url = infos[0] + '/info/'
        tag = infos[1]

        r = requests.get(url, headers=headers, params=params)
        if r.status_code != 200 : continue

        json = r.json()
        # data
        data = json['data']
        # title
        title = data.get('title', '')
        # content
        content = data.get('content', '')
        # 来源
        detail_source = data.get('detail_source', "游")
        print('insert')
        cursor.execute('insert into article(title, content, detail_source, tag)'
                       ' values(%s, %s, %s, %s)',
                       (title, content, detail_source, tag))
        r.close()

    connection.commit()
    cursor.close()
    connection.close()

