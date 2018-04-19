import re
import requests

'''
测试解析一条real url，如果成功就用来解析所有url
'''

headers = {
    'user-agent': 'my-app/0.0.1',
    'Cookie': 'tt_webid=6509739529191523843; UM_distinctid=160e4c8af9245-02b00769fccc93-3c604504-144000-160e4c8af9320f; csrftoken=86d45d56062ab75ab4dc55d6f39e1532; W2atIF=1; _ga=GA1.2.1077435743.1515667578; _gid=GA1.2.305319671.1515667578; _ba=BA0.2-20180111-51225-0LDGHqd0BsUXLPz50YYJ; __tasessionId=vek1n4x7q1515667581859; uuid="w:a9b6b3aa1279406c847243a187380354"',
    'Host':'m.toutiao.com',
    'Upgrade-Insecure-Requests':'1',
    'User-Agent':'Mozilla/5.0 (Linux; Android 6.0; Nexus 5 Build/MRA58N) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.132 Mobile Safari/537.36'
}
r = requests.get('http://toutiao.com/group/6481041189280678413/', headers = headers, allow_redirects=False)
print(r.status_code)
text = r.text
print(r.headers)
print(text[0:500])

print(re.findall(r'http://www.toutiao.com/\w*', text))
