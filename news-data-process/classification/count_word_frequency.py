"""
统计词频，存入词频文件
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

for tag in tags:
    # 读出文件
    file = open('./word_bag/'+tag+'.txt', encoding='gbk')
    lines = file.readlines()
    file.close()
    map = {}
    for word in lines:
        word = word.strip('\n')
        size = map.get(word, 0)
        map[word] = size + 1
    # 排序
    sorted_word = sorted(map.items(), key=lambda item:item[1], reverse=True)
    # 写入文件
    file = open('./word_frequency/'+tag+'_frequency.txt', 'w')
    for word, count in sorted_word:
        # 只有这个词的次数超过四次才存入文件
        if count > 4:
            file.write(word+' ' + str(count) + '\n')
    file.close()