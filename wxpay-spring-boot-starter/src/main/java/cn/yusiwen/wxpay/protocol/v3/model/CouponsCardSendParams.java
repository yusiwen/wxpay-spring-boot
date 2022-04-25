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

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.OffsetDateTime;

import lombok.Data;

/**
 * 发放代金券消费卡API请求参数
 *
 * @author yusiwen
 * @since 1.0.0.RELEASE
 */
@Data
public class CouponsCardSendParams {

    /**
     * 消费卡ID
     * <p>
     * 获取方法请参见<a target= "_blank" href=
     * "https://pay.weixin.qq.com/wiki/doc/apiv3/wxpay/marketing/coupons/chapter2_2.shtml">《接入流程》</a>中的创建消费卡。
     */
    private String cardId;
    /**
     * 消费卡归属appId
     */
    private String appId;
    /**
     * 需为消费卡归属appId生成的openId。
     */
    private String openId;
    /**
     * 商户此次发放凭据号。
     * <p>
     * 推荐使用大小写字母和数字，不同添加请求发放凭据号不同，商户侧需保证同一发券请求的out_request_no和send_time的唯一性。
     */
    private String outRequestNo;
    /**
     * 请求发卡时间,由于系统限制，暂不支持传入早于当前时间24小时以上的时间进行发券请求。
     */
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX", timezone = "GMT+8")
    private OffsetDateTime sendTime;
}
