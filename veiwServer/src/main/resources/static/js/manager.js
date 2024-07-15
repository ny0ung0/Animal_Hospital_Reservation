
  document.addEventListener('DOMContentLoaded', function () {
	    // 현재 URL 가져오기
	    const currentPath = window.location.pathname;
		console.log(currentPath);	
	    // 모든 nav-link 요소 가져오기
	    const navLinks = document.querySelectorAll('.nav-link');

	    // 각 nav-link 요소에 대해 현재 URL과 비교하여 active 클래스 추가
	    navLinks.forEach(function (link) {
	        if (link.getAttribute('href') === currentPath) {
	            link.classList.add('active');
	        }
	    });

	    // 드롭다운 메뉴 내부의 링크에 대해서도 같은 작업 수행
	    const dropdownItems = document.querySelectorAll('.dropdown-item');
	    dropdownItems.forEach(function (item) {
	        if (item.getAttribute('href') === currentPath) {
	            item.classList.add('active');
	            // 부모 드롭다운 링크에도 active 클래스 추가
	            item.closest('.dropdown').querySelector('.dropdown-toggle').classList.add('active');
	        }
	    });
	    
	    // 만약 현재 페이지가 상세보기 페이지인 경우, 부모 메뉴의 active 상태도 설정
    	if (currentPath.includes('/manager/userDetail')) {
	        // 부모 메뉴 항목에 active 클래스 추가
	        const parentMenuLink = document.querySelector('a[href="/manager/userList"]');
	        if (parentMenuLink) {
	            parentMenuLink.classList.add('active');
	        }
	    }
    
	});

