# Information
FROM alpine:3.9
LABEL maintainer="Pierre Adam"

# Health check
HEALTHCHECK --start-period=120s --interval=5s --timeout=2s --retries=1 CMD wget -q -O /dev/null -T 2 http://127.0.0.1:9000/ || exit 1

# Prepare Alpine Linux
USER root
RUN apk update \
    && apk add \
        openjdk8-jre \
        fontconfig \
        ttf-dejavu \
        bash \
	    nss \
    && adduser -D -g "" -u 1000 javauser \
    && mkdir -p /opt/playframework \
    && chown javauser:javauser /opt/playframework \
    && rm -rf /var/cache/apk/ \
    && rm -rf /root/.cache \
    && rm -rf /tmp/*

# PlayFramework
USER javauser
COPY --chown=javauser:javauser target/universal/stage/ /opt/playframework/
CMD ["/opt/playframework/bin/mqtt-web-trigger"]
