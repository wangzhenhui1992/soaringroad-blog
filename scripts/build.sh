cd /docker/srblog-springboot
docker stop srblog-backend
docker rm srblog-backend
docker rmi srblog-backend
docker build -t srblog-backend .
