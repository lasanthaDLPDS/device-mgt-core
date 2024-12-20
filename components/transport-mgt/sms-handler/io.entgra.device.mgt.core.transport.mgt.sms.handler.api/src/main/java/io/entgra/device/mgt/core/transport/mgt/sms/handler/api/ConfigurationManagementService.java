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

package io.entgra.device.mgt.core.transport.mgt.sms.handler.api;

import io.entgra.device.mgt.core.apimgt.annotations.Scope;
import io.entgra.device.mgt.core.apimgt.annotations.Scopes;
import io.entgra.device.mgt.core.transport.mgt.sms.handler.common.SMSHandlerConstants;
import io.swagger.annotations.*;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@SwaggerDefinition(
        info = @Info(
                version = "1.0.0",
                title = "",
                extensions = {
                        @Extension(properties = {
                                @ExtensionProperty(name = "name", value = "ConfigurationManagementService"),
                                @ExtensionProperty(name = "context", value = "/api/sms-handler/v1.0/configuration"),
                        })
                }
        ),
        tags = {
                @Tag(name = "transport_management", description = "")
        }
)
@Path("/configuration")
@Api(value = "Configuration Management", description = "The general SMS configuration management capabilities " +
        "are exposed through this API.")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Scopes(scopes = {
        @Scope(
                name = "View configurations",
                description = "",
                key = "conf:sms-handler:view",
                roles = {"Internal/devicemgt-user"},
                permissions = {"/sms-handler/platform-configurations/view"}
        )
})
public interface ConfigurationManagementService {

    @GET
    @ApiOperation(
            produces = MediaType.APPLICATION_JSON,
            httpMethod = "GET",
            value = "Reload SMS Configuration",
            notes = "Reload SMS Configuration in sms-config.xml file",
            tags = "SMS Configuration Management",
            extensions = {
                    @Extension(properties = {
                            @ExtensionProperty(name = SMSHandlerConstants.SCOPE, value = "conf:sms-handler:view")
                    })
            }
    )
    @ApiResponses(
            value = {
                    @ApiResponse(
                            code = 200,
                            message = "OK. \n Successfully reloaded SMS configurations.",
                            responseContainer = "List",
                            responseHeaders = {
                                    @ResponseHeader(
                                            name = "Content-Type",
                                            description = "The content type of the body"),
                                    @ResponseHeader(
                                            name = "ETag",
                                            description = "Entity Tag of the response resource.\n" +
                                                    "Used by caches, or in conditional requests."),
                                    @ResponseHeader(
                                            name = "Last-Modified",
                                            description = "Date and time the resource has been modified the last time.\n" +
                                                    "Used by caches, or in conditional requests."),
                            }
                    ),
                    @ApiResponse(
                            code = 304,
                            message = "Not Modified. \n Empty body because the client already has the latest "
                                    + "version of the requested resource."),
                    @ApiResponse(
                            code = 406,
                            message = "Not Acceptable.\n The requested media type is not supported."),
                    @ApiResponse(
                            code = 500,
                            message = "Internal Server Error. \n Server error occurred while fetching the general " +
                                    "platform configurations.")
            })
    Response reloadConfiguration();
}
