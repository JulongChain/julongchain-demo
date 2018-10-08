package org.bcia.julongchain.msp.mspconfig;

import org.bcia.julongchain.common.log.JavaChainLog;
import org.bcia.julongchain.common.log.JavaChainLogFactory;
import org.yaml.snakeyaml.Yaml;

import java.io.*;

/**
 * @author lishaojie
 * @Date: 2018/7/9
 * @company Dingxuan
 */
public class MspConfigFactory {
    private static JavaChainLog log = JavaChainLogFactory.getLog(MspConfigFactory.class);

    public MspConfigFactory() {
    }

    public static MspConfig loadMspConfig() throws FileNotFoundException {
        Yaml yaml = new Yaml();
//        FileInputStream is = null;
        InputStream is = null;

        MspConfig var3 = null;
        try {

            is = MspConfigFactory.class.getClassLoader().getResourceAsStream("config/gmcsp.yaml");

            MspConfig mspConfig = (MspConfig)yaml.loadAs(is, MspConfig.class);
            var3 = mspConfig;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(is != null) {
                try {
                    is.close();
                } catch (IOException var10) {
                    log.error(var10.getMessage(), var10);
                }
            }

        }

        return var3;
    }
}
