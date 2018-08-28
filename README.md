1.	部署说明
1.1.	git地址
git@gitlab.rd.chanjet.com:cloud-QA/iFramework.git
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
	调用框架执行用例是通过http的get请求，url里前缀是ip+端口+标示+参数的形式。如：机器部署在172.18.5.30上，那么调用的url前缀就是http://172.18.5.30:8888/iframework/irun?
2.1.2.	原理说明
框架的核心功能都以dubbo协议来暴露接口，注册中心在测试环境的zookeeper上。其中IRemoteServer接口是负责解析调用框架请求的，暴露成rest形式，目的是方便对框架的调用来执行测试用例。
2.2.	 调用参数信息
参数名
是否必填
参数说明
path
是
测试用例的xml的绝对路径。 路径放在/opt/iframework/testCases下， 需要对斜杠(／)做url转义为%2f，举例假如你要执行cia下面鉴权接口的ciaAuthBatch1.xml这个用例，路径就是
172.18.5.30:8888/iframework/irun?path=%2fopt%2fiframework%2ftestCases%2fCIA%2fAuthentication%2fciaAuthBatch1.xml, 如果不指定具体的xml，只是指定了某个文件夹，那么就会跑这个文件夹下及子文件夹下全部的用例。
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
域名，url里一旦带有这个参数，就会替换测试用例里的url前缀，如http://cenc.chanjet.com、http://cia.chanapp.chanjet.com。
注意：如果用例里没有写明前缀，那么请求参数里需要带有dn参数，这个dn会和用例里的path进行拼接成完整的url；
假设用例里没有写明前缀，如果请求参数里不传dn参数，用例会报错，因为找不到用例的url.

2.3.	

3.	用例的执行过程
1）框架解析调用的参数，举例172.18.5.30:8888/iframework/irun?path=%2fopt%2fiframework%2ftestCases%2fCIA%2fAuthentication%2fciaAuthBatch1.xml 通过这个请求，框架得知，要执行的用例是/opt/iframework/testCases/CIA/Authentication/ciaAuthBatch1.xml
2) 解析这个xml，转成框架定义的测试用例对象testCaseNode
3) 按testCaseNode定义的step顺序，顺序执行每个step，即每个接口。
4）每个step的执行过程是，先解析step里的url和请求参数，然后调用httpcient来发送你的请求，再然后对请求的返回结果做验证，验证的逻辑是写在step里的Response/Expect下的Parameter，最后是把用例执行结果存到数据库中。
5) 每个step执行结果都会添加到StringBuilder里，目的是在xml里用例都跑完之后，把完整结果返回给前端。
6) 用例执行完毕，会换行打印Finish！！！字样，如果执行异常，会把异常堆栈信息返回出来。

4.	用例的组织形式
¥	测试用例都是xml，写好xml放在部署机器上，执行调用时指定好用例的存放路径，即可执行你刚写的测试用例，不需要程序重新打包。
¥	组织用例首先按业务线名称来区分，好比第一级文件夹名称统一是testCases，第二级就是各业务线的名字，如bia, cenc,cia, tplus, 第三级就是业务线下各模块或各feature,如cia下的Authentication、login、administrator。还可以再多级，根据业务线特点来区分吧。
¥	注意一点，如果某一个模块下有10个接口可以单独执行，不需要过多依赖其它接口，那么建议统一放到一个xml文件里，文件名建议是batch1为结尾，方便其它人知道这是一个批量执行单接口的测试用例。
¥	如果是测一个场景化的接口测试用例，即需要2个以上的接口才能完成最终的测试场景，建议把这些放到一个xml里。 

¥	注意： 测试用例支持数据与用例分离，即一份测试用例可以跑多组数据，数据都放到配置文件里，即/src/main/resources/properties/下某个自定义的配置文件里，也可以放到csv文件里。

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
		4. 数据写入excel的写法参考/testCases/CIA/CiaSample6.xml
		5. 数据写入excel的写法参考/testCases/CIA/CiaSample5.xml
		6. 获取登陆token的方法： client_authentication_with_userInfo这个接口生成token，后面接口若使用token，就在请求参数里添加<Parameter name="access_token" value="#access_token#" sequence=“0”/>即可。用例参考：testCases/CIA/login/CIAcodeForUserBaseInfo1.xml。
		7. 登陆接口的写法：参考前四步：testCases/CIA/CiaSample3.xml
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
