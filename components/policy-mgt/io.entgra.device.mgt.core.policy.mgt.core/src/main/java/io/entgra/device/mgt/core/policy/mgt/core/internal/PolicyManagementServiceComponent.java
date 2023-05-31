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

package io.entgra.device.mgt.core.policy.mgt.core.internal;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.osgi.service.component.ComponentContext;
import io.entgra.device.mgt.core.device.mgt.core.config.DeviceConfigurationManager;
import io.entgra.device.mgt.core.device.mgt.core.config.policy.PolicyConfiguration;
import io.entgra.device.mgt.core.device.mgt.core.service.DeviceManagementProviderService;
import org.wso2.carbon.ntask.core.service.TaskService;
import io.entgra.device.mgt.core.policy.mgt.common.PolicyEvaluationPoint;
import io.entgra.device.mgt.core.policy.mgt.core.PolicyManagerService;
import io.entgra.device.mgt.core.policy.mgt.core.PolicyManagerServiceImpl;
import io.entgra.device.mgt.core.policy.mgt.core.config.PolicyConfigurationManager;
import io.entgra.device.mgt.core.policy.mgt.core.config.PolicyManagementConfig;
import io.entgra.device.mgt.core.policy.mgt.core.config.datasource.DataSourceConfig;
import io.entgra.device.mgt.core.policy.mgt.core.dao.PolicyManagementDAOFactory;
import io.entgra.device.mgt.core.policy.mgt.core.task.TaskScheduleService;
import io.entgra.device.mgt.core.policy.mgt.core.task.TaskScheduleServiceImpl;
import io.entgra.device.mgt.core.policy.mgt.core.util.PolicyManagerUtil;
import org.wso2.carbon.user.core.service.RealmService;

/**
 * @scr.component name="org.wso2.carbon.devicemgt.policy.manager" immediate="true"
 * @scr.reference name="user.realmservice.default"
 * interface="org.wso2.carbon.user.core.service.RealmService"
 * cardinality="1..1"
 * policy="dynamic"
 * bind="setRealmService"
 * unbind="unsetRealmService"
 * @scr.reference name="org.wso2.carbon.devicemgt.policy.evaluation.manager"
 * interface="io.entgra.device.mgt.core.policy.mgt.common.PolicyEvaluationPoint"
 * cardinality="1..n"
 * policy="dynamic"
 * bind="setPEPService"
 * unbind="unsetPEPService"
 * @scr.reference name="org.wso2.carbon.device.manager"
 * interface="io.entgra.device.mgt.core.device.mgt.core.service.DeviceManagementProviderService"
 * cardinality="1..1"
 * policy="dynamic"
 * bind="setDeviceManagementService"
 * unbind="unsetDeviceManagementService"
 * @scr.reference name="ntask.component"
 * interface="org.wso2.carbon.ntask.core.service.TaskService"
 * cardinality="1..1"
 * policy="dynamic"
 * bind="setTaskService"
 * unbind="unsetTaskService"
 */
@SuppressWarnings("unused")
public class PolicyManagementServiceComponent {

    private static final Log log = LogFactory.getLog(PolicyManagementServiceComponent.class);

    protected void activate(ComponentContext componentContext) {

        try {
            PolicyConfigurationManager.getInstance().initConfig();
            PolicyManagementConfig config = PolicyConfigurationManager.getInstance().getPolicyManagementConfig();
            DataSourceConfig dsConfig = config.getPolicyManagementRepository().getDataSourceConfig();
            PolicyManagementDAOFactory.init(dsConfig);

            PolicyManagerService policyManagerService = new PolicyManagerServiceImpl();
            componentContext.getBundleContext().registerService(
                    PolicyManagerService.class.getName(), policyManagerService, null);
            PolicyManagementDataHolder.getInstance().setPolicyManagerService(policyManagerService);

            PolicyConfiguration policyConfiguration =
                    DeviceConfigurationManager.getInstance().getDeviceManagementConfig().getPolicyConfiguration();
            if(policyConfiguration.getMonitoringEnable()) {
                TaskScheduleService taskScheduleService = new TaskScheduleServiceImpl();
                taskScheduleService.startTask(PolicyManagerUtil.getMonitoringFrequency());
            }

        } catch (Throwable t) {
            log.error("Error occurred while initializing the Policy management core.", t);
        }
    }

    @SuppressWarnings("unused")
    protected void deactivate(ComponentContext componentContext) {
        try {
            PolicyConfiguration policyConfiguration =
                    DeviceConfigurationManager.getInstance().getDeviceManagementConfig().getPolicyConfiguration();
            if (policyConfiguration.getMonitoringEnable()) {
                TaskScheduleService taskScheduleService = new TaskScheduleServiceImpl();
                taskScheduleService.stopTask();
            }
        } catch (Throwable t) {
            log.error("Error occurred while destroying the Policy management core.", t);
        }
    }


    /**
     * Sets Realm Service
     *
     * @param realmService An instance of RealmService
     */
    protected void setRealmService(RealmService realmService) {

        if (log.isDebugEnabled()) {
            log.debug("Setting Realm Service");
        }
        PolicyManagementDataHolder.getInstance().setRealmService(realmService);
    }

    /**
     * Unsets Realm Service
     *
     * @param realmService An instance of RealmService
     */
    protected void unsetRealmService(RealmService realmService) {
        if (log.isDebugEnabled()) {
            log.debug("Unsetting Realm Service");
        }
        PolicyManagementDataHolder.getInstance().setRealmService(null);
    }


/*    protected void setPIPService(PolicyInformationPoint pipService) {
        if (log.isDebugEnabled()) {
            log.debug("Setting Policy Information Service");
        }
        PolicyManagementDataHolder.getInstance().setPolicyInformationPoint(pipService);
    }

    protected void unsetPIPService(PolicyInformationPoint pipService) {
        if (log.isDebugEnabled()) {
            log.debug("Unsetting Policy Information Service");
        }
        PolicyManagementDataHolder.getInstance().setPolicyInformationPoint(null);
    }*/


    protected void setPEPService(PolicyEvaluationPoint pepService) {
        if (log.isDebugEnabled()) {
            log.debug("Setting Policy Information Service");
        }
        PolicyManagementDataHolder.getInstance().setPolicyEvaluationPoint(pepService.getName(), pepService);
    }

    protected void unsetPEPService(PolicyEvaluationPoint pepService) {
        if (log.isDebugEnabled()) {
            log.debug("Removing Policy Information Service");
        }
        PolicyManagementDataHolder.getInstance().removePolicyEvaluationPoint(pepService);
    }

    protected void setDeviceManagementService(DeviceManagementProviderService deviceManagerService) {
        if (log.isDebugEnabled()) {
            log.debug("Setting Device Management Service");
        }
        PolicyManagementDataHolder.getInstance().setDeviceManagementService(deviceManagerService);
    }

    protected void unsetDeviceManagementService(DeviceManagementProviderService deviceManagementService) {
        if (log.isDebugEnabled()) {
            log.debug("Removing Device Management Service");
        }
        PolicyManagementDataHolder.getInstance().setDeviceManagementService(null);
    }

    protected void setTaskService(TaskService taskService) {
        if (log.isDebugEnabled()) {
            log.debug("Setting the task service.");
        }
        PolicyManagementDataHolder.getInstance().setTaskService(taskService);
    }

    protected void unsetTaskService(TaskService taskService) {
        if (log.isDebugEnabled()) {
            log.debug("Removing the task service.");
        }
        PolicyManagementDataHolder.getInstance().setTaskService(null);
    }

}