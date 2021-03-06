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

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * 已实扣代金券信息
 *
 * @author yusiwen
 * @since 1.0.0.RELEASE
 */
@Data
public class ConsumeInformation {

    /**
     * 核销商户号
     */
    @JsonProperty("consume_mchid")
    private String consumeMchId;

    /**
     * 核销时间 YYYY-MM-DDTHH:mm:ss.sss+TIMEZONE
     */
    private String consumeTime;

    /**
     * 商户下单接口传的单品信息
     */
    private List<GoodsDetail> goodsDetail;

    /**
     * 核销订单号
     */
    private String transactionId;

}
