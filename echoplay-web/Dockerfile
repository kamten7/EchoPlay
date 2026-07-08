FROM openjdk:8-jre-slim

LABEL maintainer="kamten7"

WORKDIR /app

# 安装 FFmpeg（用于视频转码和封面提取）
RUN apt-get update && \
    apt-get install -y --no-install-recommends ffmpeg && \
    rm -rf /var/lib/apt/lists/*

# 创建必要的目录
RUN mkdir -p /app/logs /app/video/upload /app/video/original

COPY target/echoplay-web-1.0.jar app.jar

# 暴露端口
EXPOSE 7071

# JVM 参数优化（Docker 容器内运行）
ENV JAVA_OPTS="-Xms256m -Xmx512m \
  -XX:+UseG1GC \
  -XX:MaxGCPauseMillis=200 \
  -XX:+HeapDumpOnOutOfMemoryError \
  -XX:HeapDumpPath=/app/logs/heapdump.hprof \
  -Duser.timezone=GMT+8"

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]