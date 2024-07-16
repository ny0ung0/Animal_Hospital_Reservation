// Firebase 초기화 설정
const firebaseConfig = {
	apiKey: "AIzaSyCo4ODWmCMDJZqqgLhPOD9_GbaIcC5o1Xk",
	authDomain: "animal-reservation.firebaseapp.com",
	projectId: "animal-reservation",
	storageBucket: "animal-reservation.appspot.com",
	messagingSenderId: "254646572194",
	appId: "1:254646572194:web:6c156cba2754170343f3fb",
	measurementId: "G-9SLT551DW8"
};

// Firebase 초기화
firebase.initializeApp(firebaseConfig);
const messaging = firebase.messaging();

// 푸시 알림 권한 요청
function requestPermission() {
	console.log('푸시 알림 권한 요청 중...');
	Notification.requestPermission().then((permission) => {
		if (permission === 'granted') {
			console.log('푸시 알림 권한 허용됨.');
			getToken();
		} else {
			console.log('푸시 알림 권한 거부됨.');
		}
	});
}

// FCM 토큰 요청
function getToken() {
	messaging.getToken({ vapidKey: 'BO1Cyk9-JXkGE3i0z64FeRt9yzJuDobohGmvfimiBqJyKa1ERE1a3b_-Lc0fzfrzQU2agLAnIXpYD19a5JJ7-6Q' }).then((currentToken) => {
		if (currentToken) {
			console.log('FCM 토큰:', currentToken);
			sendTokenToServer(currentToken);
		} else {
			console.log('FCM 토큰을 얻을 수 없습니다.');
		}
	}).catch((err) => {
		console.log('FCM 토큰 요청 중 오류 발생:', err);
	});
}

// FCM 토큰을 서버로 전송
function sendTokenToServer(token) {
	fetch('http://localhost:9001/api/v1/fcm/send', {
		method: 'POST',
		headers: {
			'Content-Type': 'application/json',
			'MemberId':localStorage.getItem('MemberId')
		},
		body: JSON.stringify({ token: token })
	}).then(response => response.json()).then(data => {
		console.log('서버 응답:', data);
	}).catch(error => {
		console.error('서버로 토큰 전송 중 오류 발생:', error);
	});
}

// 푸시 알림 권한 요청 시작
requestPermission();
