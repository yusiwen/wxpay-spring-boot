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
package cn.yusiwen.wxpay.protocol.v3;

import com.fasterxml.jackson.databind.node.ObjectNode;

import java.net.URI;

import org.springframework.web.util.UriComponentsBuilder;

import cn.yusiwen.wxpay.WechatPayProperties;
import cn.yusiwen.wxpay.protocol.enumeration.WeChatServer;
import cn.yusiwen.wxpay.protocol.enumeration.WechatPayV3Type;
import cn.yusiwen.wxpay.protocol.v3.model.discountcard.DiscountCardPreRequestParams;
import cn.yusiwen.wxpay.protocol.v3.model.discountcard.UserRecordsParams;

/**
 * 微信支付先享卡.
 *
 * @author yusiwen
 * @since 1.0.0.RELEASE
 */
public class WechatDiscountCardApi extends AbstractApi {
    /**
     * Instantiates a new Abstract api.
     *
     * @param wechatPayClient
     *            the wechat pay client
     * @param tenantId
     *            the tenant id
     */
    public WechatDiscountCardApi(WechatPayClient wechatPayClient, String tenantId) {
        super(wechatPayClient, tenantId);
    }

    /**
     * 预受理领卡请求API
     * <p>
     * 商户在引导用户跳转先享卡领卡前，需要请求先享卡预受理领卡请求接口，再根据返回数据引导用户跳转领卡。
     *
     * @param params
     *            the params
     * @return the wechat response entity
     */
    public WechatResponseEntity<ObjectNode> preRequest(DiscountCardPreRequestParams params) {
        WechatResponseEntity<ObjectNode> wechatResponseEntity = new WechatResponseEntity<>();
        this.client().withType(WechatPayV3Type.DISCOUNT_CARD_PRE_REQUEST, params)
                .function((wechatPayV3Type, requestParams) -> {
                    URI uri = UriComponentsBuilder.fromHttpUrl(wechatPayV3Type.uri(WeChatServer.CHINA)).build().toUri();

                    WechatPayProperties.V3 v3 = this.wechatMetaBean().getV3();
                    requestParams.setAppId(v3.getAppId());
                    requestParams.setNotifyUrl(v3.getDomain().concat(requestParams.getNotifyUrl()));
                    return post(uri, requestParams);
                }).consumer(wechatResponseEntity::convert).request();
        return wechatResponseEntity;
    }

    /**
     * 增加用户记录API
     * <p>
     * 当用户在商户侧消费时，用户完成了微信先享卡的目标或者获取使用优惠时，商户需要把这个信息同步给微信先享卡平台，用于在微信先享卡小程序展示及先享卡到期后的用户结算。
     *
     * @param params
     *            the params
     * @return 返回http状态码 204 处理成功，应答无内容
     */
    public WechatResponseEntity<ObjectNode> addUserRecords(UserRecordsParams params) {
        WechatResponseEntity<ObjectNode> wechatResponseEntity = new WechatResponseEntity<>();
        this.client().withType(WechatPayV3Type.DISCOUNT_CARD_ADD_USER_RECORDS, params)
                .function((wechatPayV3Type, recordsParams) -> {
                    URI uri = UriComponentsBuilder.fromHttpUrl(wechatPayV3Type.uri(WeChatServer.CHINA)).build()
                            .expand(recordsParams.getOutCardCode()).toUri();
                    recordsParams.setOutCardCode(null);
                    return post(uri, recordsParams);
                }).consumer(wechatResponseEntity::convert).request();
        return wechatResponseEntity;
    }

    /**
     * 查询先享卡订单API
     * <p>
     * 商户可以通过商户领卡号查询指定的先享卡，可用于对账或者界面展示。
     *
     * @param outCardCode
     *            商户领卡号
     * @return the wechat response entity
     */
    public WechatResponseEntity<ObjectNode> addUserRecords(String outCardCode) {
        WechatResponseEntity<ObjectNode> wechatResponseEntity = new WechatResponseEntity<>();
        this.client().withType(WechatPayV3Type.DISCOUNT_CARD_INFO, outCardCode)
                .function((wechatPayV3Type, cardCode) -> {
                    URI uri = UriComponentsBuilder.fromHttpUrl(wechatPayV3Type.uri(WeChatServer.CHINA)).build()
                            .expand(cardCode).toUri();
                    return get(uri);
                }).consumer(wechatResponseEntity::convert).request();
        return wechatResponseEntity;
    }
}
