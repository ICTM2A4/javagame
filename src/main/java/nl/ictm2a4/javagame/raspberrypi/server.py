import socket
from threading import Thread
from sense_hat import SenseHat

log = False

host = "192.168.0.136"
port = 8001

sense = SenseHat()
current_button = ""

print('Starting server at ' + host + ":", port)

def joystickListener():
	global current_button
	while True:
		for event in sense.stick.get_events():
			if event.action == 'pressed' or event.action == 'held':
				current_button = event.direction
			else:
				current_button = ""



class ClientThread(Thread):
	def __init__(self, conn, ip, port):
		Thread.__init__(self)
		self.conn = conn
		self.ip = ip
		self.port = port
		if (log):
			print("Recieved connection from " + ip + ":" + str(port))

	def run(self):
		while True:
			global current_button
			data = self.conn.recv(2048)
			data = data.decode('UTF-8')
			data = data[2:]
			message = current_button

			if data == "":
				continue


			if (log):
				print("Server recieved data: ", data)

			if data == "EXIT":
				if (log):
					print('Closing connection with ' + self.ip + ":" + str(self.port))
				break

			print(message)

			if message == "":
				message = "null"

			message_to_send = message.encode('UTF-8')
			self.conn.send(len(message_to_send).to_bytes(2, byteorder='big'))
			self.conn.send(message_to_send)
		self.conn.close()

class ServerThread(Thread):
	def __init__(self, host, port):
		Thread.__init__(self)
		self.sock = socket.socket()
		self.sock.bind((host, port))
		self.sock.listen(5)

	def run(self):
		print("Awaiting connections from clients...")
		while True:
			(conn, (ip,port)) = self.sock.accept()
			newthread = ClientThread(conn, ip, port)
			newthread.start()



serverThread = ServerThread(host, port)
serverThread.start()

joystickListener()





