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

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * 适用于以下API:
 * <p>
 * 查询与用户授权记录（openId）API
 * <p>
 * 解除用户授权关系（openId）API
 *
 * @author yusiwen
 * @since 1.0.0.RELEASE
 */
@Data
public class PermissionsOpenIdParams {
    /**
     * openId，必填
     */
    @JsonProperty("openid")
    private String openId;
    /**
     * 仅仅适用于解除用户授权关系（openId）API
     */
    @JsonProperty("appid")
    private String appId;
    /**
     * 服务ID，必填
     * <p>
     * 该服务ID有本接口对应产品的权限。
     */
    private String serviceId;
    /**
     * 仅仅适用于解除用户授权关系（openId）API
     */
    private String reason;
}
