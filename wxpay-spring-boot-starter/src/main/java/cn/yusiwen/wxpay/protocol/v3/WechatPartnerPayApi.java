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
import java.security.PrivateKey;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import org.springframework.http.RequestEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriComponentsBuilder;

import cn.yusiwen.wxpay.PayException;
import cn.yusiwen.wxpay.WechatPayProperties;
import cn.yusiwen.wxpay.protocol.enumeration.WeChatServer;
import cn.yusiwen.wxpay.protocol.enumeration.WechatPayV3Type;
import cn.yusiwen.wxpay.protocol.v3.model.TransactionQueryParams;
import cn.yusiwen.wxpay.protocol.v3.model.partner.CloseTransParams;
import cn.yusiwen.wxpay.protocol.v3.model.partner.PartnerPayParams;

/**
 * 普通支付-服务商模式
 *
 * @author yusiwen
 * @since 1.0.0.RELEASE
 */
public class WechatPartnerPayApi extends AbstractApi {
    /**
     * Instantiates a new Abstract api.
     *
     * @param wechatPayClient
     *            the wechat pay client
     * @param tenantId
     *            the tenant id
     */
    public WechatPartnerPayApi(WechatPayClient wechatPayClient, String tenantId) {
        super(wechatPayClient, tenantId);
    }

    /**
     * APP下单API
     *
     * @param partnerPayParams
     *            the partner pay params
     * @return the wechat response entity
     * @since 1.0.0.RELEASE
     */
    public WechatResponseEntity<ObjectNode> appPay(PartnerPayParams partnerPayParams) {
        WechatResponseEntity<ObjectNode> wechatResponseEntity = new WechatResponseEntity<>();
        this.client().withType(WechatPayV3Type.APP_PARTNER, partnerPayParams).function(this::payFunction)
                .consumer(responseEntity -> {
                    ObjectNode body = responseEntity.getBody();
                    if (Objects.isNull(body)) {
                        throw new PayException("response body cannot be resolved");
                    }

                    SignatureProvider signatureProvider = this.client().signatureProvider();
                    WechatMetaContainer wechatMetaContainer = signatureProvider.wechatMetaContainer();
                    WechatMetaBean wechatMetaBean = wechatMetaContainer.getWechatMeta(tenantId());
                    PrivateKey privateKey = wechatMetaBean.getKeyPair().getPrivate();

                    String subAppId = partnerPayParams.getSubAppId();
                    long epochSecond = LocalDateTime.now().toEpochSecond(ZoneOffset.of("+8"));
                    String timestamp = String.valueOf(epochSecond);
                    String nonceStr = signatureProvider.nonceStrGenerator().generateId().toString().replaceAll("-", "");
                    String prepayId = body.get("prepay_id").asText();
                    String paySign = signatureProvider.doRequestSign(privateKey, subAppId, timestamp, nonceStr,
                            prepayId);

                    body.put("appid", subAppId);
                    body.put("partnerid", partnerPayParams.getSubMchId());
                    body.put("prepayid", prepayId);
                    body.put("package", "Sign=WXPay");
                    body.put("nonceStr", nonceStr);
                    body.put("timeStamp", timestamp);
                    body.put("signType", "RSA");
                    body.put("paySign", paySign);

                    wechatResponseEntity.setHttpStatus(responseEntity.getStatusCodeValue());
                    wechatResponseEntity.setBody(body);
                }).request();
        return wechatResponseEntity;
    }

    /**
     * JSAPI/小程序下单API
     *
     * @param partnerPayParams
     *            the pay params
     * @return wechat response entity
     * @since 1.0.0.RELEASE
     */
    public WechatResponseEntity<ObjectNode> jsPay(PartnerPayParams partnerPayParams) {
        WechatResponseEntity<ObjectNode> wechatResponseEntity = new WechatResponseEntity<>();
        this.client().withType(WechatPayV3Type.JSAPI_PARTNER, partnerPayParams).function(this::payFunction)
                .consumer(responseEntity -> {
                    ObjectNode body = responseEntity.getBody();
                    if (Objects.isNull(body)) {
                        throw new PayException("response body cannot be resolved");
                    }

                    SignatureProvider signatureProvider = this.client().signatureProvider();
                    WechatMetaContainer wechatMetaContainer = signatureProvider.wechatMetaContainer();
                    WechatMetaBean wechatMetaBean = wechatMetaContainer.getWechatMeta(tenantId());
                    PrivateKey privateKey = wechatMetaBean.getKeyPair().getPrivate();

                    String subAppId = partnerPayParams.getSubAppId();
                    long epochSecond = LocalDateTime.now().toEpochSecond(ZoneOffset.of("+8"));
                    String timestamp = String.valueOf(epochSecond);
                    String nonceStr = signatureProvider.nonceStrGenerator().generateId().toString().replaceAll("-", "");
                    String packageStr = "prepay_id=" + body.get("prepay_id").asText();
                    String paySign = signatureProvider.doRequestSign(privateKey, subAppId, timestamp, nonceStr,
                            packageStr);

                    body.put("appId", subAppId);
                    body.put("timeStamp", timestamp);
                    body.put("nonceStr", nonceStr);
                    body.put("package", packageStr);
                    body.put("signType", "RSA");
                    body.put("paySign", paySign);

                    wechatResponseEntity.setHttpStatus(responseEntity.getStatusCodeValue());
                    wechatResponseEntity.setBody(body);
                }).request();
        return wechatResponseEntity;
    }

    /**
     * Native下单API
     *
     * @param partnerPayParams
     *            the pay params
     * @return wechat response entity
     * @since 1.0.0.RELEASE
     */
    public WechatResponseEntity<ObjectNode> nativePay(PartnerPayParams partnerPayParams) {
        WechatResponseEntity<ObjectNode> wechatResponseEntity = new WechatResponseEntity<>();
        this.client().withType(WechatPayV3Type.NATIVE_PARTNER, partnerPayParams).function(this::payFunction)
                .consumer(wechatResponseEntity::convert).request();
        return wechatResponseEntity;
    }

    /**
     * H5下单API
     *
     * @param partnerPayParams
     *            the partner pay params
     * @return wechat response entity
     * @since 1.0.0.RELEASE
     */
    public WechatResponseEntity<ObjectNode> h5Pay(PartnerPayParams partnerPayParams) {
        WechatResponseEntity<ObjectNode> wechatResponseEntity = new WechatResponseEntity<>();
        this.client().withType(WechatPayV3Type.MWEB_PARTNER, partnerPayParams).function(this::payFunction)
                .consumer(wechatResponseEntity::convert).request();
        return wechatResponseEntity;
    }

    private RequestEntity<?> payFunction(WechatPayV3Type type, PartnerPayParams partnerPayParams) {
        WechatPayProperties.V3 v3 = this.wechatMetaBean().getV3();
        partnerPayParams.setSpAppId(v3.getAppId());
        partnerPayParams.setSpMchId(v3.getMchId());
        String notifyUrl = v3.getDomain().concat(partnerPayParams.getNotifyUrl());
        partnerPayParams.setNotifyUrl(notifyUrl);
        URI uri = UriComponentsBuilder.fromHttpUrl(type.uri(WeChatServer.CHINA)).build().toUri();
        return post(uri, partnerPayParams);
    }

    /**
     * 微信支付订单号查询API
     *
     * @param params
     *            the params
     * @return the wechat response entity
     * @since 1.0.0.RELEASE
     */
    public WechatResponseEntity<ObjectNode> queryTransactionById(TransactionQueryParams params) {
        WechatResponseEntity<ObjectNode> wechatResponseEntity = new WechatResponseEntity<>();
        this.client().withType(WechatPayV3Type.TRANSACTION_TRANSACTION_ID_PARTNER, params)
                .function(this::queryTransactionFunction).consumer(wechatResponseEntity::convert).request();
        return wechatResponseEntity;
    }

    /**
     * 商户订单号查询API
     *
     * @param params
     *            the params
     * @return the wechat response entity
     * @since 1.0.0.RELEASE
     */
    public WechatResponseEntity<ObjectNode> queryTransactionByOutTradeNo(TransactionQueryParams params) {
        WechatResponseEntity<ObjectNode> wechatResponseEntity = new WechatResponseEntity<>();
        this.client().withType(WechatPayV3Type.TRANSACTION_OUT_TRADE_NO_PARTNER, params)
                .function(this::queryTransactionFunction).consumer(wechatResponseEntity::convert).request();
        return wechatResponseEntity;
    }

    private RequestEntity<?> queryTransactionFunction(WechatPayV3Type type, TransactionQueryParams params) {
        WechatPayProperties.V3 v3 = this.wechatMetaBean().getV3();

        MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();
        queryParams.add("sp_mchid", v3.getMchId());
        queryParams.add("sub_mchid", params.getMchId());

        URI uri = UriComponentsBuilder.fromHttpUrl(type.uri(WeChatServer.CHINA)).queryParams(queryParams).build()
                .expand(params.getTransactionIdOrOutTradeNo()).toUri();
        return get(uri);
    }

    /**
     * 关单API
     *
     * @param closeTransParams
     *            the closeTransParams
     * @return the wechat response entity
     * @since 1.0.0.RELEASE
     */
    public WechatResponseEntity<ObjectNode> close(CloseTransParams closeTransParams) {
        WechatResponseEntity<ObjectNode> wechatResponseEntity = new WechatResponseEntity<>();
        this.client().withType(WechatPayV3Type.CLOSE_PARTNER, closeTransParams)
                .function(this::closeByOutTradeNoFunction).consumer(wechatResponseEntity::convert).request();
        return wechatResponseEntity;
    }

    private RequestEntity<?> closeByOutTradeNoFunction(WechatPayV3Type type, CloseTransParams closeTransParams) {
        WechatPayProperties.V3 v3 = this.wechatMetaBean().getV3();

        Map<String, String> queryParams = new HashMap<>(1);
        queryParams.put("sp_mchid", v3.getMchId());
        queryParams.put("sub_mchid", closeTransParams.getSubMchId());

        URI uri = UriComponentsBuilder.fromHttpUrl(type.uri(WeChatServer.CHINA)).build()
                .expand(closeTransParams.getOutTradeNo()).toUri();
        return post(uri, queryParams);
    }

}
