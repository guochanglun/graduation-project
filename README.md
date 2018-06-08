# graduation-project
毕业项目

## android客户端访问服务器session问题：sess.getAttibure("user") = Null
在Android使用okhttp3进行网络访问，由于存在用户登录，在服务器会把登录的用户存到session里，这时候在Android代码里就只能使用一个HttpClient对象，如果新建一个对象就相当于重新开启了一个session，这样的话之前登录时保存的User就不能访问到了。一开始我还以为时spring boot的问题，没想到会是HttpClient对象的问题。