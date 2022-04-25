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
package cn.yusiwen.wxpay.protocol.v3.model.busifavor;

import lombok.Data;

import org.springframework.web.multipart.MultipartFile;

import cn.yusiwen.wxpay.protocol.enumeration.CouponBgColor;
import cn.yusiwen.wxpay.protocol.v3.WechatMarketingFavorApi;

/**
 * 商家券样式信息.
 *
 * @author yusiwen
 * @since 1.0.0.RELEASE
 */
@Data
public class DisplayPatternInfo {

    /**
     * 背景颜色
     */
    private CouponBgColor backgroundColor;
    /**
     * 商户logo
     * <ol>
     * <li>商户logo大小需为120像素*120像素</li>
     * <li>支持JPG、JPEG、PNG格式，且图片小于1M</li>
     * </ol>
     * 仅支持通过
     * <a target= "_blank" href= "https://pay.weixin.qq.com/wiki/doc/apiv3/wxpay/marketing/chapter3_1.shtml">图片上传API</a>
     * 接口获取的图片URL地址。
     *
     * @see WechatMarketingFavorApi#marketingImageUpload(MultipartFile)
     */
    private String merchantLogoUrl;
    /**
     * 券详情图片
     * <ol>
     * <li>需为850像素*350像素</li>
     * <li>图片大小不超过2M</li>
     * <li>支持JPG/PNG格式</li>
     * </ol>
     * 仅支持通过
     * <a target= "_blank" href= "https://pay.weixin.qq.com/wiki/doc/apiv3/wxpay/marketing/chapter3_1.shtml">图片上传API</a>
     * 接口获取的图片URL地址。
     *
     * @see WechatMarketingFavorApi#marketingImageUpload(MultipartFile)
     */
    private String couponImageUrl;
    /**
     * 使用须知
     * <p>
     * 用于说明详细的活动规则，会展示在代金券详情页。
     * <p>
     * 示例值：xxx门店可用
     */
    private String description;
    /**
     * 商户名称,字数上限为16个
     */
    private String merchantName;
}
