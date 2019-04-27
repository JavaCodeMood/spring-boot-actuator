#  SpringBoot监控与管理的实现    
##  认识Actuator
在SpringBoot应用中引入spring-boot-starter-actuator依赖，它可以为开发团队提供系统运行的各项监控指标。      
在项目中引入依赖配置如下： 
   
    <dependency>
           <groupId>org.springframework.boot</groupId>
           <artifactId>spring-boot-starter-actuator</artifactId>
    </dependency>    

application.properties属性配置文件，添加如下配置：   

	#开放所有的web Endpoints
	management.endpoints.web.exposure.include= *
spring-boot-starter-actuator模块根据应用依赖和配置自动创建出来的监控和管理端点。通过这些端点，我们可以实时获取系统应用的各项监控指标。    
访问：http://localhost:8081/actuator     


可以将spring-boot-starter-actuator模块生成的原生端点，分为三大类：  
1. 应用配置类：获取应用程序中加载的应用配置、环境变量、自动化配置报告等与SpringBoot应用密切相关的配置类信息；这类端点可以帮助我们获取一系列关于Spring应用配置内容的详细报告，比如自动化配置的报告、Bean创建的报告、环境属性的报告等。        
2. 度量指标类：获取应用程序运行过程中用于监控的度量指标，比如内存信息、线程池信息、HTTP请求统计等；     
3. 操作监控类：提供了对应用的关闭等操作类功能；   
以上三类端点的详细信息，下面我们详细来说明：


##  端点信息详述
###  应用配置类：默认启用   
1. /autoconfig: 该端点用来获取应用的自动化配置报告，其中包括所有自动化配置的候选项，同时列举了每个候选项是否满足自动化配置的各个先决条件。该端点可以帮助我们方便地找到一些自动化配置为什么没有生效的具体原因。报告内容分为两部分，如下：
   (1)positiveMatches中返回的是条件匹配成功的自动化配置；   
   (2)negativeMathches中返回的是条件匹配不成功的自动化配置。     
     
2. /beans: 该端点用来获取应用上下文中创建的所有Bean。包含的具体信息如下：    
   (1)bean：Bean的名称；    
   (2)scope: Bean的作用域；   
   (3)type: Bean的Java类型；   
   (4)resource: class文件的具体路径；   
   (5)dependencies: 依赖的Bean的名称;       
    
3. /configprops: 该端点用来获取应用中配置的属性信息报告。我们可以通过该报告来看到各个属性的配置路径，比如我们要关闭端点，就可以使用endpoints.configprops.enabled=false来完成设置。   
        
4. /env: 该端点用来获取应用所有可用的环境属性报告，具体包括环境变量、JVM属性、应用的配置属性、命令行中的参数。通过该端点返回的信息，我们可以看到当前应用加载的配置信息，可以结合@ConfigurationProperties注解将它们引入到应用程序中使用。对于一些敏感属性信息，比如在属性名中包含password,secret,key这些关键词，在返回的时候会使用*来替换。   
    
5. /mappings: 该端点用来返回所有Spring MVC的控制器映射关系报告。返回的信息有：     
   (1)bean属性: 标识该映射关系的请求处理器；    
   (2)method属性：标识该映射关系的具体处理类和处理函数；   
     
6. /info:该端点用来返回一些应用自定义的信息。默认情况下，该端点只会返回一个空的JSON内容。我们可以在application.properties配置文件中设置一些以info为前缀的属性配置信息，就能看到效果。    
    


###   度量指标类：默认启用     
1. /metrics: 该端点用来返回当前应用的各类重要度量指标，比如内存信息、线程信息、垃圾回收信息等。具体信息包含如下：    
   (1)系统信息：包括处理器数量processors、运行时间uptime和instance.uptime、系统平均负载systemload.average;     
   (2)mem.*: 内存概要信息，包括分配给应用的总内存数量以及当前空闲的内存数量，这些信息来自java.lang.Runtime;     
   (3)heap.*: 堆内存使用情况。这些信息来自java.lang.management.MemoryMXBean接口中getHeapMemoryUsage方法获取的java.lang.management.MemoryUsage;    
   (4)nonheap.*: 非堆内存使用情况。这些信息来自java.lang.management.MemoryMXBean接口中getNonHeapMemoryUsage方法获取java.lang.management.MemoryUsage;     
   (5)threads.*: 线程使用情况，包括线程数、守护线程（daemon)、线程峰值（peak)等，这些信息来自java.lang.management.ThreadMXBean;    
   (6)classes.*: 应用加载和卸载的类统计，这些信息来自java.lang.managemeng.ClassLoadingMXBean;     
   (7)gc.*: 垃圾收集器的详细信息，包括垃圾回收次数gc.ps_scavenge.count、垃圾回收消耗时间gc.ps_scavenge.time、标记-清除算法的次数gc.ps_marksweep.count、标记-清除算法的消耗时间gc.ps_marksweep.time。这些信息来自java.lang.management.GarbageCollectorMXBean;    
   (8)httpsessions.*: Tomcat容器的会话使用情况。包括最大会话数httpsessions.max和活跃会话数httpsessions.active。该度量指标信息仅在引入嵌入式Tomcat作为应用容器的时候才会提供；    
   (9)gauge.*: HTTP请求的性能指标之一，它主要用来反映一个绝对值。    
   (10)counter.*: HTTP请求的性能指标之一，它主要作为计数器来使用，记录了增加量和减少量。       
    
2. /health: 该端点用来获取应用的各类健康指标信息。在spring-boot-starter-actuator模块中自带实现了一些常用资源的健康指标检测器，这些健康指标检测器都是通过HealthIndicator接口实现，并且会根据依赖关系的引入实现自动化配置。常见的一些健康指标检测器如下：    
   (1)DiskSpaceHealthIndicator: 低磁盘空间检测；    
   (2)DataSourceHealthIndicator: 检测DataSource的链接是否成功；    
   (3)MongoHealthIndicator: 检测Mongo数据库是否可用；     
   (4)RabbitHealthIndicator: 检测Rabbit服务器是否可用；   
   (5)RedisHealthIndicator: 检测Redis服务器是否可用；   
   (6)SolrHealthIndicator: 检测Solr服务器是否可用；   
   我们也可用通过实现HealthIndicator接口来自定义自己的健康指标检测器；         
   
3. /dump: 该端点用来暴露程序运行中的线程信息。它使用java.lang.management.ThreadMXBean的dumpAllThreads方法来返回所有含有同步信息的活动线程详情；  
  
4. /trace: 该端点用来返回基本的HTTP跟踪信息。默认情况下，跟踪信息的存储采用org.springframework.boot.actuate.trace.InMemoryTraceRepository实现的内存方式，始终保留最近的100条请求记录。     


###  操作控制类：需要通过属性配置来开启操作    
1. /shutdown: 该端点用来实现关闭应用的远程操作，需要配置属性endpoints.shutdown.enable=true才能实现。该操作非常危险，不建议使用。     


到此关于spring-boot-starter-actuator模块的监控与管理的相关端点信息就介绍完了、
参考资料： 《Spring Cloud微服务实战》 翟永超 著















