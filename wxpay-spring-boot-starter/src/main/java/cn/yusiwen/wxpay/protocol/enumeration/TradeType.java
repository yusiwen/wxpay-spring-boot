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
package cn.yusiwen.wxpay.protocol.enumeration;

/**
 * 微信侧返回交易类型
 *
 * @author yusiwen
 * @since 1.0.0.RELEASE
 */
public enum TradeType {
    /**
     * 公众号支付
     *
     * @since 1.0.0.RELEASE
     */
    JSAPI,
    /**
     * 扫码支付
     *
     * @since 1.0.0.RELEASE
     */
    NATIVE,
    /**
     * APP支付
     *
     * @since 1.0.0.RELEASE
     */
    APP,
    /**
     * 付款码支付
     *
     * @since 1.0.0.RELEASE
     */
    MICROPAY,
    /**
     * H5支付
     *
     * @since 1.0.0.RELEASE
     */
    MWEB,
    /**
     * 刷脸支付
     *
     * @since 1.0.0.RELEASE
     */
    FACEPAY,
}
