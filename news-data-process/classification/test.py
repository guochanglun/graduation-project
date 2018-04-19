import pymysql
import re
import jieba

"""
在测试集上测试分类器性能
总共测试了486篇文章，正确342篇，正确率70%左右
"""

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

# 分类词的总数
map_count = {
    "news_society": 0,
    "news_entertainment": 0,
    "news_military": 0,
    "news_tech": 0,
    "news_sports": 0,
    "news_finance": 0,
    "news_world": 0,
    "news_history": 0,
    "news_regimen": 0
}

# 存储词频
map_word = {
    "news_society": {},
    "news_entertainment": {},
    "news_military": {},
    "news_tech": {},
    "news_sports": {},
    "news_finance": {},
    "news_world": {},
    "news_history": {},
    "news_regimen": {}
}

# 统计词频
for tag in tags:
    file = open('./word_frequency/'+tag+'_frequency.txt')
    lines = file.readlines(3200)
    file.close()
    map = map_word[tag]
    for line in lines:
        line = line.strip('\n')
        split = line.split(' ')
        if len(split) != 2: continue
        word = split[0]
        count = split[1]
        map[word] = count
        map_count[tag] += int(count)

# 读入停用词
file = open('stop_word.dat', encoding='utf-8')
lines = file.readlines()
file.close()
stop_word = set(line.strip('\n') for line in lines)

# 连接数据库
connection = pymysql.connect(host='localhost',
                             port=3306,
                             user='root',
                             password='gcl',
                             db='gcl',
                             charset='utf8',
                             cursorclass=pymysql.cursors.DictCursor)
cursor = connection.cursor()

# 正则表达式:过滤html标签和数字
gone_html = re.compile(r'<[^>]+>|\d',re.S)

cursor.execute('select content, tag from article')
result = cursor.fetchall()

article_count = 0
curect = 0

for r in result:
    article_tag = r['tag']
    article_tag = article_tag.strip('\n')
    content = r['content']

    # 去除html标签
    content = gone_html.sub('', content)

    # 分词
    seg_list = jieba.cut(content)
    list = []
    for w in seg_list:
        list.append(w)
    seg_list.close()

    map_score = {}
    for tag in tags:
        score = 1.0
        map = map_word[tag]
        for word in list:
            if word not in stop_word and len(word) > 1:
                score += float(map.get(word, 0)) / map_count[tag]
        map_score[tag] = score
    max_value = -9999
    max_tag = ''
    for tag, value in map_score.items():
        if value > max_value:
            max_value = value
            max_tag = tag

    article_count += 1
    if max_tag == article_tag:
        curect += 1

print(article_count)
print(curect)

cursor.close()
connection.close()