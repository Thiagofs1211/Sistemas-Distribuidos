import socket

s = socket.socket()
host = "localhost"
port = 12345
s.bind((host,port))

s.listen(5)
while True:
	c, addr = s.accept()
	print 'Got connection from', addr
	c.send('Thank you for connecting')
	print c.recv(1024)
	message = raw_input("Teste Server")
	c.send(message)
	c.send("Encerrado Server")
	print c.recv(1024)
	c.close()
