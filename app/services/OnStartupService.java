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

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import org.joda.time.DateTime;
import play.Application;
import scala.concurrent.duration.Duration;

import javax.inject.Inject;
import java.util.concurrent.TimeUnit;

/**
 * OnStartupService.
 *
 * @author Pierre Adam
 * @since 19.08.26
 */
public final class OnStartupService {

    /**
     * The Actor system.
     */
    private final ActorSystem actorSystem;

    /**
     * Instantiates a new On startup service.
     *
     * @param application the application
     * @param actorSystem the actor system
     */
    @Inject
    private OnStartupService(final Application application,
                             final ActorSystem actorSystem) {
        this.actorSystem = actorSystem;

        this.scheduleActors();
    }

    /**
     * Schedule actors.
     */
    private void scheduleActors() {
    }

    /**
     * Easy schedule once.
     *
     * @param actorRef     the actor ref
     * @param secondsDelay the seconds delay
     * @param payload      the payload
     */
    private void easyScheduleOnce(final ActorRef actorRef, final long secondsDelay, final Object payload) {
        this.actorSystem.scheduler().scheduleOnce(
            Duration.create(secondsDelay, TimeUnit.SECONDS),
            actorRef,
            payload,
            this.actorSystem.dispatcher(),
            ActorRef.noSender()
        );
    }

    /**
     * Easy daily schedule.
     *
     * @param actorRef    the actor ref
     * @param secondOfDay the second of day
     * @param payload     the payload
     */
    private void easyDailySchedule(final ActorRef actorRef, final long secondOfDay, final Object payload) {
        long scheduleIn = secondOfDay - DateTime.now().getSecondOfDay();

        while (scheduleIn < 0) {
            scheduleIn += 24 * 3600;
        }

        this.actorSystem.scheduler().schedule(
            Duration.create(scheduleIn, TimeUnit.SECONDS),
            Duration.create(1, TimeUnit.DAYS),
            actorRef,
            payload,
            this.actorSystem.dispatcher(),
            ActorRef.noSender()
        );
    }
}
