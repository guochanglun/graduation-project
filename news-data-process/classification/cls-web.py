"""
使用web.py模块启动一个服务器，接收文章并返回分类结果
"""

import jieba
import web

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
print('生成词频字典完成')

# 读入停用词
file = open('stop_word.dat', encoding='utf-8')
lines = file.readlines()
file.close()
stop_word = set(line.strip('\n') for line in lines)
print('读入停用词完成')

# web
urls = (
  '/', 'tag'
)

app = web.application(urls, globals())

class tag:
    def POST(self):
        # 分词
        html = web.input().html
        seg_list = jieba.cut(html)
        list = set(seg_list)
        seg_list.close()

        map_score = {}

        for tag in tags:
            score = 1.0
            map = map_word[tag]
            for word in list:
                if word not in stop_word and len(word) > 1:
                    score += float(map.get(word, 0))/map_count[tag]
            map_score[tag] = score
            # print(tag + ' ' + str(score))

        max_value = -9999
        max_tag = ''
        for tag, value in map_score.items():
            if value > max_value:
                max_value = value
                max_tag = tag

        return max_tag
    
# 启动程序
if __name__ == "__main__":
    app.run()











    
