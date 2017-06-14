package com.lock.lib.api.site;

/**
 * Created by hubing on 16/3/29.
 */
public class OssToken {
    public String accessKeyId;
    public String accessKeySecret;
    public String expiration;
    public String securityToken;

    @Override
    public String toString() {
        return "OssToken{" +
                "accessKeyId='" + accessKeyId + '\'' +
                ", accessKeySecret='" + accessKeySecret + '\'' +
                ", expiration='" + expiration + '\'' +
                ", securityToken='" + securityToken + '\'' +
                '}';
    }
}
