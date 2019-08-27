/*
 * Copyright (c) 2019 Pierre Adam
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package services;

import com.typesafe.config.Config;
import org.eclipse.paho.client.mqttv3.IMqttClient;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.UUID;

/**
 * MQTTService.
 *
 * @author Pierre Adam
 * @since 19.08.26
 */
@Singleton
public class MQTTService {

    /**
     * The Logger.
     */
    final Logger logger = LoggerFactory.getLogger(MQTTService.class);

    /**
     * The Mqtt connect options.
     */
    final MqttConnectOptions mqttConnectOptions;

    /**
     * The Client.
     */
    IMqttClient client;

    /**
     * Instantiates a new Mqtt service.
     *
     * @param config the config
     */
    @Inject
    public MQTTService(final Config config) {
        final String serverURI = String.format("tcp://%s:%d", config.getString("mqtt.endpoint"), config.getInt("mqtt.port"));
        final String clientId = config.hasPath("mqtt.clientId") || config.getString("mqtt.clientId").equals("")
            ? config.getString("mqtt.clientId") : UUID.randomUUID().toString();

        this.mqttConnectOptions = new MqttConnectOptions();
        if (config.hasPath("mqtt.username") || config.getString("mqtt.username").equals("")) {
            this.mqttConnectOptions.setUserName(config.getString("mqtt.username"));
        }
        if (config.hasPath("mqtt.password") || config.getString("mqtt.password").equals("")) {
            this.mqttConnectOptions.setPassword(config.getString("mqtt.password").toCharArray());
        }
        this.mqttConnectOptions.setAutomaticReconnect(true);

        try {
            this.client = new MqttClient(serverURI, clientId);
            this.client.connect(this.mqttConnectOptions);
        } catch (final MqttException e) {
            this.logger.error("Unable to connect to mqtt.", e);
            this.client = null;
        }
    }

    /**
     * Gets client.
     *
     * @return the client
     */
    public IMqttClient getClient() {
        return this.client;
    }
}
