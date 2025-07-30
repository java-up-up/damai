**开源不易，还请您点个Star 多谢！🎉**

## 项目背景
可以说目前的Java程序员面试是非常激烈的，前几年大家都热衷于八股文的研究，把八股文背熟了大部分就能拿到岗位offer了，但现在仅仅靠八股文已经不好使了，现在面试官看的是真正的设计项目，**项目中的架构复杂程度怎样？**、**做过的东西哪些具有亮点？**、
**解决了哪些有价值的问题？** 将实际项目和八股文相结合从而判断面试者的能力。

而且现在内卷逐渐激烈，从一开始只会简单增删改查，到后来的各种微服务和中间件，再到现在要有类似 “商品秒杀、抵抗百万并发的压力.."等技术的亮点才行

所以大家急迫的需要一个具有新的业务，而且要有亮点的项目，要是有人能指导下如何写到简历上就更好了，你可以站在面试官的角度上考虑一下，如果你在挑选简历时，看到了之前没有见过，但亮点却很多的项目，你是不是一下子就被吸引住了呢？

## 项目介绍
大麦订票服务系统提供了可以在线上对相应的节目（包括：演唱会、话剧歌剧、体育比赛、儿童亲子）进行订票的功能，用户可以进行注册，登录，然后选择节目和座位后进行购票，支付，以及查询自己的订单功能

- **大麦项目在线体验：** [👉 点击进行体验](https://www.damai-javaup.chat)

- **大麦项目详细讲解：** [👉 点击查看讲解](https://www.javaup.chat/pages/83cf22/)

## 再来一波项目
### 大麦AI项目
此项目集成了目前最为成熟的 **SpringAI**，并支持 **Ollama**、**OpenAI**、**DeepSeek**、**阿里百炼** 多种 AI 模型。并通过 **MCP**、**Function Calling** 与大麦项目进行联动，可以让 AI 智能的帮助用户执行操作。实现 **只需聊天就能全部搞定！**
大麦 再加上 大麦AI，面试可以说是 **双剑合璧**了。

- **大麦AI项目详细讲解：** [👉 点击查看讲解](https://javaup.chat/pages/bdede9/#%E5%A4%A7%E9%BA%A6%E9%A1%B9%E7%9B%AE)

### link-flow项目
除了大麦项目外，还提供了一个用于服务请求流量切换的项目：**link-flow**，既有着 **控制微服务流量的业务功能**，又有着 **对微服务框架进行定制增强的轮子功能**。而且不仅适用于实习，对于社招来说，也是能用到的！

- **link-flow项目详细讲解：** [👉 点击查看讲解](https://www.javaup.chat/pages/09b92e/)

## 项目优势

大家对于订票时尤其是热门明星的演唱会，第一感觉就是票不好抢，一瞬间票就没有了，而本项目不仅仅是将上述的购票功能实现，并且还要解决这种票不好抢，也就是常说的 **高并发** 问题

小伙伴通过此项目能够掌握分布式微服务项目的设计、以及 **真实生产环境的高并发解决方案**。而且项目中遵循了 **高内聚 低耦合** 设计原则以及使用大量的设计模式来进行架构设计，相信小伙伴学习完此项目后技术会提升几个层次

项目中包括了 **微服务**、**本地缓存/分布式缓存**、**消息队列**、**搜索引擎**、**并发编程**、**本地锁/分布式锁**、**设计模式**、**分库分表** 等核心技术

### 本项目主打的是真实性和独特性，都是根据真实架构和实际遇到的问题总结的

- 本项目对于高并发、高吞吐量的解决方案都是经过真实生产环境验证的，而不是简单的demo而已。而且除了这些以外，还包括了实际开发中项目中的各种架构设计，因为在项目对接的时候，绝不是只是把数据提供给前端就完事了

- 针对实际生产遇到的问题进行**深度定制化的改造**，**是生产中真正用的东西**。这些都是针对业务特点单独设计的，网上也没有现成完整的资料，也不是市面上的理论八股文宝典或者简单的后台管理项目

- 对要毕业的大学生、刚参加工作不久的同学、或者工作多年想对技术有提升的小伙伴都有适合的干货

  - 生产中真正的微服务结构到底什么样
  
  - 实际中项目的接口调用要注意什么
  
  - 入参和出参到底如何应该如何设计 **加密，从而防止攻击**
  
  - **亮点较多的业务** 例如：Redis、Lua、多种数据结构结合
  
  - **并发编程的高级玩法** 例如：线程池的定制化设计、ThreadLocal的深度设计
  
  - **缓存穿透、缓存击穿、缓存雪崩** 在实际项目中的真正落地解决方案
  
  - 对使用中的 **分布式锁、本地锁进行优化** 的实际落地解决方案
  
  - **超高并发情况下，多级缓存、数据一致性** 的设计方案

  - **光是生成订单功能就有4个版本** 这是为了更好的讲解高并发下要如何去考虑，应该逐步优化哪些方面
  
  - **各种参数的精细化配置** 例如：Sentinel、Hystrix、Ribbon的配置

- 项目中主打的就是有**独特性**，包括**让人眼前一亮的业务**。也具有项目的**真实性**、真实生产中遇到的 **重点和难点** 把这些独特的东西体现到简历中和面试官来聊，保证对你印象非常的深刻。这不一下子就甩开了别人了吗

- 哪怕是常见的面试问题，也能从生产上遇到的故障来做**进一步的分析和完善**，比如常见的**分布式锁，细节就很多**。例如：在方法里还是方法外使用？直接Lock.lock就行了吗？事务存在的话需要去考虑吗？如果利用注解那么要考虑其他切面的关系吗？提供哪些策略呢？这些在 **项目中都会得到解答**

- 又比如说 **SpringBoot的自动装配原理**，很多人都背过这个，但究竟用自动装配有什么好处呢？为什么不直接写个组件然后其他项目来依赖呢？两者的区别在哪里呢？在项目中依旧会有讲解

## 技术结构

- 使用了 **SpringCloud+SpringCloudAlibaba** 的微服务结构

- 使用了 **Nacos** 作为注册中心

- 使用 **Redis** 不仅仅作为缓存，还使用了`Lua脚本`/`延迟队列`/`Stream消息队列` 等高级特性

- 引入了 **Kafka** 消息中间件，**SpringBootAdmin** 作为服务的监控通知

- **ELK** 作为日志的记录，**ElasticSearch**提供搜索和展示功能，

- **Sentinel/Hystrix** 作为熔断保护层

- 使用 **ShardingSphere** 实现分库分表，来存储海量的数据

通过以上设计，来实现应对高并发、高吞吐的能力，以及海量数据的存储和服务状态的监控

![](https://multimedia-javaup.cn/%E6%9E%B6%E6%9E%84%E5%9B%BE/%E9%A1%B9%E7%9B%AE%E6%9E%B6%E6%9E%84%E5%9B%BE%28%E5%8E%8B%E7%BC%A9%E5%90%8E%29.jpg)

## 业务结构

通过此业务结构图进一步详细的介绍项目中的功能，包括：**服务配置**、**技术选型**、**核心业务**、**设计组件**、**中间件的使用**、**监控方式**、**高并发解决策略** 等各个方面，方便大家能够对大麦项目的整体架构和设计有一个清晰的认知

![](https://multimedia-javaup.cn/%E6%9E%B6%E6%9E%84%E5%9B%BE/%E9%A1%B9%E7%9B%AE%E4%B8%9A%E5%8A%A1%E7%9A%84%E7%BB%93%E6%9E%84%E5%9B%BE%28%E4%BC%98%E5%8C%96%29.png)

## 技术选型

| 技术                 | 说明               | 官网                                                         |
| -------------------- | ------------------ | ------------------------------------------------------------ |
| Spring-Boot          | Web服务框架        | [https://spring.io/projects/spring-boot](https://spring.io/projects/spring-boot) |
| Spring-Cloud         | 微服务框架         | [https://spring.io/projects/spring-cloud](https://spring.io/projects/spring-cloud) |
| Spring-Cloud-alibaba | alibaba微服务框架  | [https://github.com/alibaba/spring-cloud-alibaba](https://github.com/alibaba/spring-cloud-alibaba) |
| Spring-Cloud-Gateway | 微服务网关         | [https://spring.io/projects/spring-cloud-gateway](https://spring.io/projects/spring-cloud-gateway) |
| Nacos                | 服务注册中心       | [https://nacos.io/zh-cn/index.html](https://nacos.io/zh-cn/index.html) |
| Sentinel             | 服务熔断           | [https://sentinelguard.io/zh-cn/](https://sentinelguard.io/zh-cn/) |
| Log4j2               | 日志框架           | [https://github.com/apache/logging-log4j2](https://github.com/apache/logging-log4j2) |
| Mysql                | 数据库             | [https://www.mysql.com/](https://www.mysql.com/)             |
| MyBatis-Plus         | ORM框架            | [https://baomidou.com](https://baomidou.com)                 |
| MyBatisGenerator     | 数据层代码生成器   | [http://www.mybatis.org/generator/index.html](http://www.mybatis.org/generator/index.html) |
| AJ-Captcha           | 图形验证码         | [https://gitee.com/anji-plus/captcha](https://gitee.com/anji-plus/captcha) |
| Kafka                | 消息队列           | [https://github.com/apache/kafka/](https://github.com/apache/kafka/) |
| Redis                | 分布式缓存         | [https://redis.io/](https://redis.io/)                       |
| Redisson             | 分布式Redis工具    | [https://redisson.org](https://redisson.org)                 |
| Elasticsearch        | 搜索引擎           | [https://github.com/elastic/elasticsearch](https://github.com/elastic/elasticsearch) |
| LogStash             | 日志收集工具       | [https://github.com/elastic/logstash](https://github.com/elastic/logstash) |
| Kibana               | 日志可视化查看工具 | [https://github.com/elastic/kibana](https://github.com/elastic/kibana) |
| Nginx                | 静态资源服务器     | [https://www.nginx.com/](https://www.nginx.com/)             |
| Docker               | 应用容器引擎       | [https://www.docker.com](https://www.docker.com)             |
| Jenkins              | 自动化部署工具     | [https://github.com/jenkinsci/jenkins](https://github.com/jenkinsci/jenkins) |
| Hikari               | 数据库连接池       | [https://github.com/brettwooldridge/HikariCP](https://github.com/brettwooldridge/HikariCP) |
| JWT                  | JWT登录支持        | [https://github.com/jwtk/jjwt](https://github.com/jwtk/jjwt) |
| Lombok               | Java语言增强插件   | [https://github.com/rzwitserloot/lombok](https://github.com/rzwitserloot/lombok) |
| Hutool               | Java工具类库       | [https://github.com/looly/hutool](https://github.com/looly/hutool) |
| Swagger-UI           | API文档生成工具    | [https://github.com/swagger-api/swagger-ui](https://github.com/swagger-api/swagger-ui) |
| Knife4j              | Swagger 增强框架   | [https://doc.xiaominfo.com](https://doc.xiaominfo.com)       |
| Hibernator-Validator | 验证框架           | [http://hibernate.org/validator](http://hibernate.org/validator) |
| XXL-Job              | 分布式定时任务框架 | [http://www.xuxueli.com/xxl-job](http://www.xuxueli.com/xxl-job) |
| ShardingSphere       | 分库分表           | [https://shardingsphere.apache.org](https://shardingsphere.apache.org) |

## 架构和组件设计

针对于分布式和微服务的项目来说，随着业务的发展，项目的数量上千个都是很正常的，但如何要把这些项目做好配置，做好架构设计，设计出组件库，都是要考虑的因素

既然组件库是要给其他服务提供使用，所以在设计时要考虑的细节非常的多，**设计模式和高内聚低耦合的思想更加的重要**，而且代码的健壮性和高效率的执行也是同样重要，而在大麦项目中，使用了SpringBoot的自动装配机制来设计组件库

除了组件库外，还有对**异常的处理、数据的封装格式、多线程的使用等等也都要进行相应的封装设计**，这些在项目中同样具备

![](https://multimedia-javaup.cn/%E6%9E%B6%E6%9E%84%E5%9B%BE/%E7%BB%84%E4%BB%B6%E6%9E%B6%E6%9E%84%E5%9B%BE.png)

## 业务流程

对于大麦项目来说，核心的业务就是用户选择节目然后进行购票功能了，项目中不仅完整了对整个业务流程的完整闭环，而且考虑到既然设计此项目是为了应对高并发的特点，那么在从业务的角度上也做了很多的优化设计

![](https://multimedia-javaup.cn/%E6%9E%B6%E6%9E%84%E5%9B%BE/%E4%B8%9A%E5%8A%A1%E5%9B%BE%E4%BC%98%E5%8C%96.png)	

## 项目的亮点质量高吗

我也是一路过来的，所以很清楚大家的心态，希望是能和实际业务结合起来进行学习项目的架构设计技巧以及解决高并发的解决方案，

#### 这里有一点要注意 就是不要让大家过于的陷入项目中的复杂业务中

一方面其实面试官并不会对你做过的业务太感兴趣，太复杂了也听不懂，**最好是你能把业务和解决问题的能力结合起来给他讲**，

另一方面项目的目的是让你提供技术能力，业务固然重要，但还是希望以后能把学到的技术应用到自己的项目中做到融会贯通。

而本项目争取让小伙伴尽可能快速理解业务的前提下，充分的来提供技术能力，尽量减少不必要的时间和精力

### 项目中的各个亮点部分

项目的亮点可以分成多个部分，比如涉及到用户服务的业务时，项目在海量并发的场景下的问题：

- **用户服务如何设计分库分表，存在用户邮箱、用户手机号多种方式登录，要怎么设计不会发生读扩散问题？**

- **当一瞬间有大量的用户注册请求时，如何防止 缓存穿透问题？网上说的那些方案到底靠谱吗？到底要怎么解决并且不影响用户体验？**

- **用户和购票人数据为了应对高并发的场景下，在缓存中要怎么设计？把所有数据都放进去吗？**

- **如何设计缓存策略？采取哪种结构来存储？采取哪种维度来存储？哪些数据适合放入缓存？哪些不适合？**

  

在用户进行浏览的过程中，对于问题的存在同样也不少：

- **如何应对高并发下的用户查询请求？在主页列表、类型列表、的请求查看下，如何将设计分库分表的数据查询方案？**

- **节目详情要怎么设计缓存？有了Redis就可以了吗？突发性流量激增的问题怎么解决？**

- **如何设计多级缓存来应对几十万，甚至几百万的访问压力？如果发生了缓存雪崩要如何解决和提前预防？**

  

而在用户购票的流程中，为了解决高并发的压力，需要考虑的问题和细节就会更多：

- **如何应对高并发下的用户购票压力？在购票流程中怎么考虑缓存和数据库的交互？**

- **库存数量在缓存中应该如何设计？用户购票和支付过程中，要怎么正确的扣除库存？异常了怎么回滚？数据库中的余票数量一致性要如何解决？**

- **分布式锁使用起来的细节到底有哪些？只要加上一行锁就可以了吗？**

- **高并发下的分布式锁如何进一步的优化？锁的粒度？网络请求的性能？**

- **幂等功能如何实现？有哪些维度需要考虑？**

- **经典的缓存数据库一致性的问题实际生产环境中到底如何解决？直接删除缓存、延迟双删 这些方案到底可行吗？**



而在整个项目的架构设计上，也有很多的问题存在：

- **高并发下订单延迟关闭功能如何实现？使用中间件作为延迟队列的问题？使用redis作为延迟队列可以吗？如何提高性能？**

- **分布式id如何生成？经典的雪花算法？直接使用MybatisPlus中的生成策略可以吗？有什么问题？**

- **订单的分库分表如何设计？既要支持订单详情查询、又要支持订单列表查询而不发生读扩散？**

- **如何执行灵活的限流规则？能支持到某个时间段、某个请求、并能记录下异常行为信息？**

- **项目的架构配置、服务配置、数据结构要如何统一设计和管理？异常如何捕获？**

- ... ... ... ...

这里只是将常见的问题列举了一下，而在本项目中解决的问题远不止上述列举这些，小伙伴可在学习时带着某个问题来思考，在项目中找到问题的答案

## 项目展示

为了尽可能的还原，本项目尽可能贴近官网的页面设计和业务流程，小伙伴可以通过前端项目一边来学习业务，一边体会业务中调取了哪些后端接口，这种学习方式是简单且高效的，也建议小伙伴在学习公司的业务时也使用这种方式

### 主页列表
![](https://multimedia-javaup.cn/%E5%A4%A7%E9%BA%A6%E9%A1%B9%E7%9B%AE%E6%88%AA%E5%9B%BE%2F%E4%B8%BB%E9%A1%B5%E5%88%97%E8%A1%A8.jpg)
### 分类列表
![](https://multimedia-javaup.cn/%E5%A4%A7%E9%BA%A6%E9%A1%B9%E7%9B%AE%E6%88%AA%E5%9B%BE%2F%E5%88%86%E7%B1%BB%E5%88%97%E8%A1%A8.jpg)
### 节目详情
![](https://multimedia-javaup.cn/%E5%A4%A7%E9%BA%A6%E9%A1%B9%E7%9B%AE%E6%88%AA%E5%9B%BE%2F%E8%8A%82%E7%9B%AE%E8%AF%A6%E6%83%85.jpg)
### 生成订单
![](https://multimedia-javaup.cn/%E5%A4%A7%E9%BA%A6%E9%A1%B9%E7%9B%AE%E6%88%AA%E5%9B%BE%2F%E6%8F%90%E4%BA%A4%E8%AE%A2%E5%8D%95.jpg)
### 订单列表
![](https://multimedia-javaup.cn/%E5%A4%A7%E9%BA%A6%E9%A1%B9%E7%9B%AE%E6%88%AA%E5%9B%BE%2F%E8%AE%A2%E5%8D%95%E5%88%97%E8%A1%A8.jpg)
### 订单详情
![](https://multimedia-javaup.cn/%E5%A4%A7%E9%BA%A6%E9%A1%B9%E7%9B%AE%E6%88%AA%E5%9B%BE%2F%E8%AE%A2%E5%8D%95%E8%AF%A6%E6%83%85.jpg)
## 欢迎联系我

小伙伴想要实时关心大麦项目更新的进展情况的话，可以关注公众号：**阿星不是程序员**

在项目中的学习过程中遇到了什么问题或者有哪些建议，欢迎添加本人wx，备注：**大麦**  来领取详细的项目学习资料

<div align=left>
<img src="https://multimedia-javaup.cn/%E4%B8%AA%E4%BA%BA/%E5%BE%AE%E4%BF%A1/javaup01.png#pic_left"  width="20%">
</div>

## 小伙伴的疑惑

#### 学生人群

无论是正在大学中或者是培训机构中的学生，其实对项目的需求更高，可以这么说能不能决定你能正式工作的因素除了学历外就是项目了，既然学历已经成了定局，那么最能提高竞争力的就是 **项目**，如果能在简历和面试中已经有了比较出色的项目经验，那么对于面试官来说绝对是必杀技！

不用担心怕自己看不懂项目，项目的文档和视频讲解非常的详细，分成了 **项目的总体介绍**、**如何启动**、**项目基础介绍**、**项目的架构设计**、**详细业务讲解**、**基础组件讲解**、**设计到的技术讲解**、**深挖细节亮点讲解**，配合 **详细的代码加注释以及解释流程和设计思路并且还结合了流程图** 方便大家更好的理解。学生可根据自己目前学习的进度来跳转到对应的目录来学习

#### 工作了几年的人群

对于这些人群，学习大麦项目更是必要的，通过项目的讲解能学习到项目的架构设计、设计模式、高并发解决方案，来让自己的技术能力得到提高，并且让自己的简历和面试中通过此项目来增加个人亮点

## 提供的服务

**文档数量120+，总字数26W+，加上视频极为细致的讲解，并且还在不断更新中，带你全方位360度无死角彻彻底底掌握项目！**

包括对 **项目从0到1的讲解**、**项目的细节和亮点总结**、**遇到的面试真题**、**详细的各种优化后的压测报告**、**如何将项目有亮点的写到简历上**。并且提供后续的答疑解惑，小伙伴在学习项目时遇到没有理解的问题，或者面试过程中遇到的问题都可以在社区中进行反馈，本人会进行详细的分析解答

## 如何启动项目
- [准备项目启动条件](https://javaup.chat/pages/10e83e/)
- [如何安装项目需要的中间件环境](https://javaup.chat/pages/5d7200/)
- [后端项目部署启动](https://javaup.chat/pages/d5116b/)
- [前端项目部署启动](https://javaup.chat/pages/b7e7ab/)
## 文档和视频目录
![](https://multimedia-javaup.cn/%E6%9E%B6%E6%9E%84%E5%9B%BE/%E6%96%87%E6%A1%A3%E7%9B%AE%E5%BD%95%28%E5%8E%8B%E7%BC%A9%E5%90%8E%29.jpg)

