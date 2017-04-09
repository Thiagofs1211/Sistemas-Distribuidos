import socket

s = socket.socket()
host = "localhost"
port = 12345

s.connect((host, port))
print s.recv(1024)

message = raw_input("Teste")
s.send(message)
print s.recv(1024)
s.send("Encerra")
s.close()
