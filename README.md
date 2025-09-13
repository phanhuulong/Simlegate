# Simlegate
Start project create server minio on port 9000
docker run -p 9000:9000 -p 9001:9001 \
-e "MINIO_ROOT_USER=admin" \
-e "MINIO_ROOT_PASSWORD=admin123" \
quay.io/minio/minio server /data --console-address ":9001"

