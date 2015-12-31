# metrics-spring-annotation-3
Metrics3.0 integrate SpringMVC and Metrics-Spring

目前Metrics 已经到了3.0版本，3.0版本工的功能增强了不少，具体信息请看官网http://dropwizard.github.io/metrics/3.1.0/manual/core/，关于metrics2.X系列和metrics3.X系列的历史渊源请看http://ju.outofmemory.cn/entry/105544，作者已经写的很好了，本博客主要是讲解对metircs3.0如何和SpringMVC进行集成及使用。由于是刚刚使用Metrics3.0难免会有很多小问题，如果博客或代码有问题请大家多多指正，共同进步。 如果添加请注明是Metrics学习者，项目的代码我会在附件中上传，博客中为了方便多用图片，请大家谅解。
小提示：最好是从git上下载Metircs3.0的源码看看，因为Metrics3.0和Metircs2.X很多类的路径不一样，但是好多类名都是一样的，这样你改路径也好改，另外源码中有好多自带的测试，对我们的开发很有帮助。

1.项目使用MAVEN进行管理，第一部下载spring倚赖包和metrics3.0以及Metrics-spring 依赖包（虽然metircs3.0有自己的注解，常用的Gauges、Counters、Meters、Timers的使用没什么区别，但是Histograms的注解我只有在引用Metrics-spring 的注解的时候才有效，另外不得不说Metrics3.0提供的Metirc注解使好多注解都变的很方便）还有日志依赖。详见pom.xml文件中
2.配置web.xml 官方介绍的Metircs和servlets集成方式请见http://dropwizard.github.io/metrics/3.1.0/manual/servlets/，但是由于我们用的Metircs3.X的版本，类库和Metrics2.X发生了变化，所以filter部分改成
<filter>
    <filter-name>instrumentedFilter</filter-name>
    <filter-class>com.codahale.metrics.servlet.InstrumentedFilter</filter-class>
    <!--<init-param>-->
        <!--<param-name>name-prefix</param-name>-->
        <!--<param-value>authentication</param-value>-->
    <!--</init-param>-->
</filter>
<servlet>
    <servlet-name>adminServlet</servlet-name>
    <servlet-class>com.codahale.metrics.servlets.AdminServlet</servlet-class>
</servlet>
<filter-mapping>
    <filter-name>instrumentedFilter</filter-name>
    <url-pattern>/*</url-pattern>
</filter-mapping>
当项目启动之后在地址栏如数localhost:<your port>/<部署名称>/metrics/ 就会进入metrics自身的管理页面
类似
Operational Menu
Metrics
Ping
Threads
Healthcheck
点击每一个则进入查看每个内容

 <servlet>
        <servlet-name>springMVC</servlet-name>
        <servlet-class>org.springframework.web.servlet.DispatcherServlet</servlet-class>
        <init-param>
            <param-name>contextConfigLocation</param-name>
            <param-value>classpath*:spring/servlet-context.xml</param-value>
        </init-param>
        <load-on-startup>1</load-on-startup>
    </servlet>
<servlet-mapping>
        <servlet-name>springMVC</servlet-name>
        <url-pattern>/trade/*</url-pattern>
    </servlet-mapping>
项目目前只截取/trade/*的路径。
3.配置Metircs3.0相关的内容
metrics-spring.xml

<bean class="org.springframework.web.context.support.ServletContextAttributeExporter">
    <property name="attributes">
        <map>
            <entry key="com.codahale.metrics.servlet.InstrumentedFilter.registry">
                <ref bean="metricRegistry"/>
            </entry>
            <entry key="com.codahale.metrics.servlets.MetricsServlet.registry">
                <ref bean="metricRegistry"/>
            </entry>
            <entry key="com.codahale.metrics.servlets.HealthCheckServlet.registry">
                <ref bean="healthCheckRegistry"/>
            </entry>
        </map>
    </property>
</bean>
具体Metircs3.0和SpringMVC集成部分请看官网http://dropwizard.github.io/metrics/3.1.0/manual/servlets/（PS:不是很详细）
如果你想添加你自己的metrics-registry和health-check-registry 一定要注意使你的类分别继承MetricRegistry和
HealthCheckRegistry
4.MainController.java 包含了所有的例子，每个例子对应的页面在WEB-INF/jsp下面
五个注解的例子都有，其实还得自己动手运行一下才知道这些到底怎么回事，这片博客的目的主要是介绍Metrics3.X如何集成SpringMVC，那些复杂的Metrics使用我这边还未研究，不过有了基础其余的都好说了。
