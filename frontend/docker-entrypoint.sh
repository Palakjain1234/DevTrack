#!/bin/sh
set -e

# Replace BACKEND_URL in nginx config
envsubst '$BACKEND_URL' < /etc/nginx/templates/default.conf.template > /etc/nginx/conf.d/default.conf

echo "Nginx config generated with BACKEND_URL=${BACKEND_URL}"
cat /etc/nginx/conf.d/default.conf

exec nginx -g 'daemon off;'
