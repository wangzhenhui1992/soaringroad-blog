docker run -d -v /logs/springboot-log:/logs/springboot-log --name srblog-backend -p 8080:8080 srblog-backend
service nginx restart
