package com.berpic.core.domain.bean;

import java.io.Serializable;
import java.util.List;

/**
 * 图片移动参数
 *
 * @author chenchao 991722899@qq.com
 *         2013-2-21 上午9:36:18
 */
public class MoveInfo implements Serializable {
    private static final long serialVersionUID = -9072311432634044047L;
    //用户ID
    private String userid;
    //签名
    private String sign;
    //图片集合
    private List<String> imgs;

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public List<String> getImgs() {
        return imgs;
    }

    public void setImgs(List<String> imgs) {
        this.imgs = imgs;
    }

}
