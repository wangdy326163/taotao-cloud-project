package com.taotao.cloud.sys.biz.controller.dto;

import java.util.List;
import java.util.Properties;
import lombok.Data;


/**
 * @author valarchie
 */
@Data
public class RedisCacheInfoDTO {

    private Properties info;
    private Object dbSize;
    private List<CommonStatusDTO> commandStats;

    @Data
    public static class CommonStatusDTO {
        private String name;
        private String value;
    }

}
