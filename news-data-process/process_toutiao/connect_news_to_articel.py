"""

"""
import pymysql

# 连接mysql
connection = pymysql.connect(host='localhost',
                             port=3306,
                             user='root',
                             password='gcl',
                             db='gcl',
                             charset='utf8',
                             cursorclass=pymysql.cursors.DictCursor)
cursor = connection.cursor()

# 获取所有的url
cursor.execute('select id, title from news')
results = cursor.fetchall()

for row in results:
    title = row['title']
    cursor.execute('select id from article where title=\'' + title +'\'')
    ids = cursor.fetchall()
    if len(ids) == 0:
        cursor.execute('update news set article_id=-1 where id='+str(row['id']))
    else:
        article_id = ids[0]['id']
        cursor.execute('update news set article_id='+str(article_id)+' where id=' + str(row['id']))

connection.commit()
cursor.close()
connection.close()