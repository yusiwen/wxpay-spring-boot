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

import java.time.OffsetDateTime;

import lombok.Data;

import cn.yusiwen.wxpay.protocol.enumeration.StockStatus;

/**
 * 查询参数，适用以下接口：
 * <p>
 * 条件查询批次列表API
 *
 * @author yusiwen
 * @since 1.0.0.RELEASE
 */
@Data
public class StocksQueryParams {
    /**
     * 必填
     * <p>
     * 条件查询批次列表API 页码从0开始，默认第0页，传递1可能出错。
     */
    private Integer offset = 0;
    /**
     * 必填
     * <p>
     * 条件查询批次列表API 分页大小，最大10。
     */
    private Integer limit = 10;
    /**
     * 选填
     * <p>
     * 起始时间 最终满足格式 {@code yyyy-MM-dd'T'HH:mm:ss.SSSXXX}
     */
    private OffsetDateTime createStartTime;
    /**
     * 选填
     * <p>
     * 终止时间 最终满足格式 {@code yyyy-MM-dd'T'HH:mm:ss.SSSXXX}
     */
    private OffsetDateTime createEndTime;
    /**
     * 根据API而定
     * <p>
     * 批次状态
     */
    private StockStatus status;
}
