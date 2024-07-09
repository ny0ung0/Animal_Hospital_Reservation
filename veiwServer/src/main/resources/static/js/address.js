
//주소1을 선택하면 주소2내용을 바꿔주는 메서드

function address1SelectChanged(callback = null) {
    let address1 = document.querySelector("#address1").value;
    document.getElementById("address2").innerHTML = '<option value="" class="always">선택</option>';

    const xhttp = new XMLHttpRequest();
    xhttp.onload = function () {
        let address2 = JSON.parse(this.responseText);
        address2.forEach(address => {
            document.getElementById("address2").innerHTML += '<option value="'+ address +'">' + address + '</option>';
        });

        if (callback) {
            callback();
        };
    };
    xhttp.open("get", "http://localhost:9001/api/v1/common/address2/" + address1, true);
    xhttp.send();
}

function readyAddress(callback = null){
	//주소관련
	let address1 =document.getElementById("address1");
	address1.innerHTML ='<option value="" class="always">선택</option>';
	document.getElementById("address2").innerHTML ='<option value="" class="always">선택</option>';
   const xhttp = new XMLHttpRequest();
   xhttp.onload = function() {
	  
   	let result =JSON.parse(this.responseText);
    Object.keys(result).forEach(key => {
        address1.innerHTML += '<option value="' + key + '">' + key + '</option>';
    });
        if (callback) {
            callback();
        };
   };
   xhttp.open("get", "http://localhost:9001/api/v1/common/address1", true);
  // xhttp.setRequestHeader("Content-type", "application/json");
   xhttp.send();
}
// 정보 수정 페이지에서 필요한 셀렉트 기본 값 지정 함수
function selectOptionByValue(selectId, valueToSelect) {
        // 셀렉트 요소를 찾기
        let selectElement = document.getElementById(selectId);
        
        // 옵션들 중에 주어진 값을 가진 옵션을 찾기
        for (let i = 0; i < selectElement.options.length; i++) {
            if (selectElement.options[i].value === valueToSelect) {
                // 해당 값을 가진 옵션을 선택
                selectElement.selectedIndex = i;
                break;
            }
        }
    }
    