# Internet_Chatter

### Goal: 

> Implement a peer to peer chatbot application, that allows communication between two peers.
> Additionally it should also support the transfer of all types of files to and fro between each other.


## Implementation:

1. Alice as a peer has both Server(writing thread) and Client(reading thread).
2. First we initiate the server of Alice on its port.
3. Alice will then wait and listen if anyone wants to connect with her port. 
4. Similarly Bob will also be waiting and listining for anyone to connect with him on his port.
5. We connect Alice and Bob by entering each others' port numbers.
6. Once the connection is established, they both can send messages and exchange files with each other.
7. The Writing thread of Alice can write messages/send files to Bob, who will recieve them through his reading thread and vice-versa.


## Code Execution:

1. Make two copies of the code ChatBot.java. One for Alice and one for Bob in directories X and Y.
2. Use the below command to start the server of Alice in directory X.
`java ChatBot Alice`
Give arguement Alice to specify that the server of Alice has to start.
3. Similarly start the  server of Bob in directory Y.
`java ChatBot Bob`
The arguement Bob specifies that the server of bob has to start.
4. In the terminal that has Alices' server enter Bob's port number and username.
5. In the terminal that has Bobs' server enter Alice's port number and username.
6. Once the connection is established, we can send messgaes to each other by typing the messages in their respective terminals.
7. To transfer file from ALice to Bob, type the below command in Alice's server terminal.
`transfer <file_name>`
8. To transfer file from Bob to Alice, type the below command in Bob's server terminal.
`transfer <file_name>`
