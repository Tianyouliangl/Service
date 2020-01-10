package com.service.zgbj.im;

import java.math.BigDecimal;

/**
 * author : fengzhangwei
 * date : 2020/1/10
 */
public class RedEnvelopeBody {
    private BigDecimal money;

    public RedEnvelopeBody(BigDecimal money) {
        this.money = money;
    }

    public BigDecimal getMoney() {
        return money;
    }

    public void setMoney(BigDecimal money) {
        this.money = money;
    }
}
