package com.study.shop.utils;

/**
 * @author Tiger
 * @date 2019-12-07
 * @see com.study.shop.utils
 **/
public class SwaggerUtils {
    public static class SWAGGERUI {
        public static final String MAIN = "/**/swagger-ui.html";
        public static final String RESOURCES = "/**/swagger-resources/**";
        public static final String API_DOCS = "/**/api-docs";
        public static final String STATIC_RESOURCES = "/**/webjars/springfox-swagger-ui/**";
        /**
         *
         * @Title: getSwaggerResource
         * @Description: 获取swagger对应ui资源
         * @return
         */
        public static String[] getSwaggerResource() {
            return new String[] { MAIN, RESOURCES, API_DOCS, STATIC_RESOURCES};
        }
    }
}
