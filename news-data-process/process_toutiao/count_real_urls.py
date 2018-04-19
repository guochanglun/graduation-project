'''
从数据读取可用url，并计数各类文章的真实数目
'''
import pymysql
# 连接mysql
connection = pymysql.connect(host='localhost',
                             port=3306,
                             user='root',
                             password='gcl',
                             db='toutiao',
                             charset='utf8',
                             cursorclass=pymysql.cursors.DictCursor)
cursor = connection.cursor()

cursor.execute('select tag from article')
result = cursor.fetchall()

news_society = 0
news_entertainment = 0
news_military = 0
news_tech = 0
news_sports = 0
news_finance = 0
news_world = 0
news_history = 0
news_regimen = 0

count = len(result)

# 统计各类文章的数目
for row in result:
    tag = row['tag']
    if 'news_society' in tag:
        news_society += 1
    elif 'news_entertainment' in tag:
        news_entertainment += 1
    elif 'news_military' in tag:
        news_military += 1
    elif 'news_tech' in tag:
        news_tech += 1
    elif 'news_sports' in tag:
        news_sports += 1
    elif 'news_finance' in tag:
        news_finance += 1
    elif 'news_world' in tag:
        news_world += 1
    elif 'news_history' in tag:
        news_history += 1
    elif 'news_regimen' in tag:
        news_regimen += 1

cursor.close()
connection.close()

print(count)
print('news_society ' + str(news_society))
print('news_entertainment ' + str(news_entertainment))
print('news_military ' + str(news_military))
print('news_tech ' + str(news_tech))
print('news_sports ' + str(news_sports))
print('news_finance ' + str(news_finance))
print('news_world ' + str(news_world))
print('news_history ' + str(news_history))
print('news_regimen ' + str(news_regimen))
