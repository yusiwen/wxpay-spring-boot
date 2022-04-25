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
package cn.yusiwen.wxpay.protocol.v3.model.profitsharing;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;
import java.util.List;

import lombok.Data;

/**
 * 服务商-微信支付分账动账通知参数
 *
 * @author yusiwen
 * @since 1.0.0.RELEASE
 */
@Data
public class PartnerProfitSharingConsumeData {

    /**
     * 服务商商户号.
     * <p>
     * 服务商模式分账发起商户
     */
    private String mchId;
    /**
     * 子商户号
     * <p>
     * 服务商模式分账出资商户
     */
    private String subMchid;

    /**
     * 微信订单号.
     * <p>
     * 微信支付订单号
     */
    private String transactionId;

    /**
     * 微信分账/回退单号.
     */
    private String orderId;

    /**
     * 商户分账/回退单号.
     * <p>
     * 分账方系统内部的分账/回退单号
     */
    private String outOrderNo;

    /**
     * 分账接收方.
     * <p>
     * 分账接收方对象
     */
    private List<Receiver> receivers;

    /**
     * 成功时间.
     * <p>
     * Rfc3339标准
     */
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ssXXX", timezone = "GMT+8")
    private LocalDateTime successTime;

}
