import requests
import pymysql

'''
爬取原始url，此url并不是文章的真正url，需要由此url跳转才能得到真正的url
'''

# 头条url
url = 'https://m.toutiao.com/list/'

# 请求头
headers = {
    'user-agent': 'my-app/0.0.1',
    'Cookie': 'tt_webid=6509739529191523843; UM_distinctid=160e4c8af9245-02b00769fccc93-3c604504-144000-160e4c8af9320f; csrftoken=86d45d56062ab75ab4dc55d6f39e1532; W2atIF=1; _ga=GA1.2.1077435743.1515667578; _gid=GA1.2.305319671.1515667578; _ba=BA0.2-20180111-51225-0LDGHqd0BsUXLPz50YYJ; __tasessionId=vek1n4x7q1515667581859; uuid="w:a9b6b3aa1279406c847243a187380354"',
    'Host':'m.toutiao.com',
    'Upgrade-Insecure-Requests':'1',
    'User-Agent':'Mozilla/5.0 (Linux; Android 6.0; Nexus 5 Build/MRA58N) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.132 Mobile Safari/537.36'
}

# 请求参数
params = {
    'tag':'news_entertainment',
    'ac':'wap',
    'count':'20',
    'format':'json_raw',
    'as':'A1F5BA7597443FE'
}

# 分类
tags = [
    #1 社会
    'news_society',

    #2 娱乐
    'news_entertainment',

    #3 军事
    'news_military',

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

# 连接mysql
connection = pymysql.connect(host='localhost',
                             port=3306,
                             user='root',
                             password='gcl',
                             db='toutiao',
                             charset='utf8',
                             cursorclass=pymysql.cursors.DictCursor)

cursor = connection.cursor()
# 请求
for tag in tags:
    params['tag'] = tag
    # 每个tag循环200次，一次请求8条左右数据，所以一共请求约9*200*8=14400条新闻
    # 每类新闻约200*8=1600条
    for i in range(200):
        res = requests.get(url, headers=headers, params=params)
        if res.status_code != 200 : continue
        json = res.json()
        print(json['return_count'])
        for news in json['data']:
            if len(news['abstract']) > 5:
                title = news['title']
                abstract = news['abstract']
                media_name = news.get('media_name', '游')
                image_url = news.get('image_url', '')
                image_list = ''
                for img in news['image_list']:
                    image_list += img['url'] + ';'
                article_url = news['article_url']
                print('insert')
                cursor.execute('insert into news(title, abstract, media_name, image_url, image_list, article_url, tag)'
                                   ' values(%s, %s, %s, %s, %s, %s, %s)',
                               (title, abstract, media_name, image_url, image_list, article_url, tag))


connection.commit()
cursor.close()
connection.close()