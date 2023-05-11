/*
 * Copyright (c) 2020 Oracle and/or its affiliates.
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
package me.daniel.se.quickstart;

import javax.net.ssl.SSLContext;

import io.helidon.config.Config;
import io.helidon.webclient.WebClient;
import io.helidon.webclient.WebClientTls;

import static me.daniel.se.quickstart.OCImTLSManager.Type.CLIENT;

public class Client {

    public static void main(String[] args) {
        Config config = Config.create();
        SSLContext sslContext = OCImTLSManager
                .create(CLIENT, config.get("security.mtls-reload"))
                .loadSSLContext();

        WebClient webClient = createWebClient(sslContext);

        System.out.println("Contacting secured endpoint!");
        System.out.println("Response: " + callSecured(webClient, 8443));
    }

    static WebClient createWebClient(SSLContext sslContext) {
        return WebClient.builder()
                .tls(WebClientTls.builder()
                             .sslContext(sslContext)
                             .build())
                .build();
    }

    static String callSecured(WebClient webClient, int port) {
        return webClient.get()
                .uri("https://localhost:" + port)
                .request(String.class)
                .await();
    }

}
