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

import org.springframework.http.RequestEntity;
import org.springframework.web.util.UriComponentsBuilder;

import cn.yusiwen.wxpay.WechatPayProperties;
import cn.yusiwen.wxpay.protocol.enumeration.WeChatServer;
import cn.yusiwen.wxpay.protocol.enumeration.WechatPayV3Type;
import cn.yusiwen.wxpay.protocol.v3.model.combine.CombineCloseParams;
import cn.yusiwen.wxpay.protocol.v3.model.combine.CombineH5PayParams;
import cn.yusiwen.wxpay.protocol.v3.model.combine.CombinePayParams;

/**
 * 微信合单支付.
 *
 * @author yusiwen
 * @since 1.0.0.RELEASE
 */
public class WechatCombinePayApi extends AbstractApi {
    /**
     * Instantiates a new Abstract api.
     *
     * @param wechatPayClient
     *            the wechat pay client
     * @param tenantId
     *            the tenant id
     */
    public WechatCombinePayApi(WechatPayClient wechatPayClient, String tenantId) {
        super(wechatPayClient, tenantId);
    }

    /**
     * 合单下单-APP支付API
     * <p>
     * 使用合单支付接口，用户只输入一次密码，即可完成多个订单的支付。目前最多一次可支持50笔订单进行合单支付。
     * <p>
     * 注意： •
     * 订单如果需要进行抽佣等，需要在合单中指定需要进行分账（profit_sharing为true）；指定后，交易资金进入二级商户账户，处于冻结状态，可在后续使用分账接口进行分账，利用分账完结进行资金解冻，实现抽佣和对二级商户的账期。
     *
     * @param combinePayParams
     *            the combine pay params
     * @return the wechat response entity
     */
    public WechatResponseEntity<ObjectNode> appPay(CombinePayParams combinePayParams) {
        WechatResponseEntity<ObjectNode> wechatResponseEntity = new WechatResponseEntity<>();
        this.client().withType(WechatPayV3Type.COMBINE_APP, combinePayParams).function(this::combinePayFunction)
                .consumer(wechatResponseEntity::convert).request();
        return wechatResponseEntity;
    }

    /**
     * 合单下单-JSAPI支付/小程序支付API
     * <p>
     * 使用合单支付接口，用户只输入一次密码，即可完成多个订单的支付。目前最多一次可支持50笔订单进行合单支付。
     * <p>
     * 注意： •
     * 订单如果需要进行抽佣等，需要在合单中指定需要进行分账（profit_sharing为true）；指定后，交易资金进入二级商户账户，处于冻结状态，可在后续使用分账接口进行分账，利用分账完结进行资金解冻，实现抽佣和对二级商户的账期。
     *
     * @param combinePayParams
     *            the combine pay params
     * @return wechat response entity
     */
    public WechatResponseEntity<ObjectNode> jsPay(CombinePayParams combinePayParams) {
        WechatResponseEntity<ObjectNode> wechatResponseEntity = new WechatResponseEntity<>();
        this.client().withType(WechatPayV3Type.COMBINE_JSAPI, combinePayParams).function(this::combinePayFunction)
                .consumer(wechatResponseEntity::convert).request();
        return wechatResponseEntity;
    }

    /**
     * Combine pay function request entity.
     *
     * @param type
     *            the type
     * @param params
     *            the params
     * @return the request entity
     */
    private RequestEntity<?> combinePayFunction(WechatPayV3Type type, CombinePayParams params) {
        WechatPayProperties.V3 v3 = this.wechatMetaBean().getV3();
        params.setCombineAppid(v3.getAppId());
        params.setCombineMchid(v3.getMchId());

        String notifyUrl = v3.getDomain().concat(params.getNotifyUrl());
        params.setNotifyUrl(notifyUrl);
        URI uri = UriComponentsBuilder.fromHttpUrl(type.uri(WeChatServer.CHINA)).build().toUri();
        return post(uri, params);
    }

    /**
     * 合单下单-H5支付API.
     * <p>
     * 使用合单支付接口，用户只输入一次密码，即可完成多个订单的支付。目前最多一次可支持50笔订单进行合单支付。
     * <p>
     * 注意： •
     * 订单如果需要进行抽佣等，需要在合单中指定需要进行分账（profit_sharing为true）；指定后，交易资金进入二级商户账户，处于冻结状态，可在后续使用分账接口进行分账，利用分账完结进行资金解冻，实现抽佣和对二级商户的账期。
     *
     * @param combineH5PayParams
     *            the combine h 5 pay params
     * @return the wechat response entity
     */
    public WechatResponseEntity<ObjectNode> h5Pay(CombineH5PayParams combineH5PayParams) {
        WechatResponseEntity<ObjectNode> wechatResponseEntity = new WechatResponseEntity<>();
        this.client().withType(WechatPayV3Type.COMBINE_MWEB, combineH5PayParams).function(this::combinePayFunction)
                .consumer(wechatResponseEntity::convert).request();
        return wechatResponseEntity;
    }

    /**
     * Combine pay function request entity.
     *
     * @param type
     *            the type
     * @param params
     *            the params
     * @return the request entity
     */
    private RequestEntity<?> combinePayFunction(WechatPayV3Type type, CombineH5PayParams params) {
        WechatPayProperties.V3 v3 = this.wechatMetaBean().getV3();
        params.setCombineAppid(v3.getAppId());
        params.setCombineMchid(v3.getMchId());

        String notifyUrl = v3.getDomain().concat(params.getNotifyUrl());
        params.setNotifyUrl(notifyUrl);
        URI uri = UriComponentsBuilder.fromHttpUrl(type.uri(WeChatServer.CHINA)).build().toUri();
        return post(uri, params);
    }

    /**
     * 合单下单-Native支付API.
     * <p>
     * 使用合单支付接口，用户只输入一次密码，即可完成多个订单的支付。目前最多一次可支持50笔订单进行合单支付。
     * <p>
     * 注意： •
     * 订单如果需要进行抽佣等，需要在合单中指定需要进行分账（profit_sharing为true）；指定后，交易资金进入二级商户账户，处于冻结状态，可在后续使用分账接口进行分账，利用分账完结进行资金解冻，实现抽佣和对二级商户的账期。
     *
     * @param combinePayParams
     *            the combine pay params
     * @return the wechat response entity
     */
    public WechatResponseEntity<ObjectNode> nativePay(CombinePayParams combinePayParams) {
        WechatResponseEntity<ObjectNode> wechatResponseEntity = new WechatResponseEntity<>();
        this.client().withType(WechatPayV3Type.COMBINE_NATIVE, combinePayParams).function(this::combinePayFunction)
                .consumer(wechatResponseEntity::convert).request();
        return wechatResponseEntity;
    }

    /**
     * 合单查询订单API.
     *
     * @param combineOutTradeNo
     *            the combine out trade no
     * @return the wechat response entity
     */
    public WechatResponseEntity<ObjectNode> queryTransactionByOutTradeNo(String combineOutTradeNo) {
        WechatResponseEntity<ObjectNode> wechatResponseEntity = new WechatResponseEntity<>();
        this.client().withType(WechatPayV3Type.COMBINE_TRANSACTION_OUT_TRADE_NO, combineOutTradeNo)
                .function((wechatPayV3Type, outTradeNo) -> {
                    URI uri = UriComponentsBuilder.fromHttpUrl(wechatPayV3Type.uri(WeChatServer.CHINA)).build()
                            .expand(outTradeNo).toUri();
                    return get(uri);
                }).consumer(wechatResponseEntity::convert).request();
        return wechatResponseEntity;
    }

    /**
     * 合单关闭订单API.
     * <p>
     * 合单支付订单只能使用此合单关单api完成关单。
     * <p>
     * 微信服务器返回 204。
     *
     * @param combineCloseParams
     *            the combine close params
     * @return the wechat response entity
     */
    public WechatResponseEntity<ObjectNode> close(CombineCloseParams combineCloseParams) {
        WechatResponseEntity<ObjectNode> wechatResponseEntity = new WechatResponseEntity<>();
        this.client().withType(WechatPayV3Type.COMBINE_CLOSE, combineCloseParams)
                .function((wechatPayV3Type, params) -> {
                    URI uri = UriComponentsBuilder.fromHttpUrl(wechatPayV3Type.uri(WeChatServer.CHINA)).build().toUri();
                    return post(uri, params);
                }).consumer(wechatResponseEntity::convert).request();
        return wechatResponseEntity;
    }
}
