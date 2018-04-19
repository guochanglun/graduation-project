"""
把新闻转化为词袋
1）从数据库中读取新闻
2）去掉停用词
3）分词
4）存储到词袋文件等待后续处理
"""
import pymysql
import jieba
import re

# 读取停用词表
lines = open('stop_word.dat', encoding='utf-8').readlines()
stop_word = set(line.strip('\n') for line in lines)

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

word_map = {
    "news_society": [],
    "news_entertainment": [],
    "news_military": [],
    "news_tech": [],
    "news_sports": [],
    "news_finance": [],
    "news_world": [],
    "news_history": [],
    "news_regimen": []
}

# 连接数据库
connection = pymysql.connect(host='localhost',
                             port=3306,
                             user='root',
                             password='gcl',
                             db='toutiao',
                             charset='utf8',
                             cursorclass=pymysql.cursors.DictCursor)
cursor = connection.cursor()

# 正则表达式:过滤html标签和数字
gone_html = re.compile(r'<[^>]+>|\d',re.S)

for tag in tags:
    cursor.execute('select content from article where tag="' + tag + '\n"')
    result = cursor.fetchall()
    for r in result:
        content = r['content']
        # 去除html标签
        content = gone_html.sub('', content)
        # 分词
        seg_list = jieba.cut(content)
        for word in seg_list:
            # 去除停用词，去除单个字
            if word not in stop_word and len(word) > 1:
                word_map[tag].append(word)

for tag, set in word_map.items():
    file = open(tag+'.txt', 'w')
    file.write('\n'.join(set))
    file.close()

cursor.close()
connection.close()