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


package io.entgra.device.mgt.core.policy.mgt.common;

import io.entgra.device.mgt.core.device.mgt.common.group.mgt.DeviceGroup;
import io.entgra.device.mgt.core.device.mgt.common.policy.mgt.Policy;

import java.util.List;
import java.util.Map;

public interface PolicyFilter {

    List<Policy> filterActivePolicies(List<Policy> policies);

    /**
     * Filter and retrieve all the general policies out of active policies
     * @param policies contains a list of active policies
     * @return a list of {@link Policy} general policies
     */
    List<Policy> filterGeneralPolicies(List<Policy> policies);

    List<Policy> filterDeviceGroupsPolicies(Map<Integer, DeviceGroup> groupMap, List<Policy> policies);

    List<Policy> filterRolesBasedPolicies(String roles[], List<Policy> policies);

    List<Policy> filterOwnershipTypeBasedPolicies(String ownershipType, List<Policy> policies);

    List<Policy> filterDeviceTypeBasedPolicies(String deviceType, List<Policy> policies);

    List<Policy> filterUserBasedPolicies(String username, List<Policy> policies);

}
