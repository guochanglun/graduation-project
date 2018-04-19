import pymysql
import requests
import re

'''
把spider_original_urls.py中获取的原始url解析成文章的真正url
'''

# 请求头
headers = {
    'user-agent': 'my-app/0.0.1',
    'Host':'m.toutiao.com',
    'Upgrade-Insecure-Requests':'1',
    'Referer':'https://m.toutiao.com',
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

# 获取cursor
cursor = connection.cursor()

# 存储url的文件
file = open('./url/news_entertainment.txt', 'w')

# 获取所有的url
cursor.execute('select * from news where tag="news_entertainment"')
results = cursor.fetchall()

# 打开一个Session
S = requests.Session()
# 循环处理
for row in results:
    url = row['article_url']
    tag = row['tag']
    headers['Referer'] += tag
    if 'http://toutiao.com' not in url : continue
    try:
        r = requests.get(url, headers=headers)
        if r.status_code == 200:
            text = r.text[0:500]
            urls = re.findall(r'http://www.toutiao.com/[a-z]\w*', text)
            u = urls[0]
            if 'redirect' in u : continue
            print(u + ' ' + tag)
            file.write(u + ' ' + tag + '\n')
        r.close()
    except Exception as e:
        print(e)
        continue
S.close()

file.flush()
file.close()

cursor.close()
connection.close()