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
package cn.yusiwen.wxpay.protocol.v3.model;

import java.util.List;

import lombok.Data;

/**
 * 代金券核销规则.
 *
 * @author yusiwen
 * @since 1.0.0.RELEASE
 */
@Data
public class CouponUseRule {

    /**
     * 可核销商品编码
     */
    private List<String> availableItems;
    /**
     * 可用商户
     */
    private List<String> availableMerchants;
    /**
     * 是否可以叠加使用
     */
    private Boolean combineUse;
    /**
     * 券生效时间（暂时未开放，日期2021-1-12，请以微信官方通知为准）
     */
    private CouponAvailableTime couponAvailableTime;
    /**
     * 固定面额满减券使用规则
     */
    private FixedNormalCoupon fixedNormalCoupon;
    /**
     * 订单优惠标记
     */
    private List<String> goodsTag;
    /**
     * 指定支付方式
     */
    private List<String> limitPay;
    /**
     * 指定银行卡BIN
     */
    private LimitCard limitCard;

    /**
     * 支付方式，枚举值：
     *
     * <ul>
     * <li>MICROAPP：小程序支付</li>
     * <li>APPPAY：APP支付</li>
     * <li>PPAY：免密支付</li>
     * <li>CARD：刷卡支付</li>
     * <li>FACE：人脸支付</li>
     * <li>OTHER：其他支付</li>
     * </ul>
     * 不填则默认“不限”
     */
    private CouponTradeType tradeType;

    /**
     * 指定银行卡BIN
     * <p>
     * 限定该批次核销的指定银行卡BIN，当批次限定了指定银行卡时方可生效
     */
    @Data
    public static class LimitCard {
        /**
         * 银行卡名称
         * <p>
         * 将在微信支付收银台向用户展示，最多4个中文字
         */
        private String name;
        /**
         * 指定卡BIN
         * <p>
         * 使用指定卡BIN的银行卡支付方可享受优惠，按json格式 特殊规则：单个卡BIN的字符长度为【6,9】,条目个数限制为【1,10】。 示例值：['62123456','62123457']
         */
        private List<String> bin;
    }

    /**
     * 代金券核销规则-支付方式.
     *
     * @author yusiwen
     * @since 1.0.0.RELEASE
     */
    public enum CouponTradeType {
        /**
         * 小程序支付
         *
         * @since 1.0.0.RELEASE
         */
        MICROAPP,
        /**
         * APP支付
         *
         * @since 1.0.0.RELEASE
         */
        APPPAY,
        /**
         * 免密支付
         *
         * @since 1.0.0.RELEASE
         */
        PPAY,
        /**
         * 刷卡支付
         *
         * @since 1.0.0.RELEASE
         */
        CARD,
        /**
         * 人脸支付
         *
         * @since 1.0.0.RELEASE
         */
        FACE,
        /**
         * 其他支付
         *
         * @since 1.0.0.RELEASE
         */
        OTHER,
    }

}
