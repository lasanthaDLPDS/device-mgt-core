/*
 * Copyright (c) 2018 - 2023, Entgra (Pvt) Ltd. (http://www.entgra.io) All Rights Reserved.
 *
 * Entgra (Pvt) Ltd. licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package io.entgra.device.mgt.core.device.mgt.extensions.device.type.template.config;

import javax.xml.bind.annotation.XmlElement;

public class Option {
    @XmlElement(name = "OptionName", required = true)
    private String name;

    @XmlElement(name = "OptionValue", required = true)
    protected String value;

    public String getOptionName() {
        return name;
    }

    public void setOptionName(String name) {
        this.name = name;
    }

    public String getOptionValue() {
        return value;
    }

    public void setOptionValue(String value) {
        this.value = value;
    }
}
