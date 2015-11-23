package com.berpic.core.domain.util;

/**
 * @Package com.berpic.core.domain.util
 * @Description: TODO()
 * @Author 任小斌  renxiaobin@berchin.com
 * @Date 15/11/23 下午6:38
 * @Version V1.0
 */
public enum EnumUitls {

    DEV("dev", 1), PROD("prod", 2), TEST("test", 3), MSG("mgs", 4), DEFAULT("dft", 5);
    // 成员变量
    private String name;
    private int index;

    // 构造方法
    private EnumUitls(String name, int index) {
        this.name = name;
        this.index = index;
    }

    public String getName() {
        return name;
    }
}
