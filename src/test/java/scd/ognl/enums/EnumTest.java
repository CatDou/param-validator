/*
 * Copyright 2021 catdou
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package scd.ognl.enums;


/**
 * @author James
 */
public enum EnumTest {
    XML,JSON;

    public static boolean contains(String name) {
        for (EnumTest enumTest : EnumTest.values()) {
            if (enumTest.name().equalsIgnoreCase(name)) {
                return true;
            }
        }
        return false;
    }

    public boolean value(String name) {
        return false;
    }
}
