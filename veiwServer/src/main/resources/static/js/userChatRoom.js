	//수정된 코드
	
	const chatRoomId = "[[${chatRoomId}]]";
    const token = localStorage.getItem("token");
    const MemberId = localStorage.getItem("MemberId");
    var receiver = "[[${hospitalId}]]";

    console.log("병원 id 출력 : " + receiver);
    console.log("챗룸 id 출력 : " + chatRoomId);

    function saveMessagesToLocalStorage(messages) {
        localStorage.setItem('chatMessages_' + chatRoomId, JSON.stringify(messages));
    }

    function loadMessagesFromLocalStorage() {
        return JSON.parse(localStorage.getItem('chatMessages_' + chatRoomId) || '[]');
    }

    function renderMessages(messages) {
        const chatBox = $('#chat-box');
        chatBox.empty();

        messages.forEach(chat => {
            const messageBox = $('<div class="messageBox"></div>');
            const metaContainer = $('<div class="meta-container"></div>');
            const messageElement = $('<div class="text-message"></div>');
            const timestampElement = $('<div class="timestamp"></div>');
            const isReadElement = $('<div class="is-read"></div>');

            const receiveImage = $('<img src="/images/chat_cat2-removebg-preview.png" class="receiveImage">');

            const sendDateArray = chat.sendDate;
            const sendDate = new Date(
                sendDateArray[0], sendDateArray[1] - 1, sendDateArray[2], sendDateArray[3], sendDateArray[4], sendDateArray[5]
            );

            const year = sendDate.getFullYear();
            const month = ('0' + (sendDate.getMonth() + 1)).slice(-2);
            const day = ('0' + sendDate.getDate()).slice(-2);
            const hours = sendDate.getHours();
            const minutes = ('0' + sendDate.getMinutes()).slice(-2);
            const seconds = ('0' + sendDate.getSeconds()).slice(-2);
            const ampm = hours >= 12 ? 'PM' : 'AM';
            const formattedDate = `${year}-${month}-${day} ${ampm} ${('0' + (hours % 12 || 12)).slice(-2)}:${minutes}:${seconds}`;

            timestampElement.text(formattedDate);

            if (chat.isRead === false) {
                isReadElement.text("1");
            } else {
                isReadElement.text(" ");
            }

            metaContainer.append(timestampElement);
            metaContainer.append(isReadElement);

            messageElement.text(chat.message);

            if (chat.sender.id == MemberId || chat.sender.id === null) {
                messageElement.addClass('sentMsg');
                messageBox.addClass('sentBox');
                messageBox.append(messageElement);
                messageBox.append(metaContainer);
            } else {
                messageElement.addClass('receivedMsg');
                messageBox.addClass('receivedBox');
                messageBox.append(receiveImage);
                messageBox.append(messageElement);
                messageBox.append(metaContainer);
            }

            chatBox.append(messageBox);
        });

        chatBox.scrollTop(chatBox.prop("scrollHeight"));
    }

    function loadChatHistory() {
        $.ajax({
            url: 'http://localhost:9001/chat/' + chatRoomId,
            type: 'GET',
            dataType: 'json',
            headers: {
                'Authorization': token,
                'MemberId': MemberId
            },
            success: function(response) {
                const hospitalName = response[0].chatRoom.hospital.hospitalName;
                $('#hospitalName').text(hospitalName);

                response.sort((a, b) => {
                    const dateA = new Date(a.sendDate[0], a.sendDate[1] - 1, a.sendDate[2], a.sendDate[3], a.sendDate[4], a.sendDate[5], a.sendDate[6] / 1000);
                    const dateB = new Date(b.sendDate[0], b.sendDate[1] - 1, b.sendDate[2], b.sendDate[3], b.sendDate[4], b.sendDate[5], b.sendDate[6] / 1000);
                    return dateA - dateB;
                });

                const chatMessages = response.map(chat => ({
                    sendDate: chat.sendDate,
                    message: chat.message,
                    isRead: chat.isRead,
                    sender: chat.sender,
                    receiver: chat.receiver
                }));

                saveMessagesToLocalStorage(chatMessages);
                renderMessages(chatMessages);

                let finalReceiver = null;
                const chatRoomInfo = response[0];

                if (MemberId == chatRoomInfo.receiver.id) {
                    finalReceiver = chatRoomInfo.sender.id;
                } else {
                    finalReceiver = chatRoomInfo.receiver.id;
                }

                connectWebSocket(finalReceiver);
            },
            error: function(xhr, status, error) {
                console.error('채팅내역 불러오기 에러발생', error);
            },
            complete: function(jqXHR, textStatus) {
                responseCheck(jqXHR);
            }
        });
    }

    function loadChatList() {
        $.ajax({
            url: 'http://localhost:9001/chatList',
            type: 'GET',
            headers: {
                'Authorization': token,
                'MemberId': MemberId
            },
            success: function(response) {
                console.log(response);
                var chatList = $("#chatList");
                chatList.empty();

                if (response.length > 0) {
                    response.sort((a, b) => {
                        return new Date(b.lastMessageSendDate) - new Date(a.lastMessageSendDate);
                    });

                    for (var i = 0; i < response.length; i++) {
                        var chat = response[i];
                        var formattedDate = moment(chat.lastMessageSendDate).format('YYYY-MM-DD A hh:mm');

                        var listItem = `
                            <div class="chatListItem list-group-item list-group-item-action">
                                <div class="d-flex w-100 justify-content-between">
                                    <span class="mb-1">🏥 ${chat.hospitalName}</span>
                                    <small class="text-muted">마지막 채팅시간 <br> ${formattedDate}</small>
                                </div>
                                <span class="mb-1" style="text-align: left; display: block;">${chat.lastMessage}</span>
                                <button class="btn btn-outline-primary" onclick="chatLink(${chat.chatRoomId})">채팅하기</button>
                            </div>
                        `;
                        chatList.append(listItem);
                    }
                }
            },
            error: function(xhr, status, error) {
                console.error('채팅목록 불러오기 에러발생', error);
            },
            complete: function(jqXHR, textStatus) {
                responseCheck(jqXHR);
            }
        });
    }

    function handleRefresh() {
        if (receiver != null && receiver !== "null") {
            console.log("새로운 채팅입니다. 웹소켓을 바로 연결합니다.");
            const storedMessages = loadMessagesFromLocalStorage();
            if (storedMessages.length > 0) {
                renderMessages(storedMessages);
            }
            connectWebSocket(receiver);
        } else {
            console.log("새로운 채팅이 아닙니다. 기존 채팅 내역을 불러옵니다.");
            loadChatHistory();
            loadChatList();
        }
    }

    $(document).ready(function() {
        handleRefresh();
    });

    function chatLink(chatRoomId) {
        localStorage.setItem('refreshNeeded', 'true');
        window.location.href = '/user/chat/' + chatRoomId;
    }

    document.addEventListener('DOMContentLoaded', (event) => {
        if (localStorage.getItem('refreshNeeded') === 'true') {
            localStorage.removeItem('refreshNeeded');
            location.reload();
        }
    });

    function connectWebSocket(receiver) {
        const sender = localStorage.getItem('MemberId');
        const socket = new WebSocket("ws://localhost:9001/ws/chat");

        socket.onopen = function(event) {
            console.log("웹소켓 연결 성공!");
            socket.send(JSON.stringify({
                type: 'join',
                sender: sender,
                receiver: receiver
            }));
            console.log("웹소켓 연결 시 sender : " + sender);
            console.log("웹소켓 연결 시 receiver : "+ receiver);
        };

        socket.onmessage = function(event) {
            const chatBox = $('#chat-box');

            const message = JSON.parse(event.data);
            const messageBox = $('<div class="messageBox"></div>');
            const messageElement = $('<div class="text-message"></div>');
            const metaContainer = $('<div class="meta-container"></div>');
            const timestampElement = $('<div class="timestamp"></div>');
            const isReadElement = $('<div class="is-read"></div>');

            const sendDate = message.sendDate;
            const localDate = new Date(
                sendDate[0], sendDate[1] - 1, sendDate[2], sendDate[3], sendDate[4], sendDate[5], sendDate[6] / 1000
            );

            const timestamp = !isNaN(localDate) ? localDate.toLocaleString('ko-KR') : 'Invalid Date';
            timestampElement.text(timestamp);

            messageElement.text(message.message);
            metaContainer.append(timestampElement);

            if (message.isRead === false) {
                isReadElement.text("1");
                metaContainer.append(isReadElement);
            }

            const chatMessages = loadMessagesFromLocalStorage();
            chatMessages.push(message);
            saveMessagesToLocalStorage(chatMessages);

            if (message.sender.id == sender) {
                messageElement.addClass('sentMsg');
                messageBox.addClass('sentBox');
                messageBox.append(messageElement);
                messageBox.append(metaContainer);
            } else {
                messageElement.addClass('receivedMsg');
                messageBox.addClass('receivedBox');
                messageBox.append(messageElement);
                messageBox.append(metaContainer);
            }

            chatBox.append(messageBox);
            chatBox.scrollTop(chatBox.prop("scrollHeight"));
        };

        socket.onclose = function() {
            console.log("서버 연결 해제");
        };

        document.getElementById('chat-form').addEventListener('submit', function(event) {
            event.preventDefault();
            const message = document.getElementById('inputMsg').value;
            socket.send(JSON.stringify({
                type: 'message',
                sender: sender,
                receiver: receiver,
                message: message
            }));
            document.getElementById('inputMsg').value = '';

            const chatMessages = loadMessagesFromLocalStorage();
            const newMessage = {
                sendDate: new Date().toISOString().split('T').join(' ').split('.')[0].split(/[-: ]/),
                message: message,
                isRead: false,
                sender: { id: sender },
                receiver: { id: receiver }
            };
            chatMessages.push(newMessage);
            saveMessagesToLocalStorage(chatMessages);
        });
    }