#!/bin/bash

# Configuration
REGISTRY="ghcr.io"
OWNER="programmerdatch"
REPO="class-osms"
VERSION="latest"  # Use timestamp as version
LATEST_TAG="$REGISTRY/$OWNER/$REPO:latest"
VERSION_TAG="$REGISTRY/$OWNER/$REPO:$VERSION"

echo "🚀 Starting deployment process..."

# Check if docker is running
if ! docker info > /dev/null 2>&1; then
    echo "❌ Docker is not running. Please start Docker and try again."
    exit 1
fi

# Build the image
echo "🏗️  Building Docker image..."
if docker build -t $LATEST_TAG .; then
    echo "✅ Build successful"
else
    echo "❌ Build failed"
    exit 1
fi

# Tag with version
echo "🏷️  Tagging image with version: $VERSION"
docker tag $LATEST_TAG $VERSION_TAG

# Push the images
echo "📤 Pushing images to registry..."
if docker push $LATEST_TAG && docker push $VERSION_TAG; then
    echo "✅ Push successful"
else
    echo "❌ Push failed. Make sure you're logged in to the registry:"
    echo "    docker login $REGISTRY -u $OWNER"
    exit 1
fi

echo "🎉 Deployment complete!"
echo "Latest image: $LATEST_TAG"
echo "Version tag: $VERSION_TAG" 