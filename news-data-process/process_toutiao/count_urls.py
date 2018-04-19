'''
统计各类目新闻url数量

此时的url并不是文章连接的真实url，大部分是中间url，会自动跳转到真实的url
'''

file = open('url.data')

news_society = 0
news_entertainment = 0
news_military = 0
news_tech = 0
news_sports = 0
news_finance = 0
news_world = 0
news_history = 0
news_regimen = 0

lines = file.readlines()

for line in lines:
    if 'news_society' in line:
        news_society += 1
    elif 'news_entertainment' in line:
        news_entertainment += 1
    elif 'news_military' in line:
        news_military += 1
    elif 'news_tech' in line:
        news_tech += 1
    elif 'news_sports' in line:
        news_sports += 1
    elif 'news_finance' in line:
        news_finance += 1
    elif 'news_world' in line:
        news_world += 1
    elif 'news_history' in line:
        news_history += 1
    elif 'news_regimen' in line:
        news_regimen += 1

print('news_society ' + str(news_society))
print('news_entertainment ' + str(news_entertainment))
print('news_tech ' + str(news_tech))
print('news_sports ' + str(news_sports))
print('news_finance ' + str(news_finance))
print('news_world ' + str(news_world))
print('news_history ' + str(news_history))
print('news_regimen ' + str(news_regimen))

# news_society 123
# news_entertainment 32
# news_tech 56
# news_sports 19
# news_finance 73
# news_world 50
# news_history 33
# news_regimen 43

file.close()