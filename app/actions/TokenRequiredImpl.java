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

package actions;

import com.typesafe.config.Config;
import play.mvc.Action;
import play.mvc.Http;
import play.mvc.Result;
import play.mvc.Results;

import javax.inject.Inject;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

/**
 * TokenRequiredImpl.
 *
 * @author Pierre Adam
 * @since 19.08.27
 */
public class TokenRequiredImpl extends Action<TokenRequired> {

    private final String token;

    @Inject
    public TokenRequiredImpl(final Config config) {
        this.token = config.getString("application.authorization");
    }

    @Override
    public CompletionStage<Result> call(final Http.Request request) {
        final Optional<String> authorization = request.getHeaders().get("Authorization");

        if (authorization.isPresent() && authorization.get().equals(this.token)) {
            return this.delegate.call(request);
        }
        return CompletableFuture.completedFuture(Results.forbidden("You shall not pass !"));
    }
}
