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
package io.entgra.device.mgt.core.certificate.mgt.cert.admin.api.beans;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.entgra.device.mgt.core.certificate.mgt.core.dto.CertificateResponse;
import io.swagger.annotations.ApiModelProperty;

import java.util.ArrayList;
import java.util.List;

public class CertificateList extends BasePaginatedResult {

    private List<CertificateResponse> certificates = new ArrayList<>();

    @ApiModelProperty(value = "List of certificates returned")
    @JsonProperty("certificates")
    public List<CertificateResponse> getList() {
        return certificates;
    }

    public void setList(List<CertificateResponse> certificates) {
        this.certificates = certificates;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("{\n");

        sb.append("  count: ").append(getCount()).append(",\n");
        sb.append("  next: ").append(getNext()).append(",\n");
        sb.append("  previous: ").append(getPrevious()).append(",\n");
        sb.append("  certificates: [").append(certificates).append("\n");
        sb.append("]}\n");
        return sb.toString();
    }

}
