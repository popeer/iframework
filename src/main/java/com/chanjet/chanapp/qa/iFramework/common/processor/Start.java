package com.chanjet.chanapp.qa.iFramework.common.processor;

import com.chanjet.chanapp.qa.iFramework.common.IDataManager;
import com.chanjet.chanapp.qa.iFramework.common.IVerifier;
import com.google.common.io.Resources;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.ConfigurationSource;
import org.apache.logging.log4j.core.config.Configurator;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.net.URL;

/**
 * Created by haijia on 12/8/16.
 */
public class Start {

    public static void main(String[] args){

        try {

            URL log4j2ConfigFileUrl =  Resources.getResource("log4j2.xml");
            System.out.println("log4j2 configFile URL = " + log4j2ConfigFileUrl.toString());

            // **** force log4j reinitialize
            LogManager.shutdown();
            Configurator.initialize(null, new ConfigurationSource(
                    Resources.asByteSource(log4j2ConfigFileUrl).openStream(),
                    log4j2ConfigFileUrl));

            ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("classpath:applicationContext.xml");
            context.start();


            Logger log = LogManager.getLogger(Start.class);

            log.info("=========================================");
            log.info("iFramework, start ... ");
            log.info("=========================================");


            Driver driver = (com.chanjet.chanapp.qa.iFramework.common.processor.Driver)context.getBean("driver");
            IVerifier verifier = (com.chanjet.chanapp.qa.iFramework.common.impl.VerifierImpl)context.getBean("verifier");
            IDataManager dataManager = (com.chanjet.chanapp.qa.iFramework.common.impl.DataManagerImpl)context.getBean("dataManager");

            while(true){
                Thread.sleep(100);
            }

//            CommandEntity commandEntity = new CommandEntity();
//            String path = "testCases/CIA/CiaSample6.xml";

//        commandEntity.setDbURL("jdbc:postgresql://db-ufpark-test01.chanjet.com.cn:5432/iframework");
//        commandEntity.setUid("iadmin");
//        commandEntity.setPwd("iadmin");
//        commandEntity.setDomainName("https://inte-cia.chanapp.chanjet.com");
////        commandEntity.setSqlScript("testCases/CIA/sqlScripts/sqlScript1");
//
//            if((null == args) || (0 == args.length)){
//                driver.Run(path, verifier, dataManager, commandEntity);
//                return;
//            } else {
//                for(String command : args){
//                    String[] s = command.split("=");
//                    if((null == s) || (0 == s.length)){
//                        log.error("Your command is invalid as = is a must!!!!");
//                        return;
//                    } else if (2 != s.length){
//                        log.error("Your command is invalid as it need a pair of parameter!!!!");
//                        return;
//                    }
//
//                    switch (s[0].toLowerCase()){
//                        case "dburl":
//                            commandEntity.setDbURL(s[1]);
//                            break;
//                        case "uid":
//                            commandEntity.setUid(s[1]);
//                            break;
//                        case "pwd":
//                            commandEntity.setPwd(s[1]);
//                            break;
//                        case "dn":
//                            commandEntity.setDomainName(s[1]);
//                            break;
//                        case "path":
//                            path = s[1];
//                            break;
//                        default:
//                            break;
//                    }
//                }
//            }
//        /Users/haijia/code/h1/iFramework/testCases/CIA/StaffDepartment/ciaStaffDepartmentBatch1.xml
///Users/haijia/code/h1/iFramework/testCases/CIA/ThirdParty/ciaThirdPartyBatch1.xml
//            /Users/haijia/code/h1/iFramework/testCases/CIA/CoverOnline/codeForAccessTokenForClient.xml
//            /Users/haijia/code/h1/iFramework/testCases/CIA/CoverOnline/findAppManagerListByOrg.xml
//            BindUnbindPWD
//            driver.Run(path, verifier, dataManager, commandEntity);
//            /Users/haijia/code/h1/iFramework/testCases/CIA/RelationForEntUser/ciaOrgAuditExistsUserApply.xml
//            driver.Execute("/Users/haijia/code/h1/iFramework/testCases/CCP/CCPSample1.xml", verifier, dataManager, null);
//            driver.Execute("/Users/haijia/code/h1/iFramework/testCases/CIA/StaffDepartment/ciagetDepartmentTreeAndTotalEmployee1.xml", verifier, dataManager, null);
//            driver.Execute("/Users/haijia/code/h1/iFramework/testCases/CIA/ThirdParty/ciaThirdPartyBatch1.xml", verifier, dataManager, null);
//            driver.Execute("/Users/haijia/code/h1/iFramework/testCases/CIA/BindUnbindPWD/ciaactiveChangeBinging1.xml", verifier, dataManager, null);
//            driver.Execute("/Users/haijia/code/h1/iFramework/testCases/CIA/RelationForEntUser/ciaorgAuditExistsUserApply.xml", verifier, dataManager, null);
//            driver.Execute("/Users/haijia/code/h1/iFramework/testCases/CIA/RelationForEntUser/ciaremoveUserFromOrgByOrgManger1.xml", verifier, dataManager, null);
//            driver.Execute("/Users/haijia/code/h1/iFramework/testCases/CIA/NewUser/CIAnewUserAuditOrgInviteWithNewEmailOrMobile1.xml", verifier, dataManager, null);
//            driver.Execute("/Users/haijia/code/h1/iFramework/testCases/CIA/RelationForEntUser/ciaorgAuditExistsUserApply.xml", verifier, dataManager, null);
//            driver.Execute("/Users/haijia/code/h1/iFramework/testCases/CIA/RelationForEntUser/cianewUserAuditOrgInvite1.xml", verifier, dataManager, null);
//            driver.Execute("/Users/haijia/code/h1/iFramework/testCases/CIA/Bugfix/TICE-937.xml", verifier, dataManager, null);
//            driver.Execute("/Users/haijia/code/h1/iFramework/testCases/CIA", verifier, dataManager, null);
//            driver.Execute("/Users/haijia/code/h1/iFramework/testCases/CIA/NewUser/ciaRegMemberUserAndLoginOrSignInExistedUserWithSMSVerificationCode1.xml", verifier, dataManager, null);
//            driver.Execute("/Users/haijia/code/h1/iFramework/testCases/CIA/TPlus/ciaTPlus1.xml", verifier, dataManager, null);
//            driver.Execute("/Users/haijia/code/h1/iFramework/testCases/CIA/CoverOnline/authenticationByCodeAndAuthCode.xml", verifier, dataManager, null);
//            driver.Execute("/Users/haijia/code/h1/iFramework/testCases/CIA/login/ciaLoginBatch1.xml", verifier, dataManager, null);
//            driver.Execute("/Users/haijia/code/h1/iFramework/testCases/CIA/login/CIAwebAuthentication1.xml", verifier, dataManager, null);
//            driver.Execute("/Users/haijia/code/h1/iFramework/testCases/CIA/TPlus/ciaGetTplusOrgUserAccessToken1.xml", verifier, dataManager, null);
//            driver.Execute("/Users/haijia/code/h1/iFramework/testCases/CIA/Administrator/ciaAdminCancelManager.xml", verifier, dataManager, null);
//            driver.Execute("/Users/haijia/code/h1/iFramework/testCases/CIA/RelationForEntUser/ciaRgEnterpriseUserOrCreateAndJoinOrgForExistedUserWithoutActive1.xml", verifier, dataManager, null);
//                                    driver.Execute("/Users/haijia/code/h1/iFramework/testCases/CIA/RelationForEntUser/ciaGetPostOrganization.xml", verifier, dataManager, null);
//                        driver.Execute("/Users/haijia/code/h1/iFramework/testCases/CIA/online2.xml", verifier, dataManager, null);

        ///Users/haijia/code/h1/iFramework/testCases/CIA/TPlus/ciaGetOrgExternal1.xml
            //Users/haijia/code/h1/iFramework/testCases/CIA/TPlus/ciaGetOrgExternal1.xml
//            driver.Execute("/Users/haijia/code/h1/iFramework/testCases/CIA/Authentication/ciaStopOrRecoverSubscribedApp.xml", verifier, dataManager, null);
//            driver.Execute("/Users/haijia/code/h1/iFramework/testCases/TPlus/getReport1.xml", verifier, dataManager, null);
//            driver.Execute("/Users/haijia/code/h1/iFramework/testCases/CIA/RelationForEntUser/ciaBatch2.xml", verifier, dataManager, null);

        } catch (Exception ex){
            ex.printStackTrace();
            Logger log = LogManager.getLogger(Start.class);
            log.error("catch exception:", ex);
        }



//        driver.Run(args);




    }
}
