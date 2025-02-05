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
package io.entgra.device.mgt.core.task.mgt.core.dao.impl;

import io.entgra.device.mgt.core.task.mgt.common.constant.TaskMgtConstants;
import io.entgra.device.mgt.core.task.mgt.common.exception.TaskManagementDAOException;
import io.entgra.device.mgt.core.task.mgt.core.dao.DynamicTaskPropDAO;
import io.entgra.device.mgt.core.task.mgt.core.dao.common.TaskManagementDAOFactory;
import io.entgra.device.mgt.core.task.mgt.core.dao.util.TaskManagementDAOUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.context.PrivilegedCarbonContext;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class DynamicTaskPropDAOImpl implements DynamicTaskPropDAO {

    private static final Log log = LogFactory.getLog(DynamicTaskPropDAOImpl.class);

    @Override
    public void addTaskProperties(int taskId, Map<String, String> properties, int tenantId)
            throws TaskManagementDAOException {
        Connection conn;
        PreparedStatement stmt = null;
        try {
            conn = TaskManagementDAOFactory.getConnection();
            stmt = conn.prepareStatement(
                    "INSERT INTO DYNAMIC_TASK_PROPERTIES(DYNAMIC_TASK_ID, PROPERTY_NAME, " +
                            "PROPERTY_VALUE, TENANT_ID) VALUES (?, ?, ?, ?)");
            for (String propertyKey : properties.keySet()) {
                stmt.setInt(1, taskId);
                stmt.setString(2, propertyKey);
                stmt.setString(3, properties.get(propertyKey));
                stmt.setInt(4, tenantId);
                stmt.addBatch();
            }
            stmt.executeBatch();
        } catch (SQLException e) {
            String msg = "Error occurred while adding task properties of task '" + taskId + "' to the db.";
            log.error(msg, e);
            throw new TaskManagementDAOException(msg, e);
        } finally {
            TaskManagementDAOUtil.cleanupResources(stmt, null);
        }
    }

    public Map<String, String> getDynamicTaskProps(int dynamicTaskId, int tenantId) throws TaskManagementDAOException {
        Connection conn;
        PreparedStatement stmt = null;
        ResultSet resultSet = null;
        Map<String, String> properties;
        try {
            conn = TaskManagementDAOFactory.getConnection();
            stmt = conn.prepareStatement(
                    "SELECT * FROM DYNAMIC_TASK_PROPERTIES WHERE DYNAMIC_TASK_ID = ? AND TENANT_ID = ?");
            stmt.setInt(1, dynamicTaskId);
            stmt.setInt(2, tenantId);
            resultSet = stmt.executeQuery();
            properties = new HashMap<>();
            while (resultSet.next()) {
                properties.put(resultSet.getString(TaskMgtConstants.Task.PROPERTY_KEY_COLUMN_NAME)
                        , resultSet.getString(TaskMgtConstants.Task.PROPERTY_VALUE_COLUMN_NAME));
            }
        } catch (SQLException e) {
            String msg = "Error occurred while fetching task properties of : '" + dynamicTaskId + "'";
            log.error(msg, e);
            throw new TaskManagementDAOException(msg, e);
        } finally {
            TaskManagementDAOUtil.cleanupResources(stmt, resultSet);
        }
        return properties;
    }

    @Override
    public void updateDynamicTaskProps(int dynamicTaskId, Map<String, String> properties, int tenantId)
            throws TaskManagementDAOException {
        if (properties.isEmpty()) {
            if (log.isDebugEnabled()) {
                log.debug("Property map of task id :" + dynamicTaskId + " is empty.");
            }
            return;
        }
        Connection conn;
        PreparedStatement stmt = null;
        try {
            conn = TaskManagementDAOFactory.getConnection();
            stmt = conn.prepareStatement("UPDATE DYNAMIC_TASK_PROPERTIES SET PROPERTY_VALUE = ? " +
                    "WHERE DYNAMIC_TASK_ID = ? AND PROPERTY_NAME = ? AND TENANT_ID = ?");

            for (Map.Entry<String, String> entry : properties.entrySet()) {
                stmt.setString(1, entry.getValue());
                stmt.setInt(2, dynamicTaskId);
                stmt.setString(3, entry.getKey());
                stmt.setInt(4, tenantId);
                stmt.addBatch();
            }
            stmt.executeBatch();
        } catch (SQLException e) {
            throw new TaskManagementDAOException
                    ("Error occurred while updating device properties to database.", e);
        } finally {
            TaskManagementDAOUtil.cleanupResources(stmt, null);
        }
    }

}
