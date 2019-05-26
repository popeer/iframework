This project is an interface testing platform for internet domain. 1. support RPC & Http(s) protocol interface.2. support scenario interface testing, which resolves a problem of transferring values of parameters and results of interfaces, and also supply a way of verifying dynamic results. 3. support main current DB such as MySQL、SqlServer、pg,etc.

中文手册：https://blog.csdn.net/popeer/article/details/82857134 

项目用途：解决互联网接口测试数据依赖的通用测试框架。
说明：
接口测试用例的接口测试对象是接口，接口的主要特征是它的入参和返回值。
接口的入参实际上就是一条数据，这条数据需要具备时效性和正确性，入参是保证接口正确执行的前置条件。
接口入参的正确性是指它的数据格式、数据本身及它所存在的位置都是符合接口业务逻辑、技术设计所规定的数据格式即string类型就不要是true or false, 一个特定的类类型，就不要是其他类类型；
数据本身：顾名思义，具体来讲入参期望是123456字符串，但只传了12345或 654321。
数据存在的位置：期望这条数据是在数据库里，或是在缓存里。弱不存在，或存在的位置不对，那么接口在执行过程中从期望位置取不到值，接口就不会返回期望的正确结果。
数据的时效性：指接口用例在当前运行时该接口的入参数据是可用的。必入用户登录成功后获取的token值是用户登录成功的令牌，这个令牌有时效性，令牌过期了即代表失效，再用这个token值做身份验证，一定会失败。所以需要重新获取。像这样入参数据时效性要在用例每次执行时都需要获取一次最新值。
入参数据的正确性和实效性的保证就需要在用例里增加调用相应的接口货访问数据库的步骤。这些步骤就是被测接口的前置条件，构造不同接口的前置条件的复杂度都可能不一致，有的只需要一步，有的需要多个步骤，并且还需要对某步步骤里的数据做特殊处理或业务自定义的操作，才能构造出被测接口所期望的入参数据。
这里的特殊处理即对 数据做构造。举例来讲，把json格式的数据里某个特值key的值做替换，若value期望是一个guid，那么久声称一个guid来替换这个特定key的值。弱value期望是一个手机号或者邮箱，那么随机生成一个手机号或者邮箱，这种场景比较常见，接口执行一个符合格式的心的数据，弱势老数据，接口就不会返回期望的值。互联网应用这种使用新邮箱和手机号的场景很常见。
业务自定义处理：是指数据按照产品或业务先自身特有的义务逻辑进行处理，这种处理不具有普遍性和通用性，是非常个性化的数据处理逻辑。
这样一个测试用例要执行的对象是接口d,但由于d需要另外三个接口(a、b、c)执行后并获取者三个接口返回结果或接口参数中的部分特定值、全部值，才能正常执行接口D。
这种多个接口顺序执行来完成一个测试用例的执行，就是测试用例接口执行之间的关联性。互联网应用的测试用例往往都是场景化的用例。


使用说明：
1.	部署说明
1.1.	git地址.
1.2.	jdk版本
部署机器需要安装jdk1.8,(jdk1.8.0_131)并在tomcat的catelina.sh里增加export JAVA_HOME=/usr/local/jdk1.8.0_131/
1.3.	打包方法
rebuild+mvn package, 生成target的lib包，依赖包，classes文件夹。
1.4.	用例存放地址
存在在/opt/iframework/testCases，测试用例用文件夹来区分、管理
1.5.	部署路径
路径是/opt/iframework/,

2.	调用框架执行接口测试的原理
2.1.	 调用原理说明
2.1.1.	调用说明
	调用框架执行用例是通过http的get请求，url里前缀是ip+端口+标示+参数的形式，即http://ip:port/iframework/irun? 。如：机器部署在172.18.5.30上，端口号8888，那么调用的url前缀就是http://172.18.5.30:8888/iframework/irun?
2.1.2.	原理说明
框架的核心功能都以dubbo协议来暴露接口，注册中心在测试环境的zookeeper上。其中IRemoteServer接口是负责解析调用框架请求的，暴露成rest形式，目的是方便对框架的调用来执行测试用例。
2.2.	 调用参数信息
参数名
是否必填
参数说明
path
是
测试用例的xml的绝对路径。 路径放在/opt/iframework/testCases下，调用路径就是
ip:port/iframework/irun?path=/opt/iframework/testCases/CIA/Authentication/ciaAuthBatch1.xml, 如果不指定具体的xml，只是指定了某个文件夹，那么就会跑这个文件夹下及子文件夹下全部的用例。
dburl
否
数据库连接字符串，如果你的用例需要访问数据库，可以通过这个参数来指定，一旦指定dburl，那么就会替换测试用例里的dburl；如果不指定，会指定用例里写的dburl
uid
否
数据库访问的用户名
pwd
否
数据库访问的密码
dn
否
域名，url里一旦带有这个参数，就会替换测试用例里的url前缀，如http://a.qa.com、http://b.qa.com。
注意：如果用例里没有写明前缀，那么请求参数里需要带有dn参数，这个dn会和用例里的path进行拼接成完整的url；
假设用例里没有写明前缀，如果请求参数里不传dn参数，用例会报错，因为找不到用例的url.

2.3.	

3.	用例的执行过程
1）框架解析调用的参数，举例172.18.5.30:8888/iframework/irun?path=/opt/iframework/testCases/CIA/Authentication/ciaAuthBatch1.xml 通过这个请求，框架得知，要执行的用例是/opt/iframework/testCases/CIA/Authentication/ciaAuthBatch1.xml
2) 解析这个xml，转成框架定义的测试用例对象testCaseNode
3) 按testCaseNode定义的step顺序，顺序执行每个step，即每个接口。
4）每个step的执行过程是，先解析step里的url和请求参数，然后调用httpcient来发送你的请求，再然后对请求的返回结果做验证，验证的逻辑是写在step里的Response/Expect下的Parameter，最后是把用例执行结果存到数据库中。
5) 每个step执行结果都会添加到StringBuilder里，目的是在xml里用例都跑完之后，把完整结果返回给前端。
6) 用例执行完毕，会换行打印Finish！！！字样，如果执行异常，会把异常堆栈信息返回出来。

4.	用例的组织形式
4.1
¥	测试用例都是xml，写好xml放在部署机器上，执行调用时指定好用例的存放路径，即可执行你刚写的测试用例，不需要程序重新打包。
¥	组织用例首先按业务线名称来区分，好比第一级文件夹名称统一是testCases，第二级就是各业务线的名字，如bia, cenc,cia, tplus, 第三级就是业务线下各模块或各feature,如cia下的Authentication、login、administrator。还可以再多级，根据业务线特点来区分吧。
¥	注意一点，如果某一个模块下有10个接口可以单独执行，不需要过多依赖其它接口，那么建议统一放到一个xml文件里，文件名建议是batch1为结尾，方便其它人知道这是一个批量执行单接口的测试用例。
¥	如果是测一个场景化的接口测试用例，即需要2个以上的接口才能完成最终的测试场景，建议把这些放到一个xml里。 

¥	注意： 测试用例支持数据与用例分离，即一份测试用例可以跑多组数据，数据都放到配置文件里，即/src/main/resources/properties/下某个自定义的配置文件里，也可以放到csv文件里。

4.2 针对接口测试不同业务场景的详细设计

4.2.1 测试用例参数化

  4.2.1.1 excel作为接口参数的数据源，接口结果验证值也在excel数据源里，excel每一行都代表用例的一组测试数据，一组测试数组包含接口参数和该记录的期望返回值。
dataPathName指明excel数据源的文件位置。
excelExpectParameterColName指明excel里的某列，该列作用是该条记录的期望返回值。

<TestCaseNode name="aaa" dataPathName="excel:testCases/exceldata/TICE.csv">
<Step name="bbb" rule="get" url="https://a.b.com/internal/note" >
    <Request>
        <Parameter name="appId" colName="appId" />
        <Parameter name="orgId" colName="orgId" sequence="0" keyword="orgId"/>
    </Request>
    <Response>
        <Expect>
            <Parameter excelFlag="true" excelExpectParameterColName="org_Trial"/>
        </Expect>
    </Response>
</Step>

4.2.2 接口参数从之前步骤里提取 
	4.2.2.1 从指定步中取特定关键字的值

<Step name="step2_api/appManager" url="http://a.b.com/api/appManager">
    <Request>
        <Parameter name="access_token" value="#access_token#" sequence="1"/>
    </Request>
</Step>

	4.2.2.2使用sequence_type_keyword_key和offlineFlag获取指定步骤某值去替换数组指定值.  random%email% 生产随机邮件名称。
<Step name="step2_createUserJoinOrgAuthApp" url="http://a.b.com/create">
    <Request>
        <Parameter name="param" offlineFlag="1" sequence_type_keyword_key="1___json___userId___userId" value="{&quot;userId&quot;:&quot;fakeduserId&quot;,&quot;email&quot;:&quot;random%email%&quot;}"/>
    </Request>
</Step>

	4.2.2.3使用storeParam自定义标签存储参数的值
<Step name="step3_internal_api/v1/Code" url="http://a.b.com /emailCode">
    <Request>
        <Parameter name="email" value="random%email%" storeParam="email" alias="email" type="string" />
    </Request>
</Step>


	4.2.2.4使用自定义标签param_type_key获取之前步骤里存储的参数值
<Step name="step1_special_api/bind" url="http://a.b.com/ user/bind">
    <Request>
        <Parameter name="bindingInfo"   sequence_type_keyword_key="0___json___userId___userId" 
offlineFlag="1"                   
storeParam="email" 
alias="email2" 
type="json"                   value="{&quot;userId&quot;:&quot;fakedUserId&quot;}"/>
    </Request>
</Step>
<Step name="step2_internal_api/v1/Code" url="http://a.b.com/Code">
    <Request>
        <Parameter name="email" value="emailIdentify" param_type_key="string___email2___emailIdentify" />
    </Request>
    </Step>


	4.2.2.5获取多个存储的参数值，以及获取多个接口返回值中的部分值
<Step name="step7_/active" url="http://a.b.com/internal_api/active">
    <Request>
        <Parameter name="activeInfo" offlineFlag="1"
                   sequence_type_keyword_key="2___json___registCode___oldIdentifyCode,4___json___registCode___newIdentifyCode"
               param_type_key="json___email___newIdentify,json___email2___oldIdentify"
                   value="{&quot;oldIdentify&quot;:&quot;fakedOldIdentify&quot;,&quot;newIdentify&quot;:&quot;fakedMail&quot;,&quot;oldIdentifyCode&quot;:&quot;&quot;,&quot;newIdentifyCode&quot;:&quot;&quot;}" />
    </Request>
</Step>


	4.2.2.6从多步里取值。从第0步和第2步取值。
<Parameter name="entUserInfo" sequence="0_2" value="$regenteruserwithmobileandssologin$" keyword="registCode"/>

4.2.2.7使用xpath来对接口结果返回的json格式特定内容的读取
<Parameter name="parentId" sequence_type_resultPath_valuePath="1___json_string___$.deptList[0].deptId___parentId" type="string"
           value="fakedDeptId"/>


4.2.3 数据库相关
4.2.3.1 读数据库，返回指定列值
<Step name="step1_getActiveCodeByUserIdFromDB" dburl="jdbc:postgresql://1.1.1.1:5432/boss_test_jc" uid="boss" pwd="1111">
    <Request>
        <Parameter name="sqltext" value="SELECT  active_code from boss.boss_user_active where user_id='61000375529' order by create_time desc limit 1" sqlSelectField="active_code" />
    </Request>
    <Response>
        <Expect>
            <Parameter path="String___str" value="548223" action="equal" type="string"/>
        </Expect>
    </Response>
</Step>

4.2.3.2 读数据库，读指定结果里的值做为数据库sql的参数来查询结果
<Step name="step2_activeCode" dburl="jdbc:postgresql://1.1.1.1:5432/boss_test_jc" uid="boss" pwd="1111">
    <Request>
        <Parameter name="sqltext" value="SELECT active_code from boss.boss_user_active where user_id='{0}'" sequence="1" keyword="userId" sqlSelectField="active_code" />
    </Request>
</Step>

4.2.3.3读数据库，读上n步接口参数里的值做为数据库sql查询参数
<Parameter name="sqltext" value="SELECT active_code from boss.boss_org_user_request where mobile='{0}'"
           sqlSelectField="active_code"
           param_type_key="string___identify___{0}" />

4.2.3.4 执行sql脚本来完成对数据库的操作
<Parameter name="sqltext1" sqlScript="testCases/CIA/sqlScripts/sqlScript1" />

4.24 结果值验证
4.2.4.1对动态结果值的验证
<Step name="finduser" url="https://a.b.com/user" rule="get">
    <Response>
        <Expect>
            <Parameter path="String___result" value="true" action="equal" type="string" />
            <Parameter path="Array___aaa___0,Array___bbb___0,String___ccc" value="自动化测试项目" action="equal" type="string" />
            <Parameter path="Array___aaa___0,String___userId" value=""
                       sequence_type_keyword="1___json___userId" action="equal" type="string" />
        </Expect>
    </Response>
</Step>


4.2.4.2非空验证
<Parameter path="String___auth_code" action="notnull" type="string" />

4.2.4.3 string字符串比较验证
	<Parameter path="String___auth_result" value="true" action="equal" type="string" />

   4.2.4.4对数组的验证
	<Parameter path="Array___appManagers" value="#userId#" keyword="userId" action="containItem" type="string" sequence="0" />
	
   4.2.4.5对json里string类型的验证
	<Parameter path="Obj___Context,String___ReportSearchType" value="Advance" action="equal" type="string" />
	
    4.2.4.6对json格式里数组类型含有元素个数的验证
	<Parameter path="Obj___Context,Array___ViewOpts" value="1" action="equal" type="array" />
    
    4.2.4.7对json格式里的对象数组里某对象元素的验证，注意此例子的type=obj
	<Parameter path="Obj___Layout,Obj___ReportHeader, Array___CarriedItems" value="{&quot;FieldName&quot;:&quot;voucherdate&quot;,&quot;Title&quot;:&quot;单据日期&quot;,&quot;Value&quot;:&quot;2017.07.01 - 2017.07.31&quot;,&quot;Width&quot;:250}" action="contain" type="obj" />
	
    4.2.4.8对json格式里数组里某数组元素的验证，注意此例子的type=array
	<Parameter path="Obj___DTO,Obj___PU_PurchaseOrderSumRpt, Array___columns" value="[&quot;PartnerCode&quot;,&quot;PartnerName&quot;,&quot;BaseQuantity&quot;,&quot;OrigDiscountAmount&quot;,&quot;OrigTax&quot;,&quot;OrigTaxAmount&quot;,&quot;OrigDiscount&quot;,&quot;sumbasequantity&quot;,&quot;GroupLevel&quot;,&quot;rowType&quot;,&quot;reportRowType&quot;]" action="contain" type="array" />
	或：
	<Parameter path="Obj___DTO,Obj___PU_PurchaseOrderSumRpt, Array___Rows" value="[[&quot;&quot;,&quot;&quot;,&quot;&quot;,&quot;&quot;,&quot;&quot;,&quot;&quot;,&quot;&quot;,&quot;&quot;,&quot;999&quot;,&quot;T&quot;,null]]" action="contain" type="array" />

4.2.4.9接口是void时(不返回值)时使用voidReturn="true"不对结果做验证
<Step name="step5_SendMessage"
      voidReturn="true" url="http://ip/path/SendMessage">
    <Request>
        <Parameter name="_args" value="1" />
    </Request>
</Step>

4.2.4.10使用jsonArray保留字作为返回结果类型为对象数组时的验证

<Step name="aaa" rule="GET" action="setheader" headerInit="0___JSON___Authorization___Authorization" url="https://domain/period">
  <Request></Request>
  <Response>
    <Expect>
      <Parameter path="jsonArray___0,String___shuijin" value="" action="notnull" type="string"/>
    </Expect>
  </Response>
</Step>


4.8.4.11使用arraystring保留字处理返回结果类型为字符串数组时的验证
<Step name="aaa" rule="GET" action="setheader" headerInit="0___JSON___Authorization___Authorization" url="https://domain/period">
  <Request></Request>
  <Response>
    <Expect>
      <Parameter path="ArrayString___0" value="true" action="equal" type="string"/>
    </Expect>
  </Response>
</Step>


4.2.5 对参数做特殊处理
4.2.5.1使用keyword给特定数值内的值做替换
Step name="aaa" url="http://a.b.com/active">
    <Request>
        <Parameter name="entUserInfo" value="$entUserInfo$" keyword="mobile" sequence="0"/>
           </Request>
</Step>

4.2.5.2 使用offlineFlag标签来声明对value里的值做自定义处理
	<Step name="aaa" url="http://a.b.com/sendMobile">
    <Request>
        <Parameter name="sendInfo" offlineFlag="1" sequence_type_keyword_key="0___json___userId___userId"           value="{&quot;mobile&quot;:&quot;random%mobile%&quot;,&quot;userId&quot;:&quot;fakedUserId&quot; }"/>
    </Request>
</Step>

4.2.5.3 对callback的处理
<Step name="step0_authorizeByJsonp" url="https://a.b.com/authorizeByJsonp" >
    <Request>
        <Parameter name="callback" value="jsonp%callback%" />
    </Request>
</Step>

4.2.5.4  对参数需要做自定义处理
	／／生成手机号
        <Parameter name="mobile" value="random%mobile%" />
	／／需对value解密
	<Parameter name="appKey" value="12121" action="decrypt" />
	//做md5操作
	<Parameter name="password" value="111111" action="md5" />
	//随机生成15位数字
	<Parameter name="authCode" value="random%string15%"/>

4.2.5.5对于参数为数组类型的支持
<Step name="step4/syncList" url="http://a.b.com/syncList">
    <Request>
        <Parameter name="subscribeList" offlineFlag="1" type="array"
                   sequence_type_keyword_key="0___json___orgId___orgId,0___json___userId___userId"
                   value="[{&quot;orgId&quot;:&quot;special#orgId#&quot;, &quot;userId&quot;:&quot;fakedUserId&quot;,&quot;authItems&quot;:{&quot;version&quot;:1,&quot;invoice&quot;:20},&quot;description&quot;:&quot;descxxxx&quot;}]" />
    </Request>
</Step>


4.2.5.6 读配置文件里定义的字符串来做自定义处理
<Parameter name="entUserInfo" value="$register$" keyword="mobile" sequence="0" storeParam="email" alias="email" type="json" />

4.8.5.7 不指定关键字的情况下，直接从某部接口结果里获取对应值来替换当前参数的值
<Parameter name="orgId" value="#orgId#" sequence="0"/>

   4.8.5.8 对参数为数组类型的参数赋值
<Parameter name="subscribeList" sequence="0" keyword="orgId" type="array" value="$syncsubscribelist$" />

4.8.5.9 参数是嵌套json：
<Step name="#2" url="https://domain/rest"
      groovyMethodName="getAuthSid"
      headerInit="0___jsonobject___Value___Value___External/aaa.groovy"
      parameterType="" action="setheader" >   
    </Request>

4.2.6 对http请求的支持
rest接口包括http和https两种形式，http含有get、post、put、delete、head、
根据HTTP标准，HTTP请求可以使用多种请求方法。
HTTP1.0定义了三种请求方法： GET, POST 和 HEAD方法。
HTTP1.1新增了五种请求方法：OPTIONS, PUT, DELETE, TRACE 和 CONNECT 方法。

GET     请求指定的页面信息，并返回实体主体。
POST     向指定资源提交数据进行处理请求（例如提交表单或者上传文件）。数据被包含在请求体中。POST请求可能会导致新的资源的建立和/或已有资源的修改。
PUT     从客户端向服务器传送的数据取代指定的文档的内容。
DELETE      请求服务器删除指定的页面。
我们对四种最常见的请求方法做了支持，即在action里指明get, post, put,delete 即可实现对应协议的执行:
如对put的支持
	<Step name="step_1" action="PUT" url="https://domain/add"></Step>

对delete的支持
      <Step name="step_1" action="DELETE" url="https://domain/GetInfo"></Step>
http的header和cookie也是接口设计中常用的设置，所以也需要增加对其的支持。
支持如下:
4.2.6.1 设置header
<Step name="aaa" url="http://aa.com/queryCcpStatus" action="setheader" headers="Content-Type:application/json;charset:UTF-8;Accept-Encoding: gzip, deflate;Accept: application/json, text/plain, */*">

4.2.6.2 header需要参数来初始化的情况：
<Step name="aaa" rule="post" action="setheader" headerInit="2___String___Authorization" url="http://ip/api/action">

4.2.6.3获取cookie
<Step name="aaa" url="http://1.1.1.1/a.ashx?method=GetVersionType" action="getcookie">

      4.8.6.4设置cookie
<Step name="step_getType" url="http://ip/getType" action="setcookie_0">

      4.8.6.5使用exchangeFlag="true"和step里的action="singlekey"对接口参数没有name，Content-Type为application/json的场景：
      <Step name="step0_sample" url=http://ip/finance/data action="singlekey">
    <Request>
        <Parameter name="paramvalue" value="{&quot;cardNo&quot;:&quot;111&quot}" exchangeFlag="true" />
    </Request>

4.2.6.6	header初始化：
<Step name="step_1" action="setheader"
      headerInit="0___JSON___Auth___Authorization"
      url="https://domain/GetInfo">
</Step>


4.2.7	对rpc调用的接口，测试用例使用type="jar"表明是jar类型的调用。
4.2.7.1	对jar包里的类类型的接口含有构造函数的支持：
clsName指明要实例化的类；parameterType指明构造函数的类型、参数值、次序；method指明要调用的接口名称。
<Step type="jar" name="aaa" clsName="com.company.thrift.serviceImpl" method="methodName" parameterType="String___abc,Int___8 " >
    <Request>
        <Parameter type="string" name="id" value="999" />
    <Response>
        <Expect>
            <Parameter path="" value="" action="" type=""/>
        </Expect>
    </Response>
</Step>

4.2.7.2	当参数是一个自定义的类类型时，
<Request>
    <Parameter type="custom" customerClass="com.company.thrift.serviceImpl " value="in" jarparam_keyvalue="setAppKey___aaa___string "/>
</Request>


5.	用例的写法
5.1.	 基础写法:
1.	Step里的url代表接口的地址。
2.	Step里的name用于区分用例。
3.	rule代表请求是get还是post
4.	action是对header、cookie的get或set
5.	在Step里的Request的Parameter里写请求参数。key是url参数的key， value是参数的值，这个值可以放到csv文件里exceldata文件夹里，也可以放到配置文件里resources/.propertities。
6.	可对在Step里的Request的Parameter的value值可做几种扩展，有action，sequence，colName，keyword，typesqlScript。分别代表：
6.1.1.	 action: action目前可做对value值的md5, 但是功能上可以扩展。
6.1.2.	sequence: 指定要读取之前结果集中特定步骤的结果。
6.1.3.	colName: 对应excel/csv参数文件里的列名。
6.1.4.	keyword: 与sequence配合，读取sequence指定返回结果集中某个结果中名称与keyword同名的对应的值。
6.1.5.	 type: 指明从结果集合中获取值的类型，可以是string, array,
6.1.6.	sqlScript:用于对数据库操作时候使用，用于指定sql脚本的路径，如<Parameter name="sqltext1" sqlScript="testCases/CIA/sqlScripts/sqlScript1" />，而value可以直接是一个sql语句，如：<Parameter name="sqltext2" value="SELECT * FROM QA.USER" />。

5.2 一些特殊场景的应用：
1.	对手机号的支持： 
◆	动态生成手机号：在参数里含有这个特殊标记random%mobile%，代表框架会自动生成一个11位手机号
◆	获取上一步接口使用的手机号：get%mobile%
		2. 生成指定长度的随机数：
◆	生成8位长度的数字和字母： random%string8%			
◆	生成15位长度的数字和字母： random%string15%			
		3. 获取上一步返回的特殊值：
◆	获取短信验证码  special#registCode#
◆	获取userId         special#userId#
◆	获取orgid           special#orgId#

		
5.3 对结果的验证：
	主要是对返回类型为json的验证，验证逻辑是：
◆	当json为json object时，path以obj开头(因为obj代表object)，然后加三个下划线___, 后面跟json的关键字；如
			     {
    "Context": {
        "ReportSearchType": "Advance",
        "MenuCode": "PU6002"
    }
}
如果是要验证ReportSearchType的值是否为Advance，那么验证这么写：<Parameter path="Obj___Context,String___ReportSearchType" value="Advance" action="equal" type="string" />
		
◆	当要验证json里的数组的个数，如验证下面json里的CarriedItems个数，那么验证逻辑这么写：
				<Parameter path="Obj___Layout,Obj___ReportHeader, Array___CarriedItems" value="3" action="equal" type="array" />

				{
    "Layout": {
        "ReportHeader": {
            "SearchItems": [],
            "CarriedItems": [
                {
                    "FieldName": "voucherdate",
                    "Title": "单据日期",
                    "Value": "2017.07.01 - 2017.07.31",
                    "Width": 250
                },
                {
                    "FieldName": "PartnerCode",
                    "Title": "供应商",
                    "Value": "",
                    "Width": 250
                },
                {
                    "FieldName": "inventoryCode",
                    "Title": "存货",
                    "Value": "",
                    "Width": 250
                }
            ]
        }
    }
}
◆	当要验证的json是数组里的每个元素，比如验证上面json的CarriedItems里的三个元素，验证逻辑就这么写：
		<Parameter path="Obj___Layout,Obj___ReportHeader, Array___CarriedItems" value="{&quot;FieldName&quot;:&quot;voucherdate&quot;,&quot;Title&quot;:&quot;单据日期&quot;,&quot;Value&quot;:&quot;2017.07.01 - 2017.07.31&quot;,&quot;Width&quot;:250}" action="contain" type="obj" />
                
		<Parameter path="Obj___Layout,Obj___ReportHeader, Array___CarriedItems" value="{&quot;FieldName&quot;:&quot;PartnerCode&quot;,&quot;Title&quot;:&quot;供应商&quot;,&quot;Value&quot;:&quot;&quot;,&quot;Width&quot;:250}" action="contain" type="obj" />

5.4 对参数转移：
需要做两种转义：
1）因为是xml用例，所以需要对冒号”做转义，即把”替换成&quot;
2）中文需要做ASCII转义，使用工具http://tool.oschina.net/encode?type=3 
3）url转义，参考http://www.cnblogs.com/kobe8/p/4030396.html 
