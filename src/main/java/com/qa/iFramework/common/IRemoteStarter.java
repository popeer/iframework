package com.qa.iFramework.common;

/**
 * Created by haijia on 6/27/17.
 */
public interface IRemoteStarter {

    /**
     * @Auther: haijia
     * @Description:调用用例执行
     * @param dburl 若涉及db切换不同的库，可指定db的连接字符串
     * @param uid 若涉及db切换不同的库，可指定db连接的用户名
     * @param pwd 若涉及db切换不同的库，可指定db连接的密码
     * @param dn 若切换不同环境的地址访问，可指定dn即domain nam，默认是用例里写的域名
     * @param productName 指定产品线名称，用于统计数据
     * @param dns 若涉及指定dns来解析域名，可指定dns
     * @param path 指定用例路径，可以是文件夹，也可以是单个文件,
     * @param operator 测试用例的执行人人名
     * @param dataSource 测试用例的执行人人名
     * @param preResults 传来了前置结果。若传了前置结果，就作为xml测试用例里的最前面的step(s)。前置结果可能是多个。
     * @Date: 1/26/18 16:17
     */
    String run(String dburl,
               String uid,
               String pwd,
               String dn,
               String productName,
               String dns,
               String path,
               String operator,
               String dataSource,
               String preResults);

    /**
     * @Auther: haijia
     * @Description:根据产品线名称(必填)、日期(默认从2017年9月26日开始)来查询用例执行统计信息
     * @param productname
     * @param rundate
     * @Date: 1/26/18 16:15
     */
    String getSummary(String productname, String rundate);

    /**
     * @Auther: haijia
     * @Description:根据路径显示该路径下所有文件和文件夹
     * @param productname
     * @Date: 1/26/18 16:14
     */
    String getDirectoriesFiles(String productname) throws Exception;

    /**
     * @Auther: haijia
     * @Description:根据用例路径显示用例内容
     * @param path:用例路径
     * @Date: 1/26/18 16:13
     */
    String showXml(String path);

    /**
     * @Auther: haijia
     * @Description:查询某产品线的历史报告ID，日期
     * @param productname
     * @param date
     * @Date: 1/26/18 16:11
     */
    String getHistoryReportList(String productname, String date);

    /**
     * @Auther: haijia
     * @Description:根据报告id显示报告详情
     * @param reportID
     * @Date: 1/26/18 16:12
     */
    String getReport(String reportID);

    /**
     * @Auther: haijia
     * @Description: 创建或编辑测试用例文件
     * @param path
     * @param content
     * @Date: 12/4/17 16:20
     */
    boolean editXml(String path, String content);

    /*
       @Auther: haijia
     * @Description:执行指定的路径下某特定标签的测试用例
     * @param online:指定路径
     * @param out:指定外网用例
     * @param in:指定内网用例
     * @param user:声明执行人
     * @Date: 08/07/18 16:12
     */
    String runFlagCases(String path, String flags, String user);

    /*
       @Auther: haijia
     * @Description:设置标签
     * @param path:执行指定的路径下某特定标签的测试用例
     * @param pn:指明产品线名称并存到数据表nodemanager里
     * @Date: 08/07/18 16:12
     */
    String generateFlags(String path, String pn);

    /*
      @Auther: haijia
     * @Description:计算某路径和其子录下含有指定标签的所有用例文件
     * @param path:执行指定的路径下的测试用例
     * @param flags:指明一组特定标签
     * @Date: 08/16/18 10:12
     */
    String countFiles(String path, String flags);

    /*
      @Auther: haijia
     * @Description:查询标签和id对应关系
     * @Date: 08/17/18 15:43
     */
    String flagMap();

    String statistics();
}
