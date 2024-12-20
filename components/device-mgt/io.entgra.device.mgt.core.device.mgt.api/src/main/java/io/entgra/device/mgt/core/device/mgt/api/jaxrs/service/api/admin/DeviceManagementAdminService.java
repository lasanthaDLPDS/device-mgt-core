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
package io.entgra.device.mgt.core.device.mgt.api.jaxrs.service.api.admin;

import io.entgra.device.mgt.core.apimgt.annotations.Scope;
import io.entgra.device.mgt.core.apimgt.annotations.Scopes;
import io.entgra.device.mgt.core.device.mgt.api.jaxrs.beans.DeviceList;
import io.entgra.device.mgt.core.device.mgt.api.jaxrs.beans.ErrorResponse;
import io.entgra.device.mgt.core.device.mgt.api.jaxrs.util.Constants;
import io.entgra.device.mgt.core.device.mgt.common.Device;
import io.swagger.annotations.*;
import org.apache.axis2.transport.http.HTTPConstants;

import javax.validation.constraints.Size;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.sql.Timestamp;
import java.util.List;

@SwaggerDefinition(
        info = @Info(
                version = "1.0.0",
                title = "",
                extensions = {
                        @Extension(properties = {
                                @ExtensionProperty(name = "name", value = "DeviceManagementAdmin"),
                                @ExtensionProperty(name = "context", value = "/api/device-mgt/v1.0/admin/devices"),
                        })
                }
        ),
        tags = {
                @Tag(name = "device_management", description = "")
        }
)
@Path("/admin/devices")
@Api(value = "Device Management Administrative Service", description = "This an  API intended to be used by " +
        "'internal' components to log in as an admin user and do a selected number of operations. " +
        "Further, this is strictly restricted to admin users only ")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Scopes(
        scopes = {
                @Scope(
                        name = "Getting Details of a Device",
                        description = "Getting Details of a Device",
                        key = "dm:admin:devices:view",
                        roles = {"Internal/devicemgt-admin"},
                        permissions = {"/device-mgt/admin/devices/view"}
                ),
                @Scope(
                        name = "Update the Device Owner",
                        description = "Update the ownership of the device",
                        key = "dm:admin:enrollment:update",
                        roles = {"Internal/devicemgt-admin"},
                        permissions = {"/device-mgt/admin/devices/update-enrollment"}
                ),
                @Scope(
                        name = "Permanently Delete the device specified by device id",
                        description = "Permanently Delete the device specified by device id",
                        key = "dm:admin:devices:permanent-delete",
                        roles = {"Internal/devicemgt-admin"},
                        permissions = {"/device-mgt/admin/devices/permanent-delete"}
                ),
                @Scope(
                        name = "Get Usage of Devices",
                        description = "Get Usage of Devices",
                        key = "dm:admin:devices:usage:view",
                        roles = {"Internal/devicemgt-admin"},
                        permissions = {"/device-mgt/admin/devices/usage/view"}
                ),
        }
)
public interface DeviceManagementAdminService {

    @GET
    @ApiOperation(
            produces = MediaType.APPLICATION_JSON,
            httpMethod = "GET",
            value = "Getting Details of a Device",
            notes = "Get the details of a device by searching via the device name, device type and the tenant domain.",
            response = Device.class,
            responseContainer = "List",
            tags = "Device Management Administrative Service",
            extensions = {
                    @Extension(properties = {
                            @ExtensionProperty(name = Constants.SCOPE, value = "dm:admin:devices:view")
                    })
            }
    )
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK. \n Successfully fetched the list of devices.",
                    response = Device.class,
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
                                    description = "Date and time the resource was last modified.\n" +
                                            "Used by caches, or in conditional requests."),
                    }),
            @ApiResponse(
                    code = 304,
                    message = "Not Modified. Empty body because the client already has the latest version of the " +
                            "requested resource.\n"),
            @ApiResponse(
                    code = 401,
                    message = "Unauthorized.\n The unauthorized access to the requested resource.",
                    response = ErrorResponse.class),
            @ApiResponse(
                    code = 404,
                    message = "Not Found.\n The specified device does not exist",
                    response = ErrorResponse.class),
            @ApiResponse(
                    code = 406,
                    message = "Not Acceptable.\n The requested media type is not supported"),
            @ApiResponse(
                    code = 500,
                    message = "Internal Server Error. \n Server error occurred while fetching the device list.",
                    response = ErrorResponse.class)
    })
    Response getDevicesByName(
            @ApiParam(
                    name = "name",
                    value = "The name of the device.If you are unsure of the name of the device, run the GET /devices" +
                            " API that is under Device Management.",
                    required = true)
            @QueryParam("name")
            @Size(max = 45)
            String name,
            @ApiParam(
                    name = "type",
                    value = "The device type name, such as ios, android, windows or fire-alarm.",
                    required = true)
            @QueryParam("type")
            @Size(min = 2, max = 45)
            String type,
            @ApiParam(
                    name = "tenant-domain",
                    value = "The name of the tenant.\n" +
                            "The default tenant domain of WSO2 EMM is carbon.super",
                    required = true,
                    defaultValue = "carbon.super")
            @QueryParam("tenant-domain") String tenantDomain,
            @ApiParam(
                    name = "If-Modified-Since",
                    value = "Checks if the requested variant was modified, since the specified date-time. \n" +
                            "Provide the value in the following format: EEE, d MMM yyyy HH:mm:ss Z. \n" +
                            "Example: Mon, 05 Jan 2014 15:10:00 +0200",
                    required = false)
            @HeaderParam("If-Modified-Since") String ifModifiedSince,
            @ApiParam(
                    name = "offset",
                    value = "The starting pagination index for the complete list of qualified items.",
                    required = false,
                    defaultValue = "0")
            @QueryParam("offset") int offset,
            @ApiParam(
                    name = "limit",
                    value = "Provide how many activity details you require from the starting pagination index/offset.",
                    required = false,
                    defaultValue = "5")
            @QueryParam("limit") int limit);

    @Path("/count")
    @GET
    @ApiOperation(
            produces = MediaType.APPLICATION_JSON,
            httpMethod = HTTPConstants.HEADER_GET,
            value = "Get the count of devices.",
            notes = "Returns count of all devices enrolled with the system.",
            tags = "Device Management Administrative Service",
            extensions = {
                    @Extension(properties = {
                            @ExtensionProperty(name = Constants.SCOPE, value = "dm:admin:devices:view")
                    })
            }
    )
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK. \n Successfully fetched the device count.",
                    response = Integer.class,
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
                    }),
            @ApiResponse(
                    code = 304,
                    message = "Not Modified. \n Empty body because the client has already the latest version of " +
                            "the requested resource."),
            @ApiResponse(
                    code = 404,
                    message = "No groups found.",
                    response = ErrorResponse.class),
            @ApiResponse(
                    code = 406,
                    message = "Not Acceptable.\n The requested media type is not supported."),
            @ApiResponse(
                    code = 500,
                    message = "Internal Server Error. \n Server error occurred while fetching the device count.",
                    response = ErrorResponse.class)
    })
    Response getDeviceCount(@ApiParam(
            name = "status",
            value = "status of group of which count should be retrieved")
                            @QueryParam("status")
                                    String status);


    @PUT
    @Path("/device-owner")
    @ApiOperation(
            produces = MediaType.APPLICATION_JSON,
            httpMethod = "PUT",
            value = "Update the device owner",
            notes = "Update enrollment owner for given device Identifiers.",
            tags = "Device Management",
            extensions = {
                    @Extension(properties = {
                            @ExtensionProperty(name = Constants.SCOPE, value = "dm:admin:enrollment:update")
                    })
            }
    )
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK. \n Successfully update the owner of devices.",
                    response = DeviceList.class,
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
                                    description = "Date and time the resource was last modified.\n" +
                                            "Used by caches, or in conditional requests."),
                    }),
            @ApiResponse(
                    code = 400,
                    message = "The incoming request has more than one selection criteria defined via the query parameters.",
                    response = ErrorResponse.class),
            @ApiResponse(
                    code = 500,
                    message = "Internal Server Error. \n Server error occurred while fetching the device list.",
                    response = ErrorResponse.class)
    }) Response updateEnrollOwner(
            @ApiParam(
                    name = "Device Owner",
                    value = "The username that is going to use for the new device owner of given devices.",
                    required = true)
            @QueryParam("owner") String owner,
            @ApiParam(
                    name = "Device Identifiers",
                    value = "List of device identifiers.",
                    required = true)
                    List<String> deviceIdentifiers);

    @PUT
    @Path("/permanent-delete")
    @ApiOperation(
            produces = MediaType.APPLICATION_JSON,
            consumes = MediaType.APPLICATION_JSON,
            httpMethod = "PUT",
            value = "Permanently remove the Device Specified by the Device ID",
            notes = "Returns the status of the permanently deleted device operation and the details of the deleted device.",
            tags = "Device Management",
            extensions = {
                    @Extension(properties = {
                            @ExtensionProperty(name = Constants.SCOPE, value = "dm:admin:devices:permanent-delete")
                    })
            }
    )
    @ApiResponses(
            value = {
                    @ApiResponse(
                            code = 200,
                            message = "OK. \n Successfully deleted the device permanently.",
                            response = Device.class,
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
                            }),
                    @ApiResponse(
                            code = 400,
                            message = "Bad Request. \n Invalid request or validation error.",
                            response = ErrorResponse.class),
                    @ApiResponse(
                            code = 500,
                            message = "Internal Server Error. \n " +
                                      "Server error occurred while retrieving information requested device.",
                            response = ErrorResponse.class)
            })
    Response deleteDevicesPermanently(
            @ApiParam(
                    name = "Device Identifiers",
                    value = "List of device identifiers.",
                    required = true)
                    List<String> deviceIdentifiers);

    @POST
    @Path("/{deviceId}/{featureCode}")
    @ApiOperation(
            consumes = MediaType.APPLICATION_JSON,
            produces = MediaType.APPLICATION_JSON,
            httpMethod = "POST",
            value = "Trigger the given corrective action",
            notes = "When violation happens it is possible to trigger a corrective action.",
            tags = "Device Management",
            extensions = {
                    @Extension(properties = {
                            @ExtensionProperty(name = Constants.SCOPE, value = "and:devices:enroll")
                    })
            }
    )
    @ApiResponses(
            value = {
                    @ApiResponse(
                            code = 200,
                            message = "OK. \n Successfully triggered the action."),
                    @ApiResponse(
                            code = 400,
                            message = "Bad Request. \n Invalid request or validation error.",
                            response = ErrorResponse.class),
                    @ApiResponse(
                            code = 404,
                            message = "Not Found. \n The specified resource does not exist.",
                            response = ErrorResponse.class),
                    @ApiResponse(
                            code = 415,
                            message = "Unsupported media type. \n The format of the requested entity was not " +
                                    "supported.\n "
                                    + "supported format."),
                    @ApiResponse(
                            code = 500,
                            message = "Internal Server Error. \n " +
                                    "Server error occurred while triggering the corrective action.",
                            response = ErrorResponse.class)
            })
    Response triggerCorrectiveActions(
            @ApiParam(
                    name = "deviceId",
                    value = "Device Identifier.",
                    required = true)
            @PathParam("deviceId")
                    String deviceId,
            @ApiParam(
                    name = "featureCode",
                    value = "Policy Feature Code.",
                    required = true)
            @PathParam("featureCode")
                    String featureCode,
            @ApiParam(
                    name = "actions",
                    value = "The list of actions to trigger when violation happens.",
                    required = true)
                    List<String> actions
    );

    @GET
    @Path("/billing")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(
            produces = MediaType.APPLICATION_JSON,
            httpMethod = "GET",
            value = "Getting Cost details of devices in a tenant",
            notes = "Provides individual cost and total cost of all devices per tenant.",
            tags = "Device Management",
            extensions = {
                    @Extension(properties = {
                            @ExtensionProperty(name = Constants.SCOPE, value = "dm:admin:devices:usage:view")
                    })
            }
    )
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK. \n Successfully fetched the list of devices.",
                    response = DeviceList.class,
                    responseHeaders = {
                            @ResponseHeader(
                                    name = "Content-Type",
                                    description = "The content type of the body"),
                            @ResponseHeader(
                                    name = "Content-Disposition",
                                    description = "The content disposition of the body"),
                            @ResponseHeader(
                                    name = "ETag",
                                    description = "Entity Tag of the response resource.\n" +
                                            "Used by caches, or in conditional requests."),
                            @ResponseHeader(
                                    name = "Last-Modified",
                                    description = "Date and time the resource was last modified.\n" +
                                            "Used by caches, or in conditional requests."),
                    }),
            @ApiResponse(
                    code = 304,
                    message = "Not Modified. \n Empty body because the client already has the latest version of " +
                            "the requested resource.\n"),
            @ApiResponse(
                    code = 400,
                    message = "The incoming request has more than one selection criteria defined via the query parameters.",
                    response = ErrorResponse.class),
            @ApiResponse(
                    code = 404,
                    message = "The search criteria did not match any device registered with the server.",
                    response = ErrorResponse.class),
            @ApiResponse(
                    code = 406,
                    message = "Not Acceptable.\n The requested media type is not supported."),
            @ApiResponse(
                    code = 500,
                    message = "Internal Server Error. \n Server error occurred while fetching the device list.",
                    response = ErrorResponse.class)
    })
    Response getBilling(
            @ApiParam(
                    name = "tenantDomain",
                    value = "The tenant domain.",
                    required = false)
            @QueryParam("tenantDomain")
                    String tenantDomain,
            @ApiParam(
                    name = "startDate",
                    value = "The start date.",
                    required = false)
            @QueryParam("startDate")
                    Timestamp startDate,
            @ApiParam(
                    name = "endDate",
                    value = "The end date.",
                    required = false)
            @QueryParam("endDate")
                    Timestamp endDate);

}
