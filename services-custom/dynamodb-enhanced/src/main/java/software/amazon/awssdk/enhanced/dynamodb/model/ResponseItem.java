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

package software.amazon.awssdk.enhanced.dynamodb.model;

import java.util.Map;
import software.amazon.awssdk.annotations.Immutable;
import software.amazon.awssdk.annotations.SdkPublicApi;
import software.amazon.awssdk.annotations.ThreadSafe;
import software.amazon.awssdk.enhanced.dynamodb.AsyncTable;
import software.amazon.awssdk.enhanced.dynamodb.Table;
import software.amazon.awssdk.enhanced.dynamodb.internal.model.DefaultResponseItem;
import software.amazon.awssdk.utils.builder.CopyableBuilder;
import software.amazon.awssdk.utils.builder.ToCopyableBuilder;

/**
 * An item that is returned by DynamoDB. An item is a single, unique entry in a DynamoDB table.
 *
 * <p>
 * A {@code ResponseItem} is a {@code Map<String, ConvertibleItemAttributeValue>}. Each attribute can be converted into a Java
 * type via {@link ConvertibleItemAttributeValue#as(Class)} or similar methods. For example:
 * {@code String id = responseItem.attribute("id").asString();}
 *
 * @see Table
 * @see AsyncTable
 */
@SdkPublicApi
@ThreadSafe
@Immutable
public interface ResponseItem extends AttributeAware<ConvertibleItemAttributeValue>,
                                      ToCopyableBuilder<ResponseItem.Builder, ResponseItem> {
    /**
     * Create a builder that can be used for configuring and creating a {@link ResponseItem}.
     *
     * <p>
     * This call should never fail with an {@link Exception}.
     */
    static Builder builder() {
        return DefaultResponseItem.builder();
    }

    /**
     * A builder that can be used for configuring and creating a {@link ResponseItem}.
     */
    interface Builder extends AttributeAware.Builder<ConvertibleItemAttributeValue>,
                              CopyableBuilder<ResponseItem.Builder, ResponseItem> {
        @Override
        Builder putAttributes(Map<String, ? extends ConvertibleItemAttributeValue> attributeValues);

        @Override
        Builder putAttribute(String attributeKey, ConvertibleItemAttributeValue attributeValue);

        @Override
        Builder removeAttribute(String attributeKey);

        @Override
        Builder clearAttributes();

        /**
         * Build a {@link ResponseItem} from the provided configuration. This method can be invoked multiple times to
         * create multiple {@code ResponseItem} instances.
         *
         * <p>
         * Reasons this call may fail with a {@link RuntimeException}:
         * <ol>
         *     <li>If any mutating methods are called in parallel with this one. This class is not thread safe.</li>
         * </ol>
         */
        @Override
        ResponseItem build();
    }
}
