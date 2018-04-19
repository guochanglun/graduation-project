import requests

'''
测试获取一篇文章
'''

url = 'http://www.toutiao.com/i6509626150271058445/info/'
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

r = requests.get(url, headers=headers, params=params)

print(r.status_code)
json = r.json()

# data
data = json['data']
# title
title = data['title']
# content
content = data['content']
# 来源
detail_source = data['detail_source']

print(title)
print(content)
print(detail_source)