# Stage 1: Build the Vite React application
FROM node:20-alpine as build

# Set the working directory
WORKDIR /app

# Copy package.json and package-lock.json (or yarn.lock if using Yarn)
COPY package*.json ./

# Install dependencies
RUN npm install

# Copy the application source code
COPY . .

# Build the application with Vite (this will output to 'dist/')
RUN npm run build

# Stage 2: Serve the application with Node.js
FROM node:20-alpine

# Set the working directory for serving the app
WORKDIR /app

# Copy the built files from the build stage
COPY --from=build /app/dist /app/dist

# Install a simple static file server (e.g., serve or http-server)
RUN npm install -g serve

# Expose the port you want to use
EXPOSE 8080

# Serve the production build on port 8080
CMD ["serve", "-s", "dist", "-l", "8080"]
