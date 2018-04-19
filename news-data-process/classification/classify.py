"""
测试对一篇新闻的分类情况
"""

import jieba

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

# 测试
news = '''
解放军是纸老虎？台专家称这是台独最大谎言
2018-01-21 15:11 
　　近来，“武统”话题一直占据着两岸舆论空间，台湾地区领导人蔡英文甚至在年终茶叙时断言“不相信大陆会对台湾动武”。对此，有“台独”分子妄言附和，宣称“解放军是纸老虎”。20日，台湾“国防咨询委员”、军事专家宋兆文在其个人脸书上刊文表示不满，直批这种说法“是精神病加白痴的台独最大谎言。”
　　以下为宋兆文脸书全文：
　　解放军是纸老虎！这是精神病加白痴的台独分子最大谎言，仅以客观的态度来描述一下这只纸老虎，我是反台独，也反共军威胁的人，以下论述是站在军事观点，意在戳破台独谎言，分析实际情况，给我乱戴红帽子的烂人，俺F-TMB！
　　中国人民解放军是中华人民共和国主要的国家武装力量，由陆军、海军、空军、火箭军、战略支援部队五大军种组成。解放军由中国共产党建立，自成立起坚持“党指挥枪”原则，由中央军事委员会所领导，中央军委主席统率全军。
现役人数
　　230万人，备役人数823万人，加上14个武警机动师约18万人，可以参战人数共计1千零71万人。
　　中国人民解放军是由中国共产党缔造领导，共产党牢牢掌控一切，用中国特色社会主义理论体系武装的人民军队，是中华人民共和国的武装力量。其宗旨是：紧紧地和人民站在一起，全心全意地为人民服务。
　　共军军人誓词
　　我是中国人民解放军军人，我宣誓：服从中国共产党的领导，全心全意为人民服务，服从命令，严守纪律，英勇顽强，不怕牺牲，苦练杀敌本领，时刻准备战斗，绝不叛离军队，誓死保卫祖国。
　　中国为传统陆权大国，陆军是中国人民解放军的主体力量，是主要在陆地进行作战任务的军种，由步兵、装甲兵、炮兵、陆军防空兵、陆军航空兵、工程兵、防化兵、通信兵等兵种和各种专业勤务部队组成，共155万人，其中机动部队85万人，地方守备部队70万人，规模世界第一。
　　中国人民解放军海军由潜艇部队、水面舰艇部队、海军航空兵、海军岸防兵、海军陆战队等兵种及专业部（分）队组成，共29万人，拥有1艘航空母舰、28艘驱逐舰、82艘护卫舰、26艘坦克登陆舰、28艘中型登陆舰、58艘柴电潜艇、13艘核潜艇、45艘近岸导弹艇，海军下辖北海、东海、南海三个舰队和海军航空兵部。
'''

# 读入停用词
file = open('stop_word.data', encoding='utf-8')
lines = file.readlines()
file.close()
stop_word = set(line.strip('\n') for line in lines)

# 分词
seg_list = jieba.cut(news)
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
    print(tag + ' ' + str(score))

# 寻找最大score和其对应的tag
max_value = -9999
max_tag = ''
for tag, value in map_score.items():
    if value > max_value:
        max_value = value
        max_tag = tag

print('=================')
print(max_tag + ' ' + str(max_value))