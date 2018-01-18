package com.chanjet.chanapp.qa.iFramework.common.Util;

import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.InitialDirContext;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

/**
 * Created by haijia on 12/21/17.
 */
public class DnsUtil {

    /**
     * @Auther: haijia
     * @Description:
     * @param serverAddr DNS地址
     * @param timeOut 连接超时时间
     * @param type 查询类型
     * @param address 查询地址
     * @Date: 12/21/17 14:47
     */
    public static List<String> search(String serverAddr, int timeOut,
                                      String type, String address) {

        InitialDirContext context = null;
        List<String> resultList = new ArrayList<String>();
        try {
            Hashtable<String, String> env = new Hashtable<String, String>();
            env.put("java.naming.factory.initial",
                    "com.sun.jndi.dns.DnsContextFactory");
            env.put("java.naming.provider.url", "dns://" + serverAddr + "/");
            env.put("com.sun.jndi.ldap.read.timeout", String.valueOf(timeOut));
            context = new InitialDirContext(env);
            String dns_attrs[] = { type };

            Attributes attrs = context.getAttributes(address, dns_attrs);
            Attribute attr = attrs.get(type);
            if (attr != null) {
                for (int i = 0; i < attr.size(); i++) {
                    resultList.add((String) attr.get(i));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(context!=null){
                try {
                    context.close();
                } catch (NamingException e) {

                }
            }
        }
        return resultList;
    }
}
