/*
 * Copyright (c) 2024 Bayerische Motoren Werke Aktiengesellschaft
 * Copyright (c) 2025 Cofinity-X GmbH
 *
 * See the NOTICE file(s) distributed with this work for additional
 * information regarding copyright ownership.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Apache License, Version 2.0 which is available at
 * https://www.apache.org/licenses/LICENSE-2.0.
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.eclipse.tractusx.bdrs.api.directory;

import org.eclipse.edc.runtime.metamodel.annotation.Extension;
import org.eclipse.edc.runtime.metamodel.annotation.Inject;
import org.eclipse.edc.runtime.metamodel.annotation.Setting;
import org.eclipse.edc.spi.system.ServiceExtension;
import org.eclipse.edc.spi.system.ServiceExtensionContext;
import org.eclipse.edc.web.spi.WebService;
import org.eclipse.edc.web.spi.configuration.PortMapping;
import org.eclipse.edc.web.spi.configuration.PortMappingRegistry;
import org.eclipse.tractusx.bdrs.spi.store.DidEntryStore;

import static org.eclipse.tractusx.bdrs.api.directory.DirectoryApiExtension.NAME;

/**
 * Loads resources for the BPN Directory API.
 */
@Extension(NAME)
public class DirectoryApiExtension implements ServiceExtension {
    public static final String NAME = "BPN Directory API";

    @Setting(value = "Port for the Directory API", required = true)
    public static final String DIRECTORY_API_PORT = "web.http.directory.port";
    @Setting(value = "Path for the Directory API", required = true)
    public static final String DIRECTORY_API_PATH = "web.http.directory.path";
    static final String CONTEXT_NAME = "directory";
    @Inject
    private DidEntryStore store;

    @Inject
    private WebService webService;

    @Inject
    private PortMappingRegistry portMappingRegistry;

    @Override
    public String name() {
        return NAME;
    }

    @Override
    public void initialize(ServiceExtensionContext context) {
        var port = context.getSetting(DIRECTORY_API_PORT, 8082);
        var path = context.getSetting(DIRECTORY_API_PATH, "/directory");
        var portMapping = new PortMapping(CONTEXT_NAME, port, path);
        portMappingRegistry.register(portMapping);

        webService.registerResource(CONTEXT_NAME, new DirectoryApiController(store));
    }

}
