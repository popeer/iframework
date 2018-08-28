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


            Driver driver = (Driver)context.getBean("driver");
            IVerifier verifier = (com.chanjet.chanapp.qa.iFramework.common.impl.VerifierImpl)context.getBean("verifier");
            IDataManager dataManager = (com.chanjet.chanapp.qa.iFramework.common.impl.DataManagerImpl)context.getBean("dataManager");

            while(true){
                Thread.sleep(100);
            }

        } catch (Exception ex){
            ex.printStackTrace();
            Logger log = LogManager.getLogger(Start.class);
            log.error("catch exception:", ex);
        }
    }
}
