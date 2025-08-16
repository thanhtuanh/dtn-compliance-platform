#!/bin/bash

# DTN Swagger UI Debug Script
echo "🔍 Swagger UI Path Debugging..."

echo "Testing different Swagger UI paths:"

# Test common Swagger UI paths
paths=(
    "/swagger-ui/"
    "/swagger-ui/index.html"
    "/swagger-ui.html"
    "/webjars/swagger-ui/index.html"
    "/webjars/swagger-ui/4.15.5/index.html"
    "/api-docs"
    "/v3/api-docs"
)

for path in "${paths[@]}"; do
    echo -n "  http://localhost:8080$path: "
    status=$(curl -s -o /dev/null -w "%{http_code}" "http://localhost:8080$path" 2>/dev/null)
    if [[ "$status" == "200" ]]; then
        echo "✅ $status"
    elif [[ "$status" == "302" ]]; then
        echo "🔄 $status (redirect)"
    else
        echo "❌ $status"
    fi
done

echo ""
echo "🔍 Checking actuator endpoints:"
curl -s http://localhost:8080/actuator | jq -r '.["_links"] | keys[]' 2>/dev/null || echo "No actuator data or jq not available"

echo ""
echo "🔍 Manual test:"
echo "  curl -v http://localhost:8080/swagger-ui/"
echo "  curl -v http://localhost:8080/swagger-ui.html"