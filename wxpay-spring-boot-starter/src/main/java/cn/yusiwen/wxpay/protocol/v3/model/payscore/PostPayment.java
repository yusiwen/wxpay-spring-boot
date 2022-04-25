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
 * 后付费项目,选填
 * <p>
 * 最多包含100条付费项目。如果传入，用户侧则显示此参数。
 *
 * @author yusiwen
 * @since 1.0.0.RELEASE
 */
@Data
public class PostPayment {
    /**
     * 付费项目名称，选填。 修改订单必填
     * <p>
     * 相同订单号下不能出现相同的付费项目名称，当参数长度超过20个字符时，报错处理。
     */
    private String name;
    /**
     * 金额，条件选填。修改订单必填
     * <p>
     * 此付费项目总金额，大于等于0，单位为分，等于0时代表不需要扣费，只能为整数，详见支付金额。如果填写了“付费项目名称”，则amount或description必须填写其一，或都填。
     */
    private Long amount;
    /**
     * 计费说明，条件选填。
     * <p>
     * 描述计费规则，不超过30个字符，超出报错处理。如果填写了“付费项目名称”，则amount或description必须填写其一，或都填。
     */
    private String description;
    /**
     * 付费数量，选填。
     * <p>
     * 付费项目的数量。 特殊规则：数量限制100，不填时默认1。
     */
    private Long count = 1L;

}
