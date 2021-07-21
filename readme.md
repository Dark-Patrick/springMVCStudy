# SSM整合开发设计思路
##框架分工
用户发起请求 -> SpringMVC接收 -> Spring中的Service对象处理业务逻辑 -> Mybatis处理数据 -> SpringMVC把请求的结果交给用户

SSM整合也叫做SSI(IBatis)，整合中有容器
1. 第一个容器SpringMVC，管理Controller控制器对象
2. 第二个容器Spring，管理Service，Dao和工具类

我们要做的是把使用的对象交给合适的容器创建，管理。把Controller还有Web开发的相关对象交给SpringMVC容器。Service，Dao对象等定义在Spring的配置文件中。

SpringMVC容器和Spring容器是有关系的，该关系已经确定好：MVC容器是Spring容器的子容器，类似java中的继承。子可以访问父的内容，在子容器的Controller
可以访问父容器中的Service对象，就可以实现controller使用service对象。

## 实现步骤
0. 使用springdb的mysql库，表使用student
1. 新建maven web项目
2. 加入依赖：SpringMVC，Spring，MyBatis三个框架的依赖；Jackson依赖，mysql驱动，druid连接池，jsp，servlet依赖
3. 写web.xml
    - 注册DispatcherServlet，目的：创建SpringMVC容器对象，才能创建Controller类对象；创建Servlet，接收用户的请求
    - 注册Spring的监听器，ContextLoaderListener，目的：创建Spring容器对象，才能创建Service，Dao等对象
    - 注册字符集过滤器，解决post乱码问题
4. 创建包，controller包，service，dao，实体类等包名
5. 写SpringMVC，Spring，MyBatis的配置文件
    - SpringMVC配置文件
    - Spring配置文件
    - MyBatis主配置文件
    - 数据库的属性配置文件
6. 业务的实现

## 异常处理
SpringMVC框架采用的是统一，全局的异常处理
，把controller中的所有异常处理都集中到一个地方。采用的是aop的思想。把业务逻辑和异常处理代码解耦

使用两个注解：
- @ExceptionHandler
- @ControllerAdvice

异常处理步骤：
1. 新建一个自定义异常类，再定义它的子类
2. 创建一个普通类作为全局异常处理类
    - 在类的上面加入@ControllerAdvice
    - 在类中定义方法，方法的上面加入@ExceptionHandler
3. 在controller抛出异常
4. 创建处理异常的视图页面
5. 创建SpringMVC配置文件
    - 组件扫描器，扫描@Controller注解
    - 组件扫描器，扫描@ConteollerAdvice所在包名
    - 声明注解驱动
    
## 拦截器
拦截器创建步骤：
1. 创建一个普通类
    - 实现HandlerInterceptor接口
    - 实现接口中的三个方法
2. 创建视图页面
3. 创建SpringMVC配置文件
    - 组件扫描器，扫描@Controller注解
    - 声明拦截器，并指定拦截的请发uri地址
    
过滤器和拦截器的区别：
1. 过滤器是servlet中的对象，拦截器是框架中的对象
2. 过滤器实现Filter接口的对象，拦截器是实现HandlerInterceptor
3. 过滤器是用来设置request，response的参数，属性的，侧重对数据过滤，拦截器是用来验证请求的，能截断请求
4. 过滤器是在拦截器之前先执行的
5. 过滤器是tomcat服务器创建的对象，拦截器是SpringMVC容器中创建的对象
6. 过滤器有一个执行点，拦截器有三个执行时间点
7. 过滤器可以处理jsp，js，html等等；拦截器侧重对controller对象的拦截，如果请求不能被DispatcherServlet接收
，这个请求不会执行拦截器内容
8. 拦截器拦截普通类方法执行，过滤器过滤servlet请求响应

问题与理解：
1. MVC无法拦截根目录下index.html问题

若在web.xml中加入
```xml
    <servlet-mapping>
        <servlet-name>default</servlet-name>
        <url-pattern>*.html</url-pattern>
    </servlet-mapping>
```
那么与html相关的静态资源都会交给tomcat管理而不经过mvc的中央调度器。那么会造成问题，如果mvc需要实现拦截器，那么将无法拦截
html相关的静态资源，因其不由中央调度器管理。

所以，不推荐使用以上方法将html交给tomcat管理，要将静态资源尽可能打包统一管理，不要放在根目录下。或使用
```xml
<mvc:resources mapping="/**" location="/"/>
```
拿到所有静态资源，同时可解决index.html无法访问的问题。

或者使用index.jsp：jsp非静态资源，且SpringMVC框架不会拦截根目录下的jsp

## SpringMVC内部处理请求的处理流程（重要）
1. 用户发起请求some.do
2. DispatcherServlet接收请求some.do，把请求转交给处理器的映射器

    - 处理器映射器：springmvc框架中的一种对象，框架把实现了HandlerMapping接口的类都叫映射器（多个）
    - 处理映射器作用：根据请求，从springmvc容器中获取处理器对象（MyController c = ctx.getBean(some.do)）。
    框架把找到的对象放到一个叫处理器执行链的类中保存（HandlerExecutionChain）
    - HandlerExecutionChain类中保存着
        1. 处理器对象（MyController）
        2. 项目中所有的拦截器（List<HandlerInterceptor> interceptorList
        
    **方法调用**：HandlerExecutionChain mappedHandler = getHandler(processedRequest)
3. DispatcherServlet把HandlerExecutionChain中的处理器对象交给了处理器适配器对象（多个）
    - 处理器适配器：springmvc框架中的对象，需要实现HandlerAdapter接口
    - 处理器适配器作用：执行处理器方法（调用MyController.doSome()方法，得到返回值ModelAndView）
    
    **方法调用**：HandlerAdapter ha = getHandlerAdapter(mappedHandler.getHandler());
    
    **执行处理器方法**：mv = ha.handle(processedRequest,response, mappedHandler.getHandler());
4. DispatcherServlet把 3 中获取的ModelAndView交给了视图解析器对象
    - 视图解析器：springmvc框架中的对象，需要实现ViewResolver接口（多个）
    - 视图解析器作用：组成完整路径，并创建View对象，View是一个接口，表示视图，在框架中jsp，html不是string表示，
    而是使用View和它的实现类表示视图。
    
    InternalResourceView：视图类，表示jsp文件，视图解析器会创建InternalResourceView类的对象，对象中有属性url=/WEB-INF/jsp/a.jsp
5. DispatcherServlet把 4 中创建的View对象获取到，调用View类自己的方法，把Model数据放到request作用域。
执行对象视图的forward。请求结束
