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

package software.amazon.awssdk.enhanced.dynamodb.converter.attribute.bundled;

import java.util.concurrent.atomic.AtomicLong;
import software.amazon.awssdk.annotations.Immutable;
import software.amazon.awssdk.annotations.SdkPublicApi;
import software.amazon.awssdk.annotations.ThreadSafe;
import software.amazon.awssdk.enhanced.dynamodb.converter.attribute.AttributeConverter;
import software.amazon.awssdk.enhanced.dynamodb.converter.attribute.ConversionContext;
import software.amazon.awssdk.enhanced.dynamodb.converter.string.bundled.AtomicLongStringConverter;
import software.amazon.awssdk.enhanced.dynamodb.model.ItemAttributeValue;
import software.amazon.awssdk.enhanced.dynamodb.model.TypeConvertingVisitor;
import software.amazon.awssdk.enhanced.dynamodb.model.TypeToken;

/**
 * A converter between {@link AtomicLong} and {@link ItemAttributeValue}.
 *
 * <p>
 * This stores values in DynamoDB as a number.
 *
 * <p>
 * This supports reading numbers between {@link Long#MIN_VALUE} and {@link Long#MAX_VALUE} from DynamoDB. For smaller
 * numbers, consider using {@link ShortAttributeConverter} or {@link IntegerAttributeConverter}. For larger numbers, consider
 * using {@link BigIntegerAttributeConverter}. Numbers outside of the supported range will cause a {@link NumberFormatException}
 * on conversion.
 *
 * <p>
 * This does not support reading decimal numbers. For decimal numbers, consider using {@link FloatAttributeConverter},
 * {@link DoubleAttributeConverter} or {@link BigDecimalAttributeConverter}. Decimal numbers will cause a
 * {@link NumberFormatException} on conversion.
 *
 * <p>
 * This can be created via {@link #create()}.
 */
@SdkPublicApi
@ThreadSafe
@Immutable
public final class AtomicLongAttributeConverter implements AttributeConverter<AtomicLong> {
    private static final Visitor VISITOR = new Visitor();
    private static final AtomicLongStringConverter STRING_CONVERTER = AtomicLongStringConverter.create();

    private AtomicLongAttributeConverter() {}

    @Override
    public TypeToken<AtomicLong> type() {
        return TypeToken.of(AtomicLong.class);
    }

    public static AtomicLongAttributeConverter create() {
        return new AtomicLongAttributeConverter();
    }

    @Override
    public ItemAttributeValue toAttributeValue(AtomicLong input, ConversionContext context) {
        return ItemAttributeValue.fromNumber(STRING_CONVERTER.toString(input));
    }

    @Override
    public AtomicLong fromAttributeValue(ItemAttributeValue input,
                                         ConversionContext context) {
        return input.convert(VISITOR);
    }

    private static final class Visitor extends TypeConvertingVisitor<AtomicLong> {
        private Visitor() {
            super(AtomicLong.class, AtomicLongAttributeConverter.class);
        }

        @Override
        public AtomicLong convertString(String value) {
            return STRING_CONVERTER.fromString(value);
        }

        @Override
        public AtomicLong convertNumber(String value) {
            return STRING_CONVERTER.fromString(value);
        }
    }
}
