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
package io.entgra.device.mgt.core.device.mgt.common;

/**
 *
 */
public class MonitoringOperation {

    private String taskName;
    private int recurrentTimes;
    private boolean responsePersistence = true;

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public int getRecurrentTimes() {
        return recurrentTimes;
    }

    public void setRecurrentTimes(int recurrentTimes) {
        this.recurrentTimes = recurrentTimes;
    }

    public boolean hasResponsePersistence() {
        return responsePersistence;
    }

    public void setResponsePersistence(boolean responsePersistence) {
        this.responsePersistence = responsePersistence;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof MonitoringOperation) {
            MonitoringOperation op = (MonitoringOperation) obj;
            return taskName != null && taskName.equals(op.getTaskName());
        }
        return false;
    }
}

