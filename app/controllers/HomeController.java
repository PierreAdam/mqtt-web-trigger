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

package controllers;

import actions.TokenRequired;
import akka.MQTTExecutionContext;
import com.typesafe.config.Config;
import org.eclipse.paho.client.mqttv3.MqttException;
import play.data.FormFactory;
import play.i18n.MessagesApi;
import play.mvc.Http;
import play.mvc.Result;
import play.mvc.Results;
import services.MQTTService;

import javax.inject.Inject;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;


/**
 * The type Home controller.
 *
 * @author Pierre Adam
 * @since 19.08.26
 */
public class HomeController extends AController {

    /**
     * The Mqtt service.
     */
    final MQTTService mqttService;

    /**
     * The Config.
     */
    final Config config;

    /**
     * The Mqtt publish execution context.
     */
    final MQTTExecutionContext mqttPublishExecutionContext;

    /**
     * Instantiates a new Home controller.
     *
     * @param messagesApi                 the messages api
     * @param formFactory                 the form factory
     * @param mqttService                 the mqtt service
     * @param config                      the config
     * @param mqttPublishExecutionContext the mqtt execution context
     */
    @Inject
    protected HomeController(final MessagesApi messagesApi,
                             final FormFactory formFactory,
                             final MQTTService mqttService,
                             final Config config,
                             final MQTTExecutionContext mqttPublishExecutionContext) {
        super(messagesApi, formFactory);
        this.mqttService = mqttService;
        this.config = config;
        this.mqttPublishExecutionContext = mqttPublishExecutionContext;
    }

    /**
     * Get home result.
     *
     * @param request the request
     * @return the result
     */
    public Result GET_Home(final Http.Request request) {
        return Results.ok("Running");
    }

    /**
     * Get home result.
     *
     * @param request the request
     * @return the result
     */
    @TokenRequired
    public CompletionStage<Result> GET_Trigger(final Http.Request request) {
        return CompletableFuture
            .supplyAsync(() -> {
                try {
                    this.mqttService.getClient().publish(this.config.getString("application.mqtt-topic"), "True".getBytes(), 2, true);
                    return true;
                } catch (final MqttException e) {
                    this.logger.error("An error occurred while publishing.", e);
                }
                return false;
            }, this.mqttPublishExecutionContext)
            .thenApply(success -> {
                if (success) {
                    return Results.ok("Triggered !");
                } else {
                    return Results.internalServerError("Fail to trigger.");
                }
            });
    }
}
