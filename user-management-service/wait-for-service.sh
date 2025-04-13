#!/bin/sh

host="$1"
port="$2"
service="$3"
shift 3
cmd="$@"

echo "Waiting for $service to be up at $host:$port..."

while ! nc -z "$host" "$port"; do
  echo "Still waiting for $service..."
  sleep 2
done

echo "$service is up. Starting application..."
exec "$@"
