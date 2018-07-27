# iframework
1.	Get,数据源
	<TestCaseNode name="NLP_TaxCode_1" dataPathName="excel:testCases/NLP/ai-tax/exceldata/aiTax784.csv">
    <Step name="analysis1" rule="get" url="http://172.16.33.176:8080/analysis1">
        <Request>
            <Parameter name="text" colName="productname" />
        </Request>    
    </Step>

</TestCaseNode>
2.	Value为json,
<Step name="step0_findUserByIdentify" url="http://a.b.com/internal_api/v1/user/findUserByIdentify">
    <Request>
        <Parameter name="userInfo" value="{&quot;userIdentify&quot;:&quot;iwWhDH5L@yg.com&quot;}"/>
    </Request>
</Step>
3.	从上步中取特定关键字的值
<Step name="step1_client_login" url="https://a.b.com/login ">
    <Request>
        <Parameter name="password" value="111111" action="md5" />
    </Request>
</Step>

<Step name="step2_api/appManager" url="http://a.b.com/api/appManager">
    <Request>
        <Parameter name="access_token" value="#access_token#" sequence="1"/>
    </Request>
</Step>

4.	使用keyword给特定数值内的值做替换
Step name="step1_active" url="http://a.b.com/active">
    <Request>
        <Parameter name="entUserInfo" value="$entUserInfo$" keyword="mobile" sequence="0"/>
           </Request>
</Step>

5.	使用sequence_type_keyword_key和offlineFlag获取指定步骤某值去替换数组指定值.  random%email% 生产随机邮件名称。
<Step name="step2_createUserJoinOrgAuthApp" url="http://a.b.com/create">
    <Request>
        <Parameter name="param" offlineFlag="1" sequence_type_keyword_key="1___json___userId___userId" value="{&quot;userId&quot;:&quot;fakeduserId&quot;,&quot;email&quot;:&quot;random%email%&quot;}"/>
    </Request>
</Step>
6.	使用offlineFlag标签来声明对value里的值做自定义处理
	<Step name="step0_user/sendMobile" url="http://a.b.com/internal_api /sendMobile">
    <Request>
        <Parameter name="sendInfo" offlineFlag="1" sequence_type_keyword_key="0___json___userId___userId"           value="{&quot;mobile&quot;:&quot;random%mobile%&quot;,&quot;userId&quot;:&quot;fakedUserId&quot; }"/>
    </Request>
</Step>
7.	读数据库，返回指定列值
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

8.	读数据库，读指定结果里的值做为数据库sql的参数来查询结果
<Step name="step2_activeCode" dburl="jdbc:postgresql://1.1.1.1:5432/boss_test_jc" uid="boss" pwd="1111">
    <Request>
        <Parameter name="sqltext" value="SELECT active_code from boss.boss_user_active where user_id='{0}'" sequence="1" keyword="userId" sqlSelectField="active_code" />
    </Request>
</Step>


9.	读数据库，读上n步接口参数里的值做为数据库sql查询参数
<Parameter name="sqltext" value="SELECT active_code from boss.boss_org_user_request where mobile='{0}'"
           sqlSelectField="active_code"
           param_type_key="string___identify___{0}" />

10.	执行sql脚本来完成对数据库的操作

<Parameter name="sqltext1" sqlScript="testCases/CIA/sqlScripts/sqlScript1" />


11.	使用storeParam自定义标签存储参数的值
<Step name="step3_internal_api/v1/Code" url="http://a.b.com /emailCode">
    <Request>
        <Parameter name="email" value="random%email%" storeParam="email" alias="email" type="string" />
    </Request>
</Step>

12.	使用自定义标签param_type_key获取之前步骤里存储的参数值
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

13.	获取多个存储的参数值，以及获取多个接口返回值中的部分值
<Step name="step7_/active" url="http://a.b.com/internal_api/active">
    <Request>
        <Parameter name="activeInfo" offlineFlag="1"
                   sequence_type_keyword_key="2___json___registCode___oldIdentifyCode,4___json___registCode___newIdentifyCode"
                   param_type_key="json___email___newIdentify,json___email2___oldIdentify"
                   value="{&quot;oldIdentify&quot;:&quot;fakedOldIdentify&quot;,&quot;newIdentify&quot;:&quot;fakedMail&quot;,&quot;oldIdentifyCode&quot;:&quot;&quot;,&quot;newIdentifyCode&quot;:&quot;&quot;}" />
    </Request>
</Step>

14.	获取存储的参数值，再替换当前参数值中的部分值
<Step name="step8_special_api/clearCache" url="https://a.b.com/clearCache">
    <Request>
        <Parameter name="keys" param_type_key="string___email___get%email%"
                   value="CIA:CODE_SEND_LIMIT|get%email%"/>
    </Request>
</Step>

15.	对callback的处理
<Step name="step0_authorizeByJsonp" url="https://a.b.com/internal_api/authorizeByJsonp" >
    <Request>
        <Parameter name="callback" value="jsonp%callback%" />
    </Request>
</Step>

16.	对动态结果值的验证
<Step name="step3_find_thirdplatform_user" url="https://a.b.com/special/api/v2/thirdplatform/user" rule="get">
    <Request>
        <Parameter name="userIdentify" value="#username#" sequence="1" />
    </Request>
    <Response>
        <Expect>
            <Parameter path="String___result" value="true" action="equal" type="string" />
            <Parameter path="Array___thirdPlatformInfo___0,Array___thirdPlatformList___0,String___thirdPlatform" value="自动化测试项目" action="equal" type="string" />
            <Parameter path="Array___thirdPlatformInfo___0,String___userId" value=""
                       sequence_type_keyword="1___json___userId" action="equal" type="string" />
        </Expect>
    </Response>
</Step>

17.	对参数需要做自定义处理

	／／生成手机号
        <Parameter name="mobile" value="random%mobile%" />
	／／需对value解密
	<Parameter name="appKey" value="12121" action="decrypt" />
	//做md5操作
	<Parameter name="password" value="111111" action="md5" />
	//随机生成15位数字
	<Parameter name="authCodeRelRandomKey" value="random%string15%"/>
		

	
 

18.	对于参数为数组类型的支持
<Step name="step4/syncList" url="http://a.b.com/syncList">
    <Request>
        <Parameter name="subscribeList" offlineFlag="1" type="array"
                   sequence_type_keyword_key="0___json___orgId___orgId,0___json___userId___userId"
                   value="[{&quot;orgId&quot;:&quot;special#orgId#&quot;, &quot;userId&quot;:&quot;fakedUserId&quot;,&quot;authItems&quot;:{&quot;version&quot;:1,&quot;invoice&quot;:20},&quot;description&quot;:&quot;descxxxx&quot;}]" />
    </Request>
</Step>

19.	对结果值的验证
1) 非空验证
<Parameter path="String___auth_code" action="notnull" type="string" />

2) string字符串比较验证
	<Parameter path="String___auth_result" value="true" action="equal" type="string" />
	
3) 对数组的验证
	<Parameter path="Array___appManagers" value="#userId#" keyword="userId" action="containItem" type="string" sequence="0" />
	
4) 对json里string类型的验证
	<Parameter path="Obj___Context,String___ReportSearchType" value="Advance" action="equal" type="string" />
	
	5) 对json格式里数组类型含有元素个数的验证
	<Parameter path="Obj___Context,Array___ViewOpts" value="1" action="equal" type="array" />
	
	6) 对json格式里的对象数组里某对象元素的验证，注意此例子的type=obj
	<Parameter path="Obj___Layout,Obj___ReportHeader, Array___CarriedItems" value="{&quot;FieldName&quot;:&quot;voucherdate&quot;,&quot;Title&quot;:&quot;单据日期&quot;,&quot;Value&quot;:&quot;2017.07.01 - 2017.07.31&quot;,&quot;Width&quot;:250}" action="contain" type="obj" />
	
	7) 对json格式里数组里某数组元素的验证，注意此例子的type=array
	<Parameter path="Obj___DTO,Obj___PU_PurchaseOrderSumRpt, Array___columns" value="[&quot;PartnerCode&quot;,&quot;PartnerName&quot;,&quot;BaseQuantity&quot;,&quot;OrigDiscountAmount&quot;,&quot;OrigTax&quot;,&quot;OrigTaxAmount&quot;,&quot;OrigDiscount&quot;,&quot;sumbasequantity&quot;,&quot;GroupLevel&quot;,&quot;rowType&quot;,&quot;reportRowType&quot;]" action="contain" type="array" />
	或：
	<Parameter path="Obj___DTO,Obj___PU_PurchaseOrderSumRpt, Array___Rows" value="[[&quot;&quot;,&quot;&quot;,&quot;&quot;,&quot;&quot;,&quot;&quot;,&quot;&quot;,&quot;&quot;,&quot;&quot;,&quot;999&quot;,&quot;T&quot;,null]]" action="contain" type="array" />

20.	读配置文件里定义的字符串来做自定义处理
<Parameter name="entUserInfo" value="$regenteruserwithemailwithoutactive$" keyword="mobile" sequence="0" storeParam="email" alias="email" type="json" />

21.	从多步里取值。从第0步和第2步取值。
<Parameter name="entUserInfo" sequence="0_2" value="$regenteruserwithmobileandssologin$" keyword="registCode"/>

22.	设置header
<Step name="step_queryCcpStatus" url="http://public.ops.chanjet.com.cn/queryCcpStatus" action="setheader" headers="Content-Type:application/json;charset:UTF-8;Accept-Encoding: gzip, deflate;Accept: application/json, text/plain, */*">

23.	header需要参数来初始化的情况：
<Step name="step3_GetReceivedMessages" rule="post" action="setheader" headerInit="2___String___Authorization"
      url="http://ip/tplus/api/v2/MessageCenterServiceImpl/GetReceivedMessages">


24.	不指定关键字的情况下，直接从某部接口结果里获取对应值来替换当前参数的值
<Parameter name="orgId" value="#orgId#" sequence="0"/>

25.	对参数为数组类型的参数赋值
<Parameter name="subscribeList" sequence="0" keyword="orgId" type="array" value="$syncsubscribelist$" />

26.	使用xpath来对接口结果返回的json格式特定内容的读取
<Parameter name="parentId" sequence_type_resultPath_valuePath="1___json_string___$.deptList[0].deptId___parentId" type="string"
           value="fakedDeptId"/>

27.	对jar包里的类类型的接口含有构造函数的支持：
clsName指明要实例化的类；parameterType指明构造函数的类型、参数值、次序；method指明要调用的接口名称。
<Step type="jar" name="step0_clientAuthenticationWithUserInfo" clsName="com.chanjet.csp.boss.cia.api.thrift.serviceImpl.AuthenticationClient"
      method="clientAuthenticationWithUserInfo" parameterType="String___172.18.2.181,Int___8090,Int___5000,String___568fe1fc-9e43-4d6f-b558-5030e9cd4260,String___6dw2de,String___teamplusapp,String___weE3Fr" >
    <Request>
        <Parameter type="string" name="username" value="test_user_007" />
        <Parameter type="string" name="password" value="111111" action="md5" />
        <Parameter type="string" name="endpointId" value="" />
        <Parameter type="string" name="rpt" value="\27{&quot;ct&quot;:&quot;091&quot;,&quot;pd&quot;:&quot;0161&quot;,&quot;tg&quot;:&quot;001&quot;,&quot;cv&quot;:&quot;1111&quot;,&quot;l1&quot;:[{&quot;d&quot;:&quot;ggy&quot;}]}\27" />
    </Request>
    <Response>
        <Expect>
            <Parameter path="" value="" action="" type=""/>
        </Expect>
    </Response>
</Step>

28.	当参数是一个自定义的类类型时，
<Request>
    <Parameter type="custom" customerClass="com.chanjet.csp.boss.cia.api.thrift.entity.AppInfoIn" value="in"
               jarparam_keyvalue="setAppKey___568fe1fc-9e43-4d6f-b558-5030e9cd4260___string,setAppSecret___6dw2de___string,setSearchAppKey___6decf230-eaae-11e2-889e-03ebd9b64ae8___string,setSearchAppSecret___123456___string"/>
</Request>

29.	excel作为接口参数的数据源，接口结果验证值也在excel数据源里，excel每一行都代表用例的一组测试数据，一组测试数组包含接口参数和该记录的期望返回值。
dataPathName指明excel数据源的文件位置。
excelExpectParameterColName指明excel里的某列，该列作用是该条记录的期望返回值。
<TestCaseNode name="CIA_V2_OrgTrial-TICE-939-940" dataPathName="excel:testCases/CIA/exceldata/TICE-939-940-online2.csv">

<Step name="step5_get_v2_org_Trial" rule="get" url="https://a.b.com/internal/api/v2/org/external/note" >
    <Request>
        <Parameter name="appKey" colName="appKey" />
        <Parameter name="appSecret" colName="appSecret" />
        <Parameter name="appId" colName="appId" />
        <Parameter name="orgId" colName="orgId" sequence="0" keyword="orgId"/>
        <Parameter name="grayMark" colName="grayMark" />
        <Parameter name="startPage" colName="startPage" />
        <Parameter name="pageSize" colName="pageSize" />
    </Request>
    <Response>
        <Expect>
            <Parameter excelFlag="true" excelExpectParameterColName="get_v2_org_Trial"/>
        </Expect>
    </Response>
</Step>

30.	获取cookie
<Step name="step_GetVersionType" url="http://1.1.1.1/tplus/ajaxpro/Ufida.T.SM.Login.UIP.LoginManager,Ufida.T.SM.Login.UIP.ashx?method=GetVersionType" action="getcookie">

31.	设置cookie
<Step name="step_getType" url="http://ip/getType" action="setcookie_0">

32.	使用exchangeFlag="true"和step里的action="singlekey"对接口参数没有name，Content-Type为application/json的场景：
	<Step name="step0_sample" url=http://ip/finance/data action="singlekey">
    <Request>
        <Parameter name="paramvalue" value="{&quot;cardNo&quot;:&quot;111&quot}" exchangeFlag="true" />
    </Request>

33.	接口是void时(不返回值)时使用voidReturn="true"不对结果做验证
<Step name="step5_SendMessage"
      voidReturn="true" url="http://ip/path/SendMessage">
    <Request>
        <Parameter name="_args" value="1" />
    </Request>
</Step>

34.	参数是嵌套json：
<Step name="#2" url="https://domain/rest"
      groovyMethodName="getAuthSid"
      headerInit="0___jsonobject___Value___Value___ External/TplusInitHeaderSID.groovy"
      parameterType="" action="setheader" >   
    </Request>
</Step>

35.	header初始化：
<Step name="step_1" action="setheader"
      headerInit="0___JSON___Authorization___Authorization"
      url="https://domain/GetInfo">
</Step>

36.	设置header
<Step name="step_1" action="dopostjsonheader" url="https://domain/add">
  <Request>
    <Parameter exchangeFlag="true" offlineFlag="1" name="{&quot;assistant&quot;: &quot;{\&quot;no\&quot;:\&quot;random%string8%\&quot;}&quot;}" value=""/>
  </Request>
  </Response>
</Step>

37.	put
	<Step name="step_1" action="PUT" url="https://domain/add"></Step>

38.	delete 
<Step name="step_1" action="DELETE" url="https://domain/GetInfo"></Step>

39.	使用jsonArray保留字作为返回结果类型为对象数组时的验证

<Step name="step_1" rule="GET" action="setheader" headerInit="0___JSON___Authorization___Authorization" url="https://domain/period">
  <Request></Request>
  <Response>
    <Expect>
      <Parameter path="jsonArray___0,String___shuijin" value="" action="notnull" type="string"/>
    </Expect>
  </Response>
</Step>

40.	使用arraystring保留字处理返回结果类型为字符串数组时的验证
<Step name="step_1" rule="GET" action="setheader" headerInit="0___JSON___Authorization___Authorization" url="https://domain/period">
  <Request></Request>
  <Response>
    <Expect>
      <Parameter path="ArrayString___0" value="true" action="equal" type="string"/>
    </Expect>
  </Response>
</Step>
41.	
