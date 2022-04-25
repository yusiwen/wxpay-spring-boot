/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package cn.yusiwen.wxpay.protocol.v3.model.payscore;

import lombok.Data;

/**
 * 订单风险金信息，必填
 *
 * @author yusiwen
 * @since 1.0.0.RELEASE
 */
@Data
public class RiskFund {

    /**
     * 风险金名称，必填
     */
    private Type name;
    /**
     * 风险金额，必填
     * <p>
     * 1、数字，必须&gt;0（单位分）。 2、风险金额&gt;=每个服务ID的风险金额上限。 3、当商户优惠字段为空时，付费项目总金额≤服务ID的风险金额上限 （未填写金额的付费项目，视为该付费项目金额为0）。
     * 4、完结金额可大于、小于或等于风险金额。详细可见QA <a target= "_blank" href=
     * "https://pay.weixin.qq.com/wiki/doc/apiv3/wxpay/payscore/chapter11_2.shtml#menu1">关于订单风险金额问题</a>
     */
    private Long amount;
    /**
     * 风险说明，选填
     * <p>
     * 文字，不超过30个字。
     */
    private String description;

    /**
     * 风险金类型
     */
    public enum Type {
        /**
         * 押金
         */
        DEPOSIT,
        /**
         * 预付款
         */
        ADVANCE,
        /**
         * 保证金
         */
        CASH_DEPOSIT,
        /**
         * 预估订单费用
         * <p>
         * 【先享模式】（评估不通过不可使用服务）
         */
        ESTIMATE_ORDER_COST
    }

}
