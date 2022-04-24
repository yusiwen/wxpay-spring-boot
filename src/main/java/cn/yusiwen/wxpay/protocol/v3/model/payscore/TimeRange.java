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
 * 服务时间段，必填
 *
 * @author yusiwen
 * @since 1.0.0.RELEASE
 */
@Data
public class TimeRange {
    /**
     * 服务开始时间
     * <p>
     * 用户端展示用途。 用户下单时确认的服务开始时间（比如用户今天下单，明天开始接受服务，这里指的是明天的服务开始时间）。
     * <p>
     * 支持三种格式：yyyyMMddHHmmss、yyyyMMdd和 OnAccept
     * <p>
     * ● 传入20091225091010表示2009年12月25日9点10分10秒。
     * <p>
     * ● 传入20091225默认认为时间为2009年12月25日
     * <p>
     * ● 传入OnAccept表示用户确认订单成功时间为【服务开始时间】。
     * <p>
     * 根据传入时间精准度进行校验
     * <p>
     * 1）若传入时间精准到秒，则校验精准到秒：【服务开始时间】&gt;【商户调用创建订单接口时间
     * <p>
     * 2）若传入时间精准到日，则校验精准到日：【服务开始时间】&gt;=【商户调用创建订单接口时间】
     */
    private String startTime;
    /**
     * 服务开始时间备注说明，服务开始时间有填时，可填写服务开始时间备注，不超过20个字符，超出报错处理。
     */
    private String startTimeRemark;
    /**
     * 用户端展示用途，支持两种格式：yyyyMMddHHmmss和yyyyMMdd ● 传入20091225091010表示2009年12月25日9点10分10秒。
     * <p>
     * ● 传入20091225默认认为时间为2009年12月25日 根据传入时间精准度进行校验
     * <p>
     * 1、若传入时间精准到秒，则校验精准到秒：
     * <p>
     * 1）【预计服务结束时间】&gt;【服务开始时间】
     * <p>
     * 2）【预计服务结束时间】&gt;【商户调用接口时间+1分钟】
     * <p>
     * 2、若传入时间精准到日，则校验精准到日：
     * <p>
     * 1）【预计服务结束时间】&gt;=【服务开始时间】
     * <p>
     * 2）【预计服务结束时间】&gt;=【商户调用接口时间】 【建议】 1、用户下单时【未确定】服务结束时间，不填写。 2、用户下单时【已确定】服务结束时间，填写。
     */
    private String endTime;
    /**
     * 预计服务结束时间备注说明，预计服务结束时间有填时，可填写预计服务结束时间备注，不超过20个字符，超出报错处理。
     */
    private String endTimeRemark;
}
