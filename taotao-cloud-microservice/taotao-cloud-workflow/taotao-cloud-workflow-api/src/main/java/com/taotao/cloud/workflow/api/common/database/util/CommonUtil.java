package com.taotao.cloud.workflow.api.common.database.util;

import com.alibaba.druid.pool.DruidDataSource;
import java.util.Random;


/**
 * 数据库字段数据装配工具类
 *
 */
public class CommonUtil {

    /**
     * 获取DruidDataSource
     *
     * @param dataSourceUtil
     * @return
     */
    public static DruidDataSource getDruidDataSource(DataSourceUtil dataSourceUtil) throws DataException {
        DruidDataSource druidDataSource = new DruidDataSource();
        druidDataSource.setUsername(dataSourceUtil.getUserName());
        druidDataSource.setPassword(dataSourceUtil.getPassword());
        //jdbc-url
        druidDataSource.setUrl(ConnUtil.getUrl(dataSourceUtil));
        //数据库驱动
        druidDataSource.setDriverClassName(DbTypeUtil.getDb(dataSourceUtil).getDriver());
        return druidDataSource;
    }

    /**
     * 随机生成包含大小写字母及数字的字符串
     *
     * @param length
     * @return
     */
    public static String getStringRandom(int length) {
        String val = "";
        Random random = new Random();
        //参数length，表示生成几位随机数
        for (int i = 0; i < length; i++) {
            String charOrNum = random.nextInt(2) % 2 == 0 ? "char" : "num";
            //输出字母还是数字
            if ("char".equalsIgnoreCase(charOrNum)) {
                //输出是大写字母还是小写字母
                int temp = random.nextInt(2) % 2 == 0 ? 65 : 97;
                val += (char) (random.nextInt(26) + temp);
            } else if ("num".equalsIgnoreCase(charOrNum)) {
                val += String.valueOf(random.nextInt(10));
            }
        }
        return val;
    }

}

