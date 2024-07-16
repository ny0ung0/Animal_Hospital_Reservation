importScripts('https://www.gstatic.com/firebasejs/9.6.10/firebase-app-compat.js');
importScripts('https://www.gstatic.com/firebasejs/9.6.10/firebase-messaging-compat.js');

// Firebase 초기화
firebase.initializeApp({
	apiKey: "AIzaSyCo4ODWmCMDJZqqgLhPOD9_GbaIcC5o1Xk",
	authDomain: "animal-reservation.firebaseapp.com",
	projectId: "animal-reservation",
	storageBucket: "animal-reservation.appspot.com",
	messagingSenderId: "254646572194",
	appId: "1:254646572194:web:6c156cba2754170343f3fb",
	measurementId: "G-9SLT551DW8"
});

const messaging = firebase.messaging();

// 백그라운드 메시지 수신
messaging.onBackgroundMessage((payload) => {
	console.log('백그라운드 메시지 수신:', payload);
	// Customize notification here
	const notificationTitle = '백그라운드 메시지 제목';
	const notificationOptions = {
		body: '백그라운드 메시지 본문.',
		icon: '/firebase-logo.png'
	};

	self.registration.showNotification(notificationTitle, notificationOptions);
});
