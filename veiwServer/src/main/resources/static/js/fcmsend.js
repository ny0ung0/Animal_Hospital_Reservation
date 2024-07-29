
// FCM 나의 알람
function sendTokenToMessage(title, body) {
    fetch('http://localhost:9001/api/v1/fcm/message', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
            'MemberId': localStorage.getItem('MemberId'),
            'Authorization': localStorage.getItem('token')
        },
        body: JSON.stringify({ token: localStorage.getItem('fcm'), title: title, body:body })
    }).then(response => {
        console.log('나의 알람:', response);
    }).catch(error => {
        console.error('서버로 토큰 전송 중 오류 발생:', error);
    });
}

// FCM 대상자 지정 메세지 전송
function sendTokenToMessageReceiver(receiverId, title, body){
	fetch(`http://localhost:9001/api/v1/fcm/message/${receiverId}`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
            'MemberId': localStorage.getItem('MemberId'),
            'Authorization': localStorage.getItem('token')
        },
        body: JSON.stringify({ title: title, body:body })
    }).then(response => {
        console.log('나의 알람:', response);
    }).catch(error => {
        console.error('서버로 토큰 전송 중 오류 발생:', error);
    });
}

// 포그라운드에서 푸시 메시지 수신
messaging.onMessage((payload) => {
    console.log('포그라운드 메시지 수신: ', payload);

    const notificationTitle = payload.notification.title;
    const notificationOptions = {
        body: payload.notification.body,
        icon: '/images/logo_user.png'
    };

    // 브라우저에서 알림을 표시
    if (Notification.permission === 'granted') {
        new Notification(notificationTitle, notificationOptions);
    }
});