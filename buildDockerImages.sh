# docker build -t sharedpaymentsbackend ./backend/.
cd ./backend/parent-project && mvn clean install
cd ../../ && docker build -t sharedpaymentsfrontend ./frontend/.