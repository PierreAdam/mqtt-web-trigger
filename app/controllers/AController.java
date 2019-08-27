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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import play.data.FormFactory;
import play.i18n.Messages;
import play.i18n.MessagesApi;
import play.libs.typedmap.TypedEntry;
import play.libs.typedmap.TypedMap;
import play.mvc.Controller;
import play.mvc.Http;

import java.util.List;
import java.util.function.Function;

/**
 * AController.
 *
 * @author Pierre Adam
 * @since 19.08.26
 */
public class AController extends Controller {

    /**
     * The Logger.
     */
    protected final Logger logger;

    /**
     * The Messages api.
     */
    protected final MessagesApi messagesApi;

    /**
     * The Form factory.
     */
    protected final FormFactory formFactory;

    /**
     * Instantiates a new A controller.
     *
     * @param messagesApi the messages api
     * @param formFactory the form factory
     */
    protected AController(final MessagesApi messagesApi, final FormFactory formFactory) {
        this.logger = LoggerFactory.getLogger(this.getClass());
        this.messagesApi = messagesApi;
        this.formFactory = formFactory;
    }

    /**
     * With messages.
     *
     * @param <R>      the type parameter
     * @param request  the request
     * @param callback the callback
     * @return the r
     */
    protected <R> R withMessages(final Http.Request request, final Function<Messages, R> callback) {
        return callback.apply(this.messagesApi.preferred(request));
    }

    /**
     * Request add attrs r.
     *
     * @param <R>      the type parameter
     * @param request  the request
     * @param attr     the attr
     * @param callback the callback
     * @return the r
     */
    protected <R> R requestAddAttrs(final Http.Request request, final TypedEntry attr, final Function<Http.Request, R> callback) {
        final TypedMap typedMap = request.attrs().putAll(attr);
        return callback.apply(request.withAttrs(typedMap));
    }

    /**
     * Request add attrs r.
     *
     * @param <R>      the type parameter
     * @param request  the request
     * @param attrs    the attrs
     * @param callback the callback
     * @return the r
     */
    protected <R> R requestAddAttrs(final Http.Request request, final List<TypedEntry> attrs, final Function<Http.Request, R> callback) {
        TypedMap typedMap = request.attrs();
        for (final TypedEntry attr : attrs) {
            typedMap = typedMap.putAll(attr);
        }
        return callback.apply(request.withAttrs(typedMap));
    }
}
