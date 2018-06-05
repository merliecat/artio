/*
 * Copyright 2015-2017 Real Logic Ltd.
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
package uk.co.real_logic.artio.builder;

public final class EnumSentinel
{
    public static final String CODEC_ENUM_SENTINEL_VALUE_ENABLED_PROP = "fix.codecs.use_enum_sentinel_value";
    public static final boolean CODEC_ENUM_SENTINEL_VALUE_ENABLED =
        Boolean.getBoolean(CODEC_ENUM_SENTINEL_VALUE_ENABLED_PROP);
}
