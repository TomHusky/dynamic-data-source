# dynamic-data-source
## 基于mybatisplus的动态数据源SAAS系统

#### 主要逻辑：
  使用TOKEN保存用户登录的信息（或者把登录信息存入redis，和TOKEN对应起来），使用AOP拦截请求，解析TOKEN信息存入ThreadLocal（线程私有内存）中
自定义的AbstractDataSource对象获取租户id对应的DataSource对象，拿到相应的数据库连接，然后处理对应的业务。

#### 流程图如下所示：

![流程图](https://img2020.cnblogs.com/blog/1556860/202107/1556860-20210705150859085-1176020107.png)
