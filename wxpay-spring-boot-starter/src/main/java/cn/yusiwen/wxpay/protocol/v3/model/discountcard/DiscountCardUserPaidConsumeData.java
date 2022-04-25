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
package cn.yusiwen.wxpay.protocol.v3.model.discountcard;

import lombok.Data;

import cn.yusiwen.wxpay.protocol.enumeration.ContractStatus;
import cn.yusiwen.wxpay.protocol.enumeration.UnfinishedReason;

/**
 * 先享卡扣费状态变化通知解密
 *
 * @author yusiwen
 * @since 1.0.0.RELEASE
 */
@Data
public class DiscountCardUserPaidConsumeData {

    /**
     * 应用appId需要绑定微信商户平台
     */
    private String appId;
    /**
     * 先享卡ID，唯一标识一个先享卡
     */
    private String cardId;
    /**
     * 先享卡模板ID，唯一定义此资源的标识。创建模板后可获得
     */
    private String cardTemplateId;
    /**
     * 商户号
     */
    private String mchId;
    /**
     * 用户标识，用户在{@code appId}下的唯一标识
     */
    private String openId;
    /**
     * 商户领卡号，商户在请求领卡预受理接口时传入的领卡请求号，同一个商户号下必须唯一，要求32个字符内，只能是数字、大小写字母_-|*
     */
    private String outCardCode;
    /**
     * 先享卡的守约状态
     */
    private ContractStatus state;
    /**
     * 享受优惠总金额，单位为 “分”
     */
    private Long totalAmount;
    /**
     * 未完成约定原因
     */
    private UnfinishedReason unfinishedReason;
    /**
     * 用户退回优惠的付款信息
     */
    private PayInformation payInformation;

    /**
     * 用户退回优惠的付款信息
     * <p>
     * 当状态为{@link ContractStatus#UNFINISHED}(用户未完成约定)时，且需要退回已享受的优惠金额时，返回此字段；
     */
    @Data
    public static class PayInformation {

        /**
         * 付款金额,用户需要退回优惠而付款的金额，单位为：分；
         */
        private Long payAmount;
        /**
         * 用户付款状态，
         */
        private PayState payState;
        /**
         * 付款时间
         */
        private String payTime;
        /**
         * 微信支付订单号，仅在订单成功收款时才返回
         */
        private String transactionId;
    }

    /**
     * 付款状态
     *
     * @since 1.0.0.RELEASE
     */
    public enum PayState {
        /**
         * 付款中
         */
        PAYING,
        /**
         * 已付款
         */
        PAID
    }
}
