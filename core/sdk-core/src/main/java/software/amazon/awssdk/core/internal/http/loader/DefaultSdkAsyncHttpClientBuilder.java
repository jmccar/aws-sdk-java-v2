/*
 * Copyright 2010-2019 Amazon.com, Inc. or its affiliates. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License").
 * You may not use this file except in compliance with the License.
 * A copy of the License is located at
 *
 *  http://aws.amazon.com/apache2.0
 *
 * or in the "license" file accompanying this file. This file is distributed
 * on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language governing
 * permissions and limitations under the License.
 */

package software.amazon.awssdk.core.internal.http.loader;

import java.util.ServiceLoader;
import software.amazon.awssdk.annotations.SdkInternalApi;
import software.amazon.awssdk.core.exception.SdkClientException;
import software.amazon.awssdk.http.async.SdkAsyncHttpClient;
import software.amazon.awssdk.http.async.SdkAsyncHttpService;
import software.amazon.awssdk.utils.AttributeMap;

/**
 * Utility that looks for an HTTP client that implements {@link SdkAsyncHttpClient}.
 *
 * It does this by looking for an implementation of {@link SdkAsyncHttpService} in this order:
 * <ol>
 *   <li>Java System Properties - {@code software.amazon.awssdk.http.async.service.impl}</code></li>
 *   <li>Environment Variables - {@code ASYNC_HTTP_SERVICE_IMPL}</li>
 *   <li>Class Path - Uses {@link ServiceLoader#load(Class)} to find an implementation of
 *   {@link SdkAsyncHttpService}</li>
 * </ol>
 */
@SdkInternalApi
public final class DefaultSdkAsyncHttpClientBuilder implements SdkAsyncHttpClient.Builder {

    private static final SdkHttpServiceProvider<SdkAsyncHttpService> DEFAULT_CHAIN = new CachingSdkHttpServiceProvider<>(
            new SdkHttpServiceProviderChain<>(
                    SystemPropertyHttpServiceProvider.asyncProvider(),
                    ClasspathSdkHttpServiceProvider.asyncProvider()
            ));

    @Override
    public SdkAsyncHttpClient buildWithDefaults(AttributeMap serviceDefaults) {
        // TODO We create and build every time. Do we want to cache it instead of the service binding?
        return DEFAULT_CHAIN
                .loadService()
                .map(SdkAsyncHttpService::createAsyncHttpClientFactory)
                .map(f -> f.buildWithDefaults(serviceDefaults))
                .orElseThrow(
                    () -> SdkClientException.builder()
                                            .message("Unable to load an HTTP implementation from any provider in the" +
                                                    "chain. You must declare a dependency on an appropriate HTTP" +
                                                    "implementation or pass in an SdkHttpClient explicitly to the" +
                                                    "client builder.")
                                            .build());
    }

}
