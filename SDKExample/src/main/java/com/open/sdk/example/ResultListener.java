/*
 * Copyright © YOLANDA. All Rights Reserved
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.open.sdk.example;



public interface ResultListener {
    /**
     * 访问成功
     *
     * @param hint   失败提示
     * @param result 结果
     */
    void onSucceed(String hint, String resultCode, String result);

    /**
     * 失败
     *
     * @param hint         失败提示
     * @param errorMessage 错误信息
     */
    void onOther(String hint, String resultCode, String errorMessage);

    /**
     * 无网络
     *
     * @param hint         失败提示
     * @param errorMessage 错误信息
     */
    void onFailure(String hint, String resultCode, String errorMessage);




}
