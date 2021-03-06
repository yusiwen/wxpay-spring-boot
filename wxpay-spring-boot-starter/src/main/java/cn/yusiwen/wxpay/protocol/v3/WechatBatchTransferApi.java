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
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Assert;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.util.UriComponentsBuilder;

import cn.yusiwen.wxpay.protocol.enumeration.FundFlowAccountType;
import cn.yusiwen.wxpay.protocol.enumeration.WeChatServer;
import cn.yusiwen.wxpay.protocol.enumeration.WechatPayV3Type;
import cn.yusiwen.wxpay.protocol.v3.model.batchtransfer.CreateBatchTransferParams;
import cn.yusiwen.wxpay.protocol.v3.model.batchtransfer.QueryBatchTransferDetailParams;
import cn.yusiwen.wxpay.protocol.v3.model.batchtransfer.QueryBatchTransferParams;
import cn.yusiwen.wxpay.protocol.v3.model.batchtransfer.QueryDayBalanceParams;
import cn.yusiwen.wxpay.protocol.v3.model.batchtransfer.QueryIncomeRecordParams;
import cn.yusiwen.wxpay.protocol.v3.model.batchtransfer.TransferDetailElectronicParams;

/**
 * ?????????????????????
 *
 * @author yusiwen
 * @since 1.0.0.RELEASE
 */
public class WechatBatchTransferApi extends AbstractApi {
    /**
     * Instantiates a new Abstract api.
     *
     * @param wechatPayClient
     *            the wechat pay client
     * @param tenantId
     *            the tenant id
     */
    public WechatBatchTransferApi(WechatPayClient wechatPayClient, String tenantId) {
        super(wechatPayClient, tenantId);
    }

    /**
     * ??????????????????API
     *
     * @param createBatchTransferParams
     *            the batchTransferParams
     * @return the wechat response entity
     * @since 1.0.0.RELEASE
     */
    public WechatResponseEntity<ObjectNode> batchTransfer(CreateBatchTransferParams createBatchTransferParams) {
        WechatResponseEntity<ObjectNode> wechatResponseEntity = new WechatResponseEntity<>();
        this.client().withType(WechatPayV3Type.BATCH_TRANSFER_REQ, createBatchTransferParams)
                .function(this::batchTransferFunction).consumer(wechatResponseEntity::convert).request();
        return wechatResponseEntity;
    }

    private RequestEntity<?> batchTransferFunction(WechatPayV3Type type,
            CreateBatchTransferParams createBatchTransferParams) {
        List<CreateBatchTransferParams.TransferDetailListItem> transferDetailList = createBatchTransferParams
                .getTransferDetailList();

        SignatureProvider signatureProvider = this.client().signatureProvider();
        X509WechatCertificateInfo certificate = signatureProvider.getCertificate(this.wechatMetaBean().getTenantId());
        final X509Certificate x509Certificate = certificate.getX509Certificate();
        List<CreateBatchTransferParams.TransferDetailListItem> encrypted = transferDetailList.stream()
                .peek(transferDetailListItem -> {
                    String userName = transferDetailListItem.getUserName();
                    String encryptedUserName = signatureProvider.encryptRequestMessage(userName, x509Certificate);
                    transferDetailListItem.setUserName(encryptedUserName);
                    String userIdCard = transferDetailListItem.getUserIdCard();
                    if (StringUtils.hasText(userIdCard)) {
                        String encryptedUserIdCard = signatureProvider.encryptRequestMessage(userIdCard,
                                x509Certificate);
                        transferDetailListItem.setUserIdCard(encryptedUserIdCard);
                    }
                }).collect(Collectors.toList());

        createBatchTransferParams.setTransferDetailList(encrypted);
        URI uri = UriComponentsBuilder.fromHttpUrl(type.uri(WeChatServer.CHINA)).build().toUri();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Wechatpay-Serial", certificate.getWechatPaySerial());
        return post(uri, createBatchTransferParams, httpHeaders);
    }

    /**
     * ?????????????????????????????????API
     *
     * @param queryBatchTransferParams
     *            the queryBatchTransferParams
     * @return the wechat response entity
     * @since 1.0.0.RELEASE
     */
    public WechatResponseEntity<ObjectNode> queryBatchByBatchId(QueryBatchTransferParams queryBatchTransferParams) {
        WechatResponseEntity<ObjectNode> wechatResponseEntity = new WechatResponseEntity<>();
        this.client().withType(WechatPayV3Type.BATCH_TRANSFER_BATCH_ID, queryBatchTransferParams)
                .function((type, params) -> {
                    MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();
                    queryParams.add("need_query_detail", params.getNeedQueryDetail().toString());
                    queryParams.add("offset", params.getOffset().toString());
                    queryParams.add("limit", params.getLimit().toString());
                    queryParams.add("detail_status", params.getDetailStatus().name());

                    URI uri = UriComponentsBuilder.fromHttpUrl(type.uri(WeChatServer.CHINA)).queryParams(queryParams)
                            .build().expand(params.getCode()).toUri();
                    return get(uri);
                }).consumer(wechatResponseEntity::convert).request();
        return wechatResponseEntity;
    }

    /**
     * ?????????????????????????????????API
     *
     * @param queryBatchTransferDetailParams
     *            the queryBatchTransferDetailParams
     * @return the wechat response entity
     * @since 1.0.0.RELEASE
     */
    public WechatResponseEntity<ObjectNode> queryBatchDetailByWechat(
            QueryBatchTransferDetailParams queryBatchTransferDetailParams) {
        WechatResponseEntity<ObjectNode> wechatResponseEntity = new WechatResponseEntity<>();
        this.client().withType(WechatPayV3Type.BATCH_TRANSFER_DETAIL_WECHAT, queryBatchTransferDetailParams)
                .function((type, params) -> {
                    Map<String, String> pathParams = new HashMap<>(2);
                    pathParams.put("batch_id", params.getBatchIdOrOutBatchNo());
                    pathParams.put("detail_id", params.getDetailIdOrOutDetailNo());

                    URI uri = UriComponentsBuilder.fromHttpUrl(type.uri(WeChatServer.CHINA)).build().expand(pathParams)
                            .toUri();
                    return get(uri);
                }).consumer(wechatResponseEntity::convert).request();
        return wechatResponseEntity;
    }

    /**
     * ?????????????????????????????????API
     *
     * @param queryBatchTransferParams
     *            the queryBatchTransferParams
     * @return the wechat response entity
     * @since 1.0.0.RELEASE
     */
    public WechatResponseEntity<ObjectNode> queryBatchByOutBatchNo(QueryBatchTransferParams queryBatchTransferParams) {
        WechatResponseEntity<ObjectNode> wechatResponseEntity = new WechatResponseEntity<>();
        this.client().withType(WechatPayV3Type.BATCH_TRANSFER_OUT_BATCH_NO, queryBatchTransferParams)
                .function((type, params) -> {
                    MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();
                    queryParams.add("need_query_detail", params.getNeedQueryDetail().toString());
                    queryParams.add("offset", params.getOffset().toString());
                    queryParams.add("limit", params.getLimit().toString());
                    queryParams.add("detail_status", params.getDetailStatus().name());

                    URI uri = UriComponentsBuilder.fromHttpUrl(type.uri(WeChatServer.CHINA)).queryParams(queryParams)
                            .build().expand(params.getCode()).toUri();
                    return get(uri);
                }).consumer(wechatResponseEntity::convert).request();
        return wechatResponseEntity;
    }

    /**
     * ?????????????????????????????????API
     *
     * @param queryBatchTransferDetailParams
     *            the queryBatchTransferDetailParams
     * @return the wechat response entity
     * @since 1.0.0.RELEASE
     */
    public WechatResponseEntity<ObjectNode> queryBatchDetailByMch(
            QueryBatchTransferDetailParams queryBatchTransferDetailParams) {
        WechatResponseEntity<ObjectNode> wechatResponseEntity = new WechatResponseEntity<>();
        this.client().withType(WechatPayV3Type.BATCH_TRANSFER_DETAIL_MCH, queryBatchTransferDetailParams)
                .function((type, params) -> {
                    Map<String, String> pathParams = new HashMap<>(2);
                    pathParams.put("batch_id", params.getBatchIdOrOutBatchNo());
                    pathParams.put("detail_id", params.getDetailIdOrOutDetailNo());

                    URI uri = UriComponentsBuilder.fromHttpUrl(type.uri(WeChatServer.CHINA)).build().expand(pathParams)
                            .toUri();
                    return get(uri);
                }).consumer(wechatResponseEntity::convert).request();
        return wechatResponseEntity;
    }

    /**
     * ??????????????????????????????API
     *
     * @param outBatchNo
     *            the outBatchNo
     * @return the wechat response entity
     * @since 1.0.0.RELEASE
     */
    public WechatResponseEntity<ObjectNode> receiptBill(String outBatchNo) {
        WechatResponseEntity<ObjectNode> wechatResponseEntity = new WechatResponseEntity<>();
        this.client().withType(WechatPayV3Type.BATCH_TRANSFER_BILL_RECEIPT, outBatchNo).function((type, batchNo) -> {
            Map<String, String> body = new HashMap<>(1);
            body.put("out_batch_no", outBatchNo);
            URI uri = UriComponentsBuilder.fromHttpUrl(type.uri(WeChatServer.CHINA)).build().toUri();
            return post(uri, body);
        }).consumer(wechatResponseEntity::convert).request();
        return wechatResponseEntity;
    }

    /**
     * ?????????????????????????????????API
     *
     * @param outBatchNo
     *            the outBatchNo
     * @return the wechat response entity
     * @since 1.0.0.RELEASE
     */
    public ResponseEntity<Resource> downloadBill(String outBatchNo) {
        WechatResponseEntity<ObjectNode> wechatResponseEntity = new WechatResponseEntity<>();
        this.client().withType(WechatPayV3Type.BATCH_TRANSFER_DOWNLOAD_BILL, outBatchNo).function((type, batchNo) -> {
            URI uri = UriComponentsBuilder.fromHttpUrl(type.uri(WeChatServer.CHINA)).build().expand(batchNo).toUri();
            return get(uri);
        }).consumer(wechatResponseEntity::convert).request();
        String downloadUrl = wechatResponseEntity.getBody().get("download_url").asText();
        Assert.hasText(downloadUrl, "download url has no text");
        return this.billResource(downloadUrl);
    }

    /**
     * ??????????????????????????????API
     * <p>
     * ??????????????????????????????????????????????????????????????????????????????????????????????????????????????????
     * <p>
     * ??????????????????????????????{@link #downloadBillResponse(String, String)}????????????
     *
     * @param params
     *            the params
     * @return the wechat response entity
     * @since 1.0.0.RELEASE
     */
    public WechatResponseEntity<ObjectNode> transferElectronic(TransferDetailElectronicParams params) {
        WechatResponseEntity<ObjectNode> wechatResponseEntity = new WechatResponseEntity<>();
        this.client().withType(WechatPayV3Type.BATCH_TRANSFER_ELECTRONIC, params)
                .function((type, transferDetailElectronic) -> {
                    URI uri = UriComponentsBuilder.fromHttpUrl(type.uri(WeChatServer.CHINA)).build().toUri();
                    return post(uri, transferDetailElectronic);
                }).consumer(wechatResponseEntity::convert).request();
        return wechatResponseEntity;
    }

    /**
     * ??????????????????????????????????????????API
     * <p>
     * ????????????????????????????????????????????????????????????????????????????????????????????????????????????????????? ???????????????????????????????????????????????????hash?????????????????????????????????????????????
     * <p>
     * ??????????????????????????????{@link #downloadBillResponse(String, String)}????????????
     *
     * @param params
     *            the params
     * @return the wechat response entity
     * @since 1.0.0.RELEASE
     */
    public WechatResponseEntity<ObjectNode> queryTransferElectronicResult(TransferDetailElectronicParams params) {
        WechatResponseEntity<ObjectNode> wechatResponseEntity = new WechatResponseEntity<>();
        this.client().withType(WechatPayV3Type.BATCH_TRANSFER_ELECTRONIC_DETAIL, params)
                .function((type, transferDetailElectronic) -> {
                    MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();
                    queryParams.add("accept_type", transferDetailElectronic.getAcceptType().name());
                    queryParams.add("out_batch_no", transferDetailElectronic.getOutBatchNo());
                    queryParams.add("out_detail_no", transferDetailElectronic.getOutDetailNo());

                    URI uri = UriComponentsBuilder.fromHttpUrl(type.uri(WeChatServer.CHINA)).queryParams(queryParams)
                            .build().toUri();
                    return get(uri);
                }).consumer(wechatResponseEntity::convert).request();
        return wechatResponseEntity;
    }

    /**
     * ????????????????????????API
     * <p>
     * ?????????????????????????????????????????????????????????????????????
     *
     * @param accountType
     *            the account type
     * @return the wechat response entity
     * @since 1.0.0.RELEASE
     */
    public WechatResponseEntity<ObjectNode> queryFundBalance(FundFlowAccountType accountType) {
        WechatResponseEntity<ObjectNode> wechatResponseEntity = new WechatResponseEntity<>();
        this.client().withType(WechatPayV3Type.BATCH_TRANSFER_FUND_BALANCE, accountType)
                .function((type, flowAccountType) -> {
                    URI uri = UriComponentsBuilder.fromHttpUrl(type.uri(WeChatServer.CHINA)).build()
                            .expand(flowAccountType.name()).toUri();
                    return get(uri);
                }).consumer(wechatResponseEntity::convert).request();
        return wechatResponseEntity;
    }

    /**
     * ????????????????????????API
     * <p>
     * ?????????????????????????????????????????????????????????24?????????????????????
     *
     * @param queryDayBalanceParams
     *            the transfer day balance params
     * @return the wechat response entity
     * @since 1.0.0.RELEASE
     */
    public WechatResponseEntity<ObjectNode> queryDayFundBalance(QueryDayBalanceParams queryDayBalanceParams) {
        WechatResponseEntity<ObjectNode> wechatResponseEntity = new WechatResponseEntity<>();
        this.client().withType(WechatPayV3Type.BATCH_TRANSFER_FUND_DAY_BALANCE, queryDayBalanceParams)
                .function((type, params) -> {
                    URI uri = UriComponentsBuilder.fromHttpUrl(type.uri(WeChatServer.CHINA))
                            .queryParam("date", params.getDate().toString()).build()
                            .expand(params.getAccountType().name()).toUri();
                    return get(uri);
                }).consumer(wechatResponseEntity::convert).request();
        return wechatResponseEntity;
    }

    /**
     * ????????????????????????API
     * <p>
     * ?????????????????????????????????????????????????????????????????????????????? ?????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
     * <p>
     * ?????????
     * <ol>
     * <li>?????????????????????????????????????????????????????????????????????????????????????????????</li>
     * <li>???????????????????????????????????????????????????????????????????????????</li>
     * <li>???????????????????????????????????????????????????????????????????????????????????????</li>
     * <li>?????????????????????90?????????????????????????????????????????????????????????????????????????????????????????????????????????????????????</li>
     * <li>????????????????????????????????????????????????????????????????????????????????????????????????</li>
     * <li>?????????????????????????????????????????????50TPS???</li>
     * </ol>
     *
     * @param queryIncomeRecordParams
     *            the transfer day balance params
     * @return the wechat response entity
     * @since 1.0.0.RELEASE
     */
    public WechatResponseEntity<ObjectNode> queryIncomeRecords(QueryIncomeRecordParams queryIncomeRecordParams) {
        WechatResponseEntity<ObjectNode> wechatResponseEntity = new WechatResponseEntity<>();
        this.client().withType(WechatPayV3Type.BATCH_TRANSFER_FUND_INCOME_RECORDS, queryIncomeRecordParams)
                .function((type, params) -> {
                    MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();
                    queryParams.add("account_type", params.getAccountType().name());
                    queryParams.add("date", params.getDate().toString());
                    queryParams.add("offset", Optional.ofNullable(params.getOffset()).orElse(0).toString());
                    queryParams.add("limit", params.getLimit().toString());
                    URI uri = UriComponentsBuilder.fromHttpUrl(type.uri(WeChatServer.CHINA)).queryParams(queryParams)
                            .build().toUri();
                    return get(uri);
                }).consumer(wechatResponseEntity::convert).request();
        return wechatResponseEntity;
    }
}
